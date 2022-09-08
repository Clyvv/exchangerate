package com.mercedesbenz.exchangerate;

import com.mercedesbenz.exchangerate.helper.XmlHelper;
import com.mercedesbenz.exchangerate.service.ExchangeRatesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.MockitoAnnotations.initMocks;

public class ExchangeRatesServiceTest {

    @Mock
    WebClient webClient;

    @Mock
    XmlHelper xmlHelper;

    ExchangeRatesServiceImpl exchangeRatesService;

    @BeforeEach
    void init() {
        initMocks(this);
        exchangeRatesService = new ExchangeRatesServiceImpl(webClient, xmlHelper);
    }

    @Test()
    void successfully_schedule_a_visit() {

    }
}
