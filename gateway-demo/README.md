# spring-cloud-gateway document
https://cloud.spring.io/spring-cloud-gateway/reference/html
Next: 8
need to test: 6.5 , 6.6, 6.10, 6.16, 6.19, 7.2, 7.9(part)


# conclusion by myself
- each request only matches one route.


# Marking An Exchange As Routed
- Below Filters use ServerWebExchangeUtils.setAlreadyRouted
```
ForwardPathFilter
ForwardRoutingFilter
NettyRoutingFilter
WebClientHttpRoutingFilter
WebsocketRoutingFilter
```

- Below Filters use ServerWebExchangeUtils.setAlreadyRouted 
```
NettyRoutingFilter
WebClientHttpRoutingFilter
WebsocketRoutingFilter
JsonToGrpcGatewayFilterFactory
```


