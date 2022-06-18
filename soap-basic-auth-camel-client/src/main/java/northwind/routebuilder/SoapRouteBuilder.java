package northwind.routebuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import northwind.domain.customer.GetCustomerResponse;

@Component
public class SoapRouteBuilder extends RouteBuilder {
		
	@Value("${service.username}")
	private String username;
	
	@Value("${service.password}")
	private String password;
	
    @Override
    public void configure() throws Exception {
		   String endpoint = "cxf://http://localhost:8888/service/customers?serviceClass=northwind.domain.customer.CustomersPort"
			   		+ "&serviceName=CustomersPortService&wsdlURL=customers.wsdl"
			   		+"&username="+username+"&password="+password;
		   System.out.println(endpoint);
		   CxfEndpoint cxfEndpoint = (CxfEndpoint)getContext().getEndpoint(endpoint);
		   cxfEndpoint.setUsername(username);
		   cxfEndpoint.setPassword(password);
		   
		   
        from("direct:start")
            .to(endpoint)
            .process((exchange)->{    	
            	if(Objects.nonNull(exchange.getIn()) && Objects.nonNull(exchange.getIn().getBody())) {
            		Object response = exchange.getIn().getBody();
            		if(response instanceof List) {
            			List contents = (List)response;
            			if(contents.size() > 0 ) {
            				Optional<GetCustomerResponse> customerResponseOptional =  contents.stream().filter(content -> content instanceof GetCustomerResponse).findFirst();
            				customerResponseOptional.ifPresent(customerResponse -> {
            					System.out.println("CustomerName="+customerResponse.getCustomer().getCustomerName());	
            				});
            			}
            			
            		}
            	}
            });
    }
}