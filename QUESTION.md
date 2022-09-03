1. Should I consider input datetime series with different days?
I assume yes unless the API would specifically describe dates should be limited to single day.
My idea would be group input date time series by date and then use stream to fan-out and calc each date's tax, later collect/sum it together.