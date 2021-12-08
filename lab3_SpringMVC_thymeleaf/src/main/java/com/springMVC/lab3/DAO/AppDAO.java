package com.springMVC.lab3.DAO;

import com.springMVC.lab3.model.Question;
import com.springMVC.lab3.model.Quiz;
import com.springMVC.lab3.model.Results;
import com.springMVC.lab3.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AppDAO {
    public boolean validate(User user);
    public List<Quiz> getQuizzes();
    public List<Results> getResults(User user);
    public List<Question> getQuestions(Quiz quiz);
    public void markQuiz(HttpServletRequest request);
    public void updateScore(User user, int currentQuizId, int newScore);
}
