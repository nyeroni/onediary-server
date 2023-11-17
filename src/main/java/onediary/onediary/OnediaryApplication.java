package onediary.onediary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnediaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnediaryApplication.class, args);
	}

}
