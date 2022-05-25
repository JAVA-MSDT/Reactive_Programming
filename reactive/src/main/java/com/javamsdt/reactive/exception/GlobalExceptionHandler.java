package com.javamsdt.reactive.exception;

import java.util.Map;
import java.util.Optional;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    /**
     * Create a new {@code AbstractErrorWebExceptionHandler}.
     *
     * @param errorAttributes    the error attributes
     * @param webproperties          the resources configuration properties
     * @param applicationContext the application context
     * @since 2.4.0
     */
    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
            WebProperties webproperties,
            ApplicationContext applicationContext,
            ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, webproperties.getResources(), applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::formatErrorResponse);
    }

    private Mono<ServerResponse> formatErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        int statusCode = (int) Optional.ofNullable(errorAttributesMap.get("status")).orElse(500);
        return ServerResponse
                .status(statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributesMap));
    }
}
