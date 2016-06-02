package chat.chatListner;



import chat.Constants;
import chat.InvalidTokenException;
import chat.common.models.Message;
import chat.common.models.User;
import chat.storage.FileMessageStorage;
import chat.storage.MessageStorage;
import chat.storage.Portion;
import chat.utils.MessageHelper;
import chat.utils.StringUtils;
import org.json.simple.parser.ParseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/chat")//value = "/chat")
public class MainListner extends HttpServlet{

    private MessageStorage messageStorage = new FileMessageStorage();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        String query = req.getQueryString();
        if (query == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/homepage.html");
            dispatcher.forward(req,resp);
            return;
        }
        if (query.equals("users")) {
            List<User> users = messageStorage.getUsers();
            String responseBody = MessageHelper.buildServerResponseBodyUsers(users, messageStorage.userCounter());
            resp.addHeader("users", responseBody);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/homepage.html");
            dispatcher.forward(req,resp);
            return;
        }
        Map<String, String> map = queryToMap(query);
        String token = map.get(Constants.REQUEST_PARAM_TOKEN);
        if (StringUtils.isEmpty(token)) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/homepage.html");
            dispatcher.forward(req,resp);
            return;
        }
        try {
            int index = MessageHelper.parseToken(token);
            if (index > messageStorage.size()) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/homepage.html");
                dispatcher.forward(req,resp);
                return;
            }
            Portion portion = new Portion(index);
            List<Message> messages = messageStorage.getPortion(portion);
            String responseBody = MessageHelper.buildServerResponseBody(messages, messageStorage.size());

            resp.addHeader("messages", responseBody);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/homepage.html");
            dispatcher.forward(req,resp);

            return;
//            return Response.ok(responseBody);
        } catch (InvalidTokenException e) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/homepage.html");
            dispatcher.forward(req,resp);
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        String query = req.getQueryString();

        try {
            if(query!= null&&query.contains("users")){
                Map<String, String> map = queryToMap(query);
                String token = map.get(Constants.REQUEST_PARAM_USER_RESPONCE);
                if(token.equals("add")){

                    User user = MessageHelper.getNewUser(req.getInputStream());

//
                    messageStorage.addUser(user);
                    ((HttpServletResponse) resp).sendRedirect("/login.jsp");
                    return;
                }else if(token.equals("update")){
                    User user = MessageHelper.getNewUser(req.getInputStream());
//                    logger.info(String.format("user edit profile : %s", user));
                    messageStorage.updateUser(user);
                    return;
//                    return Response.ok();
                }

            }
            Message message = MessageHelper.getClientMessage(req.getInputStream());
//            logger.info(String.format("Received new message from user: %s", message));
            messageStorage.addMessage(message);
            return;
//            return Response.ok();
        } catch (ParseException e) {
//            logger.error("Could not parse message.", e);
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPut(req, resp);

        try {
            Message message = MessageHelper.getEditMessage(req.getInputStream());
            //logger.info(String.format("message has been changed));
            if(messageStorage.updateMessage(message)){
                return;
            }
        } catch (ParseException e) {
//            logger.error("Could not parse message.", e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doDelete(req, resp);
        String query = req.getQueryString();
        if (query == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/homepage.html");
            dispatcher.forward(req,resp);
        }
        Map<String, String> map = queryToMap(query);
        String token = map.get(Constants.REQUEST_PARAM_MESSAGE_ID);
        if (StringUtils.isEmpty(token)) {
            return;
//            return Response.badRequest("Token query parameter is required");
        }
        try {
            Message message = MessageHelper.getDelMessage(req.getInputStream());
//            Message message = MessageHelper.getDelMessage(httpExchange.getRequestBody());
            String id = token;
//            if(!messageStorage.removeMessage(id)){
            if(!messageStorage.replaceMessage(id, message)){
                return;
            }
        } catch (InvalidTokenException e) {
        }catch (ParseException e) {
//            return new Response(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }

    }
}
