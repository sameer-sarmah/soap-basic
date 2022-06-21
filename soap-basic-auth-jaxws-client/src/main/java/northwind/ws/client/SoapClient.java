package northwind.ws.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.io.IOUtils;
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
		//GetCustomerResponse customerResponse = getCustomerResponse(customerRequest);
		GetCustomerResponse customerResponse = getCustomerResponseWithDispatcher(customerRequest);
		return Optional.of(customerResponse.getCustomer());

	}
	
	private GetCustomerResponse getCustomerResponse(GetCustomerRequest customerRequest) {
		URL wsdlUrl = this.getClass().getClassLoader().getResource("customers.wsdl");
		QName serviceName = new QName("http://domain.northwind/customer","CustomersPortService");
		Service customerService = Service.create(wsdlUrl, serviceName);
		CustomersPort customerInterface = customerService.getPort(CustomersPort.class);
		BindingProvider prov = (BindingProvider)customerInterface;
		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY,username);
		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,password);
		GetCustomerResponse customerResponse = customerInterface.getCustomer(customerRequest);
		return customerResponse;
	}
	
	private GetCustomerResponse getCustomerResponseWithDispatcher(GetCustomerRequest customerRequest) {
		URL wsdlUrl = this.getClass().getClassLoader().getResource("customers.wsdl");
		QName serviceName = new QName("http://domain.northwind/customer","CustomersPortService");
		Service customerService = Service.create(wsdlUrl, serviceName);
		QName portName = new QName("http://domain.northwind/customer","CustomersPortSoap11");
		customerService.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:8888/service/customer");
	    JAXBContext jaxbContext =  getJAXBContext();

	    Dispatch<Object> dispatch = customerService.createDispatch(portName, jaxbContext, Service.Mode.PAYLOAD);
	    Map<String, Object> requestContext = dispatch.getRequestContext();
	    requestContext.put(Dispatch.PASSWORD_PROPERTY, password);
	    requestContext.put(Dispatch.USERNAME_PROPERTY, username);
	    
		GetCustomerResponse customerResponse = (GetCustomerResponse)dispatch.invoke(customerRequest);
		return customerResponse;
	}
	
    private JAXBContext getJAXBContext() {
        JAXBContext jaxBcontext = null;
		try {
			jaxBcontext = JAXBContext.newInstance("northwind.domain.customer");
	        Marshaller marshaller= jaxBcontext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);	        
		} catch (JAXBException e) {
			e.printStackTrace();
		} 
		return jaxBcontext;

	}
}
