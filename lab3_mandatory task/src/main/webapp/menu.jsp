<%@ page import="model.Quiz" %>
<%@ page import="model.User" %>
<%@ page import="model.Results" %>
<%--
  Created by IntelliJ IDEA.
  User: pppp
  Date: 2021/12/1
  Time: 18:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Super Quiz!</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            .lab3 {
                background-color: DarkGray;
                color:white;
                margin:20px;
                padding:20px;
            }
            .center{
                background-color: DarkGray;
                color:white;
                width: 800px;
                height: 450px;
                position:absolute;
                top: 50%;
                left: 50%;
                margin: -225px 0 0 -400px;
            }
        </style>
    </head>
    <body>
        <div class="lab3">
            <h1 style="text-align:center">Quiz subjects</h1>
            <p style="text-align:center"> Select a subject to start the challenge </p>
            <table align="center" border="2" frame=void style ="color:white">
                <tr>
            <%

                Quiz[] quizzes = (Quiz[])application.getAttribute("quizzes");
                Results[] results = (Results[])session.getAttribute("results");
                System.out.println(results.length);
                int i =0;
                for (Quiz q : quizzes){
                    out.println("<th width=300 >" + q.getSubject());
                    Results temp =  results[i];
                    if (temp != null && temp.getQuizId() == q.getId()){
                        out.println("：" + temp.getScore() + " score ");
                        i++;
                    }else{
                        out.println(": 0 score ");
                    }
            %>
                    <br>
                    <form action = "/demo_war_exploded/quiz" method="POST">
                        <input type="hidden" name="action" value="startQuiz"/>
                        <input type="hidden" name="quizId" value=<%=q.getId()%> />
                        <input type="hidden" name="quizSubject"value=<%=q.getSubject()%> />
                        <input type="submit" value="start" />
                    </form>
            <%
                    out.print("</tr>");
                }
            %>
                    <th>
                        <br>
                        <form action = "/demo_war_exploded/quiz" method="POST">
                            <input type="hidden" name="action" value="return"/>
                            <input type="submit" value="return" />
                        </form>
                    </th>
                </tr>
            </table>
        </div>
    </body>
</html>
