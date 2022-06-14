package northwind.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

import northwind.ws.client.SoapClient;

@ComponentScan(basePackages = { "northwind" })
@Configuration
public class WebServiceConfig {

	@Autowired
	private HttpUrlConnectionMessageSender messageSender;

	@Value("${service.wsdl.url}")
	private String wsdlUrl;

	@Autowired
	private SoapClient client;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage> specified in
		// pom.xml
		marshaller.setContextPath("northwind.domain.customer.wsdl");
		return marshaller;
	}

	@Primary
	@Bean
	public SoapClient soapCustomerStub(Jaxb2Marshaller marshaller) throws Exception {
		client.setDefaultUri(wsdlUrl);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		client.setMessageSender(messageSender);
		return client;
	}

}
