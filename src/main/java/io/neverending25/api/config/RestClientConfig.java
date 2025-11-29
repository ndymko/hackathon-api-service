package io.neverending25.api.config;

import io.neverending25.api.client.ParserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {
    @Value("${parser.url}")
    private String parserUrl;

    @Bean
    @Profile("prod")
    public ParserClient parserClient() {
        RestClient restClient = RestClient.builder()
            .baseUrl(parserUrl).build();
        RestClientAdapter restClientAdapter =
            RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory =
            HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxyFactory.createClient(ParserClient.class);
    }
}