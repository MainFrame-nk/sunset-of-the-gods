package main.frame.apigateway.filter;

//@Component
//public class GlobalErrorFilter extends AbstractGatewayFilterFactory<GlobalErrorFilter.Config> {
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> chain.filter(exchange)
//                .onErrorResume(throwable -> {
//                    ServerHttpResponse response = exchange.getResponse();
//                    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//                    return response.setComplete();
//                });
//    }
//
//    public static class Config {
//        // Настройки фильтра, если нужны
//    }
//}
