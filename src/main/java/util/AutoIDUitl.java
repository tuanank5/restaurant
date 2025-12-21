package util;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import config.RestaurantApplication;
import dao.Ban_DAO;
import dao.DonDatBan_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import dao.KhuyenMai_DAO;
import dao.MonAn_DAO;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.KhachHang;
import entity.MonAn;

public class AutoIDUitl {
	private static int extractNumber(String id) {
        if (id == null) return 0;

        String number = id.replaceAll("\\D+", "");  // giữ lại số
        if (number.isEmpty()) return 0;

        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
            return 0;
        }
    }
	
	private static String formatID(String prefix, int number, int digits) {
        return prefix + String.format("%0" + digits + "d", number);
    }
	
	public static String sinhMaBan() {
	    Ban_DAO banDAO = RestaurantApplication.getInstance()
	            .getDatabaseContext()
	            .newEntity_DAO(Ban_DAO.class);

	    String maxMa = banDAO.getMaxMaBan();

	    if (maxMa == null) {
	        return "B01";
	    }

	    try {
	        int so = Integer.parseInt(maxMa.replaceAll("\\D+", "")); 

	        return String.format("B%02d", so + 1); 
	    } catch (Exception e) {
	        return "B01";
	    }
	}

	public static String sinhMaMon() {
		List<MonAn> listMon = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(MonAn_DAO.class)
                .getDanhSach(MonAn.class, null);

        int max = 0;
        for (MonAn m : listMon) {
            try {
                int so = Integer.parseInt(m.getMaMon().substring(1));
                if (so > max) max = so;
            } catch (Exception e) {}
        }

        return String.format("M%03d", max + 1);
	}
	
	public static String phatSinhMaKH(){
    	KhachHang_DAO dao = RestaurantApplication.getInstance()
    			.getDatabaseContext()
    			.newEntity_DAO(KhachHang_DAO.class);
    	//Lấy danh sách khách hàng hiện có
    	List<KhachHang> dsKH = dao.getDanhSach(KhachHang.class, new HashMap<>());
    	//Tìm mã KH > nhất hiện tại
    	int max = 0;
    	for(KhachHang kh : dsKH) {
    		String ma = kh.getMaKH();
    		if(ma != null && ma.startsWith("KH")) {
    			try {
    				int num = Integer.parseInt(ma.substring(2));
    				if(num > max) max = num;
    			}catch (NumberFormatException e) {
					// TODO: handle exception
				}
    		}
    	}
    	return String.format("KH%04d", max + 1);
    }

	public static String sinhMaKhuyenMai() {
		// Lấy mã KM lớn nhất trong DB
	    KhuyenMai_DAO kmDAO = RestaurantApplication.getInstance()
	            .getDatabaseContext()
	            .newEntity_DAO(KhuyenMai_DAO.class);

	    String max = kmDAO.getMaxMaKM(); // ví dụ: "KM012"

	    if (max == null) {
	        return "KM001";
	    }

	    try {
	        int so = Integer.parseInt(max.substring(2)); // "KM012" -> 12
	        return "KM" + String.format("%03d", so + 1);
	    } catch (Exception e) {
	        // fallback nếu DB bị sai format
	        return "KM001";
	    }
	}

	public static String sinhMaDonDatBan() {
		DonDatBan_DAO ddbDAO = RestaurantApplication.getInstance()
	            .getDatabaseContext()
	            .newEntity_DAO(DonDatBan_DAO.class);

	    String max = ddbDAO.getMaxMaDatBan(); // ví dụ: DDB012

	    if (max == null) {
	        return "DDB001";
	    }

	    try {
	        int so = Integer.parseInt(max.substring(3)); // "DDB012" -> 12
	        return "DDB" + String.format("%03d", so + 1);
	    } catch (Exception e) {
	        return "DDB001";
	    }
	}	

	public static String sinhMaHoaDon() {
        HoaDon_DAO hdDAO = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(HoaDon_DAO.class);

        String max = hdDAO.getMaxMaHoaDon();
        int next = extractNumber(max) + 1;
        return formatID("HD", next, 4);
    }
	
	public static String sinhMaNhanVien() {
	    NhanVien_DAO nvDAO = RestaurantApplication.getInstance()
	            .getDatabaseContext()
	            .newEntity_DAO(NhanVien_DAO.class);
	    String max = nvDAO.getMaxMaNV(); 
	    if (max == null) {
	        return "NV001";
	    }
	    try {
	        int so = Integer.parseInt(max.substring(2)); 
	        return "NV" + String.format("%03d", so + 1);
	    } catch (Exception e) {
	        return "NV001";
	    }
	}
	
	public static String sinhMaTaiKhoan() {
	    TaiKhoan_DAO tkDAO = RestaurantApplication.getInstance()
	            .getDatabaseContext()
	            .newEntity_DAO(TaiKhoan_DAO.class);
	    String max = tkDAO.getMaxMaTK(); // ví dụ: TK021
	    if (max == null) {
	        return "TK001";
	    }
	    try {
	        int so = Integer.parseInt(max.substring(2)); // "TK021" -> 21
	        return "TK" + String.format("%03d", so + 1);
	    } catch (Exception e) {
	        return "TK001";
	    }
	}
	
	public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

}
