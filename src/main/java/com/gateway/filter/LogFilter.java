package com.gateway.filter;

import io.netty.buffer.EmptyByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 打印请求日志
 */
@Component
public class LogFilter implements GlobalFilter, Ordered {

        private static Logger logger = LoggerFactory.getLogger(LogFilter.class.getSimpleName());

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

            return cacheRequestBody(exchange, (serverHttpRequest) -> {
                // don't mutate and build if same request object
                if (serverHttpRequest == exchange.getRequest()) {
                    return chain.filter(exchange);
                }
                return chain.filter(exchange.mutate().request(serverHttpRequest).build());
            });
        }

        @Override
        public int getOrder() {
            return -4;
        }

        //---------------------------------------------- private ---------------------------------------------------------

        private void logInfo(ServerHttpRequest request, String body) {

            String uri = request.getPath().value();
            String params = request.getQueryParams().toString();
            String method = request.getMethodValue();
            String ip = request.getRemoteAddress().toString();
            String headers = request.getHeaders().entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + ": [" + String.join(";", entry.getValue()) + "]")
                    .collect(Collectors.joining("\n"));
            long accessDate = System.currentTimeMillis();

            StringBuilder sb = new StringBuilder();
            sb.append("\n==================================[API_CALL]==================================\n");
            sb.append("uri : " + uri + "\n");
            sb.append("method : " + method + "\n");
            sb.append("ip : " + ip + "\n");
            sb.append("params : " + params + "\n");
            sb.append("body : " + body + "\n");
            sb.append("accessDate : " + accessDate + "\n");
            sb.append("headers : { \n" + headers + "  }\n");
            sb.append("==============================================================================\n");
            logger.info(String.valueOf(sb));
        }

        private Mono<Void> cacheRequestBody(ServerWebExchange exchange, Function<ServerHttpRequest, Mono<Void>> function) {
            ServerHttpResponse response = exchange.getResponse();
            ServerHttpRequest request = exchange.getRequest();
            NettyDataBufferFactory factory = (NettyDataBufferFactory) response.bufferFactory();
            // Join all the DataBuffers so we have a single DataBuffer for the body
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .defaultIfEmpty(factory.wrap(new EmptyByteBuf(factory.getByteBufAllocator())))
                    .map((dataBuffer) -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String bodyString = new String(bytes, StandardCharsets.UTF_8);
                        logInfo(request, bodyString);
                        // 这里下面的代码我原先没写，后续的转发直接失效，因为body数据被拿出来了
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory()
                                    .wrap(bytes);
                            return Mono.just(buffer);
                        });

                        return (ServerHttpRequest) new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };
                    }).switchIfEmpty(Mono.just(exchange.getRequest())).flatMap(function);
        }

    }




