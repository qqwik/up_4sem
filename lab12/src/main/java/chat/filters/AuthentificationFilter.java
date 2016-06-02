package chat.filters;

import chat.MainServlet;
import chat.common.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

@WebFilter(value = "/homepage.html")
public class AuthentificationFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uidParam = request.getParameter(MainServlet.PARAM_UID);
        if (uidParam == null && request instanceof HttpServletRequest) {
            Cookie[] cookies = ((HttpServletRequest) request).getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(MainServlet.COOKIE_USER_ID)) {
                    uidParam = cookie.getValue();
                }
            }
        }
        boolean auhenticated = checkAuthenticated(uidParam);
        if (auhenticated) {
            chain.doFilter(request, response);
        }else if(response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).sendRedirect("/login.jsp");
        } else {
            response.getOutputStream().println("403, Forbidden");
        }
    }
    private boolean checkAuthenticated(String id){
        List<User> users = new ArrayList<User>();
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<User>>(){}.getType();

        try {
            Scanner sc = new Scanner(new File(MainServlet.DEFAULT_USERS_STORAGE));
            if(sc.hasNext()) {
                StringBuilder sb = new StringBuilder(sc.nextLine());
                while (sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }
                users = gson.fromJson(sb.toString(),collectionType);
            }
            for(User u:users){
                if(u.getId().equals(id)){
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void destroy() {

    }
}
