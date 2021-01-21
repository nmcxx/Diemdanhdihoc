package com.example.diemdanhdihoc;

public class SinhVien {
    private String maSinhVien;
    private String hoTen;
    private boolean isCheck;

    public SinhVien(String maSinhVien, String hoTen) {
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
    }

    public SinhVien(String maSinhVien, String hoTen, boolean isCheck) {
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.isCheck = isCheck;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return ""+maSinhVien+" - "+hoTen;
    }

    public String test(){
        return ""+maSinhVien+" - "+hoTen+" - "+isCheck;
    }
}
