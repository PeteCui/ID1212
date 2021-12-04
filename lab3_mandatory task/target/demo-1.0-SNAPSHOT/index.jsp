<%@page contentType="text/html"  pageEncoding="UTF-8" %>
<!DOCTYPE html>
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
            <h1 style="text-align:center">Super Quiz!</h1>
            <p style="text-align:center">Enter username and password to log in</p>
            <form action = "/demo_war_exploded/login" method="POST" style="text-align:center">
                Username: <input type="text" name="username">
                <br>
                Password: <input type="password" name="pwd">
                <br>
                <input type="submit" value="Login">

                <script>
                    const validate = "<%=request.getAttribute("validate")%>";
                    if(validate == "false"){
                        window.alert("Verification failure");
                    }
                </script>
            </form>
        </div>
    </body>

</html>