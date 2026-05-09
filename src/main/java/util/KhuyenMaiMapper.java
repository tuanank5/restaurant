package util;

import dto.KhuyenMai_DTO;
import entity.KhuyenMai;

public class KhuyenMaiMapper {

    public static KhuyenMai toEntity(KhuyenMai_DTO dto) {
        if (dto == null) return null;

        return KhuyenMai.builder()
                .maKM(dto.getMaKM())
                .tenKM(dto.getTenKM())
                .loaiKM(dto.getLoaiKM())
                .ngayBatDau(dto.getNgayBatDau())
                .ngayKetThuc(dto.getNgayKetThuc())
                .phanTramGiamGia(dto.getPhanTramGiamGia())
                .build();
    }

    public static KhuyenMai_DTO toDTO(KhuyenMai e) {
        if (e == null) return null;

        return KhuyenMai_DTO.builder()
                .maKM(e.getMaKM())
                .tenKM(e.getTenKM())
                .loaiKM(e.getLoaiKM())
                .ngayBatDau(e.getNgayBatDau())
                .ngayKetThuc(e.getNgayKetThuc())
                .phanTramGiamGia(e.getPhanTramGiamGia())
                .build();
    }
}