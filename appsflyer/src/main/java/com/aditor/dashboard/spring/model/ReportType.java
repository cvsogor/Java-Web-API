package com.aditor.dashboard.spring.model;

/**
 * Created by art on 12/2/15.
 */
public enum ReportType {

    INSTALL("install_report", "installs_report", "appsflyer_tmp_install_events"),
    IN_APP_EVENTS("in_app_event_report", "in_app_events_report", "appsflyer_tmp_inapp_events");

    private String webParam;
    private String urlName;
    private String tempTablename;

    ReportType(String webParam, String urlName, String tempTablename){
        this.webParam = webParam;
        this.urlName = urlName;
        this.tempTablename = tempTablename;
    }

    public static ReportType findByWebParam(String webParam){
        for( ReportType reportType : values()){
            if(reportType.webParam.equals(webParam)){
                return reportType;
            }
        }
        throw new IllegalArgumentException("Illegal web param: " + webParam);
    }

    public String getTempTablename() {
        return tempTablename;
    }

    public String getUrlName() {
        return urlName;
    }

    public String getWebParam() {
        return webParam;
    }

    public String getConfigFileName() {
        return this.name().toLowerCase();
    }
}
