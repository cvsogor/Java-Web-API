package com.aditor.dashboard.spring;

import com.aditor.dashboard.spring.model.*;
import com.google.api.client.util.DateTime;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

/**
 * Created by art on 12/2/15.
 */
@Repository
public class Dao {

    public static final String INSTALL_EVENT_NAME = "Install";
    private Logger logger = LoggerFactory.getLogger(getClass());

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public long importDataFileIntoTempTable(String query) {
        return jdbcTemplate.getJdbcOperations().update(query);
    }

    public long importCostsFile(String filePath, String fileName)
    {
        try
        {
            filePath = filePath.replaceAll("\\\\","\\\\\\\\"); //fix for windows path backslash
            String query = "LOAD DATA LOCAL INFILE '" + filePath + "' INTO TABLE " + fileName + " FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 ROWS";
            return jdbcTemplate.getJdbcOperations().update(query);
        }
        catch (Exception e)
        {
            logger.error("DB import error: ", e);
        }
        return -1;
    }

    private void updateColumnValue(String tempTableName, String columnName, String columnValue) {
        String query = "update " + tempTableName + " set " + columnName + " = \"" + columnValue + "\" where " + columnName + " is null; ";
        jdbcTemplate.getJdbcOperations().update(query);
        //logger.info("Query: " + query);
    }

    public int copyTempTableToPermanent(ReportType reportType, AppsflyerImportConfig importEntry, AppsflyerTableType tableType) {
        String sql = null;
        if(reportType == ReportType.INSTALL){
            sql = "INSERT IGNORE INTO " + tableType.getTableName() + "\n" +
                    " (event_name, original_event_name, event_time, impression_time, click_time, install_time, agency_pmd,\n" +
                    " media_source, channel, keywords, campaign_name, campaign_id, adset_name, adset_id, ad_name, ad_id,\n" +
                    " ad_type, site_id, cost_model, cost, cost_currency, region, country_code, state, city, ip, wifi, language, event_source, appsflyer_device_id, customer_user_id,\n" +
                    " idfa, idfv, mac, device_name, devise_type, os_version, sdk_version, app_version, sub_param_1, sub_param_2, sub_param_3, sub_param_4, sub_param_5, impression_url,\n" +
                    " click_url, http_referrer, contributor_1_media_source, contributor_1_campaign, contributor_2_media_source, contributor_2_campaign, contributor_3_media_source,\n" +
                    " contributor_3_campaign, android_id, imei, advertising_id, operator, carrier, installer_package,\n" +
                    " reported_platform, platform_type, advertiser, offer_id, offer_name, offer_url, offer_preview_url, os, business_model, commission, import_time )\n" +
                    " SELECT \"" + INSTALL_EVENT_NAME + "\",\"" + INSTALL_EVENT_NAME + "\", install_time, impression_time,\n" +
                    "click_time, install_time, agency_pmd, media_source, channel, keywords, campaign_name, campaign_id,\n" +
                    " adset_name, adset_id, ad_name, ad_id, ad_type, site_id, cost_model, cost, cost_currency, region, country_code, state, city, ip, wifi,\n" +
                    " language, event_source, appsflyer_device_id, customer_user_id, idfa, idfv, mac, device_name, devise_type, os_version, sdk_version,\n" +
                    " app_version, sub_param_1, sub_param_2, sub_param_3, sub_param_4, sub_param_5, impression_url, click_url, http_referrer,\n" +
                    " contributor_1_media_source, contributor_1_campaign, contributor_2_media_source, contributor_2_campaign, contributor_3_media_source,\n" +
                    " contributor_3_campaign, android_id, imei, advertising_id, operator, carrier, installer_package,\n" +
                    " :platform_name, :platform_type, :advertiser, :offer_id, :offer_name, :offer_url, :offer_preview_url, :os, :business_model, :commission, :import_time \n" +
                    " FROM appsflyer_tmp_install_events";
        }else if(reportType == ReportType.IN_APP_EVENTS){
            sql = " INSERT IGNORE INTO " + tableType.getTableName() + " (impression_time, click_time, install_time, event_time, event_name, original_event_name, event_value,\n" +
                    "event_revenue, currency, receipt_id, is_validated, agency_pmd, media_source, channel,\n" +
                    "keywords, campaign_name, campaign_id, adset_name, adset_id, ad_name, ad_id, ad_type, site_id, cost_model, cost,\n" +
                    "cost_currency, region, country_code, state, city, ip, wifi, language, event_source, appsflyer_device_id,\n" +
                    "customer_user_id, idfa, idfv, mac, device_name, devise_type, os_version, sdk_version, app_version, sub_param_1,\n" +
                    "sub_param_2, sub_param_3, sub_param_4, sub_param_5, impression_url, click_url, http_referrer, contributor_1_media_source,\n" +
                    "contributor_1_campaign, contributor_2_media_source, contributor_2_campaign, contributor_3_media_source, contributor_3_campaign,\n" +
                    "android_id, imei, advertising_id, operator, carrier, installer_package,\n" +
                    "reported_platform, platform_type, advertiser, offer_id, offer_name, offer_url, offer_preview_url, os, business_model, commission, import_time)\n" +
                    "SELECT impression_time, click_time, install_time, event_time, normalized_event_name, event_name,\n" +
                    "event_value, event_revenue, currency, receipt_id, is_validated, agency_pmd, media_source, channel,\n" +
                    "keywords, campaign_name, campaign_id, adset_name, adset_id, ad_name, ad_id, ad_type, site_id, cost_model, cost, cost_currency, region,\n" +
                    "country_code, state, city, ip, wifi, language, event_source, appsflyer_device_id, customer_user_id, idfa, idfv, mac, device_name, devise_type,\n" +
                    "os_version, sdk_version, app_version, sub_param_1, sub_param_2, sub_param_3, sub_param_4, sub_param_5, impression_url, click_url, http_referrer,\n" +
                    "contributor_1_media_source, contributor_1_campaign, contributor_2_media_source, contributor_2_campaign, contributor_3_media_source,\n" +
                    "contributor_3_campaign, android_id, imei, advertising_id, operator, carrier, installer_package,\n" +
                    " :platform_name, :platform_type, :advertiser, :offer_id, :offer_name, :offer_url, :offer_preview_url, :os, :business_model, :commission, :import_time \n" +
                    " FROM appsflyer_tmp_inapp_events";
        }

        final String query = sql;

        Map namedParameters = importEntry.getNamedParametersMap();
        namedParameters.put("import_time", Timestamp.valueOf(LocalDateTime.now()));
        int rowCount = jdbcTemplate.update(query, namedParameters);

//        jdbcTemplate.getJdbcOperations().execute("update appsflyer_events set impression_time = NULL where impression_time = \"0000-00-00 00:00:00\"");
        logger.info("Successfully copied from temp table to permanent");
        return rowCount;
    }

