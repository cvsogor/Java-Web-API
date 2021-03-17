package com.aditor.dashboard.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Created by DGordiichuk on 21.01.2016.
 */
public class CooladataExportEntry implements Comparable<CooladataExportEntry>{
    private Integer id;
    private Timestamp impression_time;
    private Timestamp click_time;
    private Timestamp install_time;
    private Timestamp event_time;
    private String event_name;
    private String original_event_name;
    private String event_value;
    private Double event_revenue;
    private String currency;
    private String receipt_id;
    private Boolean is_validated;
    private String agency_pmd;
    private String media_source;
    private String channel;
    private String keywords;
    private String campaign_name;
    private String site_id;
    private String campaign_id;
    private String adset_name;
    private String adset_id;
    private String ad_name;
    private String ad_id;
    private String ad_type;
    private String cost_model;
    private Double cost;
    private String cost_currency;
    private String region;
    private String country_code;
    private String state;
    private String city;
    @JsonProperty("af_ip")
    private String ip;
    private String wifi;
    private String language;
    private String event_source;
    private String appsflyer_device_id;
    @JsonProperty("af_customer_user_id")
    private String customer_user_id;
    private String idfa;
    private String idfv;
    private String android_id;
    private String imei;
    private String mac;
    private String device_name;
    private String advertising_id;
    @JsonProperty("device_type")
    private String devise_type;
    private String os_version;
    private String sdk_version;
    private String app_version;
    private String operator;
    private String carrier;
    private String sub_param_1;
    private String sub_param_2;
    private String sub_param_3;
    private String sub_param_4;
    private String sub_param_5;
    private String impression_url;
    private String click_url;
    private String http_referrer;
    private String installer_package;
    private String contributor_1_media_source;
    private String contributor_1_campaign;
    private String contributor_2_media_source;
    private String contributor_2_campaign;
    private String contributor_3_media_source;
    private String contributor_3_campaign;
    private String os;
    private String creative;
    private String targeting_type;
    private String targeting_group;
    private String gender;
    private String bid;
    private Double real_cost;
    private Double real_revenue;
    private String reported_platform;
    private String platform_type;
    private String advertiser;
    private String offer_id;
    private String offer_name;
    private String offer_url;
    private String offer_preview_url;
    private Double calc_cost;
    private String business_model;
    private Double commission;
    private String department;
    private String am_name;
    private String cooladata_username;
    private String permission_type;
    private String user_id;
    private Long event_timestamp_epoch;
    private Long sendtime = System.currentTimeMillis();
    private Boolean todelete = false;

    public CooladataExportEntry() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getImpression_time() {
        return impression_time;
    }

    public void setImpression_time(Timestamp impression_time) {
        this.impression_time = impression_time;
    }

    public Timestamp getClick_time() {
        return click_time;
    }

    public void setClick_time(Timestamp click_time) {
        this.click_time = click_time;
    }

    public Timestamp getInstall_time() {
        return install_time;
    }

    public void setInstall_time(Timestamp install_time) {
        this.install_time = install_time;
    }

    public Timestamp getEvent_time() {
        return event_time;
    }

    public void setEvent_time(Timestamp event_time) {
        this.event_time = event_time;
        this.event_timestamp_epoch = event_time.getTime();
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = emptyCheck(event_name);
    }

    public String getOriginal_event_name() {
        return original_event_name;
    }

    public void setOriginal_event_name(String original_event_name) {
        this.original_event_name = emptyCheck(original_event_name);
    }

    public String getEvent_value() {
        return event_value;
    }

    public void setEvent_value(String event_value) {
        this.event_value = emptyCheck(event_value);
    }

    public Double getEvent_revenue() {
        return event_revenue;
    }

    public void setEvent_revenue(Double event_revenue) {
        this.event_revenue = event_revenue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = emptyCheck(currency);
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = emptyCheck(receipt_id);
    }

    public Boolean getIs_validated() {
        return is_validated;
    }

    public void setIs_validated(Boolean is_validated) {
        this.is_validated = is_validated;
    }

    public String getAgency_pmd() {
        return agency_pmd;
    }

    public void setAgency_pmd(String agency_pmd) {
        this.agency_pmd = emptyCheck(agency_pmd);
    }

    public String getMedia_source() {
        return media_source;
    }

    public void setMedia_source(String media_source) {
        this.media_source = emptyCheck(media_source);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = emptyCheck(channel);
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = emptyCheck(keywords);
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = emptyCheck(campaign_name);
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = emptyCheck(site_id);
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = emptyCheck(campaign_id);
    }

