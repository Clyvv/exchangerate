package com.mercedesbenz.exchangerate.service;

import com.mercedesbenz.exchangerate.helper.XmlHelper;
import com.mercedesbenz.exchangerate.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.bind.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService{

    private final WebClient webClient;
    Logger logger = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);

    @Value("${exchangerate-api.base-code}")
    private String defaultBaseCode;

    @Value("${exchangerate-api.symbols}")
    private String[] defaultSymbols;

    private final String filename = "Exchangerates.xml";

    private final XmlHelper xmlHelper;

    @Autowired
    public ExchangeRatesServiceImpl(WebClient webClient, XmlHelper xmlHelper) {
        this.webClient = webClient;
        this.xmlHelper = xmlHelper;
    }

    @Scheduled(fixedRate = 2*60*60*1000)
    @Override
    public void fetchExchangeRates() {
        logger.debug("Fetching exchange rates from external api");
        Flux<ExchangeRate> exchangeRates = Flux.fromArray(defaultSymbols).flatMap(this::requestExchangeRates);
        exchangeRates.subscribe(this::saveFetchedData);
    }

    private void saveFetchedData(ExchangeRate exchangeRate){
        logger.debug("Saving exchange rate data");
        File file = new File(filename);
        ListExchangeRates listExchangeRates = new ListExchangeRates();
        try {
            Path path = Paths.get(filename);
            if(Files.exists(path)){
                listExchangeRates= xmlHelper.unmarshallListExchangeRates(file);
            }
            listExchangeRates.add(exchangeRate);
            xmlHelper.marshallListExchangeRates(file,listExchangeRates);
            //update daily report
            generateExchangeRateDailyReport(exchangeRate);
        } catch (JAXBException e) {
            logger.error("Failed to save or retrieve data from XML file"+e.getMessage());
            e.printStackTrace();
        }


    }

    public void generateExchangeRateDailyReport(ExchangeRate exchangeRate){
        logger.debug("Generating Exchange Rate Daily Report");
        String date = LocalDate.now().toString();
        String filename = date+"_ExchangeRateDailyReport.xml";
        File file = new File(filename);
        ExchangeRateDailyReport report = new ExchangeRateDailyReport();
        try {
            Path path = Paths.get(filename);
            if(Files.exists(path)){
                report = xmlHelper.unmarshallReportData(file);
                ExchangeRateDailyReportData reportData = report.getExchangeRates().stream().filter(rate->rate.getDate().equals(exchangeRate.getDate()) && rate.getBase().equals(exchangeRate.getBase())).findFirst().orElse(null);
                if (reportData != null){
                    report.getExchangeRates().forEach(data -> {
                        if (data.getDate().equals(exchangeRate.getDate()) && data.getBase().equals(exchangeRate.getBase())){
                            IntervalRate intervalRate = new IntervalRate();
                            intervalRate.setTimestamp(exchangeRate.getTimestamp());
                            intervalRate.setRates(exchangeRate.getRates());
                            reportData.addInterval(intervalRate);
                        }
                    });
                }else{
                    ExchangeRateDailyReportData newReportData = new ExchangeRateDailyReportData();
                    newReportData.setDate(exchangeRate.getDate());
                    newReportData.setBase(exchangeRate.getBase());

                    IntervalRate intervalRate = new IntervalRate();
                    intervalRate.setTimestamp(exchangeRate.getTimestamp());
                    intervalRate.setRates(exchangeRate.getRates());

                    newReportData.addInterval(intervalRate);

                    report.add(newReportData);
                }

            }else{
                ExchangeRateDailyReportData reportData = new ExchangeRateDailyReportData();
                reportData.setDate(exchangeRate.getDate());
                reportData.setBase(exchangeRate.getBase());

                IntervalRate intervalRate = new IntervalRate();
                intervalRate.setTimestamp(exchangeRate.getTimestamp());
                intervalRate.setRates(exchangeRate.getRates());

                reportData.addInterval(intervalRate);
                report.add(reportData);
            }

            xmlHelper.marshallReportData(file,report);
        }catch (JAXBException e) {
            logger.error("Failed to save or retrieve data from XML file"+e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public ExchangeRate readCurrentExchangeRate(String symbol,String baseCode) {
        logger.debug("Fetching current exchange rate for "+symbol+"/"+baseCode);
        File file = new File(filename);
        ExchangeRate exchangeRate = new ExchangeRate();
        try {
            Path path = Paths.get(filename);
            if(Files.exists(path)){
                ListExchangeRates listExchangeRates = xmlHelper.unmarshallListExchangeRates(file);
                exchangeRate = listExchangeRates.getExchangeRates().stream().filter(rate -> rate.getBase().equals(baseCode)).findFirst().orElse(null);
                if(exchangeRate!=null){
                    Map.Entry<String, Double> filtered = exchangeRate.getRates().entrySet().stream().filter(rate -> rate.getKey().equals(symbol)).findFirst().orElse(null);
                    if(filtered !=null){
                        exchangeRate.getRates().clear();
                        exchangeRate.getRates().put(filtered.getKey(), filtered.getValue());
                    }else{
                        logger.debug("Symbol not found: " + symbol);
                        throw new ExchangeRateException(symbol, "Unknown Symbol");
                    }

                }else{
                    logger.debug("Base symbol not found: " + baseCode);
                    throw new ExchangeRateException(baseCode, "Unknown Symbol");
                }

            }else{
                logger.debug("No Exchange Rate data found, attempting to fetch the data from an external");
                fetchExchangeRates();
            }
        } catch (JAXBException e) {
            logger.error("Failed to save or retrieve data from XML file"+e.getMessage());
            e.printStackTrace();
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRateDailyReportData readDailyReport(String baseCode) {
        String date = LocalDate.now().toString();
        String filename = date+"_ExchangeRateDailyReport.xml";
        File file = new File(filename);
        ExchangeRateDailyReportData reportData = null;
        try {
            Path path = Paths.get(filename);
            if(Files.exists(path)) {
                ExchangeRateDailyReport report = xmlHelper.unmarshallReportData(file);
                reportData = report.getExchangeRates().stream().filter(rate -> rate.getBase().equals(baseCode)).findFirst().orElse(null);
            }
        }catch (JAXBException e) {
            logger.error("Failed to save or retrieve data from XML file"+e.getMessage());
            e.printStackTrace();
        }
        if (reportData == null){
            throw new ExchangeRateException(baseCode, "No ExchangeRate Daily Report Found");
        }
        return reportData;
    }

    private Mono<ExchangeRate> requestExchangeRates(String baseCode) {
        return webClient.get()
                .uri("/latest?base="+baseCode, "1")
                .retrieve()
                .bodyToMono(ExchangeRate.class);
    }
}