    public void truncateTempTable(ReportType reportType) {
        String sql = "truncate table " + reportType.getTempTablename();
        jdbcTemplate.getJdbcOperations().execute(sql);
        logger.info("Table " + reportType.getTempTablename() + " truncated");
    }

    public String getQuerySQL(String offerName, String country, String agency, String mediaSource, String campaign, String siteId){
        StringBuilder sb = new StringBuilder();
        addQuerySingleField(country, sb, "country_code");
        addQuerySingleField(offerName, sb, "offer_name");
        addQuerySingleField(agency, sb, "agency_pmd");
        addQuerySingleField(campaign, sb, "campaign_name");
        addQuerySingleField(mediaSource, sb, "media_source");
        addQuerySingleField(siteId, sb, "site_id");
        return sb.toString();
    }

    private void addQuerySingleField(String fieldValue, StringBuilder sb, String fieldName) {
        if(!StringUtils.isEmpty(fieldValue) && !StringUtils.isEmpty(fieldValue.trim())){
            sb.append(" and " + fieldName + "=\"" + fieldValue + "\" ");
        }
    }


    public Map<GroupedFields, Long> countInstallsByDayGF(Date fromDate, Date toDate, String offerName, String country, String agency, String mediaSource, String campaign, String siteId){


        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
        String query = "select DATE(install_time), offer_name, agency_pmd, media_source, count(*) from appsflyer_events where install_time between ? and ? and event_name = ? " + queryStr + " group by DATE(install_time), offer_name, agency_pmd, media_source";
        Object[] args = {fromDate, toDate, INSTALL_EVENT_NAME};

        List<ImmutablePair<GroupedFields, Long>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFields, Long>>() {
            public ImmutablePair<GroupedFields, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {

                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String media_source = rs.getString(4);
                return ImmutablePair.of(new GroupedFields(new Date(date.getTime()), offerName, agency, media_source), rs.getLong(5));
            }
        });



        Map<GroupedFields, Long> result = new HashMap<GroupedFields, Long>();
        for (ImmutablePair<GroupedFields, Long> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : " + query + " with params " + fromDate + " ," + toDate + ", " + INSTALL_EVENT_NAME + "we have count : " + result.size());
        return result;
    }

