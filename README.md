1, get token
post http://localhost/oauth/token?grant_type=client_credentials&client_id=app&client_secret=app
Content-Type:application/x-www-form-urlencoded
 
2, send log

post http://localhost/api/v1/collector/hs/app/log?token=xxxx

Content-Type:application/json

{ "indexName" : "myindex",     "code" : "1",          "message" : "testMsg",          "position" : "usa","eventType" : "alarm","eventTimestamp" : "2019-01-23T10:57:17+08:00",          "@timestamp" : "2019-01-23T10:57:17+08:00"}

或者用http header方式
1） 给/oauth/token 发送post请求获取token
请求头：Authorization:Basic +clientid:secret 的base64加密字符串 (认证服务器中设置的client信息)

2）给/oauth/token 发送post请求刷新token
请求头: 不需要
请求参数：
grant_type:refresh_token
refresh_token:获取token时返回的refreshToken的value
3）访问受保护的资源，比如/user/2
类型：get请求
请求头：Authorization: bearer + tokenValue


