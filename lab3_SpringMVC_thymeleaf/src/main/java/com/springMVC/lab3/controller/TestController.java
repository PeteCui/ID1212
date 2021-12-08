package com.springMVC.lab3.controller;

import com.springMVC.lab3.DAO.TestDAO;
import com.springMVC.lab3.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class TestController {

    @Autowired
    private TestDAO testDAO;

    @GetMapping("/hello")
    public ModelAndView viewTest(){
        System.out.println("Now at test");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("test");
        return mv;
    }

    @GetMapping("/dbTest")
    public String dbTest(Model m){

        //call the dao method to get the data
        List<User> testData = testDAO.loadTestData();

        for(User user: testData){
            System.out.println(user.toString());
        }
        m.addAttribute("testData", testData.get(0));

        return "testDB";
    }

}
