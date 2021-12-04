package model;

public class Question {

    private int id;
    private String text;
    private String[] options;
    private String[] answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options.split("/");
    }

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer.split("/");
    }
}
