package com.aditor.dashboard.spring;

/**
 * Created by detan001 on 09.12.2015.
 */
public class temp {

    //countInstallsByDay, countFdsByDay, countDepositsByDay

//    public Map<Date, Long> countInstallsByDay(Date fromDate, Date toDate, String offerName, String country, String agency, String mediaSource, String campaign, String siteId){
//        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
//        String query = "select DATE(install_time) , count(*) from appsflyer_events where install_time between ? and ? and event_name = ? " + queryStr + " group by DATE(install_time)";
//        Object[] args = {fromDate, toDate, INSTALL_EVENT_NAME};
//        List<ImmutablePair<Date, Long>> countList = jdbcTemplate.query(query, args, new RowMapper<ImmutablePair<Date, Long>>() {
//            public ImmutablePair<Date, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
//                java.sql.Date date = rs.getDate(1);
//                return ImmutablePair.of(new Date(date.getTime()), rs.getLong(2));
//            }
//        });
//
//
//
//        Map<Date, Long> result = new HashMap<Date, Long>();
//        for (ImmutablePair<Date, Long> dateLongImmutablePair : countList) {
//            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
//        }
//        return result;
//    }

//    public Map<Date, Long> countFdsByDay(Date fromDate, Date toDate, String fdEventName, int dayNum, String offerName, String country, String agency, String mediaSource, String campaign, String siteId){
//        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
//        String query = "select DATE(install_time,), count(*) from appsflyer_events where DATE(DATE_ADD(install_time,INTERVAL ? DAY)) = DATE(event_time) " +
//                "and event_name = ? " +
//                "and install_time between ? and ? " +
//                queryStr +
//                "GROUP BY DATE(install_time);";
//        Object[] args = {dayNum, fdEventName, fromDate, toDate};
//        List<ImmutablePair<Date, Long>> countList = jdbcTemplate.query(query, args, new RowMapper<ImmutablePair<Date, Long>>() {
//            public ImmutablePair<Date, Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
//                java.sql.Date date = rs.getDate(1);
//                return ImmutablePair.of(new Date(date.getTime()), rs.getLong(2));
//            }
//        });
//
//        Map<Date, Long> result = new HashMap<Date, Long>();
//        for (ImmutablePair<Date, Long> dateLongImmutablePair : countList) {
//            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
//        }
//        return result;
//    }



//    public Map<Date, DailyData> countDepositsByDay(Date fromDate, Date toDate, String fdEventName, int dayNum, String offerName, String country, String agency, String mediaSource, String campaign, String siteId){
//        String queryStr = getQuerySQL(offerName, country, agency, mediaSource, campaign, siteId);
//
//        String query = "select DATE(install_time), count(*), sum(event_revenue) from appsflyer_events where DATE(DATE_ADD(install_time,INTERVAL ? DAY)) = DATE(event_time) " +
//                "and event_name = ? " +
//                "and install_time between ? and ? " +
//                queryStr +
//                "GROUP BY DATE(install_time);\n";
//        Object[] args = {dayNum, fdEventName, fromDate, toDate};
//        List<ImmutablePair<Date, DailyData>> countList = jdbcTemplate.query(query, args, new RowMapper<ImmutablePair<Date, DailyData>>() {
//            public ImmutablePair<Date, DailyData> mapRow(ResultSet rs, int rowNum) throws SQLException {
//                java.sql.Date date = rs.getDate(1);
//                return ImmutablePair.of(new Date(date.getTime()), new DailyData(0, rs.getLong(2), rs.getDouble(3)));
//            }
//        });
//
//        Map<Date, DailyData> result = new HashMap<Date, DailyData>();
//        for (ImmutablePair<Date, DailyData> dateLongImmutablePair : countList) {
//            result.put(dateLongImmutablePair.getLeft(), dateLongImmutablePair.getRight());
//        }
//        return result;
//    }


    }
