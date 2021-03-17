package com.aditor.dashboard.spring;

import java.util.Comparator;
import java.util.Date;
/**
 * Created by detan001 on 08.12.2015.
 */
public class GroupedFields implements Comparable<GroupedFields>{
    private Date installTime;
    private String offerName;
    private String agency;
    private String mediaSource;


    public GroupedFields(Date installTime, String offerName, String agency, String mediaSource){
        this.installTime = installTime;
        this.offerName = offerName;
        this.agency = agency;
        this.mediaSource = mediaSource;
    }


    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getMediaSource() {
        return mediaSource;
    }

    public void setMediaSource(String mediaSource) {
        this.mediaSource = mediaSource;
    }


    @Override
    public int compareTo(GroupedFields o) {
        GroupedFields gf1 = this;
        GroupedFields gf2 = o;

        if (gf1.getInstallTime().equals(gf2.getInstallTime())) {

            if (gf1.getOfferName().equals(gf2.getOfferName())) {

                if (gf1.getAgency().equals(gf2.getAgency())) {

                    return gf1.getMediaSource().compareTo(gf2.getMediaSource());
                }
                return gf1.getAgency().compareTo(gf2.getAgency());
            }
            return gf1.getOfferName().compareTo(gf2.getOfferName());
        }
        return gf1.getInstallTime().compareTo(gf2.getInstallTime());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupedFields that = (GroupedFields) o;

        if (installTime != null ? !installTime.equals(that.installTime) : that.installTime != null) return false;
        if (!offerName.equals(that.offerName)) return false;
        if (!agency.equals(that.agency)) return false;
        return mediaSource.equals(that.mediaSource);

    }

    @Override
    public int hashCode() {
        int result = installTime != null ? installTime.hashCode() : 0;
        result = 31 * result + offerName.hashCode();
        result = 31 * result + agency.hashCode();
        result = 31 * result + mediaSource.hashCode();
        return result;
    }


}
