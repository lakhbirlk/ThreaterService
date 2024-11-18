package org.theatremanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
public class TheatreManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(TheatreManagementApplication.class, args);
    }
}
