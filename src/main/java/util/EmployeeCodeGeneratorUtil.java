package util;

import java.time.LocalDate;

import config.RestaurantApplication;
import dao.NhanVien_DAO;
import entity.NhanVien;

public class EmployeeCodeGeneratorUtil {
	private static long currentEmployeeCount = RestaurantApplication
            .getInstance()
            .getDatabaseContext()
            .newEntity_DAO(NhanVien_DAO.class)
            .count("NhanVien.count", NhanVien.class);;
            
    public static String generateEmployeeCode() {
        // Lấy năm hiện tại
        int year = LocalDate.now().getYear();
        String yearSuffix = String.format("%02d", year % 100); // Hai ký tự cuối của năm

        currentEmployeeCount++;
        // Đảm bảo số lượng nhân viên không vượt quá 9999
        if (currentEmployeeCount > 9999) {
            throw new IllegalStateException("Số lượng nhân viên đã vượt quá giới hạn");
        }

        // Định dạng số thứ tự thành 4 chữ số
        String employeeNumber = String.format("%04d", currentEmployeeCount);

        // Tạo mã nhân viên
        return "NV" + yearSuffix + employeeNumber;
    }
}
