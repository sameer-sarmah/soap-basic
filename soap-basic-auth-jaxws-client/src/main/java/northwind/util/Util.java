package northwind.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;

public class Util {
    public static String getCustomerResponseXml(DOMSource domSource) throws TransformerConfigurationException,
    TransformerFactoryConfigurationError, TransformerException{
	    Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    StringWriter stringWriter = new StringWriter();
	    transformer.transform(domSource, new StreamResult(stringWriter));
	    return stringWriter.toString();
    }
    
    
    public static String marshallInstaceToXml(Class classToMarshall,Object instanceToMashall) {
        JAXBContext jaxBcontext;
        String responseXml = null;
		try {
			jaxBcontext = JAXBContext.newInstance(classToMarshall);
	        Marshaller marshaller= jaxBcontext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        var outputStream = new ByteArrayOutputStream();
	        marshaller.marshal(instanceToMashall, outputStream);
	        var inputStream = new ByteArrayInputStream(outputStream.toByteArray());
	        responseXml = IOUtils.toString(inputStream, Charset.defaultCharset());
	        
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseXml;

	}
    
    public static <T> String marshallInstaceToXml(JAXBElement<T> element) {
        JAXBContext jaxBcontext;
        String responseXml = null;
		try {
			jaxBcontext = JAXBContext.newInstance(element.getDeclaredType());
	        Marshaller marshaller= jaxBcontext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
	        var outputStream = new ByteArrayOutputStream();
	        marshaller.marshal(element, outputStream);
	        var inputStream = new ByteArrayInputStream(outputStream.toByteArray());
	        responseXml = IOUtils.toString(inputStream, Charset.defaultCharset());
	        
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseXml;

	}
}
