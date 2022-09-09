package com.mercedesbenz.exchangerate.service;

import com.mercedesbenz.exchangerate.model.ExchangeRate;
import com.mercedesbenz.exchangerate.model.ExchangeRateDailyReportData;

public interface ExchangeRatesService {

    void fetchExchangeRates();

    ExchangeRate readCurrentExchangeRate(String to,String from);

    ExchangeRateDailyReportData readDailyReport(String baseCode);
}