    public Map<GroupedFields, DailyData> countDepositsByDayGF(Date fromDate, Date toDate, int dayNum, String offerName, String country, String agency, final String mediaSource, String campaign, String siteId){
        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);

        String query = "select DATE(install_time), offer_Name, agency_pmd, media_Source, count(*), sum(event_revenue) from appsflyer_events where DATE(DATE_ADD(install_time,INTERVAL ? DAY)) = DATE(event_time) " +
                "and install_time between ? and ? " +
                queryStr +
                "GROUP BY DATE(install_time), offer_Name, agency_pmd, media_Source;\n";
        Object[] args = {dayNum, fromDate, toDate};
        List<ImmutablePair<GroupedFields, DailyData>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFields, DailyData>>() {
            public ImmutablePair<GroupedFields, DailyData> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String mediaSource = rs.getString(4);
                return ImmutablePair.of(new GroupedFields(new Date(date.getTime()), offerName, agency, mediaSource), new DailyData(0, rs.getLong(5), rs.getDouble(6),0));
            }
        });

        Map<GroupedFields, DailyData> result = new HashMap<GroupedFields, DailyData>();
        for (ImmutablePair<GroupedFields, DailyData> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : " + query + " with params " + dayNum + " ," + fromDate + "," + toDate + "we have count : " + result.size());

        return result;
    }

    public Map<GroupedFields, CountAndSum> countFdsByDayGF(Date fromDate, Date toDate, int dayNum, final String offerName, String country, final String agency, final String mediaSource, String campaign, String siteId){
        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
        String query = "select DATE(install_time), offer_Name, agency_pmd, media_Source, count(*), sum(event_revenue) from appsflyer_events where DATE(DATE_ADD(install_time,INTERVAL ? DAY)) = DATE(event_time) " +
                "and install_time between ? and ? " +
                queryStr +
                "GROUP BY DATE(install_time), offer_Name, agency_pmd, media_Source;";
        Object[] args = {dayNum, fromDate, toDate};
        List<ImmutablePair<GroupedFields, CountAndSum>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFields, CountAndSum>>() {
            public ImmutablePair<GroupedFields, CountAndSum> mapRow(ResultSet rs, int rowNum) throws SQLException {

                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String mediaSource = rs.getString(4);
                return ImmutablePair.of(new GroupedFields(new Date(date.getTime()), offerName, agency, mediaSource), new CountAndSum(rs.getLong(5), rs.getDouble(6)));
            }
        });

        Map<GroupedFields, CountAndSum> result = new HashMap<GroupedFields, CountAndSum>();
        for (ImmutablePair<GroupedFields, CountAndSum> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : " + query + " with params " + dayNum + " ," + fromDate + "," + toDate + "we have count : " + result.size());

        return result;
    }

