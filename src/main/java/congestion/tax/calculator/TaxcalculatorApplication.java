package congestion.tax.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class TaxcalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaxcalculatorApplication.class, args);
	}

}
