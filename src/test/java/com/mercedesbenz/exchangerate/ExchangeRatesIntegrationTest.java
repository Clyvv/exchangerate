package com.mercedesbenz.exchangerate;

import com.mercedesbenz.exchangerate.model.ExchangeRate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExchangeRatesIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    public void getCurrentExchangeRateReturnsOKStatus() throws InterruptedException {
        // wait for the initial fetch request
        TimeUnit.SECONDS.sleep(2);
        this.webTestClient
                .get()
                .uri("http://localhost:" + port + "/exchangerates/current/USD/EUR")
                .exchange()
                .expectStatus()
                .isEqualTo(OK);
    }

    @Test
    public void getDailyReportReturnsBadRequestStatus() {
        this.webTestClient
                .get()
                .uri("http://localhost:" + port + "/exchangerates/dailyreport/ZWL")
                .exchange()
                .expectStatus()
                .isEqualTo(BAD_REQUEST);
    }

    @AfterAll
    static void tearDown() throws IOException {
        String date = LocalDate.now().toString();
        String file1name = date+"_ExchangeRateDailyReport.xml";
        String filename = "ExchangeRates.xml";
        Path path = Paths.get(file1name);
        if(Files.exists(path)){
            Files.delete(path);
        }

        path = Paths.get(filename);
        if(Files.exists(path)){
            Files.delete(path);
        }
    }


}
