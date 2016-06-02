package chat;

import chat.common.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@WebServlet(value = "/login", initParams = {
        @WebInitParam(name = "cookie-live-time", value = "3000")})
public class MainServlet extends HttpServlet{
    public static final String DEFAULT_USERS_STORAGE = "users.txt";
    public static final String COOKIE_USER_ID = "uid";
    public static final String PARAM_UID = COOKIE_USER_ID;

    private int coockieLifeTime = -1;

    public boolean inside(List<User> list, User u){
        for(User user:list){
            if(user.getName().equals(u.getName())){
                return true;
            }
        }
        return false;
    }

    public String getId(List<User> list, User u){
        for(User user: list){
            if(user.equals(u)){
                return user.getId();
            }
        }
        return "";
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        coockieLifeTime = Integer.parseInt(config.getInitParameter("cookie-live-time"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("user-login");
        String password = req.getParameter("user-password");
        if(!login.equals("")&&!password.equals("")){
            String enter = req.getParameter("enter");
            String register = req.getParameter("register");
            List<User> users = new ArrayList<User>();
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Collection<User>>(){}.getType();

            User testing_user = null;
            try {
                testing_user = new User(login, Hashcode.encryptPassword(password), UUID.randomUUID().toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if(register != null){
                try {
                    Scanner sc = new Scanner(new File(DEFAULT_USERS_STORAGE));
                    if(sc.hasNext()) {
                        StringBuilder sb = new StringBuilder(sc.nextLine());
                        while (sc.hasNextLine()) {
                            sb.append(sc.nextLine());
                        }
                        users = gson.fromJson(sb.toString(),collectionType);
                    }
                    if(inside(users,testing_user)){
                        req.setAttribute("result", "user with such name exist");
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
                        dispatcher.forward(req,resp);
                    }else{
                        users.add(testing_user);
                        FileWriter fw = new FileWriter(DEFAULT_USERS_STORAGE);
                        fw.write(gson.toJson(users));
                        fw.close();

//                        UUID uuid = UUID.randomUUID();
                        Cookie userIdCookie = new Cookie(COOKIE_USER_ID, testing_user.getId());
                        userIdCookie.setMaxAge(coockieLifeTime);
                        resp.addCookie(userIdCookie);

                        Cookie userNameCookie = new Cookie("name", testing_user.getName());
                        userIdCookie.setMaxAge(coockieLifeTime);
                        resp.addCookie(userNameCookie);

//                        resp.sendRedirect("/userinfo.jsp");

                        resp.sendRedirect(req.getContextPath() + "/homepage.html");
                    }
                } catch (FileNotFoundException e) {
                    users.add(testing_user);
                    FileWriter fw = new FileWriter(DEFAULT_USERS_STORAGE);
                    fw.write(gson.toJson(users));
                    fw.close();

                    Cookie userIdCookie = new Cookie(COOKIE_USER_ID, testing_user.getId());
                    userIdCookie.setMaxAge(coockieLifeTime);
                    resp.addCookie(userIdCookie);

                    Cookie userNameCookie = new Cookie("name", testing_user.getName());
                    userIdCookie.setMaxAge(coockieLifeTime);
                    resp.addCookie(userNameCookie);

                    resp.sendRedirect(req.getContextPath() + "/homepage.html");
                    return;
                }
            }else if(enter != null){
                try {
                    Scanner sc = new Scanner(new File(DEFAULT_USERS_STORAGE));
                    if(sc.hasNext()) {
                        StringBuilder sb = new StringBuilder(sc.nextLine());
                        while (sc.hasNextLine()) {
                            sb.append(sc.nextLine());
                        }
                        users = gson.fromJson(sb.toString(),collectionType);
                    }
                    String id = getId(users, testing_user);
                    if(!id.equals("")){//users.contains(testing_user)
                        Cookie userIdCookie = new Cookie(COOKIE_USER_ID, id);//testing_user.getId()
                        userIdCookie.setMaxAge(coockieLifeTime);
                        resp.addCookie(userIdCookie);

                        Cookie userNameCookie = new Cookie("name", testing_user.getName());
                        userIdCookie.setMaxAge(coockieLifeTime);
                        resp.addCookie(userNameCookie);

                        resp.sendRedirect(req.getContextPath() + "/homepage.html");
                    }else{
//                    resp.sendRedirect(req.getContextPath() + "/login.html");
                        req.setAttribute("result", "user don't exist");
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
                        dispatcher.forward(req,resp);
                    }
                } catch (FileNotFoundException e) {
//                resp.sendRedirect(req.getContextPath() + "/login.html");
                    req.setAttribute("result", "user don't exist");
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
                    dispatcher.forward(req,resp);
                }
            }
        }else{
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
            dispatcher.forward(req,resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
}
