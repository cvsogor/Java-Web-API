package com.aditor.dashboard.spring;

/**
 * Created by detan on 09/12/15.
 */
public class CountAndSum {
    long m_Count;
    double m_Sum;

    public CountAndSum(long m_Count, double m_Sum) {
        this.m_Count = m_Count;
        this.m_Sum = m_Sum;
    }

    public long getM_Count() {
        return m_Count;
    }

    public void setM_Count(long m_Count) {
        this.m_Count = m_Count;
    }

    public double getM_Sum() {
        return m_Sum;
    }

    public void setM_Sum(double m_Sum) {
        this.m_Sum = m_Sum;
    }
}
