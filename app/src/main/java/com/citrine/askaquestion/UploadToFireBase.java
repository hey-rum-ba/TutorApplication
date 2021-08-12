package com.citrine.askaquestion;

public class UploadToFireBase {
    public String username;
    public String email;
    public String passsword;
    public boolean teacher;
    public UploadToFireBase(){ }
    public UploadToFireBase(String username, String email, String passsword, boolean teacher) {
        this.username = username;
        this.email = email;
        this.passsword = passsword;
        this.teacher = teacher;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }
}

