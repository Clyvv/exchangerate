package com.mercedesbenz.exchangerate.helper;

import com.mercedesbenz.exchangerate.model.ExchangeRateDailyReport;
import com.mercedesbenz.exchangerate.model.ListExchangeRates;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Component
public class XmlHelper {

    public ListExchangeRates unmarshallListExchangeRates(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ListExchangeRates.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (ListExchangeRates) unmarshaller.unmarshal(file);
    }

    public ExchangeRateDailyReport unmarshallReportData(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeRateDailyReport.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (ExchangeRateDailyReport) unmarshaller.unmarshal(file);
    }

    public void marshallListExchangeRates(File file, ListExchangeRates listExchangeRates) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ListExchangeRates.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.marshal(listExchangeRates, file);
        marshaller.marshal(listExchangeRates, System.out);
    }

    public void marshallReportData(File file, ExchangeRateDailyReport report) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeRateDailyReport.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        marshaller.marshal(report, file);
        marshaller.marshal(report, System.out);
    }
}
