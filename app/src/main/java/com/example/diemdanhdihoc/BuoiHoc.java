package com.example.diemdanhdihoc;

public class BuoiHoc {
    private String tenLop;
    private String hoTen;
    private int soBuoi;

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
        return "Buổi "+soBuoi+" - ";
    }
}
