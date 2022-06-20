package northwind.app;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import northwind.config.CxfConfig;
import northwind.domain.customer.Customer;
import northwind.util.Util;
import northwind.ws.client.SoapClient;

@Import({CxfConfig.class})
@SpringBootApplication
public class CustomerJaxWsClientRunner implements ApplicationRunner{

	@Autowired
	private SoapClient client;
	
	public static void main(String[] args) {
		SpringApplication.run(CustomerJaxWsClientRunner.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
		client.getCustomer("ALFKI").ifPresent((customer)->{
			QName customerQName = new QName("http://domain.northwind/customer","Customer");
			JAXBElement<Customer> customerElement = new JAXBElement(customerQName, Customer.class,customer );
			String customerXml = Util.marshallInstaceToXml(customerElement);
			System.out.println(customerXml);
		});

		}
		catch (Exception e) {
			System.err.println("ExceptionMessage="+e.getMessage());
		}
	}

}
