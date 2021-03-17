package com.aditor.bi.commercials.persistence;


import com.aditor.bi.commercials.domain.Commercial;
import com.aditor.bi.commercials.persistence.impl.SelectQueryBuilder;
import com.aditor.bi.commercials.persistence.impl.UpdateQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrackingRecordRepository {
    private final Logger logger = LoggerFactory.getLogger(TrackingRecordRepository.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TrackingRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getList(Commercial commercial) {
        String query = new SelectQueryBuilder()
                .withCommercial(commercial, "")
                .buildSelectSample();

        return jdbcTemplate.queryForList(query);
    }

    public List<Date> findUniqueDates(Commercial commercial) {

        String query = new SelectQueryBuilder()
                .withCommercial(commercial, "")
                .buildDateRange();

        logger.info("Executing query: " + query);

        return jdbcTemplate.query(query, rs -> {
            List<Date> dates = new ArrayList<>();

            while(rs.next()) {
                dates.add(rs.getDate(1));
            }

            return dates;
        });
    }

    public int updateCosts(Commercial commercial, Double revenue, Double payout) {
        String query = new UpdateQueryBuilder()
                .withCommercial(commercial, "")
                .withRevenue(revenue)
                .withPayout(payout)
                .buildGlobalUpdate();

        logger.info("Updating commercial #" + commercial.getId() + " costs using query: " + query);

        return jdbcTemplate.update(query);
    }

    public int updateCosts(Commercial commercial, Double revenue, Double payout, Date date) {
        String query = new UpdateQueryBuilder()
                .withCommercial(commercial, "")
                .withRevenue(revenue)
                .withPayout(payout)
                .withEventDate(date)
                .buildDateUpdate();

        logger.info("Updating commercial #" + commercial.getId() + " costs using query: " + query);

        return jdbcTemplate.update(query);
    }

    public int updateCosts(Commercial commercial, Double revenue, Double payout, Long startingId) {
        String query = new UpdateQueryBuilder()
                .withCommercial(commercial, "")
                .withRevenue(revenue)
                .withPayout(payout)
                .withStartingId(startingId)
                .buildDailyUpdate();

        logger.info("Updating commercial #" + commercial.getId() + " costs using query: " + query);

        return jdbcTemplate.update(query);
    }

    public int resetCosts(Commercial commercial) {
        String query = new UpdateQueryBuilder()
                .withCommercial(commercial, "")
                .withRevenue(null)
                .withPayout(null)
                .buildGlobalUpdate();

        logger.info("Resetting commercial #" + commercial.getId() + " cost and payout values." + query);

        return jdbcTemplate.update(query);
    }

    public Long getMaxRecordId(Commercial commercial) {
        String query = new SelectQueryBuilder()
                .withCommercial(commercial, "")
                .buildSelectMaxRecordId();

        return jdbcTemplate.query(query, rs -> {
            boolean success = rs.first();
            return success ? rs.getLong(1) : 0;
        });
    }

    public List<String> getMediaSources() {
        String query = "SELECT DISTINCT media_source FROM appsflyer_events;";

        return jdbcTemplate.query(query, rs -> {
            List<String> mediaSources = new ArrayList<>();

            while(rs.next()) {
                mediaSources.add(rs.getString(1));
            }

            return mediaSources;
        });
    }

    public List<String> getColumnsList() {
        String query = "SELECT COLUMN_NAME FROM information_schema.columns WHERE table_name = 'appsflyer_events' AND table_schema = 'appsflyer' AND COLUMN_NAME != 'id' AND DATA_TYPE = 'varchar';";

        return jdbcTemplate.query(query, rs -> {
            List<String> columns = new ArrayList<>();

            while(rs.next()) {
                columns.add(rs.getString(1));
            }

            return columns;
        });
    }

    public Map<String, Set<String>> getUniqueFieldValues() {
        List<String> columns = Arrays.asList("campaign_name", "city", "country_code", "wifi", "city", "os", "os_version");
        String query = "SELECT DISTINCT " + columns.stream().reduce((s1, s2) -> s1 + "," + s2).get() + " FROM appsflyer_events;";

        logger.info("Loading new field value list with query: " + query);

        return jdbcTemplate.query(query, rs -> {
            Map<String, Set<String>> columnValues = new HashMap<String, Set<String>>();
            for(int i = 0; i < columns.size(); i++) {
                columnValues.put(columns.get(i), new HashSet<String>());
            }

            while(rs.next()) {
                for(int i = 0; i < columns.size(); i++) {
                    String nextValue = rs.getString(i + 1);
                    if(nextValue != null && !nextValue.isEmpty()) {
                        columnValues.get(columns.get(i)).add(nextValue);
                    }
                }
            }

            return columnValues;
        });
    }

    public List<String> getOfferIds() {
        String query = "SELECT DISTINCT offer_id FROM appsflyer_events;";

        return jdbcTemplate.query(query, rs -> {
            List<String> offerIds = new ArrayList<>();

            while(rs.next()) {
                offerIds.add(rs.getString(1));
            }

            return offerIds;
        });
    }
}
