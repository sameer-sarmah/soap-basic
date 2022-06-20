package northwind.response.handler;

import java.util.List;
import java.util.Objects;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;

import org.apache.camel.component.cxf.CxfPayload;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Element;

import northwind.util.Util;

@Component
public class HandlerForDataformatPayload implements IResponseHandler{

	@Override
	public boolean canHandle(Object response) {
		return response instanceof CxfPayload;
	}

	@Override
	public void handle(Object response) {
		 if(response instanceof CxfPayload) {
			CxfPayload cxfPayload = (CxfPayload)response;
			if(Objects.nonNull(cxfPayload.getBody())) {
				if(cxfPayload.getBody() instanceof List && !CollectionUtils.isEmpty(cxfPayload.getBody()) ) {
					
					 if(cxfPayload.getBody().get(0) instanceof Element) {
						Element element = (Element)cxfPayload.getBody().get(0);
						DOMSource domSource = new DOMSource(element);
    					try {
							System.out.println( Util.getCustomerResponseXml(domSource));
						} catch (TransformerFactoryConfigurationError | TransformerException e) {
							e.printStackTrace();
						}
					} 
				}
				
				
			}
			
		}
		
	}

}
