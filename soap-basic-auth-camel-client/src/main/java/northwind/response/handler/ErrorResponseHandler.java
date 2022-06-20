package northwind.response.handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseHandler implements IResponseHandler{

	@Override
	public void handle(Object response) {
		 if(response instanceof InputStream) {
			 InputStream responseInputStream = (InputStream)response;
			 String responseBody = null;
			try {
				responseBody = IOUtils.toString(responseInputStream, Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
			}
			 System.out.println(responseBody);
		}
		
	}

	@Override
	public boolean canHandle(Object response, Map<String, Object> headers) {
		String contentType = getContentType(headers);	
		return contentType.contains("application/json") && response instanceof InputStream;
	}

}