/*----------------------------Report with Country aka second ---------------------------------------------------*/



    public Map<GroupedFieldsSecond, Long> countInstallsByDayGFSecond(Date fromDate, Date toDate, String offerName, String country, String agency, String mediaSource, String campaign, String siteId){


        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
        String query = "select DATE(install_time), offer_name, agency_pmd, media_source, country_code, count(*) from appsflyer_events where install_time between ? and ? and event_name = ? " + queryStr + " group by DATE(install_time), offer_name, agency_pmd, media_source";
        Object[] args = {fromDate, toDate, INSTALL_EVENT_NAME};

        List<ImmutablePair<GroupedFieldsSecond, Long>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFieldsSecond, Long>>() {
            public ImmutablePair<GroupedFieldsSecond, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {

                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String media_source = rs.getString(4);
                String country_code = rs.getString(5);
                return ImmutablePair.of(new GroupedFieldsSecond (new Date(date.getTime()), offerName, agency, media_source, country_code), rs.getLong(6));
            }
        });



        Map<GroupedFieldsSecond, Long> result = new HashMap<GroupedFieldsSecond, Long>();
        for (ImmutablePair<GroupedFieldsSecond, Long> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : "+query + " with params " + fromDate + " ," + toDate  + ", "+ INSTALL_EVENT_NAME + "we have count : "+result.size());
        return result;
    }

    public Map<GroupedFieldsSecond, DailyData> countDepositsByDayGFSecond(Date fromDate, Date toDate, int dayNum, String offerName, String country, String agency, final String mediaSource, String campaign, String siteId){
        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);

        String query = "select DATE(install_time), offer_Name, agency_pmd, media_Source, country_code, count(*), sum(event_revenue) from appsflyer_events where DATE(DATE_ADD(install_time,INTERVAL ? DAY)) = DATE(event_time) " +
                "and install_time between ? and ? " +
                queryStr +
                "GROUP BY DATE(install_time), offer_Name, agency_pmd, media_Source;\n";
        Object[] args = {dayNum, fromDate, toDate};
        List<ImmutablePair<GroupedFieldsSecond, DailyData>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFieldsSecond, DailyData>>() {
            public ImmutablePair<GroupedFieldsSecond, DailyData> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String mediaSource = rs.getString(4);
                String country = rs.getString(5);
                return ImmutablePair.of(new GroupedFieldsSecond(new Date(date.getTime()), offerName, agency, mediaSource, country), new DailyData(0, rs.getLong(6), rs.getDouble(7),0));
            }
        });

        Map<GroupedFieldsSecond, DailyData> result = new HashMap<GroupedFieldsSecond, DailyData>();
        for (ImmutablePair<GroupedFieldsSecond, DailyData> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : "+query + " with params " + dayNum + " ," + fromDate + ","+toDate+ "we have count : "+result.size());

        return result;
    }

    public Map<GroupedFieldsSecond, CountAndSum> countFdsByDayGFSecond(Date fromDate, Date toDate, int dayNum, final String offerName, String country, final String agency, final String mediaSource, String campaign, String siteId){
        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
        String query = "select DATE(install_time), offer_Name, agency_pmd, media_Source, country_code, count(*), sum(event_revenue) from appsflyer_events where DATE(DATE_ADD(install_time,INTERVAL ? DAY)) = DATE(event_time) " +
                "and install_time between ? and ? " +
                queryStr +
                "GROUP BY DATE(install_time), offer_Name, agency_pmd, media_Source;";
        Object[] args = {dayNum, fromDate, toDate};
        List<ImmutablePair<GroupedFieldsSecond, CountAndSum>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFieldsSecond, CountAndSum>>() {
            public ImmutablePair<GroupedFieldsSecond, CountAndSum> mapRow(ResultSet rs, int rowNum) throws SQLException {

                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String mediaSource = rs.getString(4);
                String country = rs.getString(5);
                return ImmutablePair.of(new GroupedFieldsSecond(new Date(date.getTime()), offerName, agency, mediaSource,country), new CountAndSum(rs.getLong(6), rs.getDouble(7)));
            }
        });

        Map<GroupedFieldsSecond, CountAndSum> result = new HashMap<GroupedFieldsSecond, CountAndSum>();
        for (ImmutablePair<GroupedFieldsSecond, CountAndSum> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : "+query + " with params " + dayNum + " ," + fromDate + ","+toDate+ "we have count : "+result.size());

        return result;
    }





