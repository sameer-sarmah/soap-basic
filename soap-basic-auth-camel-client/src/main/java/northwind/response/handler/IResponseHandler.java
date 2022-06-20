package northwind.response.handler;

import java.util.Map;
import java.util.Optional;

public interface IResponseHandler {
 boolean canHandle(Object response,Map<String,Object> headers);
 void handle(Object response);
 default String getContentType(Map<String,Object> headers) {
		Map<String,Object> responseContext = (Map<String, Object>) headers.get("ResponseContext");
		Optional<Map.Entry<String, Object>> contentTypeEntry = responseContext.entrySet().stream().
				filter(responseEntry -> responseEntry.getKey().contains("Content-Type"))
				.findAny();
		return contentTypeEntry.isPresent() ? (String) contentTypeEntry.get().getValue() : null;
 }
}
