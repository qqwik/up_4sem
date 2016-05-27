package by.bsu.up.chat.server;

import by.bsu.up.chat.Constants;
import by.bsu.up.chat.InvalidTokenException;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.up.chat.logging.impl.FileLogger;
import by.bsu.up.chat.utils.MessageHelper;
import by.bsu.up.chat.utils.StringUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerHandler implements HttpHandler {

    private static final Logger logger = Log.create(ServerHandler.class);
    private static final Logger ServerLogger = new FileLogger("serverlogger.txt");

    private List<String> messageStorage = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Response response;

        ServerLogger.info("Request begin");
        try {
            response = dispatch(httpExchange);
        } catch (Throwable e) {
            logger.error("An error occurred when dispatching request.", e);
            ServerLogger.error("An error occurred when dispatching request.", e);
            response = new Response(Constants.RESPONSE_CODE_INTERNAL_SERVER_ERROR, "Error while dispatching message");
        }
        sendResponse(httpExchange, response);
        ServerLogger.info("Request end");
    }

    private Response dispatch(HttpExchange httpExchange) {
        ServerLogger.info("Method " + httpExchange.getRequestMethod());
        if (Constants.REQUEST_METHOD_GET.equals(httpExchange.getRequestMethod())) {
            return doGet(httpExchange);
        } else if (Constants.REQUEST_METHOD_POST.equals(httpExchange.getRequestMethod())) {
            return doPost(httpExchange);
        } else {
            return new Response(Constants.RESPONSE_CODE_METHOD_NOT_ALLOWED,
                    String.format("Unsupported http method %s", httpExchange.getRequestMethod()));
        }
    }

    private Response doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) {
            ServerLogger.info("Response: absent query in request");
            return Response.badRequest("Absent query in request");
        }
        Map<String, String> map = queryToMap(query);
        String token = map.get(Constants.REQUEST_PARAM_TOKEN);
        ServerLogger.info("Request parameters: token=" + token);
        if (StringUtils.isEmpty(token)) {
            ServerLogger.info("Response: Token query parameter is required");
            return Response.badRequest("Token query parameter is required");
        }
        try {
            int index = MessageHelper.parseToken(token);
            if (index > messageStorage.size()) {
                ServerLogger.info(String.format("Incorrect token in request: %s. Server does not have so many messages", token));
                return Response.badRequest(
                        String.format("Incorrect token in request: %s. Server does not have so many messages", token));
            }
            String responseBody = MessageHelper.buildServerResponseBody(messageStorage.subList(index, messageStorage.size()), messageStorage.size());
            ServerLogger.info("Response: History size=" + messageStorage.size());
            return Response.ok(responseBody);
        } catch (InvalidTokenException e) {
            ServerLogger.info("Response: Incorrect format of token");
            return Response.badRequest(e.getMessage());
        }
    }

    private Response doPost(HttpExchange httpExchange) {
        try {
            String message = MessageHelper.getClientMessage(httpExchange.getRequestBody());
            ServerLogger.info(String.format("Received new message from user: %s", message));
            logger.info(String.format("Received new message from user: %s", message));
            messageStorage.add(message);
            return Response.ok();
        } catch (ParseException e) {
            ServerLogger.error("Could not parse message.", e);
            logger.error("Could not parse message.", e);
            return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
    }

    private void sendResponse(HttpExchange httpExchange, Response response) {
        try (OutputStream os = httpExchange.getResponseBody()) {
            byte[] bytes = response.getBody().getBytes();

            Headers headers = httpExchange.getResponseHeaders();
            headers.add(Constants.REQUEST_HEADER_ACCESS_CONTROL,"*");
            httpExchange.sendResponseHeaders(response.getStatusCode(), bytes.length);

            os.write( bytes);
            ServerLogger.info("Response sent");
        } catch (IOException e) {
            ServerLogger.error("Could not send response", e);
            logger.error("Could not send response", e);
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();

        for (String queryParam : query.split(Constants.REQUEST_PARAMS_DELIMITER)) {
            String paramKeyValuePair[] = queryParam.split("=");
            if (paramKeyValuePair.length > 1) {
                result.put(paramKeyValuePair[0], paramKeyValuePair[1]);
            } else {
                result.put(paramKeyValuePair[0], "");
            }
        }
        return result;
    }
    private Map<String, String> queryToMap2(String query) {
        return Stream.of(query.split(Constants.REQUEST_PARAMS_DELIMITER))
                .collect(Collectors.toMap(
                        keyValuePair -> keyValuePair.split("=")[0],
                        keyValuePair -> keyValuePair.split("=")[1]
                ));
    }
}
