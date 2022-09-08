package com.mercedesbenz.exchangerate.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "exchangeRates")
public class ListExchangeRates {

    List<ExchangeRate> exchangeRates;

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    @XmlElement(name = "exchangeRate")
    public void setExchangeRates(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public void add(ExchangeRate exchangeRate) {
        if (this.exchangeRates == null) {
            this.exchangeRates = new ArrayList<ExchangeRate>();
        }
        this.exchangeRates.add(0,exchangeRate);
    }

    @Override
    public String toString() {
        return "ListExchangeRates{" +
                "exchangeRates=" + exchangeRates +
                '}';
    }
}
