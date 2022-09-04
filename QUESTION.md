### Should I consider input datetime series from different days?
I assume yes unless the API would specifically describe dates should be limited to single day.
Not implemented but a possible solution would be group input date_time series by date and then stream/fan-out and calc each date's tax, later collect/sum together.
### Replaced Date class with LocalDateTime class with the service
1.LocalDateTime has more modern api
2.Easier for communication as it more nature to communication in local time for example making tax rules with bussiness parties
### Use zoned time in the rest api, should also reflect in the api swagger
Reason is it's hard to predict zone info between service call in between, it is better to provide detailed zone info
### Add possibility to customize tax rule via json config file
1. Not a complete implementation, just added the framework and for demo purpose
2. The idea is one without tech background could edit and compose the rules
### Added some unit/integration test
### 
