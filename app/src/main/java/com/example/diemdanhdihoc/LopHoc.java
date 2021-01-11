package com.example.diemdanhdihoc;

public class LopHoc {
    private int id;
    private String maLop;
    private String userName;
    private String tenLop;
    private String hoTen;
    private int soBuoi;

    public LopHoc(int id, String maLop, String userName, String tenLop, String hoTen, int soBuoi) {
        this.id = id;
        this.maLop = maLop;
        this.userName = userName;
        this.tenLop = tenLop;
        this.hoTen = hoTen;
        this.soBuoi = soBuoi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getSoBuoi() {
        return soBuoi;
    }

    public void setSoBuoi(int soBuoi) {
        this.soBuoi = soBuoi;
    }

    @Override
    public String toString() {
        return ""+tenLop+" - "+hoTen;
    }
}
