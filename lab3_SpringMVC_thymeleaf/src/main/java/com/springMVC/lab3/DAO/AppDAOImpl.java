package com.springMVC.lab3.DAO;

import com.springMVC.lab3.model.Question;
import com.springMVC.lab3.model.Quiz;
import com.springMVC.lab3.model.Results;
import com.springMVC.lab3.model.User;
import com.springMVC.lab3.rowmapper.QuestionRowMapper;
import com.springMVC.lab3.rowmapper.QuizRowMapper;
import com.springMVC.lab3.rowmapper.ResultRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AppDAOImpl implements AppDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean validate(User user) {
        String sql = "SELECT COUNT(*) FROM users WHERE username= '" + user.getUsername() + "' AND password=MD5('" + user.getPassword() + "')";
        int count = jdbcTemplate.queryForObject(sql,Integer.class);
        if(count == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<Quiz> getQuizzes() {

        String sql;
        sql = "SELECT * FROM quizzes";
        List<Quiz> quizList = jdbcTemplate.query(sql,new QuizRowMapper());

        return quizList;
    }

    @Override
    public List<Results> getResults(User user) {

        //query user id
        String sql;
        sql = "SELECT id FROM users WHERE username='" + user.getUsername() + "'";
        int id = jdbcTemplate.queryForObject(sql, Integer.class);
        //query the count of quiz

        //query the results of this user
        sql = "SELECT * FROM results WHERE user_id=" + id + " ORDER BY quiz_id";
        List<Results> myResultsList = jdbcTemplate.query(sql,new ResultRowMapper());
        List<Results> wholeResultList = new ArrayList<>();
        int i = 1;
        for (Results r : myResultsList){
            while(r.getQuizId() != i){
                Results temp = new Results();
                temp.setQuizId(i);
                temp.setScore(0);
                wholeResultList.add(temp);
                i++;
            }
            if(r.getQuizId() == i){
                wholeResultList.add(r);
                i++;
            }

        }
        return wholeResultList;
    }

    @Override
    public List<Question> getQuestions(Quiz quiz) {
        String sql = "SELECT * FROM questions JOIN selector ON questions.id = selector.question_id WHERE quiz_id = " + quiz.getId();
        List<Question> questionList = jdbcTemplate.query(sql,new QuestionRowMapper());
        return questionList;
    }

    @Override
    public void markQuiz(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Question> currentQuestion = (List<Question>) session.getAttribute("currentQuestions");
        int newScore = 0;
        for (Question cq : currentQuestion){
//            System.out.println(cq.getText());
            String[] myAnswer = new String[cq.getOptions().length];
            for(int i = 1; i <= cq.getOptions().length; i++){
//                System.out.println(cq.getOptions()[i-1]);
//                System.out.println("question id:" + cq.getId());
//                System.out.println("option index:" + i);
                if(request.getParameter(cq.getId() + ":" + i) != null && request.getParameter( cq.getId() + ":" + i).equals("1")){
                    myAnswer[i-1] = "1";
                } else {
                    myAnswer[i-1] = "0";
                }
            }
            int flag = 1;

            for(int j = 0; j < cq.getAnswer().length; j++){
                if(cq.getAnswer()[j].equals(myAnswer[j])){
                    continue;
                }else if(!cq.getAnswer()[j].equals(myAnswer[j])){
                    flag = 0;
                }
            }

            if(flag == 1){
                newScore++;
            }
        }
//        System.out.println(myGrade);
        int currentQuizId = ((Quiz)session.getAttribute("currentQuiz")).getId();

        updateScore((User) session.getAttribute("user"), currentQuizId, newScore);
    }

    @Override
    public void updateScore(User user, int currentQuizId, int newScore) {

        String sql;
        sql = "SELECT id FROM users WHERE username = '" + user.getUsername() + "'";
        int userId = jdbcTemplate.queryForObject(sql,Integer.class);

        sql = "SELECT COUNT(*) FROM results WHERE user_id = '" + userId + "' AND quiz_id = '" + currentQuizId + "'";
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        if(count == 0){
            sql = "INSERT INTO results(user_id, quiz_id, score) VALUES(" + userId + "," + currentQuizId + "," + newScore + ")";
            jdbcTemplate.execute(sql);
            System.out.println("INSERT");

        }else{
            sql = "UPDATE results SET score=" + newScore + " WHERE user_id='" + userId + "' AND quiz_id='" + currentQuizId + "'";
            jdbcTemplate.execute(sql);
            System.out.println("UPDATE");
        }

    }


}
