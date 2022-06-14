package northwind.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.core.util.ArrayUtils;
import org.springframework.web.filter.GenericFilterBean;

public class AfterBasicAuthenticationFilter extends GenericFilterBean{
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String authHeader = httpRequest.getHeader("Authorization");
		if(Objects.nonNull(authHeader) && authHeader.contains("Basic")) {
			String[] parts = authHeader.split("Basic");
		       if(ArrayUtils.getLength(parts) > 1) {
		    	   decode(parts[parts.length-1].trim()); 
		       }
		}
		chain.doFilter(request, response);
	}
	
	private static void decode(final String encoded) {
       byte[] decodedBytes  = Base64.getDecoder().decode(encoded);
       String pair = new String(decodedBytes);
       String[] userDetails = pair.split(":", 2);
       if(ArrayUtils.getLength(userDetails) == 2) {
    	   System.out.println("Username="+userDetails[0]+",Password="+userDetails[1]);  
       }     
    }
}
