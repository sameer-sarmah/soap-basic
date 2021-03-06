package northwind.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.ws.client.WebServiceTransportException;

import northwind.config.WebServiceConfig;
import northwind.ws.client.SoapClient;

@Import({WebServiceConfig.class})
@SpringBootApplication
public class CustomerWSBasicAuthClientRunner implements ApplicationRunner{

	@Autowired
	private SoapClient client;
	
	public static void main(String[] args) {
		SpringApplication.run(CustomerWSBasicAuthClientRunner.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
		client.getCustomer("ALFKI").ifPresent((customer)->{
			System.out.println(customer.getCustomerName());
		});
		}
		catch (Exception e) {
			if(e instanceof WebServiceTransportException) {
				System.err.println("[WebServiceTransportException]	ExceptionMessage="+e.getMessage());
			}
			else {
				System.err.println("ExceptionMessage="+e.getMessage());
			}
		}
	}

}
