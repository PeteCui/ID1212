package controller;

import model.User;
import unilt.DBHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author pppp
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("GET! at LoginController");

        //get current session
        HttpSession session = request.getSession(true);
        //never login, redirect to the index.jsp
        if (session.isNew()){
            response.sendRedirect("/demo_war_exploded/index.jsp");
            System.out.println("You have to log in!");
        }else if (session.getAttribute("validate") == "true"){
            //log in successfully, redirect to the menu.jsp
            //RequestDispatcher rd = request.getRequestDispatcher("/menu");
            //rd.forward(request,response);
            System.out.println("This is an old user!");
            response.sendRedirect("/demo_war_exploded/quiz");
        }else{
            //log in unsuccessfully, redirect to the index.jsp
            System.out.println("You have to login again");
            response.sendRedirect("/demo_war_exploded/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("POST! at LoginController");
        //set response content type
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //get session
        // JSP will implicitly call getSession(true), we have to disable it in the index.jsp
        HttpSession session = request.getSession(true);
        //get context
        ServletContext application = request.getServletContext();
        //get database handler and set user variable
        DBHandler dbh = (DBHandler)application.getAttribute("dbh");
        //create a new user
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("pwd"));
        //validate user
        if(dbh.validate(user)){
            System.out.println("Successful login");
            session.setAttribute("user",user);
            session.setAttribute("validate","true");
            //RequestDispatcher rd = request.getRequestDispatcher("/menu");
            //rd.forward(request,response);
            //sendRedirect make the address more logic at the view of user
            //You can not forward forever!
            response.sendRedirect("/demo_war_exploded/quiz");
        }else{
            System.out.println("Login failed");
            session.setAttribute("user",null);
            session.setAttribute("validate","false");
            request.setAttribute("validate","false");
            RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
            rd.forward(request,response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}