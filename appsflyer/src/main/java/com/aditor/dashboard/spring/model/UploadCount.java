package com.aditor.dashboard.spring.model;

/**
 * Created by dgordiichuk on 21.12.2015.
 */
public class UploadCount {
    private long countTempTable;
    private long countInsert;
    private long countDuplicate;
    private int countFile;
    private long countMissingMediaSource;

    public UploadCount() {

    }

    public UploadCount(long countTempTable, long countInsert, long countMissingMediaSource, int countFile) {
        this.countTempTable = countTempTable;
        this.countInsert = countInsert;
        this.countMissingMediaSource = countMissingMediaSource;
        this.countDuplicate = countTempTable - countInsert - countMissingMediaSource;
        this.countFile = countFile;
    }

    public long getCountTempTable() {
        return countTempTable;
    }

    public long getCountInsert() {
        return countInsert;
    }

    public long getCountDuplicate() {
        return countDuplicate;
    }

    public int getCountFile() {
        return countFile;
    }

    public long getCountMissingMediaSource() {
        return countMissingMediaSource;
    }

    public void addUploadCount(UploadCount uploadCount) {
        countTempTable+=uploadCount.countTempTable;
        countInsert+=uploadCount.countInsert;
        countDuplicate+=uploadCount.countDuplicate;
        countFile+=uploadCount.countFile;
        countMissingMediaSource+=uploadCount.countMissingMediaSource;
    }
}
