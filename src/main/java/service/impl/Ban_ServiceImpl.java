package service.impl;

import dao.Ban_DAO;
import dao.impl.Ban_DAOImpl;
import dto.Ban_DTO;
import dto.LoaiBan_DTO;
import entity.Ban;
import entity.LoaiBan;
import service.Ban_Service;
import util.MapperUtil;

import java.util.List;

public class Ban_ServiceImpl implements Ban_Service {

    private Ban_DAO ban_DAO;

    public Ban_ServiceImpl() {
        ban_DAO = new Ban_DAOImpl();
    }

    @Override
    public LoaiBan_DTO timLoaiBanTheoTen(String tenLoaiBan) {
        if (tenLoaiBan == null || tenLoaiBan.trim().isEmpty()) {
            throw new IllegalArgumentException("tenLoaiBan không được rỗng");
        }
        LoaiBan loaiBan = ban_DAO.timLoaiBanTheoTen(tenLoaiBan);
        return MapperUtil.map(loaiBan, LoaiBan_DTO.class);
    }

    @Override
    public String getMaxMaBan() {
        return ban_DAO.getMaxMaBan();
    }

    @Override
    public boolean them(Ban_DTO ban_DTO) {
        if (ban_DTO == null) {
            throw new IllegalArgumentException("ban_DTO không được rỗng");
        }
        if (ban_DTO.getMaBan() == null || ban_DTO.getMaBan().trim().isEmpty()) {
            throw new IllegalArgumentException("ban_DTO.maBan không được rỗng");
        }
        if (ban_DTO.getMaLoaiBan() == null || ban_DTO.getMaLoaiBan().trim().isEmpty()) {
            throw new IllegalArgumentException("ban_DTO.maLoaiBan không được rỗng");
        }
        Ban ban = new Ban();
        ban.setMaBan(ban_DTO.getMaBan());
        ban.setViTri(ban_DTO.getViTri());
        ban.setTrangThai(ban_DTO.getTrangThai() != null ? ban_DTO.getTrangThai() : "Trống");
        LoaiBan lb = new LoaiBan();
        lb.setMaLoaiBan(ban_DTO.getMaLoaiBan());
        ban.setLoaiBan(lb);
        return ban_DAO.them(ban);
    }

    @Override
    public boolean xoaTheoMa(String maBan) {
        if (maBan == null || maBan.trim().isEmpty()) {
            throw new IllegalArgumentException("maBan không được rỗng");
        }
        Ban ban = ban_DAO.timTheoMa(maBan);
        if (ban == null) {
            return false;
        }
        return ban_DAO.xoa(ban);
    }

    @Override
    public String sinhMaBanTiepTheo() {
        String maxMa = ban_DAO.getMaxMaBan();
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

    @Override
    public boolean sua(Ban_DTO ban_DTO) {
        if (ban_DTO == null) {
            throw new IllegalArgumentException("ban_DTO không được rỗng");
        }
        if (ban_DTO.getMaBan() == null || ban_DTO.getMaBan().trim().isEmpty()) {
            throw new IllegalArgumentException("ban_DTO.maBan không được rỗng");
        }
        Ban banCu = ban_DAO.timTheoMa(ban_DTO.getMaBan());
        if (banCu == null) {
            throw new IllegalArgumentException("Không tìm thấy bàn");
        }

        banCu.setTrangThai(ban_DTO.getTrangThai());
        banCu.setViTri(ban_DTO.getViTri());
        return ban_DAO.sua(banCu);
    }

    @Override
    public List<Ban_DTO> getAllBan() {
        List<Ban> ds = ban_DAO.getAllBan();
        return ds.stream().map(ban -> {
            Ban_DTO dto = MapperUtil.map(ban, Ban_DTO.class);
            if (ban.getLoaiBan() != null) {
                dto.setMaLoaiBan(ban.getLoaiBan().getMaLoaiBan());
                dto.setTenLoaiBan(ban.getLoaiBan().getTenLoaiBan());
            }
            return dto;
        }).toList();
    }

}
