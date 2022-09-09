
# Exchange Rate Service

A simple Exchange Rate Service for the currencies USD, GBP, EUR and CHF only.  The application is dockerized and 
therefore Docker must be installed before running it. Use Http REST calls to interact with the application.

## Starting up

The application exposes port 9090 internally. Use the following command to start the application.
``docker run -d -p 9090:9090 cmawoko/exchangerate`` <br><br>

This should be enough to start and test.  However, the application uses [Fixer-API](https://apilayer.com/marketplace/fixer-api) to fetch exchange rates.  
The configured api-key for this service should be enough to test but it has a daily limit of 100 requests. It is 
advisable to get a fresh api-key from this service and starting the application specifying the key as follows.
``docker run -p 9090:9090 -e "FIXERAPI_APIKEY=you-api-key" cmawoko/exchangerate``
<br><br>
**The image is pulled from Dockerhub**
## Usage

The application makes requests to the [Fixer-API](https://apilayer.com/marketplace/fixer-api) every 2 hours.  
This process can be triggered manually by making a POST request to the following api.
``/exchangerates/latest`` <br><br>

Make a GET request to the ``/exchangerates/current/{to}/{from}`` api to get the current exchange rate between the 2 
specified currencies where ``{to}`` and ``{from}`` are symbols in ``[USD, GBP, EUR, CHF]``.<br><br>

To get a daily report for a specific currency, make a GET request to the ``/exchangerates/dailyreport/{baseCode}`` api.