package com.example.projectquiz;

public class PerformanceUpdate {
    String correct;
    String wrong;
    String total;
    String passEmail;


    public PerformanceUpdate(String correctCount, String wrongCount, String totalCount , String passEmail) {
        this.correct = correctCount;
        this.wrong = wrongCount;
        this.total = totalCount;
        this.passEmail = passEmail;
    }

    public PerformanceUpdate(String correctCount, String wrongCount, String totalCount) {

    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getWrong() {
        return wrong;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    public String getPassEmail() {
        return passEmail;
    }

    public void setPassEmail(String passEmail) {
        this.passEmail = passEmail;
    }
}

