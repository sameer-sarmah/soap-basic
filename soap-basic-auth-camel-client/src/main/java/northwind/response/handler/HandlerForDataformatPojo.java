package northwind.response.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import northwind.domain.customer.GetCustomerResponse;
import northwind.util.Util;

@Component
public class HandlerForDataformatPojo implements IResponseHandler{

	@Override
	public boolean canHandle(Object response) {
		if(response instanceof List) {
			List contents = (List)response;
			if(contents.size() > 0 ) {
				Optional<GetCustomerResponse> customerResponseOptional =  contents.stream()
																			.filter(content -> content instanceof GetCustomerResponse)
																			.findFirst();
				return customerResponseOptional.isPresent();
			}
			
		}
		return false;
	}

	@Override
	public void handle(Object response) {
		//When dataformat=POJO ,which is the default value
		if(response instanceof List) {
			List contents = (List)response;
			if(contents.size() > 0 ) {
				Optional<GetCustomerResponse> customerResponseOptional =  contents.stream()
																			.filter(content -> content instanceof GetCustomerResponse)
																			.findFirst();
				customerResponseOptional.ifPresent(customerResponse -> {
					String responseXml = Util.marshallInstaceToXml(GetCustomerResponse.class, customerResponse);
					System.out.println(responseXml);
				});
			}
			
		}
		
	}

}
