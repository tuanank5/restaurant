package dao;

import entity.KhuyenMai;

public interface KhuyenMai_DAO extends Entity_DAO<KhuyenMai> {
    String getMaxMaKM();
    KhuyenMai timTheoMa(String maKM);
    boolean sua(KhuyenMai km);
    boolean xoa(String maKM);
}