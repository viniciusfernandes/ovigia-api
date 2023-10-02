package br.com.ovigia.error;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalExceptionHandlerConfig {

    @Bean
    public WebExceptionHandler webExceptionHandler() {
        return (exchange, ex) -> {
            ServerHttpResponse response = exchange.getResponse();
            if (ex instanceof InvalidRequestException) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(ex.getMessage().getBytes())));
            } else {
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return response.setComplete();
            }
        };
    }
}
