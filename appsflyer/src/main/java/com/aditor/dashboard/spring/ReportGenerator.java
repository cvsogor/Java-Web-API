package com.aditor.dashboard.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by art on 12/3/15.
 */
@Component
public class ReportGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    Dao dao;

    public String generateReportFirst(Date fromDate, Date toDate, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        Map<GroupedFields, Long> installsByDay = dao.countInstallsByDayGF(fromDate, toDate, offerName, country, agency, mediaSource, campaign, siteId);


        Map<GroupedFields, InstallReportSingleRow> reportData = new TreeMap<GroupedFields, InstallReportSingleRow>();

        for (Map.Entry<GroupedFields, Long> installsByDayEntry : installsByDay.entrySet()) {
            GroupedFields key = installsByDayEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(installsByDayEntry.getValue());
                reportData.put(key, installReportSingleRow);
            } else {
                logger.info("Key duplication");
            }

        }

        logger.info("Size of reoprts after install stage is " + reportData.size());
        for (int i = 0; i <= 10; i++) {
            calculateFdsXDaysToThePast(fromDate, toDate, reportData, i, offerName, country, agency, mediaSource, campaign, siteId);
            calculateDepositsXDaysToThePast(fromDate, toDate,  reportData, i, offerName, country, agency, mediaSource, campaign, siteId);
        }

        logger.info("Size of reports after 10 day stage is " + reportData.size());

        calculateFdsXDaysToThePast(fromDate, toDate, reportData, 30, offerName, country, agency, mediaSource, campaign, siteId);
        calculateDepositsXDaysToThePast(fromDate, toDate, reportData, 30, offerName, country, agency, mediaSource, campaign, siteId);

        logger.info("Size of reports after 30 day stage is " + reportData.size());

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<GroupedFields, InstallReportSingleRow> installEntry : reportData.entrySet()) {
            sb.append(dateFormat.format(installEntry.getKey().getInstallTime()));
            sb.append(',');
            sb.append(installEntry.getKey().getOfferName());
            sb.append(',');
            sb.append(installEntry.getKey().getAgency());
            sb.append(',');
            sb.append(installEntry.getKey().getMediaSource());
            sb.append(',').append(installEntry.getValue().toCSV()).append('\n');
        }

        return buildHeaders().append('\n').append(sb).toString();
    }

    private void calculateDepositsXDaysToThePast(Date fromDate, Date toDate, Map<GroupedFields, InstallReportSingleRow> reportData, int dayNum, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {
        Map<GroupedFields, DailyData> fdsByDay = dao.countDepositsByDayGF(fromDate, toDate, dayNum, offerName, country, agency, mediaSource, campaign, siteId);
        for (Map.Entry<GroupedFields, DailyData> fdsEntry : fdsByDay.entrySet()) {
            GroupedFields key = fdsEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(0);
                reportData.put(key, installReportSingleRow);
            }
            Map<Integer, DailyData> dataByDate = installReportSingleRow.getDataByDate();
            dataByDate.get(dayNum).setDeposits(fdsEntry.getValue().getDeposits());
            dataByDate.get(dayNum).setRevenue(fdsEntry.getValue().getRevenue());
        }
    }

    private void calculateFdsXDaysToThePast(Date fromDate, Date toDate, Map<GroupedFields, InstallReportSingleRow> reportData, int dayNum, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {
        Map<GroupedFields, CountAndSum> fdsByInstallDay = dao.countFdsByDayGF(fromDate, toDate, dayNum, offerName, country, agency, mediaSource, campaign, siteId);
        for (Map.Entry<GroupedFields, CountAndSum> fdsEntry : fdsByInstallDay.entrySet()) {
            GroupedFields key = fdsEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(0);
                reportData.put(key, installReportSingleRow);
            }
            Map<Integer, DailyData> dataByDate = installReportSingleRow.getDataByDate();
            dataByDate.get(dayNum).setFds(fdsEntry.getValue().getM_Count());
            dataByDate.get(dayNum).setFdsum(fdsEntry.getValue().getM_Sum());

        }
    }

    public StringBuilder buildHeaders() {
        StringBuilder sb = buildHeader("Install time,Offer Name,Agency,Media Source,Installs count,");
        return sb;
    }

    private String dayHeader(int i) {
        return "Day " + i + " FD Count, Day " + i + " FD Sum, Day " + i + " Deposits Count,Day " + i + " Deposit Sum,";
    }


    public String generateReportSecond(Date fromDate, Date toDate, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        Map<GroupedFieldsSecond, Long> installsByDay = dao.countInstallsByDayGFSecond(fromDate, toDate, offerName, country, agency, mediaSource, campaign, siteId);


        Map<GroupedFieldsSecond, InstallReportSingleRow> reportData = new TreeMap<GroupedFieldsSecond, InstallReportSingleRow>();

        for (Map.Entry<GroupedFieldsSecond, Long> installsByDayEntry : installsByDay.entrySet()) {
            GroupedFieldsSecond key = installsByDayEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(installsByDayEntry.getValue());
                reportData.put(key, installReportSingleRow);
            } else {
                logger.info("Key duplication");
            }

        }

        logger.info("Size of reoprts after install stage is " + reportData.size());
        for (int i = 0; i <= 10; i++) {
            calculateFdsXDaysToThePastSec(fromDate, toDate, reportData, i, offerName, country, agency, mediaSource, campaign, siteId);
            calculateDepositsXDaysToThePastSec(fromDate, toDate, reportData, i, offerName, country, agency, mediaSource, campaign, siteId);
        }

        logger.info("Size of reports after 10 day stage is " + reportData.size());

        calculateFdsXDaysToThePastSec(fromDate, toDate, reportData, 30, offerName, country, agency, mediaSource, campaign, siteId);
        calculateDepositsXDaysToThePastSec(fromDate, toDate, reportData, 30, offerName, country, agency, mediaSource, campaign, siteId);

        logger.info("Size of reports after 30 day stage is " + reportData.size());

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<GroupedFieldsSecond, InstallReportSingleRow> installEntry : reportData.entrySet()) {
            sb.append(dateFormat.format(installEntry.getKey().getInstallTime()));
            sb.append(',');
            sb.append(installEntry.getKey().getOfferName());
            sb.append(',');
            sb.append(installEntry.getKey().getAgency());
            sb.append(',');
            sb.append(installEntry.getKey().getMediaSource());
            sb.append(',');
            sb.append(installEntry.getKey().getCountry());
            sb.append(',');
            sb.append(',').append(installEntry.getValue().toCSV()).append('\n');
        }

        return buildHeadersSec().append('\n').append(sb).toString();
    }


    private void calculateDepositsXDaysToThePastSec(Date fromDate, Date toDate, Map<GroupedFieldsSecond, InstallReportSingleRow> reportData, int dayNum, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {
        Map<GroupedFieldsSecond, DailyData> fdsByDay = dao.countDepositsByDayGFSecond(fromDate, toDate, dayNum, offerName, country, agency, mediaSource, campaign, siteId);
        for (Map.Entry<GroupedFieldsSecond, DailyData> fdsEntry : fdsByDay.entrySet()) {
            GroupedFieldsSecond key = fdsEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(0);
                reportData.put(key, installReportSingleRow);
            }
            Map<Integer, DailyData> dataByDate = installReportSingleRow.getDataByDate();
            dataByDate.get(dayNum).setDeposits(fdsEntry.getValue().getDeposits());
            dataByDate.get(dayNum).setRevenue(fdsEntry.getValue().getRevenue());
        }
    }

    private void calculateFdsXDaysToThePastSec(Date fromDate, Date toDate, Map<GroupedFieldsSecond, InstallReportSingleRow> reportData, int dayNum, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {
        Map<GroupedFieldsSecond, CountAndSum> fdsByInstallDay = dao.countFdsByDayGFSecond(fromDate, toDate, dayNum, offerName, country, agency, mediaSource, campaign, siteId);
        for (Map.Entry<GroupedFieldsSecond, CountAndSum> fdsEntry : fdsByInstallDay.entrySet()) {
            GroupedFieldsSecond key = fdsEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(0);
                reportData.put(key, installReportSingleRow);
            }
            Map<Integer, DailyData> dataByDate = installReportSingleRow.getDataByDate();
            dataByDate.get(dayNum).setFds(fdsEntry.getValue().getM_Count());
            dataByDate.get(dayNum).setFdsum(fdsEntry.getValue().getM_Sum());

        }
    }

    public StringBuilder buildHeadersSec() {
        StringBuilder sb = buildHeader("Install time,Offer Name,Agency,Media Source, country, Installs count,");
        return sb;
    }

