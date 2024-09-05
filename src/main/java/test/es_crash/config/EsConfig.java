package test.es_crash.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class EsConfig extends ElasticsearchConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsConfig.class);
    private final EsProperties esProperties;

    public EsConfig(EsProperties esProperties) {
        this.esProperties = esProperties;
    }

    @Override
    public ClientConfiguration clientConfiguration() {
        LOGGER.info("Will connect to {}", esProperties.getEsHost());
        return ClientConfiguration.builder()
                .connectedTo(esProperties.getEsHost())
                .withBasicAuth("elastic", "elastic123")
                .build();
    }
}
