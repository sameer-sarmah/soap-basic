package northwind.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;

import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.support.DefaultMessage;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.ws.client.WebServiceTransportException;

import northwind.config.CxfConfig;
import northwind.domain.customer.GetCustomerRequest;
import northwind.routebuilder.SoapRouteBuilder;

@Import({CxfConfig.class})
@SpringBootApplication
public class CustomerCamelClientRunner implements ApplicationRunner{
	
	@Autowired
	private SoapRouteBuilder routeBuilder;
	
	@Autowired 
	private ProducerTemplate producerTemplate;
	
	@Autowired
	private CamelContext context ;
	
	public static void main(String[] args) {
		SpringApplication.run(CustomerCamelClientRunner.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
			//sendRequestForDataFormatPayload();
			//sendRequestForDataFormatPojo();
			sendRequestForDataFormatMessage();
		}
	
	
	private void sendRequestForDataFormatMessage() {
		try {
			context.addRoutes(routeBuilder);
	        context.start();
	        
			InputStream inputStream = CustomerCamelClientRunner.class.getClassLoader().getResourceAsStream("GetCustomerRequest.xml");
			String getCustomerRequest  = IOUtils.toString(inputStream, Charset.defaultCharset());
			producerTemplate.sendBody("direct:start", getCustomerRequest);
	        Thread.sleep(10000);
	        context.stop();

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
	
	private void sendRequestForDataFormatPayload() {
		try {
			context.addRoutes(routeBuilder);
	        context.start();
	        
			GetCustomerRequest customerRequest = new GetCustomerRequest();
			customerRequest.setCustomerID("ALFKI");
		
			String customerRequestXml = marshallCustomerRequest(customerRequest);
			
			producerTemplate.sendBody("direct:start", customerRequestXml);
			/*Alternate approach
			 * 
			SOAPMessage soapMsg = MessageFactory.newInstance()
					.createMessage(null, IOUtils.toInputStream(customerRequestXml, Charset.defaultCharset().toString()));
			producerTemplate.sendBody("direct:start", soapMsg);
			 * 
			 * */
	        Thread.sleep(10000);
	        context.stop();

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
	
	private void sendRequestForDataFormatPojo() {
		try {
			context.addRoutes(routeBuilder);
	        context.start();
	        
			GetCustomerRequest customerRequest = new GetCustomerRequest();
			customerRequest.setCustomerID("ALFKI");
			
	      	producerTemplate.sendBody("direct:start", customerRequest);
	        Thread.sleep(10000);
	        context.stop();

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
	
	private String marshallCustomerRequest(GetCustomerRequest customerRequest) {

		try {
			JAXBContext jaxBcontext = JAXBContext.newInstance(northwind.domain.customer.GetCustomerRequest.class);
			 Marshaller marshaller = jaxBcontext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        var outputStream = new ByteArrayOutputStream();
	        marshaller.marshal(customerRequest, outputStream);
	        var inputStream = new ByteArrayInputStream(outputStream.toByteArray());
	        String customerRequestXml = IOUtils.toString(inputStream, Charset.defaultCharset());
	        return customerRequestXml;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
