package com.springMVC.lab3.controller;

import com.springMVC.lab3.DAO.AppDAO;
import com.springMVC.lab3.model.Quiz;
import com.springMVC.lab3.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@SessionAttributes({"user", "validate", "quizzes", "results", "currentQuestions","currentQuiz"})
public class MyAppController {

    @Autowired
    private AppDAO appDAO;

    @GetMapping("/index")
    public String viewIndex(){
        System.out.println("Now at index");
        return "index";
    }

    @PostMapping("/login")
    public String validateUser(User user, Model model){
        System.out.println("Now at login");
        if (appDAO.validate(user)){
            System.out.println("good user");
            //store user information
            model.addAttribute("user",user);
            model.addAttribute("validate","true");
            model.addAttribute("quizzes",appDAO.getQuizzes());
            model.addAttribute("results",appDAO.getResults(user));
            return "menu";
        }
        System.out.println("bad user");
        model.addAttribute("validate","false");
        return "index";
    }

    @PostMapping("/startQuiz")
    public String startQuiz(Quiz quiz, Model model){
        model.addAttribute("currentQuiz",quiz);
        model.addAttribute("currentQuestions",appDAO.getQuestions(quiz));

        return "quiz";
    }

    @PostMapping("/submitQuiz")
    public String submitQuiz(HttpServletRequest request, Model model){
        System.out.println("Now at submitQuiz");
        appDAO.markQuiz(request);
        //update
        model.addAttribute("quizzes",appDAO.getQuizzes());
        model.addAttribute("results",appDAO.getResults((User) request.getSession().getAttribute("user")));
        return "menu";
    }

    @GetMapping({"/startQuiz", "/submitQuiz", "/login"})
    public String backApplication(Model model, HttpSession session){
        System.out.println("Now back to application");
        User user = (User) session.getAttribute("user");
        if (appDAO.validate(user)){
            System.out.println("good user");
            //update information
            model.addAttribute("quizzes",appDAO.getQuizzes());
            model.addAttribute("results",appDAO.getResults(user));
            return "menu";
        }
        System.out.println("bad user");
        model.addAttribute("validate","false");
        return "index";
    }

}