//THIRD report with CAMPAIGN and SITE ID -----------------------------------------------------------------------

    public String generateReportThird(Date fromDate, Date toDate, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        Map<GroupedFieldsThird, Long> installsByDay = dao.countInstallsByDayGFThird(fromDate, toDate, offerName, country, agency, mediaSource, campaign, siteId);


        Map<GroupedFieldsThird, InstallReportSingleRow> reportData = new TreeMap<GroupedFieldsThird, InstallReportSingleRow>();

        for (Map.Entry<GroupedFieldsThird, Long> installsByDayEntry : installsByDay.entrySet()) {
            GroupedFieldsThird key = installsByDayEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(installsByDayEntry.getValue());
                reportData.put(key, installReportSingleRow);
            } else {
                logger.info("Key duplication");
            }

        }

        logger.info("Size of reports after install stage is " + reportData.size());
        for (int i = 0; i <= 10; i++) {
            calculateFdsXDaysToThePastThird(fromDate, toDate, reportData, i, offerName, country, agency, mediaSource, campaign, siteId);
            calculateDepositsXDaysToThePastThird(fromDate, toDate, reportData, i, offerName, country, agency, mediaSource, campaign, siteId);
        }

        logger.info("Size of reports after 10 day stage is " + reportData.size());

        calculateFdsXDaysToThePastThird(fromDate, toDate, reportData, 30, offerName, country, agency, mediaSource, campaign, siteId);
        calculateDepositsXDaysToThePastThird(fromDate, toDate, reportData, 30, offerName, country, agency, mediaSource, campaign, siteId);

        logger.info("Size of reports after 30 day stage is " + reportData.size());

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<GroupedFieldsThird, InstallReportSingleRow> installEntry : reportData.entrySet()) {
            sb.append(dateFormat.format(installEntry.getKey().getInstallTime()));
            sb.append(',');
            sb.append('\"').append(installEntry.getKey().getOfferName()).append('\"');
            sb.append(',');
            sb.append('\"').append(installEntry.getKey().getAgency()).append('\"');
            sb.append(',');
            sb.append('\"').append(installEntry.getKey().getMediaSource()).append('\"');
            sb.append(',');
            sb.append(installEntry.getKey().getCountry());
            sb.append(',');
            sb.append('\"').append(installEntry.getKey().getCampaign()).append('\"');
            sb.append(',');
            sb.append('\"').append(installEntry.getKey().getSiteId()).append('\"');
            sb.append(',').append(installEntry.getValue().toCSV()).append('\n');
        }

        return buildHeader("Install time,Offer Name,Agency,Media Source, country, Campaign, Site Id, Installs count,").append('\n').append(sb).toString();
    }


    private void calculateDepositsXDaysToThePastThird(Date fromDate, Date toDate, Map<GroupedFieldsThird, InstallReportSingleRow> reportData, int dayNum, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {
        Map<GroupedFieldsThird, DailyData> fdsByDay = dao.countDepositsByDayGFThird(fromDate, toDate, dayNum, offerName, country, agency, mediaSource, campaign, siteId);
        for (Map.Entry<GroupedFieldsThird, DailyData> fdsEntry : fdsByDay.entrySet()) {
            GroupedFieldsThird key = fdsEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(0);
                reportData.put(key, installReportSingleRow);
            }
            Map<Integer, DailyData> dataByDate = installReportSingleRow.getDataByDate();
            dataByDate.get(dayNum).setDeposits(fdsEntry.getValue().getDeposits());
            dataByDate.get(dayNum).setRevenue(fdsEntry.getValue().getRevenue());
        }
    }

    private void calculateFdsXDaysToThePastThird(Date fromDate, Date toDate, Map<GroupedFieldsThird, InstallReportSingleRow> reportData, int dayNum, String offerName, String country, String agency, String mediaSource, String campaign, String siteId) {
        Map<GroupedFieldsThird, CountAndSum> fdsByInstallDay = dao.countFdsByDayGFThird(fromDate, toDate, dayNum, offerName, country, agency, mediaSource, campaign, siteId);
        for (Map.Entry<GroupedFieldsThird, CountAndSum> fdsEntry : fdsByInstallDay.entrySet()) {
            GroupedFieldsThird key = fdsEntry.getKey();
            InstallReportSingleRow installReportSingleRow = reportData.get(key);
            if (installReportSingleRow == null) {
                installReportSingleRow = new InstallReportSingleRow(0);
                reportData.put(key, installReportSingleRow);
            }
            Map<Integer, DailyData> dataByDate = installReportSingleRow.getDataByDate();
            dataByDate.get(dayNum).setFds(fdsEntry.getValue().getM_Count());
            dataByDate.get(dayNum).setFdsum(fdsEntry.getValue().getM_Sum());

        }
    }

    public StringBuilder buildHeader(String headerBeginning) {
        StringBuilder sb = new StringBuilder();
        sb.append(headerBeginning);
        for (int i = 0; i < 10; i++) {
            sb.append(dayHeader(i));
        }
        sb.append(dayHeader(10)).append(dayHeader(30));
        return sb;
    }

    public String generateOptimizedReport(Date fromDate, Date toDate) {
        return buildOptimizedHeaders("Country").append(dao.optimizedReportCountry(fromDate, toDate)).append("\n\n").
                append(buildOptimizedHeaders("")).append(dao.optimizedReportOffer(fromDate, toDate)).append("\n\n").
                append(buildOptimizedHeaders("Campaing")).append(dao.optimizedReportCampaing(fromDate, toDate)).append("\n\n").
                append(buildOptimizedHeaders("SiteId")).append(dao.optimizedReportSiteId(fromDate, toDate)).append("\n\n").
                append(buildOptimizedHeaders("Weekday")).append(dao.optimizedReportWeekday(fromDate, toDate)).append("\n\n").
                append(buildOptimizedHeaders("DeviceType")).append(dao.optimizedReportDeviceType(fromDate, toDate)).append("\n\n").
                append(buildOptimizedHeaders("OsVersion")).append(dao.optimizedReportOsVersion(fromDate, toDate)).append("\n\n").
                append(buildOptimizedHeaders("Month")).append(dao.optimizedReportMonth(fromDate, toDate)).append("\n\n").toString();
    }

    private StringBuilder buildOptimizedHeaders(String firstField) {
        StringBuilder sb = new StringBuilder(firstField + ",offerName,revenue,cost,impressions,clicks,ctr,installs,ecpi,fdCount,fdAmount,depositsCount,depositsAmount,instalsDepositAmount,ecpa,instalToRfd,arpu,arppu,cpc,roi,cti,ctd,ecpm\n");
        return sb;
    }
}

