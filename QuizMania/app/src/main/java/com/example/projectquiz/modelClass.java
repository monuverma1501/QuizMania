package com.example.projectquiz;

public class modelClass {
    String Question;
    String answer;
    String optA;
    String optB;
    String optC;
    String optD;

    public modelClass(){

    }

    public modelClass(String question, String optA, String optB, String optC, String optD, String answer) {
        this.Question = question;
        this.answer = answer;
        this.optA = optA;
        this.optB = optB;
        this.optC = optC;
        this.optD = optD;

    }


    public  String getQuestion() {
        return Question;
    }

    public  void setQuestion(String question) {
        Question = question;
    }

    public  String getOptA() {
        return optA;
    }

    public void setOptA(String optA) {
        this.optA = optA;
    }

    public String getOptB() {
        return optB;
    }

    public void setOptB(String optB) {
        this.optB = optB;
    }

    public String getOptC() {
        return optC;
    }

    public void setOptC(String optC) {
        this.optC = optC;
    }

    public String getOptD() {
        return optD;
    }

    public void setOptD(String optD) {
        this.optD = optD;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
