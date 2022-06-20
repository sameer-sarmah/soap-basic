package northwind.routebuilder;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMSource;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Element;

import com.sun.mail.iap.ResponseHandler;

import northwind.domain.customer.GetCustomerResponse;
import northwind.response.handler.IResponseHandler;
import northwind.util.Util;

@Component
public class SoapRouteBuilder extends RouteBuilder {
		
	@Value("${service.username}")
	private String username;
	
	@Value("${service.password}")
	private String password;
	
	private String CamelCxfMessage = "CamelCxfMessage";
	
	@Autowired
	private List<IResponseHandler> responseHandlers;
	
    @Override
    public void configure() throws Exception {
		   String endpoint = "cxf://http://localhost:8888/service/customers?serviceClass=northwind.domain.customer.CustomersPort"
			   		+ "&serviceName=CustomersPortService&wsdlURL=customers.wsdl"
			   		+"&username="+username+"&password="+password;
		   endpoint = endpoint +"&dataformat=PAYLOAD";
		   	  //endpoint=endpoint 	+"&dataformat=MESSAGE";
		   System.out.println(endpoint);
		   CxfEndpoint cxfEndpoint = (CxfEndpoint)getContext().getEndpoint(endpoint);
		   cxfEndpoint.setUsername(username);
		   cxfEndpoint.setPassword(password);
		   
		   
        from("direct:start")
            .to(endpoint)
            .process((exchange)->{    	
            	if(Objects.nonNull(exchange.getIn()) && Objects.nonNull(exchange.getIn().getBody())) {
            		Object response = exchange.getIn().getBody();
            		
            		Map<String,Object> headers = exchange.getIn().getHeaders();
            		logHeaders(headers);
            		List<IResponseHandler> relevantResponseHandlers = responseHandlers
            			.stream()
            			.filter(responseHandler -> responseHandler.canHandle(response))
            			.collect(Collectors.toList());
            		relevantResponseHandlers.forEach(responseHandler -> responseHandler.handle(response));
            	}
            });
    }
    
    
    private void logHeaders(Map<String,Object> headers ) {
    	System.out.println("Response Code="+headers.get("CamelHttpResponseCode"));
		Map<String,Object> responseContext = (Map<String, Object>) headers.get("ResponseContext");
		for(Map.Entry<String, Object> responseEntry :responseContext.entrySet()) {
			if(Objects.nonNull(responseEntry.getValue()) && (responseEntry.getValue() instanceof String 
					|| responseEntry.getValue() instanceof QName)) {
				System.out.println(responseEntry.getKey()+"="+responseEntry.getValue());
			}
		}
    }
    
    
}