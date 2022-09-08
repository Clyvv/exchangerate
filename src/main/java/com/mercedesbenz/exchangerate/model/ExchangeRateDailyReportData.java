package com.mercedesbenz.exchangerate.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "exchangeRate")
public class ExchangeRateDailyReportData {
    private String date;
    private String base;
    List<IntervalRate> intervals;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<IntervalRate> getIntervals() {
        return intervals;
    }

    @XmlElement(name = "interval")
    public void setIntervals(List<IntervalRate> intervals) {
        this.intervals = intervals;
    }

    public void addInterval(IntervalRate interval) {
        if (this.intervals == null) {
            this.intervals = new ArrayList<IntervalRate>();
        }
        this.intervals.add(0,interval);
    }
}
