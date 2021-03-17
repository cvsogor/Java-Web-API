package com.aditor.bi.commercials.test.functional;

import com.aditor.bi.commercials.Application;
import com.aditor.bi.commercials.BuildProfiles;
import com.aditor.bi.commercials.domain.*;
import com.aditor.bi.commercials.domain.dto.CommercialDTO;
import com.aditor.bi.commercials.domain.dto.TargetConditionDTO;
import com.aditor.bi.commercials.domain.dto.mapper.CommercialMapper;
import com.aditor.bi.commercials.domain.util.DateUtils;
import com.aditor.bi.commercials.persistence.CommercialRepository;
import com.aditor.bi.commercials.persistence.TargetConditionRepository;
import com.aditor.bi.commercials.persistence.TargetRepository;
import com.aditor.bi.commercials.tracking.TestCommercialCostsUpdater;
import com.aditor.bi.commercials.tracking.TestCommercialUpdateNotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles(BuildProfiles.TEST)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class CommercialControllerTest {

    private static final String OFFER_ID = "HouseOfFun";
    private static final String MEDIA_SOURCE = "vungle_int";
    private static final Date DATE_START = DateUtils.parseDate("2016-01-01");
    private static final Date DATE_END = DateUtils.parseDate("2016-01-02");
    private static final EventType EVENT_TYPE = EventType.INSTALL;
    private static final CommercialType COMMERCIAL_TYPE = CommercialType.PAYOUT;
    private static final Double AMOUNT = 1.0;

    @Autowired
    private CommercialRepository commercialRepository;

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private TargetConditionRepository targetConditionRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CommercialMapper commercialMapper;

    @Autowired
    private TestCommercialUpdateNotificationService commercialUpdateNotificationService;

    @Autowired
    private TestCommercialCostsUpdater commercialCostsUpdater;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        targetConditionRepository.deleteAll();
        targetConditionRepository.flush();

        targetRepository.deleteAll();
        targetRepository.flush();

        commercialRepository.deleteAll();
        commercialRepository.flush();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        commercialUpdateNotificationService.reset();
        commercialCostsUpdater.reset();
    }

    @Test
    public void testGetAllCommercials() throws Exception {
        commercialRepository.saveAndFlush(createDefault());
        commercialRepository.saveAndFlush(createDefault());

        mockMvc.perform(get("/api/commercials"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCommercialById() throws Exception {
        Commercial commercial = commercialRepository.saveAndFlush(createDefault());
        Long commercialId = commercial.getId();

        mockMvc.perform(get("/api/commercials/" + commercialId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateCommercial() throws Exception {
        CommercialDTO commercialDTO = createDefaultDTO();

        mockMvc.perform(post("/api/commercials")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(toJson(commercialDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        Commercial commercial = commercialRepository.findAll().get(0);

        Assert.assertTrue(commercial.getOfferId().equals(commercialDTO.offerId));
        Assert.assertTrue(commercial.getMediaSource().equals(commercialDTO.mediaSource));
        Assert.assertEquals(commercial.getId(), commercialCostsUpdater.getLastUpdated().getId());
    }

    @Test
    public void testEditCommercial() throws Exception {
        Commercial commercial = createDefault();
        commercial.getTarget().setActive(false);

        commercial = commercialRepository.saveAndFlush(commercial);
        Long commercialId = commercial.getId();

        commercial.setOfferId("offer id");
        commercial.setMediaSource("media source");
        commercial.getTarget().setActive(true);
        commercial.getTarget().getConditions().clear();
        commercial.getTarget().addCondition(new TargetCondition(
                Operator.EQUALS,
                "wifi",
                "yes",
                false,
                commercial.getTarget()
        ));

        String commercialJson = toJson(commercialMapper.toDTO(commercial));

        mockMvc.perform(put("/api/commercials/" + commercialId)
                .content(commercialJson)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Commercial edited = commercialRepository.findOne(commercialId);

        Assert.assertEquals("Edit was not performed on commercial",
                "offer id", edited.getOfferId());
        Assert.assertTrue("Edit was not performed on commercial's target",
                edited.isActive());
        Assert.assertEquals("Edit was not performed on commercial's target conditions",
                "wifi", edited.getTarget().getConditions().get(0).getFieldName());

        Assert.assertEquals("Commercial was inserted as a copy instead of replacing old one",
                1, commercialRepository.findAll().size());
        Assert.assertEquals("Old target was not purged from repository",
                1, targetRepository.findAll().size());
        Assert.assertEquals("Old target conditions were not purged from repository",
                1, targetConditionRepository.findAll().size());

        Assert.assertEquals(commercial.getId(), commercialCostsUpdater.getLastResetted().getId());
        Assert.assertEquals(commercial.getId(), commercialCostsUpdater.getLastUpdated().getId());

    }

    @Test
    public void testDeleteCommercial() throws Exception {
        Commercial commercial = commercialRepository.saveAndFlush(createDefault());
        Long commercialId = commercial.getId();

        mockMvc.perform(delete("/api/commercials/" + commercialId))
                .andExpect(status().isNoContent());

        Assert.assertEquals(commercial.getId(), commercialCostsUpdater.getLastResetted().getId());
    }

    public CommercialDTO createDefaultDTO() {
        CommercialDTO commercialDTO = new CommercialDTO();
        commercialDTO.offerId = OFFER_ID;
        commercialDTO.mediaSource = MEDIA_SOURCE;
        commercialDTO.dateStart = DATE_START;
        commercialDTO.dateEnd = DATE_END;
        commercialDTO.eventType = EventType.INSTALL;
        commercialDTO.type = CommercialType.PAYOUT;
        commercialDTO.amount = 1.0;
        commercialDTO.isActive = true;
        commercialDTO.conditions = Sets.newHashSet(
                new TargetConditionDTO(Operator.IN_LIST, "campaign_name", "1,2,3", false),
                new TargetConditionDTO(Operator.IN_LIST, "campaign_id", "1,2", true)
        );

        return commercialDTO;
    }

    public Commercial createDefault() {
        return new Commercial.Builder()
                .withOfferId(OFFER_ID)
                .withMediaSource(MEDIA_SOURCE)
                .withStartDate(DATE_START)
                .withEndDate(DATE_END)
                .withEventType(EventType.INSTALL)
                .withType(CommercialType.PAYOUT)
                .withAmount(1.0)
                .withActiveTarget()
                .withCondition(Operator.IN_LIST, "campaign_name", "1,2,3", false)
                .withCondition(Operator.IN_LIST, "campaign_id", "1,2", true)
                .build();
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
