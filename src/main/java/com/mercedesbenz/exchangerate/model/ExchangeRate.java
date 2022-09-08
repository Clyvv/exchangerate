package com.mercedesbenz.exchangerate.model;

import com.mercedesbenz.exchangerate.helper.LocalDateTimeAdapter;
import com.mercedesbenz.exchangerate.helper.MapAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@XmlRootElement(name = "exchangeRate")
public class ExchangeRate {
    private String base;
    private String date;
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime timestamp;

    private Map<String, Double> rates = new HashMap<>();

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

    public Map<String, Double> getRates() {
        return rates;
    }

    @XmlJavaTypeAdapter(value = MapAdapter.class)
    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "base='" + base + '\'' +
                ", date='" + date + '\'' +
                ", timestamp=" + timestamp +
                ", rates=" + rates +
                '}';
    }
}
