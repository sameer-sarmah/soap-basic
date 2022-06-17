package northwind.config;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import northwind.domain.customer.CustomersPort;

@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
@ComponentScan(basePackages = {"northwind"})
public class CxfConfig {

	@Value("${service.username}")
	private String username;
	
	@Value("${service.password}")
	private String password;
	
    @Bean
    public LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setPrettyLogging(true);
        return loggingFeature;
    }

    @Bean(name = "customerServiceClient")
    public CustomersPort customerServiceClient(@Autowired LoggingFeature loggingFeature){
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(CustomersPort.class);
        factoryBean.setPassword(password);
        factoryBean.setUsername(username);
        Map<String, Object> properties = factoryBean.getProperties();
        if(properties == null){
            properties = new HashMap<>();
        }

        properties.put("javax.xml.ws.client.connectionTimeout", 5000);
        properties.put("javax.xml.ws.client.receiveTimeout", 3000);

        factoryBean.setProperties(properties);

        factoryBean.getFeatures().add(loggingFeature);

        CustomersPort theService = (CustomersPort) factoryBean.create();
        BindingProvider bindingProvider = (BindingProvider) theService;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8888/service");

        return theService;
    }
    
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
