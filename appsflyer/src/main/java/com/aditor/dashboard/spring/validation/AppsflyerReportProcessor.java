package com.aditor.dashboard.spring.validation;

import com.aditor.dashboard.spring.model.AppsflyerTableType;
import com.aditor.dashboard.spring.model.ReportType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang3.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppsflyerReportProcessor {

    private static final String REGEX = "[^a-zA-Z0-9- ./()_\\[\\]\\\\]*";
    private static final Logger logger = LoggerFactory.getLogger(AppsflyerReportProcessor.class);

    private ColumnMappingConfigsHolder holder;

    @Autowired
    public AppsflyerReportProcessor(ColumnMappingConfigsHolder holder) {
        this.holder = holder;
    }

    public String prepareSQLforImport(List<String> fileLines, String importFileName, ReportType reportType, AppsflyerTableType tableType) {
        String tableName = reportType.getTempTablename();
        String reportTypeName = reportType.getConfigFileName();
        List<AppsflyerReportColumn> columns = parseColumns(fileLines);
        long originalSize = columns.size();
        columns = matchingAndFilteringColumns(columns, reportTypeName);
        if (tableType == AppsflyerTableType.GENERAL) {
            columns = filterInvalidColumns(columns);
            if(!areAllMandatoryColumnsPresent(columns, reportTypeName)) {
                logger.error("Not all mandatory columns are present in the report");
                throw new MandatoryColumnsMissingException("Mandatory columns missing in file " + importFileName);
            }
        }
        return generateSQL(columns, importFileName, tableName, originalSize);
    }

    private List<AppsflyerReportColumn> parseColumns(List<String> rows) {
        List<AppsflyerReportColumn> columns = new ArrayList<>();
        Table<Integer, String, String> data = HashBasedTable.create();
        List<String> headers = parseHeader(rows.get(0));

        for(int rowNumber = 1; rowNumber < rows.size(); rowNumber++) {
            List<String> values;
            values = parseRow(rows.get(rowNumber));

            for(int columnNumber = 0; columnNumber < values.size(); columnNumber++) {
                data.put(rowNumber, headers.get(columnNumber), values.get(columnNumber));
            }
        }

        for(int i = 0; i < headers.size(); i++) {
            AppsflyerReportColumn column = new AppsflyerReportColumn();
            column.setIndex(i);
            column.setHeaderName(headers.get(i));
            column.setSampledValues(new ArrayList<>(data.column(headers.get(i)).values()));
            columns.add(column);
        }

        return columns;
    }

    private List<AppsflyerReportColumn> matchingAndFilteringColumns(List<AppsflyerReportColumn> columns, String reportType) {
        for(AppsflyerReportColumn column : columns)
            column.setColumnMappingConfig(holder.getByAlias(column.getHeaderName(), reportType));

        List<String> nonMatchedColumns = columns.stream()
                .filter(column -> column.getColumnMappingConfig() == null)
                .map(AppsflyerReportColumn::getHeaderName)
                .collect(Collectors.toList());

        if(!nonMatchedColumns.isEmpty())
            logger.warn("Non matched columns: " + toJson(nonMatchedColumns));

        columns = columns.stream()
                .filter(AppsflyerReportColumn::isMatched)
                .collect(Collectors.toList());

        return columns;
    }

    private List<AppsflyerReportColumn> filterInvalidColumns(List<AppsflyerReportColumn> columns) {

        List<String> invalidColumns = columns.stream()
                .filter(column -> !isValidColumn(column))
                .map(AppsflyerReportColumn::getHeaderName)
                .collect(Collectors.toList());

        if(!invalidColumns.isEmpty())
            logger.warn("Invalid columns: " + toJson(invalidColumns));

        return columns.stream()
                .filter(this::isValidColumn)
                .collect(Collectors.toList());
    }

    private boolean areAllMandatoryColumnsPresent(List<AppsflyerReportColumn> columns, String type) {
        List<String> mandatory = holder.getAllMandatoryColumnNames(type);
        List<String> present = columns.stream()
                .map(column -> column.getColumnMappingConfig().getName())
                .collect(Collectors.toList());

        return present.containsAll(mandatory);
    }

    private String generateSQL(List<AppsflyerReportColumn> columns, String fileName, String tableName, long originalColumnNumber) {

        Map<String, AppsflyerReportColumn> columnMap = new HashMap<>();
        columns.forEach(column -> columnMap.put(column.getColumnMappingConfig().getName(), column));

        String sql = "LOAD DATA LOCAL INFILE '"+ fileName +"' " +
                "INTO TABLE " + tableName + " " +
                "FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES " +
                "(%file_columns%) SET %columns_set_part%";

        String columnListPart = "@col1";
        String columnSetPart = "";

        for(int i = 1; i < originalColumnNumber; i++)
            columnListPart += ", @col" + (i + 1);

        for(String columnName : columnMap.keySet()) {
            String fileColumnVar = "@col" + (columnMap.get(columnName).getIndex() + 1);
            if(columnSetPart.isEmpty()) {
                columnSetPart = columnName + "=" + fileColumnVar;
            } else {
                columnSetPart += ", " + columnName + "=" + fileColumnVar;
            }
        }

        sql = sql.replace("%file_columns%", columnListPart);
        sql = sql.replace("%columns_set_part%", columnSetPart);

        sql += ";";
        return sql;
    }


    private List<String> parseHeader(String header) {
        StrTokenizer tokenizer =  new StrTokenizer(header, ',', '"');

        return tokenizer.getTokenList().stream()
                .map(s -> s.replaceAll(REGEX, ""))
                .collect(Collectors.toList());
    }

    private List<String> parseRow(String row) {
        StrTokenizer tokenizer = new StrTokenizer(row, ',', '"');
        tokenizer.setIgnoreEmptyTokens(false);

        return tokenizer.getTokenList();
    }

    private boolean isValidColumn(AppsflyerReportColumn column) {
        switch (column.getColumnMappingConfig().getType()) {
            case DATETIME:
                for(String value : column.getSampledValues()) {
                    if(!isValidDateValue(value, column.getColumnMappingConfig().isMandatory()))
                        return false;
                }
                break;
            case DOUBLE:
                for(String value : column.getSampledValues()) {
                    if(!isValidDoubleValue(value, column.getColumnMappingConfig().isMandatory()))
                        return false;
                }
                break;
            case STRING:
                for(String value : column.getSampledValues()) {
                    if(!isValidStringValue(value, column.getColumnMappingConfig().isMandatory()))
                        return false;
                }
                break;
            default:
                return false;
        }

        return true;
    }

    private boolean isValidDateValue(String dateValue, boolean isMandatory) {

        if(!isMandatory && dateValue.isEmpty())
            return true;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setLenient(false);

        try {
            format.parse(dateValue);
            return true;
        } catch (Exception ignored) {
            logger.error("Invalid date value: " + dateValue);
            return false;
        }
    }

    private boolean isValidDoubleValue(String doubleValue, boolean isMandatory) {
        if(!isMandatory)
            return true;
        try {
            Double.parseDouble(doubleValue);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean isValidStringValue(String stringValue, boolean isMandatory) {
        return (!isMandatory && stringValue.isEmpty()) ||
                !stringValue.isEmpty();

    }
    private String toJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(object);
        } catch (Exception ignored) {
            //REFACTOR
            return "";
        }
    }
}
