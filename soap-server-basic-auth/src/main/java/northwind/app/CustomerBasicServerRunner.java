package northwind.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import northwind.config.WebServiceConfig;
//https://localhost:8888/service/customers.wsdl
@Import({WebServiceConfig.class})
@SpringBootApplication
public class CustomerBasicServerRunner {

	public static void main(String[] args) {
		SpringApplication.run(CustomerBasicServerRunner.class, args);
	}

}
