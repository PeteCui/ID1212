<%@ page import="model.Question" %><%--
  Created by IntelliJ IDEA.
  User: pppp
  Date: 2021/12/2
  Time: 19:56
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
        </style>
    </head>

    <body>
        <div class="lab3">
            <h1 style="text-align:center"><%=session.getAttribute("currentSubject")%></h1>
                <form action = "/demo_war_exploded/quiz" method="POST">
                <%
                    Question[] questions = (Question[]) session.getAttribute("currentQuestions");
                    int i = 0;
                    for (Question q : questions){
                        out.println(i+1 + " : "+ q.getText() + "<br>");
                            int j = 0;
                            for (String option : q.getOptions()){

                        %>
                                <input type="checkbox" name="<%=q.getId() + ":"+ j %>" value= "1" ><%=option%><br>
                        <%
                                j++;
                            }
                            out.println("<br>");
                        i++;
                    }
                %>
                    <input type="hidden" name="action" value="submitQuiz"/>
                    <input type="submit" value="submit" />

                </form>
        </div>
    </body>
<body>

</body>
</html>
