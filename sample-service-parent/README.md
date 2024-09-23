Sample Service can be invoked as follows:

http://localhost:8081/rest/trades/1

curl -v localhost:8081/rest/trades

curl -v localhost:8081/rest/trades/2

curl -X POST localhost:8081/rest/trades -H 'Content-type:application/json' -d '{"instrument": "SWAP", "counterparty": "
UBS", "notional": 50000, "buySell": "BUY", "currency": "USD"}'

curl -X DELETE localhost:8081/rest/trades/3

curl -X PUT localhost:8081/rest/trades/2 -H 'Content-type:application/json' -d '{"instrument": "SWAP", "counterparty": "
UBS", "notional": 50000, "buySell": "BUY", "currency": "USD"}'

Swagger docs:
http://localhost:8081/rest/v3/api-docs

http://localhost:8081/rest/swagger-ui/index.html