    public String getAdset_name() {
        return adset_name;
    }

    public void setAdset_name(String adset_name) {
        this.adset_name = emptyCheck(adset_name);
    }

    public String getAdset_id() {
        return adset_id;
    }

    public void setAdset_id(String adset_id) {
        this.adset_id = emptyCheck(adset_id);
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = emptyCheck(ad_name);
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = emptyCheck(ad_id);
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = emptyCheck(ad_type);
    }

    public String getCost_model() {
        return cost_model;
    }

    public void setCost_model(String cost_model) {
        this.cost_model = emptyCheck(cost_model);
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCost_currency() {
        return cost_currency;
    }

    public void setCost_currency(String cost_currency) {
        this.cost_currency = emptyCheck(cost_currency);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = emptyCheck(region);
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = emptyCheck(country_code);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = emptyCheck(state);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = emptyCheck(city);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = emptyCheck(ip);
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = emptyCheck(wifi);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = emptyCheck(language);
    }

    public String getEvent_source() {
        return event_source;
    }

    public void setEvent_source(String event_source) {
        this.event_source = emptyCheck(event_source);
    }

    public String getAppsflyer_device_id() {
        return appsflyer_device_id;
    }

    public void setAppsflyer_device_id(String appsflyer_device_id) {
        this.appsflyer_device_id = emptyCheck(appsflyer_device_id);
        this.user_id = emptyCheck(appsflyer_device_id);
    }

    public String getCustomer_user_id() {
        return customer_user_id;
    }

    public void setCustomer_user_id(String customer_user_id) {
        this.customer_user_id = emptyCheck(customer_user_id);
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = emptyCheck(idfa);
    }

    public String getIdfv() {
        return idfv;
    }

    public void setIdfv(String idfv) {
        this.idfv = emptyCheck(idfv);
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = emptyCheck(android_id);
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = emptyCheck(imei);
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = emptyCheck(mac);
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = emptyCheck(device_name);
    }

    public String getAdvertising_id() {
        return advertising_id;
    }

    public void setAdvertising_id(String advertising_id) {
        this.advertising_id = emptyCheck(advertising_id);
    }

    public String getDevise_type() {
        return devise_type;
    }

    public void setDevise_type(String devise_type) {
        this.devise_type = emptyCheck(devise_type);
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = emptyCheck(os_version);
    }

    public String getSdk_version() {
        return sdk_version;
    }

    public void setSdk_version(String sdk_version) {
        this.sdk_version = emptyCheck(sdk_version);
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = emptyCheck(app_version);
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = emptyCheck(operator);
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = emptyCheck(carrier);
    }

    public String getSub_param_1() {
        return sub_param_1;
    }

    public void setSub_param_1(String sub_param_1) {
        this.sub_param_1 = emptyCheck(sub_param_1);
    }

    public String getSub_param_2() {
        return sub_param_2;
    }

    public void setSub_param_2(String sub_param_2) {
        this.sub_param_2 = emptyCheck(sub_param_2);
    }

    public String getSub_param_3() {
        return sub_param_3;
    }

    public void setSub_param_3(String sub_param_3) {
        this.sub_param_3 = emptyCheck(sub_param_3);
    }

    public String getSub_param_4() {
        return sub_param_4;
    }

    public void setSub_param_4(String sub_param_4) {
        this.sub_param_4 = emptyCheck(sub_param_4);
    }

    public String getSub_param_5() {
        return sub_param_5;
    }

    public void setSub_param_5(String sub_param_5) {
        this.sub_param_5 = emptyCheck(sub_param_5);
    }

    public String getImpression_url() {
        return impression_url;
    }

    public void setImpression_url(String impression_url) {
        this.impression_url = emptyCheck(impression_url);
    }

    public String getClick_url() {
        return click_url;
    }

    public void setClick_url(String click_url) {
        this.click_url = emptyCheck(click_url);
    }

    public String getHttp_referrer() {
        return http_referrer;
    }

    public void setHttp_referrer(String http_referrer) {
        this.http_referrer = emptyCheck(http_referrer);
    }

    public String getInstaller_package() {
        return installer_package;
    }

    public void setInstaller_package(String installer_package) {
        this.installer_package = emptyCheck(installer_package);
    }

    public String getContributor_1_media_source() {
        return contributor_1_media_source;
    }

    public void setContributor_1_media_source(String contributor_1_media_source) {
        this.contributor_1_media_source = emptyCheck(contributor_1_media_source);
    }

    public String getContributor_1_campaign() {
        return contributor_1_campaign;
    }

    public void setContributor_1_campaign(String contributor_1_campaign) {
        this.contributor_1_campaign = emptyCheck(contributor_1_campaign);
    }

    public String getContributor_2_media_source() {
        return contributor_2_media_source;
    }

    public void setContributor_2_media_source(String contributor_2_media_source) {
        this.contributor_2_media_source = emptyCheck(contributor_2_media_source);
    }

    public String getContributor_2_campaign() {
        return contributor_2_campaign;
    }

    public void setContributor_2_campaign(String contributor_2_campaign) {
        this.contributor_2_campaign = emptyCheck(contributor_2_campaign);
    }

    public String getContributor_3_media_source() {
        return contributor_3_media_source;
    }

    public void setContributor_3_media_source(String contributor_3_media_source) {
        this.contributor_3_media_source = emptyCheck(contributor_3_media_source);
    }

    public String getContributor_3_campaign() {
        return contributor_3_campaign;
    }

    public void setContributor_3_campaign(String contributor_3_campaign) {
        this.contributor_3_campaign = emptyCheck(contributor_3_campaign);
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = emptyCheck(os);
    }

    public String getCreative() {
        return creative;
    }

    public void setCreative(String creative) {
        this.creative = emptyCheck(creative);
    }

    public String getTargeting_type() {
        return targeting_type;
    }

    public void setTargeting_type(String targeting_type) {
        this.targeting_type = emptyCheck(targeting_type);
    }

    public String getTargeting_group() {
        return targeting_group;
    }

    public void setTargeting_group(String targeting_group) {
        this.targeting_group = emptyCheck(targeting_group);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = emptyCheck(gender);
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = emptyCheck(bid);
    }

    public Double getReal_cost() {
        return real_cost;
    }

    public void setReal_cost(Double real_cost) {
        this.real_cost = real_cost;
    }

    public Double getReal_revenue() {
        return real_revenue;
    }

    public void setReal_revenue(Double real_revenue) {
        this.real_revenue = real_revenue;
    }

    public String getReported_platform() {
        return reported_platform;
    }

    public void setReported_platform(String reported_platform) {
        this.reported_platform = emptyCheck(reported_platform);
    }

    public String getPlatform_type() {
        return platform_type;
    }

    public void setPlatform_type(String platform_type) {
        this.platform_type = emptyCheck(platform_type);
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = emptyCheck(advertiser);
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = emptyCheck(offer_id);
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = emptyCheck(offer_name);
    }

    public String getOffer_url() {
        return offer_url;
    }

    public void setOffer_url(String offer_url) {
        this.offer_url = emptyCheck(offer_url);
    }

    public String getOffer_preview_url() {
        return offer_preview_url;
    }

    public void setOffer_preview_url(String offer_preview_url) {
        this.offer_preview_url = emptyCheck(offer_preview_url);
    }

    public Double getCalc_cost() {
        return calc_cost;
    }

    public void setCalc_cost(Double calc_cost) {
        this.calc_cost = calc_cost;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public String getBusiness_model() {
        return business_model;
    }

    public void setBusiness_model(String business_model) {
        this.business_model = emptyCheck(business_model);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = replaceNullWithUnknown(department);
    }

    public String getAm_name() {
        return am_name;
    }

    public void setAm_name(String am_name) {
        this.am_name = replaceNullWithUnknown(am_name);
    }

    public String getCooladata_username() {
        return cooladata_username;
    }

    public void setCooladata_username(String cooladata_username) {
        this.cooladata_username = replaceNullWithUnknown(cooladata_username);
    }

    public String getPermission_type() {
        return permission_type;
    }

    public void setPermission_type(String permission_type) {
        this.permission_type = replaceNullWithUnknown(permission_type);
    }

    public Long getEvent_timestamp_epoch() {
        return event_timestamp_epoch;
    }

    public String getUser_id() {
        return user_id;
    }

    public Long getSendtime() {
        return sendtime;
    }

    public Boolean getTodelete() {
        return todelete;
    }

    private String emptyCheck(String string) {
        return "".equals(string) ? null : string;
    }

    private String replaceNullWithUnknown(String string) {
        return (string == null) ? "Unknown" : string;
    }

    @Override
    public int compareTo(CooladataExportEntry o) {
        return (this.event_timestamp_epoch).compareTo(o.event_timestamp_epoch);
    }
}
