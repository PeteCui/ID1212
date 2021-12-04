package controller;

import model.Question;
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

@WebServlet(name = "MenuController", urlPatterns = {"/quiz"})
public class QuizController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("GET! at QuizController");

        //get context
        ServletContext application = request.getServletContext();
        //get current session
        HttpSession session = request.getSession(true);
        //never login, redirect to the index.jsp
        if (session.isNew()){
            response.sendRedirect("/demo_war_exploded/index.jsp");
        }else if (session.getAttribute("validate") == "true"){
            //log in successfully, redirect to the menu.jsp
            //get database handler and set user variable
            DBHandler dbh = (DBHandler)application.getAttribute("dbh");
            //get quizzes from the database
            application.setAttribute("quizzes", dbh.getQuizzes());
            //get the results of current user
            session.setAttribute("results", dbh.getResults((User) session.getAttribute("user")));
//            RequestDispatcher rd = request.getRequestDispatcher("/menu.jsp");
//            rd.forward(request, response);
            response.sendRedirect("/demo_war_exploded/menu.jsp");
        }else{
            //log in unsuccessfully, redirect to the index.jsp
            response.sendRedirect("/demo_war_exploded/index.jsp");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("POST! at QuizController");
        //set response content type
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //get context
        ServletContext application = request.getServletContext();
        //get current session
        HttpSession session = request.getSession(true);
        //never login, redirect to the index.jsp
        if (session.isNew()){
            response.sendRedirect("/demo_war_exploded/index.jsp");
        }else if (session.getAttribute("validate") == "true") {
            //get dbh
            DBHandler dbh = (DBHandler) application.getAttribute("dbh");

            String action = request.getParameter("action");
            if(action.equals("startQuiz")){
                System.out.println("POST! at startQuiz");
                //get quizId
                int quizId = Integer.parseInt(request.getParameter("quizId"));
                //get quizSubject
                String quizSubject = request.getParameter("quizSubject");
                Question[] questions = dbh.getQuestions(quizId);
                //update session
                session.setAttribute("currentQuizId", quizId);
                session.setAttribute("currentSubject", quizSubject);
                session.setAttribute("currentQuestions", questions);
                response.sendRedirect("/demo_war_exploded/quiz.jsp");
            }else if(action.equals("submitQuiz")){
                System.out.println("POST! at submitQuiz");
                dbh.markQuiz(request, session);
                response.sendRedirect("/demo_war_exploded/quiz");
            }else if(action.equals("return")){
                System.out.println("POST! at return");
                response.sendRedirect("/demo_war_exploded/index.jsp");
            }

        }else{
            //log in unsuccessfully, redirect to the index.jsp
            response.sendRedirect("/demo_war_exploded/index.jsp");
        }

    }
}
