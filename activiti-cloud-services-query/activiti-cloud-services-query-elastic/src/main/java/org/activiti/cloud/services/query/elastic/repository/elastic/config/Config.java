package org.activiti.services.query.app.repository.es.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.activiti.services.query.app.model.es")
@ComponentScan(basePackages = {"org.activiti.services.query.app.model.es"})
public class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    @Value("${elasticsearch.home:/Users/gmalanga/Desktop/elasticsearch-5.5.0}")
    private String elasticsearchHome;

    @Value("${elasticsearch.clusterName:my-application")
    private String clusterName;

    @Bean
    public Client client() {
        try {
            final Path tmpDir = Files.createTempDirectory(Paths.get("/tmp/"),
                                                          "elasticsearch_data");
            logger.debug(tmpDir.toAbsolutePath().toString());

            final Settings elasticsearchSettings = Settings.builder()
                                                           .put("cluster.name", clusterName)
                                                           .put("client.transport.sniff", true)
                                                           .put("http.enabled", "false")
                                                           .put("path.data", tmpDir.toAbsolutePath().toString())
                                                           .put("path.home", elasticsearchHome)
                                                           .build();

            TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);

            return client;

        } catch (final IOException ioex) {
            logger.error("Cannot create temp dir", ioex);
            throw new RuntimeException();
        }
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }
}
