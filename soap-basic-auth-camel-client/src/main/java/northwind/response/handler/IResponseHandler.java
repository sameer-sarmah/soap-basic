package northwind.response.handler;

public interface IResponseHandler {
 boolean canHandle(Object response);
 void handle(Object response);

}
