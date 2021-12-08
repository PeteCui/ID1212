package com.springMVC.lab3.rowmapper;

import com.springMVC.lab3.model.Question;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionRowMapper implements RowMapper<Question> {

    @Override
    public Question mapRow(ResultSet resultSet, int i) throws SQLException {
        Question question = new Question();

        question.setId(resultSet.getInt("id"));
        question.setText(resultSet.getString("text"));
        question.setOptions(resultSet.getString("options"));
        question.setAnswer(resultSet.getString("answer"));

        return question;
    }
}
