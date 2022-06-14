package northwind.ws.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import northwind.domain.customer.wsdl.Customer;
import northwind.domain.customer.wsdl.GetCustomerRequest;
import northwind.domain.customer.wsdl.GetCustomerResponse;

@Component
public class SoapClient extends WebServiceGatewaySupport{

	@Value("${service.url}")
	private String serviceUrl;
	
	public Optional<Customer> getCustomer(String customerId) {
		GetCustomerRequest customerRequest = new GetCustomerRequest();
		customerRequest.setCustomerID(customerId);
		
		GetCustomerResponse customerResponse = (GetCustomerResponse)getWebServiceTemplate()
		        .marshalSendAndReceive(serviceUrl, customerRequest);
		
		return Optional.of(customerResponse.getCustomer());
	}
}