// Third report with CAMPAIGN and SITE ID -----------------------------------------------------------------------




    public Map<GroupedFieldsThird, Long> countInstallsByDayGFThird(Date fromDate, Date toDate, String offerName, String country, String agency, String mediaSource, String campaign, String siteId){


        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
        String query = "select DATE(install_time), offer_name, agency_pmd, media_source, country_code, campaign_name, site_id, count(*) from appsflyer_events where install_time between ? and ? and event_name = ? " + queryStr + " group by DATE(install_time), offer_name, agency_pmd, media_source";
        Object[] args = {fromDate, toDate, INSTALL_EVENT_NAME};

        List<ImmutablePair<GroupedFieldsThird, Long>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFieldsThird, Long>>() {
            public ImmutablePair<GroupedFieldsThird, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {

                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String media_source = rs.getString(4);
                String country_code = rs.getString(5);
                String campaign = rs.getString(6);
                String siteId = rs.getString(7);

                return ImmutablePair.of(new GroupedFieldsThird(new Date(date.getTime()), offerName, agency, media_source, country_code, campaign, siteId), rs.getLong(8));
            }
        });



        Map<GroupedFieldsThird, Long> result = new HashMap<GroupedFieldsThird, Long>();
        for (ImmutablePair<GroupedFieldsThird, Long> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : "+query + " with params " + fromDate + " ," + toDate  + ", "+ INSTALL_EVENT_NAME + "we have count : "+result.size());
        return result;
    }

    public Map<GroupedFieldsThird, DailyData> countDepositsByDayGFThird(Date fromDate, Date toDate, int dayNum, String offerName, String country, String agency, final String mediaSource, String campaign, String siteId){
        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);

        String query = "select DATE(install_time), offer_Name, agency_pmd, media_Source, country_code, campaign_name, site_id, count(*), sum(event_revenue) from appsflyer_events where DATE(DATE_ADD(install_time,INTERVAL ? DAY)) = DATE(event_time) " +
                "and install_time between ? and ? " +
                queryStr +
                "GROUP BY DATE(install_time), offer_Name, agency_pmd, media_Source;\n";
        Object[] args = {dayNum, fromDate, toDate};
        List<ImmutablePair<GroupedFieldsThird, DailyData>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFieldsThird, DailyData>>() {
            public ImmutablePair<GroupedFieldsThird, DailyData> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String mediaSource = rs.getString(4);
                String country = rs.getString(5);
                String campaign = rs.getString(6);
                String siteId = rs.getString(7);

                return ImmutablePair.of(new GroupedFieldsThird(new Date(date.getTime()), offerName, agency, mediaSource, country, campaign, siteId), new DailyData(0, rs.getLong(8), rs.getDouble(9),0));
            }
        });

        Map<GroupedFieldsThird, DailyData> result = new HashMap<GroupedFieldsThird, DailyData>();
        for (ImmutablePair<GroupedFieldsThird, DailyData> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : "+query + " with params " + dayNum + " ," + fromDate + ","+toDate+ "we have count : "+result.size());

        return result;
    }

    public Map<GroupedFieldsThird, CountAndSum> countFdsByDayGFThird(Date fromDate, Date toDate, int dayNum, final String offerName, String country, final String agency, final String mediaSource, String campaign, String siteId){
        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
        String query = "select DATE(install_time), offer_Name, agency_pmd, media_Source, country_code, campaign_name, site_id,  count(*), sum(event_revenue) from appsflyer_events where DATE(DATE_ADD(install_time,INTERVAL ? DAY)) = DATE(event_time) " +
                "and install_time between ? and ? " +
                queryStr +
                "GROUP BY DATE(install_time), offer_Name, agency_pmd, media_Source;";
        Object[] args = {dayNum, fromDate, toDate};
        List<ImmutablePair<GroupedFieldsThird, CountAndSum>> countList = jdbcTemplate.getJdbcOperations().query(query, args, new RowMapper<ImmutablePair<GroupedFieldsThird, CountAndSum>>() {
            public ImmutablePair<GroupedFieldsThird, CountAndSum> mapRow(ResultSet rs, int rowNum) throws SQLException {

                Date date = rs.getDate(1);
                String offerName = rs.getString(2);
                String agency = rs.getString(3);
                String mediaSource = rs.getString(4);
                String country = rs.getString(5);
                String campaign = rs.getString(6);
                String siteId = rs.getString(7);
                return ImmutablePair.of(new GroupedFieldsThird(new Date(date.getTime()), offerName, agency, mediaSource, country, campaign, siteId), new CountAndSum(rs.getLong(8), rs.getDouble(9)));
            }
        });

        Map<GroupedFieldsThird, CountAndSum> result = new HashMap<GroupedFieldsThird, CountAndSum>();
        for (ImmutablePair<GroupedFieldsThird, CountAndSum> dateLongImmutablePair : countList) {
            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
        }

        System.out.println("For the request : "+query + " with params " + dayNum + " ," + fromDate + ","+toDate+ "we have count : "+result.size());

        return result;
    }

    class OptimizedReportRowMapper implements RowMapper {
        @Override
        public OptimizedReportData mapRow(ResultSet resultSet, int i) throws SQLException {
            String groupByFild = resultSet.getString(1);
            String offerId = resultSet.getString(2);
            double revenue = resultSet.getDouble(3);
            double cost = resultSet.getDouble(4);
            double impressions = resultSet.getDouble(5);
            double clicks = resultSet.getDouble(6);
            double ctr = resultSet.getDouble(7);
            double installs = resultSet.getDouble(8);
            double ecpi = resultSet.getDouble(9);
            double fdCount = resultSet.getDouble(10);
            double fdAmount = resultSet.getDouble(11);
            double depositsCount = resultSet.getDouble(12);
            double depositsAmount = resultSet.getDouble(13);
            double instalsDepositAmount = resultSet.getDouble(14);
            double ecpa = resultSet.getDouble(15);
            double instalToRfd = resultSet.getDouble(16);
            double arpu = resultSet.getDouble(17);
            double arppu = resultSet.getDouble(18);
            double cpc = resultSet.getDouble(19);
            double roi = resultSet.getDouble(20);
            double cti = resultSet.getDouble(21);
            double ctd = resultSet.getDouble(22);
            double ecpm = resultSet.getDouble(23);

            return new OptimizedReportData(groupByFild, offerId, revenue, cost, impressions, clicks, ctr, installs, ecpi, fdCount, fdAmount, depositsCount, depositsAmount, instalsDepositAmount, ecpa, instalToRfd, arpu, arppu, cpc, roi, cti, ctd, ecpm);
        }
    }

    public String optimizedReportCountry(Date fromDate, Date toDate){
        List<OptimizedReportData> list = jdbcTemplate.getJdbcOperations().query("CALL appsflyer.generate_country_report(?,?)", new OptimizedReportRowMapper(), fromDate, toDate);
        logger.debug("After call optimizedReportCountry() with params " + fromDate + ","+toDate+ "we have count : "+list.size());
        return listToCsv(list);
    }

    public String optimizedReportOffer(Date fromDate, Date toDate){
        List<OptimizedReportData> list = jdbcTemplate.getJdbcOperations().query("CALL appsflyer.generate_offer_report(?,?)", new OptimizedReportRowMapper(), fromDate, toDate);
        logger.debug("After call optimizedReportOffer() with params " + fromDate + ","+toDate+ "we have count : "+list.size());
        return listToCsv(list);
    }

    public String optimizedReportCampaing(Date fromDate, Date toDate){
        List<OptimizedReportData> list = jdbcTemplate.getJdbcOperations().query("CALL appsflyer.generate_campaing_report(?,?)", new OptimizedReportRowMapper(), fromDate, toDate);
        logger.debug("After call optimizedReportCampaing() with params " + fromDate + ","+toDate+ "we have count : "+list.size());
        return listToCsv(list);
    }

    public String optimizedReportSiteId(Date fromDate, Date toDate){
        List<OptimizedReportData> list = jdbcTemplate.getJdbcOperations().query("CALL appsflyer.generate_siteid_report(?,?)", new OptimizedReportRowMapper(), fromDate, toDate);
        logger.debug("After call optimizedReportSiteId() with params " + fromDate + ","+toDate+ "we have count : "+list.size());
        return listToCsv(list);
    }

    public String optimizedReportWeekday(Date fromDate, Date toDate){
        List<OptimizedReportData> list = jdbcTemplate.getJdbcOperations().query("CALL appsflyer.generate_weekday_report(?,?)", new OptimizedReportRowMapper(), fromDate, toDate);
        logger.debug("After call optimizedReportWeekday() with params " + fromDate + ","+toDate+ "we have count : "+list.size());
        return listToCsv(list);
    }

    public String optimizedReportDeviceType(Date fromDate, Date toDate){
        List<OptimizedReportData> list = jdbcTemplate.getJdbcOperations().query("CALL appsflyer.generate_devise_type_report(?,?)", new OptimizedReportRowMapper(), fromDate, toDate);
        logger.debug("After call optimizedReportDeviceType() with params " + fromDate + ","+toDate+ "we have count : "+list.size());
        return listToCsv(list);
    }

    public String optimizedReportOsVersion(Date fromDate, Date toDate){
        List<OptimizedReportData> list = jdbcTemplate.getJdbcOperations().query("CALL appsflyer.generate_os_version_report(?,?)", new OptimizedReportRowMapper(), fromDate, toDate);
        logger.debug("After call optimizedReportOsVersion() with params " + fromDate + ","+toDate+ "we have count : "+list.size());
        return listToCsv(list);
    }

    public String optimizedReportMonth(Date fromDate, Date toDate){
        List<OptimizedReportData> list = jdbcTemplate.getJdbcOperations().query("CALL appsflyer.generate_month_report(?,?)", new OptimizedReportRowMapper(), fromDate, toDate);
        logger.debug("After call optimizedReportMonth() with params " + fromDate + ","+toDate+ "we have count : "+list.size());
        return listToCsv(list);
    }

    private String listToCsv(List<OptimizedReportData> list){
        StringBuffer result = new StringBuffer();
        for (OptimizedReportData data : list) {
            result.append(data.toCSV()+"\n");
        }
        return result.toString();
    }

    public String getSettings(String settingName) throws SQLException {
        String query = "SELECT value FROM appsflyer.settings\n" +
                "where setting_key = ?;";
        String setting = jdbcTemplate.getJdbcOperations().query(connection -> {
            PreparedStatement preparedStatement1 = connection.prepareStatement(query);
            preparedStatement1.setString(1, settingName);
            return preparedStatement1;
        }, resultSet -> {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return null;
        });
        return setting;
    }

    public void setNormalizeEventName(String normalizedEventName, String whereStatement) throws SQLException {
        String query = "UPDATE appsflyer_tmp_inapp_events\n" +
                "SET normalized_event_name = " + '"' + normalizedEventName + '"' +  '\n' +
                "WHERE " + whereStatement;
        query = query.split(";")[0];
        logger.info("Normalize Event Names : " + whereStatement + " with: " + normalizedEventName);
        jdbcTemplate.getJdbcOperations().execute(query);
    }

    public void setNullableNormalizeEventNames() throws SQLException {
        String query = "UPDATE appsflyer_tmp_inapp_events\n" +
                "SET normalized_event_name = event_name\n" +
                "WHERE normalized_event_name IS NULL";
        jdbcTemplate.getJdbcOperations().execute(query);
    }

    public int deleteRowsWithNullColumn(ReportType type, String column) {
        String query = "DELETE FROM " + type.getTempTablename() + " WHERE (" + column + " IS NULL OR " + column + " = '')";
        return jdbcTemplate.getJdbcOperations().update(query);
    }

    public void setLastExportedEntry(int lastId ){
        String query = "REPLACE INTO settings\n" +
                "VALUES (\"last_imported_to_cooladata\", ?);";

        jdbcTemplate.getJdbcOperations().update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(lastId));
            return preparedStatement;
        });
    }

    public List<AppsflyerImportConfig> getAppsflyerImportConfig() {
        String query = "SELECT * FROM appsflyer_import_config";
        return jdbcTemplate.getJdbcOperations().query(query, new BeanPropertyRowMapper(AppsflyerImportConfig.class));
    }

    public List<AppsflyerImportConfig> getAppsflyerImportConfigFull() {
        String query = "SELECT * FROM appsflyer_import_config_full";
        return jdbcTemplate.getJdbcOperations().query(query, new BeanPropertyRowMapper(AppsflyerImportConfig.class));
    }

    public List<AppsflyerImportConfig> selectOneOfferFromAppsflyerImportConfig(String offerId) {
        String query = "SELECT * FROM appsflyer_import_config \n" +
                "WHERE offer_id = :offer_id";
        Map namedParameters = new HashMap<String, String>();
        namedParameters.put("offer_id", offerId);
        return jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper(AppsflyerImportConfig.class));
    }

    public List<AppsflyerImportConfig> selectOneOfferFromAppsflyerImportConfigFull(String offerId) {
        String query = "SELECT * FROM appsflyer_import_config_full \n" +
                "WHERE offer_id = :offer_id";
        Map namedParameters = new HashMap<String, String>();
        namedParameters.put("offer_id", offerId);
        return jdbcTemplate.query(query, namedParameters, new BeanPropertyRowMapper(AppsflyerImportConfig.class));
    }

    public boolean cooladataExportFilterIsEmpty() {
        return !jdbcTemplate.getJdbcOperations().queryForObject("select exists (select offer_id from cooladata_export_filter)" , Boolean.class);
    }

    public List<CooladataExportEntry> selectNewEntry(int firstId, int limit) {
        return selectCooladataExportList(String.format("id > %1$d",firstId,limit), String.format("\n\tLIMIT %1$d", limit));
    }

    public List<CooladataExportEntry> selectCooladataExportList(String whereClause, String limitClause) {
        logger.info("selectCooladataExportList Where clause = " + whereClause + " Limit lause = " + limitClause);
        boolean filterIsEmpty = cooladataExportFilterIsEmpty();

        String filteringSelect = "\t\tINNER JOIN cooladata_export_filter as D\n" +
                "\t\tON ((D.offer_id IS NOT NULL AND C.offer_id = D.offer_id) \n" +
                "\t\t\tOR D.offer_id IS NULL) \n" +
                "\t\tAND ((D.event_name IS NOT NULL AND C.event_name = D.event_name) \n" +
                "\t\t\tOR (D.event_name IS NULL)) \n" +
                "\t\tAND ((D.agency_pmd IS NOT NULL AND C.agency_pmd = D.agency_pmd) \n" +
                "\t\t\tOR D.agency_pmd IS NULL) \n";

        String query = "SELECT id, impression_time, click_time, install_time, event_time, event_name, original_event_name, \n" +
                "event_value, event_revenue, currency, receipt_id, is_validated, agency_pmd, A.media_source, channel, \n" +
                "keywords, campaign_name, site_id, campaign_id, adset_name, adset_id, ad_name, ad_id, ad_type, cost_model, \n" +
                "cost, cost_currency, region, country_code, state, city, ip, wifi, language, event_source, appsflyer_device_id, \n" +
                "customer_user_id, idfa, idfv, android_id, imei, mac, device_name, advertising_id, devise_type, os_version, sdk_version, \n" +
                "app_version, operator, carrier, sub_param_1, sub_param_2, sub_param_3, sub_param_4, sub_param_5, impression_url, \n" +
                "click_url, http_referrer, installer_package, contributor_1_media_source, contributor_1_campaign, contributor_2_media_source, \n" +
                "contributor_2_campaign, contributor_3_media_source, contributor_3_campaign, os, creative, targeting_type, targeting_group, \n" +
                "gender, bid, real_cost, real_revenue, A.reported_platform, platform_type, advertiser, A.offer_id, offer_name, offer_url, \n" +
                "offer_preview_url, calc_cost, A.business_model, commission, department, am_name, cooladata_username, permission_type \n" +
                "FROM \n" +
                "\t(SELECT id, impression_time, click_time, install_time, event_time, C.event_name, original_event_name, \n" +
                "\t\tevent_value, event_revenue, currency, receipt_id, is_validated, C.agency_pmd, media_source, channel, \n" +
                "\t\tkeywords, campaign_name, site_id, campaign_id, adset_name, adset_id, ad_name, ad_id, ad_type, cost_model, \n" +
                "\t\tcost, cost_currency, region, country_code, state, city, ip, wifi, language, event_source, appsflyer_device_id, \n" +
                "\t\tcustomer_user_id, idfa, idfv, android_id, imei, mac, device_name, advertising_id, devise_type, os_version, sdk_version, \n" +
                "\t\tapp_version, operator, carrier, sub_param_1, sub_param_2, sub_param_3, sub_param_4, sub_param_5, impression_url, \n" +
                "\t\tclick_url, http_referrer, installer_package, contributor_1_media_source, contributor_1_campaign, contributor_2_media_source, \n" +
                "\t\tcontributor_2_campaign, contributor_3_media_source, contributor_3_campaign, os, creative, targeting_type, targeting_group, \n" +
                "\t\tgender, bid, real_cost, real_revenue, reported_platform, platform_type, advertiser, C.offer_id, offer_name, offer_url, \n" +
                "\t\toffer_preview_url, calc_cost, business_model, commission \n" +
                "\tFROM appsflyer_events as C \n" +
                "%1$s" +
                "\t\tWHERE (%2$s) %3$s ) AS A \n" +
                "\tLEFT JOIN (select * from account_management) AS B \n" +
                "\tON ((B.offer_id IS NOT NULL AND A.offer_id = B.offer_id) \n" +
                "\t\tOR B.offer_id IS NULL) \n" +
                "\tAND A.reported_platform = B.reported_platform \n" +
                "\tAND A.business_model = B.business_model \n" +
                "\tAND ((B.media_source IS NOT NULL AND A.media_source = B.media_source) \n" +
                "\t\tOR B.media_source IS NULL) \n" +
                "ORDER BY id";
        query = String.format(query, filterIsEmpty ? "" : filteringSelect, whereClause.split(";")[0], limitClause.split(";")[0]);
        return jdbcTemplate.query(query, new BeanPropertyRowMapper(CooladataExportEntry.class));
    }

    public void setFirstExportTime (Set<Integer> ids) {
        String query = "UPDATE appsflyer_events\n" +
                "SET first_export_time = :first_export_time \n" +
                "WHERE id IN (:ids) AND first_export_time IS NULL ";
        Map<String,Object> namedParameters = new HashMap<>();
        namedParameters.put("first_export_time", Timestamp.valueOf(LocalDateTime.now()));
        namedParameters.put("ids", ids);
        jdbcTemplate.update(query,namedParameters);
    }

    public Timestamp getLastEventTimeForOfferId(String offerId, ReportType reportType, AppsflyerTableType tableType) {
        logger.info("getLastEventTimeForOfferId offer id = " + offerId + " report type = " + reportType.getWebParam());
        String comparisonOperator = reportType == ReportType.INSTALL ? " = " : " <> ";
        String sql = "SELECT MAX(event_time) FROM " + tableType.getTableName() +" \n" +
                "WHERE offer_id = \'" + offerId + "\' AND event_name " + comparisonOperator + '\'' + INSTALL_EVENT_NAME + '\'';
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, Timestamp.class);
    }
}
