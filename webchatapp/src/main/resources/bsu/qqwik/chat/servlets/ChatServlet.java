package bsu.qqwik.chat.servlets;

import bsu.qqwik.chat.InMemoryMessageStorage;
import bsu.qqwik.chat.MessageHelper;
import bsu.qqwik.chat.MessageStorage;
import bsu.qqwik.chat.Portion;
import bsu.qqwik.chat.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static bsu.qqwik.chat.MessageHelper.inputStreamToString;
import static bsu.qqwik.chat.MessageHelper.stringToJsonObject;



@WebServlet(value = "/chat")
public class ChatServlet extends HttpServlet {
    MessageStorage messageStorage;

    @Override
    public void init() throws ServletException {
        super.init();
        messageStorage = new InMemoryMessageStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        try {
            resp.setCharacterEncoding("UTF-8");
            int index = MessageHelper.parseToken(token);
            Portion portion = new Portion(index);
            List<Message> messages = messageStorage.getPortion(portion);
            String responseBody = MessageHelper.buildServerResponseBody(messages, messageStorage.size());
            PrintWriter out = resp.getWriter();
            out.print(responseBody);
            out.flush();
        } catch (Exception e) {
            resp.sendError(400, "Bad Request");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.addMessage(message);
        } catch (ParseException e) {

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JSONObject jsonObject = stringToJsonObject(inputStreamToString(req.getInputStream()));
            String id = ((String) jsonObject.get(Constants.Message.FIELD_ID));
            String text = ((String) jsonObject.get(Constants.Message.FIELD_TEXT));
            Message message = new Message();
            message.setId(id);
            message.setText(text);
            messageStorage.updateMessage(message);
        } catch (ParseException e) {
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JSONObject jsonObject = stringToJsonObject(inputStreamToString(req.getInputStream()));
            String id = ((String) jsonObject.get(Constants.Message.FIELD_ID));
            messageStorage.removeMessage(id);
        } catch (ParseException e) {
        }
    }
}
