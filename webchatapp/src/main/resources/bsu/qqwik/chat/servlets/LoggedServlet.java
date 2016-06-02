package com.anastasia.chat.servlets;

import com.anastasia.chat.encryption.HashCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@WebServlet( value = "/logged")
public class LoggedServlet extends HttpServlet{

    private Map<String, String> users = new HashMap<String, String>();

    {
        users.put("User1", "7110eda4d09e062aa5e4a390b0a572ac0d2c0220");
        users.put("User2", "2abd55e001c524cb2cf6300a89ca6366848a77d5");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = (String)req.getParameter("username");
        String password = null;
        try {
            password = HashCode.encryptPassword((String)req.getParameter("password"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> item: users.entrySet()) {
            if (username.equalsIgnoreCase(item.getKey()) && password.equalsIgnoreCase(item.getValue())) {
                req.getSession().setAttribute("isLogged", "true");
                resp.sendRedirect("/login");
                return;
            }
        }

        req.getRequestDispatcher("start.jsp").forward(req, resp);
    }
}
