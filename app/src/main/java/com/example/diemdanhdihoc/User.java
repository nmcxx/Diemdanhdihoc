package com.example.diemdanhdihoc;

public class User {
    private String userName;
    private String passWord;
    private String hoTen;
    private int userType;

    public User(String userName, String passWord, String hoTen, int userType) {
        this.userName = userName;
        this.passWord = passWord;
        this.hoTen = hoTen;
        this.userType = userType;
    }

    public User() {

    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
