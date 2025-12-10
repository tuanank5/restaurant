package dao;

import java.util.List;
import entity.ChiTietHoaDon;

public interface ChiTietHoaDon_DAO extends Entity_DAO<ChiTietHoaDon> {
    
    boolean themChiTiet(ChiTietHoaDon cthd);

    List<ChiTietHoaDon> getChiTietTheoMaHoaDon(String maHD);

}
