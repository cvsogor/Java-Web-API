package com.aditor.bi.commercials.persistence;

import com.aditor.bi.commercials.domain.CampaignModel;
import com.aditor.bi.commercials.domain.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class OfferRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OfferRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Offer findOne(String offerId) {
        Offer offer = null;
        String query = "SELECT offer_id, business_model, commission " +
                "FROM appsflyer_import_config " +
                "WHERE offer_id = '" + offerId +"'";
        offer = jdbcTemplate.query(query, new ResultSetExtractor<Offer>() {
            @Override
            public Offer extractData(ResultSet rs) throws SQLException, DataAccessException {
                boolean hasData = rs.first();
                if(!hasData)
                    return null;

                String offerId = rs.getString(1);
                CampaignModel model = rs.getString(2).equals("Agency") ? CampaignModel.AGENCY : CampaignModel.NETWORK;
                Double percentage = rs.getDouble(3);

                return new Offer(offerId, model, percentage);
            }
        });

        return offer;
    }

}
