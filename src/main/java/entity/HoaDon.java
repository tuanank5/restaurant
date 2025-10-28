package entity;

import java.sql.Date;

public class HoaDon {
    private String maHoaDon;
    private Date ngayLap;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private KhuyenMai khuyenMai;
    private String trangThai;
    private boolean kieuThanhToan; // true = tiền mặt, false = chuyển khoản (ví dụ)

    public HoaDon() {}

    public HoaDon(String maHoaDon, Date ngayLap, KhachHang khachHang, NhanVien nhanVien, 
                  KhuyenMai khuyenMai, String trangThai, boolean kieuThanhToan) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.khuyenMai = khuyenMai;
        this.trangThai = trangThai;
        this.kieuThanhToan = kieuThanhToan;
    }

    // Getters và Setters
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public boolean isKieuThanhToan() {
        return kieuThanhToan;
    }

    public void setKieuThanhToan(boolean kieuThanhToan) {
        this.kieuThanhToan = kieuThanhToan;
    }

    @Override
    public String toString() {
        return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap + 
               ", khachHang=" + (khachHang != null ? khachHang.getMaKH() : "null") +
               ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNV() : "null") +
               ", khuyenMai=" + (khuyenMai != null ? khuyenMai.getMaKM() : "null") +
               ", trangThai=" + trangThai + ", kieuThanhToan=" + kieuThanhToan + "]";
    }
}
