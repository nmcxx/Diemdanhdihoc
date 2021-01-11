package com.example.diemdanhdihoc;

public class ThongKeHocSinh {
    private String maSV;
    private String hoTen;
    private int soBuoiVang;

    public ThongKeHocSinh(String maSV, String hoTen, int soBuoiVang) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.soBuoiVang = soBuoiVang;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getSoBuoiVang() {
        return soBuoiVang;
    }

    public void setSoBuoiVang(int soBuoiVang) {
        this.soBuoiVang = soBuoiVang;
    }
}
