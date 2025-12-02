package util;

import java.util.List;

import config.RestaurantApplication;
import dao.Ban_DAO;
import entity.Ban;

public class MaTuDongUtil {
	
	public static String sinhMaBan() {
        // Lấy danh sách bàn hiện có
        List<Ban> listBan = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .getDanhSach(Ban.class, null);

        int max = 0;
        for (Ban b : listBan) {
            try {
                // Giả sử mã bàn có dạng "B01", "B02", ...
                int so = Integer.parseInt(b.getMaBan().substring(1));
                if (so > max) max = so;
            } catch (Exception e) {
                // bỏ qua nếu format khác
            }
        }
        return String.format("B%02d", max + 1); // Tạo mã mới
    }
}
