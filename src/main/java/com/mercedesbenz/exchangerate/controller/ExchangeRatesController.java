package com.mercedesbenz.exchangerate.controller;

import com.mercedesbenz.exchangerate.model.ExchangeRate;
import com.mercedesbenz.exchangerate.model.ExchangeRateDailyReportData;
import com.mercedesbenz.exchangerate.service.ExchangeRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchangerates")
public class ExchangeRatesController {

    private final ExchangeRatesService exchangeRatesService;

    @Autowired
    public ExchangeRatesController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @PostMapping("/latest")
    @ResponseStatus(HttpStatus.OK)
    private void fetchExchangeRates() {
        exchangeRatesService.fetchExchangeRates();
    }

    @GetMapping("/current/{to}/{from}")
    private ResponseEntity<ExchangeRate> getCurrentExchangeRate(@PathVariable String to, @PathVariable String from) {
        ExchangeRate exchangeRate =exchangeRatesService.readCurrentExchangeRate(to,from);
        HttpStatus status = exchangeRate != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<ExchangeRate>(exchangeRate, status);
    }

    @GetMapping("/dailyreport/{baseCode}")
    private ResponseEntity<ExchangeRateDailyReportData> getDailyReport(@PathVariable String baseCode) {
        ExchangeRateDailyReportData dailyReport =exchangeRatesService.readDailyReport(baseCode);
        HttpStatus status = dailyReport != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<ExchangeRateDailyReportData>(dailyReport, status);
    }
}
