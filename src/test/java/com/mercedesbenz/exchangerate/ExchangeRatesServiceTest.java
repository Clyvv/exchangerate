package com.mercedesbenz.exchangerate;

import com.mercedesbenz.exchangerate.helper.XmlHelper;
import com.mercedesbenz.exchangerate.model.ExchangeRate;
import com.mercedesbenz.exchangerate.model.ExchangeRateDailyReportData;
import com.mercedesbenz.exchangerate.model.ExchangeRateException;
import com.mercedesbenz.exchangerate.model.ListExchangeRates;
import com.mercedesbenz.exchangerate.service.ExchangeRatesServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExchangeRatesServiceTest {

    @Mock
    WebClient webClient;

    private final XmlHelper xmlHelper = new XmlHelper();

    ExchangeRatesServiceImpl exchangeRatesService;

    private final String filename = "TestExchangeRates.xml";
    private final String[] symbols = new String[]{"EUR"};
    private final String dailyReportFilename = "_TestExchangeRateDailyReport.xml";

    @BeforeEach
    void init() {
        initMocks(this);
        exchangeRatesService = new ExchangeRatesServiceImpl(webClient, xmlHelper);
        exchangeRatesService.setFilename(filename);
        exchangeRatesService.setDefaultSymbols(symbols);
        exchangeRatesService.setDailyReportFilename(dailyReportFilename);
    }

    @AfterAll
    static void tearDown() throws IOException {
        String date = LocalDate.now().toString();
        String file1name = date+"_TestExchangeRateDailyReport.xml";
        String filename = "TestExchangeRates.xml";
        Path path = Paths.get(file1name);
        if(Files.exists(path)){
            Files.delete(path);
        }

        path = Paths.get(filename);
        if(Files.exists(path)){
            Files.delete(path);
        }
    }

    @Test()
    @Order(1)
    void successfully_fetch_exchangerates_from_external_api() throws JAXBException {
        final var uriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        final var headersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        final var responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);
        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri("/latest?base=EUR","1"))
                .thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve())
                .thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(ExchangeRate.class))
                .thenReturn(Mono.just(mockExchangeRate()));
        exchangeRatesService.fetchExchangeRates();
        File file = new File(filename);
        ListExchangeRates listExchangeRates = xmlHelper.unmarshallListExchangeRates(file);
        assertThat(listExchangeRates.getExchangeRates().size(), equalTo(1));
        assertThat(listExchangeRates.getExchangeRates().get(0).getBase(), equalTo("EUR"));
        assertThat(listExchangeRates.getExchangeRates().get(0).getDate(), equalTo(LocalDate.now().toString()));
    }

    @Test()
    void successfully_read_current_exchange_rate() {

        ExchangeRate exchangeRate = exchangeRatesService.readCurrentExchangeRate("USD","EUR");

        assertThat(exchangeRate.getBase(), equalTo("EUR"));
        assertThat(exchangeRate.getDate(), equalTo(LocalDate.now().toString()));
        assertThat(exchangeRate.getRates().size(), equalTo(1));
        assertThat(exchangeRate.getRates().get("USD"), equalTo(1.3002));
    }

    @Test()
    void an_unknown_symbol_causes_an_exception_when_reading_current_exchange_rate() {
        Exception exception = assertThrows(ExchangeRateException.class, () -> {
            exchangeRatesService.readCurrentExchangeRate("ZWL","EUR");
        });
        assertThat(exception.getMessage(), equalTo("Unknown Symbol : ZWL"));
        exception = assertThrows(ExchangeRateException.class, () -> {
            exchangeRatesService.readCurrentExchangeRate("USD","GBP");
        });
        assertThat(exception.getMessage(), equalTo("Unknown Symbol : GBP"));

    }

    @Test()
    void successfully_read_daily_report_data()  {

        ExchangeRateDailyReportData reportData = exchangeRatesService.readDailyReport("EUR");
        assertThat(reportData.getBase(), equalTo("EUR"));
        assertThat(reportData.getDate(), equalTo(LocalDate.now().toString()));
        assertThat(reportData.getIntervals().size(), equalTo(1));
        assertThat(reportData.getIntervals().get(0).getRates().size(), equalTo(4));
        assertThat(reportData.getIntervals().get(0).getRates().get("USD"), equalTo(1.3002));
        assertThat(reportData.getIntervals().get(0).getRates().get("CHF"), equalTo(0.99000));
    }

    @Test()
    void an_exception_is_thrown_when_reading_daily_report_for_an_unknown_symbol() {
        Exception exception = assertThrows(ExchangeRateException.class, () -> {
            exchangeRatesService.readDailyReport("ZWL");
        });
        assertThat(exception.getMessage(), equalTo("No ExchangeRate Daily Report Found : ZWL"));
    }

    private ExchangeRate  mockExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBase("EUR");
        exchangeRate.setDate(LocalDate.now().toString());
        exchangeRate.setTimestamp(System.currentTimeMillis()/1000);
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.3002);
        rates.put("GBP", 2.100);
        rates.put("CHF", 0.99000);
        rates.put("EUR", 1D);
        exchangeRate.setRates(rates);
        return exchangeRate;
    }
}
