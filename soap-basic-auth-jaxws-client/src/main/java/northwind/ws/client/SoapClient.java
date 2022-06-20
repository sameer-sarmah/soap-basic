package northwind.ws.client;

import java.net.URL;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import northwind.domain.customer.Customer;
import northwind.domain.customer.CustomersPort;
import northwind.domain.customer.GetCustomerRequest;
import northwind.domain.customer.GetCustomerResponse;


@Component
public class SoapClient {
	

	@Value("${service.username}")
	private String username;
	
	@Value("${service.password}")
	private String password;
	
	public Optional<Customer> getCustomer(String customerId) {
		GetCustomerRequest customerRequest = new GetCustomerRequest();
		customerRequest.setCustomerID(customerId);
		URL wsdlUrl = this.getClass().getClassLoader().getResource("customers.wsdl");
		QName serviceName = new QName("http://domain.northwind/customer","CustomersPortService");
		Service customerService = Service.create(wsdlUrl, serviceName);
		CustomersPort customerInterface = customerService.getPort(CustomersPort.class);
		BindingProvider prov = (BindingProvider)customerInterface;
		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,username);
		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,password);
		GetCustomerResponse customerResponse = customerInterface.getCustomer(customerRequest);
		return Optional.of(customerResponse.getCustomer());

	}
}
