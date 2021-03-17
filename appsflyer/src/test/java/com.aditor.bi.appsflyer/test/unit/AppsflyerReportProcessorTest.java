package com.aditor.bi.appsflyer.test.unit;

import com.aditor.dashboard.spring.model.AppsflyerTableType;
import com.aditor.dashboard.spring.model.ReportType;
import com.aditor.dashboard.spring.validation.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppsflyerReportProcessorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testSimpleColumnMapping() {
        AppsflyerReportProcessor processor = new AppsflyerReportProcessor(new ColumnMappingConfigsHolderBuilder()
                .withMandatoryString("A", "a-column")
                .withMandatoryString("C", "c-column", "ccolumn")
                .withType(ReportType.INSTALL)
                .build());

        List<String> file = Arrays.asList(
                "ccolumn,b-column,a-column",
                "hello world,hello,world"
        );

        String expected = "LOAD DATA LOCAL INFILE 'filename' " +
                "INTO TABLE appsflyer_tmp_install_events " +
                "FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' " +
                "LINES TERMINATED BY '\\n' IGNORE 1 LINES " +
                "(@col1, @col2, @col3) SET A=@col3, C=@col1;";

        String generated = processor.prepareSQLforImport(file, "filename", ReportType.INSTALL, AppsflyerTableType.GENERAL);

        Assert.assertEquals("Generated invalid SQL in simple column mapping case", expected, generated);
    }

    @Test
    public void testMissingMandatoryColumns() {
        AppsflyerReportProcessor processor = new AppsflyerReportProcessor(new ColumnMappingConfigsHolderBuilder()
                .withMandatoryString("A", "a-column")
                .withOptionalString("B", "b-column")
                .withType(ReportType.INSTALL)
                .build());

        List<String> file = Arrays.asList(
                "ccolumn,b-column",
                "hello world,hello"
        );

        expectedException.expect(MandatoryColumnsMissingException.class);
        processor.prepareSQLforImport(file, "filename", ReportType.INSTALL, AppsflyerTableType.GENERAL);
    }

    public class ColumnMappingConfigsHolderBuilder {
        private List<ColumnMappingConfig> configList = new ArrayList<>();
        private ReportType type;

        public ColumnMappingConfigsHolderBuilder withOptionalString(String columnName, String... aliases) {
            ColumnMappingConfig config = new ColumnMappingConfig();
            config.setName(columnName);
            config.setMandatory(false);
            config.setType(ColumnType.STRING);
            config.setAliases(Arrays.asList(aliases));
            configList.add(config);

            return this;
        }

        public ColumnMappingConfigsHolderBuilder withMandatoryString(String columnName, String... aliases) {
            ColumnMappingConfig config = new ColumnMappingConfig();
            config.setName(columnName);
            config.setMandatory(true);
            config.setType(ColumnType.STRING);
            config.setAliases(Arrays.asList(aliases));
            configList.add(config);

            return this;
        }

        public ColumnMappingConfigsHolderBuilder withOptionalDate(String columnName, String... aliases) {
            ColumnMappingConfig config = new ColumnMappingConfig();
            config.setName(columnName);
            config.setMandatory(false);
            config.setType(ColumnType.DATETIME);
            config.setAliases(Arrays.asList(aliases));
            configList.add(config);

            return this;
        }

        public ColumnMappingConfigsHolderBuilder withMandatoryDate(String columnName, String... aliases) {
            ColumnMappingConfig config = new ColumnMappingConfig();
            config.setName(columnName);
            config.setMandatory(true);
            config.setType(ColumnType.DATETIME);
            config.setAliases(Arrays.asList(aliases));
            configList.add(config);

            return this;
        }

        public ColumnMappingConfigsHolderBuilder withOptionalDouble(String columnName, String... aliases) {
            ColumnMappingConfig config = new ColumnMappingConfig();
            config.setName(columnName);
            config.setMandatory(false);
            config.setType(ColumnType.DOUBLE);
            config.setAliases(Arrays.asList(aliases));
            configList.add(config);

            return this;
        }

        public ColumnMappingConfigsHolderBuilder withMandatoryDouble(String columnName, String... aliases) {
            ColumnMappingConfig config = new ColumnMappingConfig();
            config.setName(columnName);
            config.setMandatory(true);
            config.setType(ColumnType.DOUBLE);
            config.setAliases(Arrays.asList(aliases));
            configList.add(config);

            return this;
        }

        public ColumnMappingConfigsHolderBuilder withType(ReportType type) {
            this.type = type;

            return this;
        }

        public ColumnMappingConfigsHolder build() {
            ColumnMappingConfigsHolder holder = new ColumnMappingConfigsHolder();
            holder.addConfigList(configList, type.getConfigFileName());

            return holder;
        }
    }
}
