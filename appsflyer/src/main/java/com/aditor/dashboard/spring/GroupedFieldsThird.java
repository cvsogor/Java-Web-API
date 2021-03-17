package com.aditor.dashboard.spring;

import java.util.Date;

/**
 * Created by detan on 13/12/15.
 */
public class GroupedFieldsThird implements  Comparable<GroupedFieldsThird>{

        private Date installTime;
        private String offerName;
        private String agency;
        private String mediaSource;
        private String country;
        private String campaign;
        private String siteId;


    public GroupedFieldsThird(Date installTime, String offerName, String agency, String mediaSource, String country, String campaign, String siteId){
        this.installTime = installTime;
        this.offerName = offerName;
        this.agency = agency;
        this.mediaSource = mediaSource;
        this.country = country;
        this.campaign = campaign;
        this.siteId = siteId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getCampaign() {  return campaign;   }

        public void setCampaign(String campaign) {   this.campaign = campaign;  }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
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
        public int compareTo(GroupedFieldsThird o) {
            GroupedFieldsThird gf1 = this;
            GroupedFieldsThird gf2 = o;

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

            GroupedFieldsThird that = (GroupedFieldsThird) o;

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
            result = 31 * result + country.hashCode();
            result = 31 * result + campaign.hashCode();
            result = 31 * result + siteId.hashCode();
            return result;
        }

    }


