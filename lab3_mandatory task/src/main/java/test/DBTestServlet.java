/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


//SET GLOBAL time_zone = '+9:00'
//@WebServlet(name = "DBTestServlet", urlPatterns = {"/DBServlet"})
public class DBTestServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse
            response) throws IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Connecting to DB.<br>");
        try{
            //lookup from context
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup("jdbc/mysql");
            //connect to the lab3 database
            Connection conn = ds.getConnection();
            //create statement
            Statement stmt = conn.createStatement();
            //SQL command
            String sql = "SELECT * FROM users";
            //execute SQL command and get the resultset
            ResultSet rs = stmt.executeQuery(sql);
            //traverse the whole resultset
            while(rs.next()){
                out.println(rs.getString("username")+"<br>");
            }

        }
        catch(Exception e){
            out.println("Exception: "+e.getMessage());
        }
        out.println("Finished.");
    }
}