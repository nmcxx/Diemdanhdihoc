package com.example.diemdanhdihoc;

public class DiemDanh {
    private String username;
    private String hoTen;
    private int soBuoi;
    private int diemDanh;

    public DiemDanh(String username, String hoTen, int soBuoi, int diemDanh) {
        this.username = username;
        this.hoTen = hoTen;
        this.soBuoi = soBuoi;
        this.diemDanh = diemDanh;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getDiemDanh() {
        return diemDanh;
    }

    public void setDiemDanh(int diemDanh) {
        this.diemDanh = diemDanh;
    }

    @Override
    public String toString() {
        String loaiDD = (diemDanh==0) ? "Vắng" : "Có mặt";
        return "Buổi thứ "+soBuoi+": "+loaiDD;
    }
}
