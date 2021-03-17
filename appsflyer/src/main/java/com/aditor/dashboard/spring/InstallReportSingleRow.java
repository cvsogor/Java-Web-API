package com.aditor.dashboard.spring;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
//
/**
 * Created by art on 12/2/15.
 */
public class InstallReportSingleRow {

    private long installCount;
    private Map<Integer, DailyData> dataByDate;

    public InstallReportSingleRow(long installCount) {
        this.installCount = installCount;
        this.dataByDate = new TreeMap<Integer, DailyData>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        for(int i = 0; i <= 10; i++){
            dataByDate.put(i,new DailyData(0,0,0,0));
        }
        dataByDate.put(30,new DailyData(0,0,0,0));
    }

    public Map<Integer, DailyData> getDataByDate() {
        return dataByDate;
    }

    public String toCSV(){
        StringBuilder sb = new StringBuilder();
        sb.append(installCount).append(',') ;
        for (DailyData dailyData : dataByDate.values()) {
            sb.append(dailyData.getFds() == 0 ? "" : dailyData.getFds()).append(',').append(dailyData.getFdsum()).append(',').append(dailyData.getDeposits() == 0 ? "" : dailyData.getDeposits()).append(',').append(dailyData.getRevenue() == 0 ? "" : dailyData.getRevenue()).append(',');
        }

        return sb.substring(0, sb.length() - 1);
    }
}
