# KriptoRoboWeb

www.kriptorobo.com is a javaweb project that uses BTC API's and generate orders. The project was really good until cryptocurrency prices down dramatically. I shut down the project and share source codes.

## Project Purpose
A Java Thread is observing prices and look for specific price to decide generate an order. You are giving buy price, sell price and trigger price.

When a crypto's price down to buy price, engine buys the crypto. If it raise to trigger price within specified time thenn engine looks for the sell price to sell. If price go up continuously so sell price also.


## Key Features
* https://github.com/BTCTrader/broker-api-docs is used to track price and generate orders.
* User based design. Someone can register and login to generate orders.
* There's a ID check that I used for Turkey with an API (https://tckimlik.nvi.gov.tr/Service/KPSPublic.asmx).
* UI is designed with https://getbootstrap.com/.
* For parallel processing Java ThreadPool is used.
* Google reCAPTCHA is used for login and registration.
* I used https://developers.google.com/chart/ to generate reports.

