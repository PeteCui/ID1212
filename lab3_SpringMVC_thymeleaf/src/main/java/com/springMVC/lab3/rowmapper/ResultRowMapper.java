package com.springMVC.lab3.rowmapper;

import com.springMVC.lab3.model.Results;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultRowMapper implements RowMapper<Results> {

    @Override
    public Results mapRow(ResultSet resultSet, int i) throws SQLException {

        Results results = new Results();
        results.setQuizId(resultSet.getInt("quiz_id"));
        results.setScore(resultSet.getInt("score"));

        return results;
    }
}
