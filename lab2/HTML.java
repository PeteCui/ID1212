package lab2;

public class HTML {

    public static final String HEAD_AND_STYLE = "<!DOCTYPE html>" +
            "<html lang=\"en\">" +
            "<!--head-->" +
            "<head>" +
            "    <meta charset=\"UTF-8\">" +
            "    <title>Number Guess Game</title>" +
            "" +
            "    <style>" +
            "    .lab2 {" +
            "        background-color: DarkGray;" +
            "        color:white;" +
            "        margin:20px;" +
            "        padding:20px;" +
            "    }" +
            "    </style>" +
            "</head>";

    public static final String HIGHER1 = "<body>\n" +
            "<div class=\"lab2\">\n" +
            "    <h1 style=\"text-align:center\">Wrong! Guess higher</h1>\n" +
            "    <p style=\"text-align:center\">You have made ";

    public static final String HIGHER2 = " guess(es)</p>\n" +
            "    <form method=\"POST\" style=\"text-align:center\">\n" +
            "        My guess: <input type=\"text\" name=\"number\">\n" +
            "    </form>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    public static final String LOWER1 = "<body>\n" +
            "<div class=\"lab2\">\n" +
            "    <h1 style=\"text-align:center\">Wrong! Guess lower</h1>\n" +
            "    <p style=\"text-align:center\">You have made ";

    public static final String LOWER2 = " guess(es)</p>\n" +
            "    <form method=\"POST\" style=\"text-align:center\">\n" +
            "        My guess: <input type=\"text\" name=\"number\">\n" +
            "    </form>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    public static final String YES1 = "<body>\n" +
            "<div class=\"lab2\">\n" +
            "    <h1 style=\"text-align:center\">Success!</h1>\n" +
            "    <p style=\"text-align:center\">You have made ";;

    public static final String YES2 = " guess(es)</p>\n" +
            "    <form method=\"GET\" style=\"text-align:center\">\n" +
            "        <input type=\"submit\" value=\"New Game\">\n" +

            "    </form>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
}
