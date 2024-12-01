package main.frame.apigateway.filter;

//@Component
//public class AddRequestHeaderFilter extends AbstractGatewayFilterFactory<AddRequestHeaderFilter.Config> {
//
//    public AddRequestHeaderFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            exchange.getRequest().mutate().header("X-Request-ID", UUID.randomUUID().toString());
//            return chain.filter(exchange);
//        };
//    }
//
//    public static class Config {
//        // Настройки фильтра, если нужны
//    }
//}
