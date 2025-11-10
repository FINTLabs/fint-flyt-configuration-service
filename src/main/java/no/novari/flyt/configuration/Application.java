package no.novari.flyt.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
        "no.novari",
        "no.fintlabs"
})
@EnableJpaAuditing(auditorAwareRef = "tokenAuditorAware")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

}
