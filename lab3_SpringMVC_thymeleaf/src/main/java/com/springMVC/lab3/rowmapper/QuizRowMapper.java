package com.springMVC.lab3.rowmapper;

import com.springMVC.lab3.model.Quiz;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizRowMapper implements RowMapper<Quiz> {

    @Override
    public Quiz mapRow(ResultSet resultSet, int i) throws SQLException{

        Quiz quiz = new Quiz();

        quiz.setId(resultSet.getInt("id"));
        quiz.setSubject(resultSet.getString("subject"));

        return quiz;
    }
}
