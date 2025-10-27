package controller;

import entity.TaiKhoan;

public class Dashboard_Controller {
	private TaiKhoan taiKhoan;

	public void setThongTin(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        String hoTen = taiKhoan.getNhanVien().getTenNV();
//        txtThongTinNhanVien.setText(hoTen);
    }
}
