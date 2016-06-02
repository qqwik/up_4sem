package bsu.qqwik.chat.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(value = "/vars")
public class VarsServlet extends HttpServlet{


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String[] vars = { "JAVA_HOME", "M2_HOME"};
        for (String var : vars) {
            resp.getOutputStream().println(String.format("%s=%s", var, System.getenv(var)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("vars.jsp").forward(req, resp);
    }
}
