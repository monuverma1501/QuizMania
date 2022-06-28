package com.example.projectquiz;

public class UserData {
    // below are the variables to hold the data from the front end
    String name , email , password , mobile , pass_email;

    public UserData(String name, String email, String password, String mobile , String passEmail) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.pass_email = passEmail;
    }

    // constructor
    public UserData() {

    }

    // getter and setter function
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPass_email() {
        return pass_email;
    }

    public void setPass_email(String pass_email) {
        this.pass_email = pass_email;
    }
}
