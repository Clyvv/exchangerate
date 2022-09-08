package com.mercedesbenz.exchangerate.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ExchangeRateDailyReport")
public class ExchangeRateDailyReport {

    List<ExchangeRateDailyReportData> exchangeRates;

    public List<ExchangeRateDailyReportData> getExchangeRates() {
        return exchangeRates;
    }

    @XmlElement(name = "exchangeRate")
    public void setExchangeRates(List<ExchangeRateDailyReportData> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public void add(ExchangeRateDailyReportData exchangeRateDailyReportData) {
        if (this.exchangeRates == null) {
            this.exchangeRates = new ArrayList<ExchangeRateDailyReportData>();
        }
        this.exchangeRates.add(exchangeRateDailyReportData);
    }
}
