package com.springMVC.lab3.DAO;

import com.springMVC.lab3.model.User;
import com.springMVC.lab3.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

//a special annotation used over a Persistence class
//help us in exception handling
//the spring will create the object of this particular class
//This is our DAO layer
@Repository
public class TestDAOImpl implements TestDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> loadTestData() {
        //paste our data from database to this new list
        //write some logic to get data from the database
        String sql = "SELECT * FROM users";
        //jdbcTemplate provide some utility method to talk with database
        List<User> theListOfUser = jdbcTemplate.query(sql, new UserRowMapper());
        return theListOfUser;


    }
}
