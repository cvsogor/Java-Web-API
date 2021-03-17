package com.aditor.dashboard.spring.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dgordiichuk on 26.01.2016.
 */
public class AppsflyerImportConfig {
    private String business_model;
    private Double commission;
    private String advertiser;
    private String offer_name;
    private String offer_id;
    private String os;
    private String offer_url;
    private String offer_preview_url;
    private String reported_platform;
    private String token;
    private String platform_type;

    public AppsflyerImportConfig() {
    }

    public AppsflyerImportConfig(String advertiser, String offerId, String offerName, String offerUrl, String offerPreviewUrl, String os, String platformName, String platformType, String businessModel, Double commission) {
        this.advertiser = advertiser.trim();
        this.offer_id = offerId.trim();
        this.offer_name = offerName.trim();
        this.offer_url = offerUrl.trim();
        this.offer_preview_url = offerPreviewUrl.trim();
        this.os = os.trim();
        this.reported_platform = platformName.trim();
        this.platform_type = platformType.trim();
        this.business_model = businessModel.trim();
        this.commission = commission;
    }

    public String getBusiness_model() {
        return business_model;
    }

    public void setBusiness_model(String business_model) {
        this.business_model = business_model;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOffer_url() {
        return offer_url;
    }

    public void setOffer_url(String offer_url) {
        this.offer_url = offer_url;
    }

    public String getOffer_preview_url() {
        return offer_preview_url;
    }

    public void setOffer_preview_url(String offer_preview_url) {
        this.offer_preview_url = offer_preview_url;
    }

    public String getReported_platform() {
        return reported_platform;
    }

    public void setReported_platform(String reported_platform) {
        this.reported_platform = reported_platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatform_type() {
        return platform_type;
    }

    public void setPlatform_type(String platform_type) {
        this.platform_type = platform_type;
    }

    public Map<String,Object> getNamedParametersMap() {
        Map namedParameters = new HashMap<String, Object>();
        namedParameters.put("platform_name", reported_platform);
        namedParameters.put("platform_type", platform_type);
        namedParameters.put("advertiser", advertiser);
        namedParameters.put("offer_id", offer_id);
        namedParameters.put("offer_name", offer_name);
        namedParameters.put("offer_url", offer_url);
        namedParameters.put("offer_preview_url", offer_preview_url);
        namedParameters.put("os", os);
        namedParameters.put("business_model", business_model);
        namedParameters.put("commission", commission);
        return namedParameters;
    }
}