1, get token
post http://localhost/oauth/token?grant_type=client_credentials&client_id=app&client_secret=app
 Content-Type:application/x-www-form-urlencoded
 
2, send log

post http://localhost/api/v1/collector/hs/app/log?token=xxxx
Content-Type:application/json


{ "indexName" : "myindex",     "code" : "1",          "message" : "testMsg",          "position" : "usa","eventType" : "alarm","eventTimestamp" : "2019-01-23T10:57:17+08:00",          "@timestamp" : "2019-01-23T10:57:17+08:00"}

