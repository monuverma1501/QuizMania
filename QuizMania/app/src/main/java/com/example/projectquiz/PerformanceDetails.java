package com.example.projectquiz;

public class PerformanceDetails  {

    int correct;
    int total;
    int wrong;

    public PerformanceDetails(int correct, int total, int wrong) {
        this.correct = correct;
        this.total = total;
        this.wrong = wrong;
    }

    public PerformanceDetails() {

    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }
}
