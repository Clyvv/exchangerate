package com.mercedesbenz.exchangerate.model;

import com.mercedesbenz.exchangerate.helper.LocalDateTimeAdapter;
import com.mercedesbenz.exchangerate.helper.MapAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name = "interval")
public class IntervalRate {
    private LocalDateTime timestamp;

    private Map<String, Double> rates = new HashMap<>();

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    @XmlJavaTypeAdapter(value = MapAdapter.class)
    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
