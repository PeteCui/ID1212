package unilt;

import model.Question;
import model.Quiz;
import model.Results;
import model.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;

public class DBHandler {
    private Connection conn = null;
    private Statement stmt = null;

    public DBHandler() {
        try {
            //lookup from context
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/mysql");
            //connect to the lab3 database
            conn = ds.getConnection();
            //create statement
            stmt = conn.createStatement();

        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }
    }

    //test username:zhanbo@kth.se password: 123 before encrypt
    public boolean validate(User user) {
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE username= '" + user.getUsername() + "' AND password=MD5('" + user.getPassword() + "')";
            ResultSet rs = stmt.executeQuery(sql);

            rs.next();
            int count = rs.getInt(1);
            if (count == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Quiz[] getQuizzes() {
        Quiz[] quizzes = null;
        try {
            //assign length
            String sql;
            sql = "SELECT COUNT(*) FROM quizzes";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            quizzes = new Quiz[rs.getInt(1)];
            //query question
            sql = "SELECT * FROM quizzes";
            rs = stmt.executeQuery(sql);
            int i = 0;
            while (rs.next()) {
                Quiz q = new Quiz();
                q.setId(rs.getInt("id"));
                q.setSubject(rs.getString("subject"));
                quizzes[i++] = q;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizzes;

    }

    public Results[] getResults(User user) {
        Results[] results = null;
        try {
            //query user id
            String sql = "SELECT id FROM users WHERE username='" + user.getUsername() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int id = rs.getInt("id");

            //query all score form results table
            sql = "SELECT COUNT(*) FROM quizzes";
            rs = stmt.executeQuery(sql);
            rs.next();
            int count = rs.getInt(1);
            results = new Results[count];
            sql = "SELECT * FROM results WHERE user_id=" + id + " ORDER BY quiz_id";
            rs = stmt.executeQuery(sql);
            int i = 1;
            System.out.println("id: " + id);
            System.out.println("len:" + results.length);
            while (rs.next()){
                while(i != rs.getInt("quiz_id")){
                    Results temp = new Results();
                    temp.setQuizId(i);
                    temp.setScore(-1);
                    results[i-1] = temp;
                    i++;
                }
                if (i == rs.getInt("quiz_id")){
                    Results temp = new Results();
                    temp.setQuizId(rs.getInt("quiz_id"));
                    temp.setScore(rs.getInt("score"));
                    results[i-1] = temp;
                    i++;
                }
            }
            System.out.println(results[0].getQuizId() + ":" + results[0].getScore());
            System.out.println(results[1].getQuizId() + ":" + results[0].getScore());

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(results[0].getQuizId()+":"+results[0].getScore());
        System.out.println(results[1].getQuizId()+":"+results[1].getScore());
        return results;
    }

    public Question[] getQuestions(int quizId) {
        Question[] questions = null;
        try {
            //join two tables to query eligible question
            //query length
            String sql = "SELECT COUNT(*) FROM questions JOIN selector ON questions.id = selector.question_id WHERE quiz_id = " + quizId;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            questions = new Question[rs.getInt(1)];
            //query all questions which is eligible
            sql = "SELECT * FROM questions JOIN selector ON questions.id = selector.question_id WHERE quiz_id = " + quizId;
            rs = stmt.executeQuery(sql);

            int i = 0;
            while (rs.next()) {
                Question temp = new Question();
                temp.setId(rs.getInt("id"));
                temp.setText(rs.getString("text"));
                temp.setOptions(rs.getString("options"));
                temp.setAnswer(rs.getString("answer"));
                questions[i++] = temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    public void markQuiz(HttpServletRequest request, HttpSession session) {
        Question[] currentQuestion = (Question[]) session.getAttribute("currentQuestions");
        int myGrade = 0;
        for (Question cq : currentQuestion) {
            String[] myAnswer = new String[cq.getOptions().length];
            for (int i = 0; i < cq.getOptions().length; i++) {
                if (request.getParameter(cq.getId() + ":" + i) != null && request.getParameter(cq.getId() + ":" + i).equals("1")) {
                    myAnswer[i] = "1";
                } else {
                    myAnswer[i] = "0";
                }
//                System.out.println(cq.getId()+":"+i);
//                System.out.println( cq.getOptions()[i] + ":" +myAnswer[i]);
            }
            int flag = 1;
//            System.out.println(myAnswer[0]+":"+myAnswer[1]+":"+myAnswer[2]);
//            System.out.println(cq.getAnswer()[0]+":"+cq.getAnswer()[1]+":"+cq.getAnswer()[2]);
            for (int j = 0; j < cq.getAnswer().length; j++) {
                if (cq.getAnswer()[j].equals(myAnswer[j])) {
                    continue;
                }else if(!cq.getAnswer()[j].equals(myAnswer[j])) {
                    flag = 0;
                }
            }
            if (flag == 1){
                myGrade++;
            }

        }
//        System.out.println(myGrade);
        updateGrade(((User)session.getAttribute("user")).getUsername(), (Integer) session.getAttribute("currentQuizId"), myGrade);
    }

    public void updateGrade(String userName, int currentQuizId, int myGrade){
        try {
            String sql;
            sql = "SELECT id FROM users WHERE username = '" + userName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int userId = rs.getInt(1);
            System.out.println("myId:" + userId);
            System.out.println("myGrade:" + myGrade);

            sql = "SELECT COUNT(*) FROM results WHERE user_id = '" + userId + "' AND quiz_id = '" + currentQuizId + "'";
            rs = stmt.executeQuery(sql);
            rs.next();
            System.out.println("COUNT:" +rs.getInt(1));

            if (rs.getInt(1) == 0){
                sql = "INSERT INTO results(user_id, quiz_id, score) VALUES(" + userId + "," + currentQuizId + "," + myGrade + ")";
                stmt.executeUpdate(sql);
                System.out.println("INSERT");
            }else{
                sql = "UPDATE results SET score=" + myGrade + " WHERE user_id='" + userId + "' AND quiz_id='" + currentQuizId + "'";
                stmt.executeUpdate(sql);
                System.out.println("UPDATE");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

