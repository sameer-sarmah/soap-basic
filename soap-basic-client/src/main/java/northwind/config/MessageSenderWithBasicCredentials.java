package northwind.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

@Component
public class MessageSenderWithBasicCredentials extends HttpUrlConnectionMessageSender{

	@Value("${service.username}")
	private String username;
	
	@Value("${service.password}")
	private String password;
	
    @Override
    protected void prepareConnection(HttpURLConnection connection)
            throws IOException {
        String userpassword = username+":"+password;
        String encodedAuthorization = Base64.getEncoder().encodeToString(userpassword.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        super.prepareConnection(connection);
    }
}
