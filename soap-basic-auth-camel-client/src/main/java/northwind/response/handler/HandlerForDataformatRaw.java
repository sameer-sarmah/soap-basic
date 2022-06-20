package northwind.response.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Component
public class HandlerForDataformatRaw  implements IResponseHandler{
	@Override
	public void handle(Object response) {
		 if(response instanceof InputStream) {
			 InputStream responseInputStream = (InputStream)response;
			 String responseXml = null;
			try {
				responseXml = IOUtils.toString(responseInputStream, Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
			}
			 System.out.println(formatXml(responseXml));
		}
		
	}

	@Override
	public boolean canHandle(Object response, Map<String, Object> headers) {
		String contentType = getContentType(headers);	
		return contentType.contains("text/xml") && response instanceof InputStream;
	}
	
	private String formatXml(String input) {

		try {
			InputSource src = new InputSource(new StringReader(input));
	        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return input;
	
	}
}
