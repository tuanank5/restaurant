package handler;

import java.util.List;

import dto.HoaDon_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.HoaDon_Service;
import service.impl.HoaDon_ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class HoaDonHandler implements CommandHandler {

    private final HoaDon_Service hoaDonService;

    public HoaDonHandler() {
        hoaDonService = new HoaDon_ServiceImpl();
    }

    @Override
    public Response handle(Request request) {

        switch (request.getCommandType()) {

            case HOADON_GET_ALL_HOADON_NV_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                List<HoaDon_DTO> list =
                        hoaDonService.getAllHoaDonNVTheoThang(thang, nam, maNV);

                return new Response(true, list, "OK");
            }

            case HOADON_GET_TONG_DOANHTHU_NV_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                Double tongDoanhThu =
                        hoaDonService.getTongDoanhThuNVTheoThang(
                                thang,
                                nam,
                                maNV
                        );

                return new Response(true, tongDoanhThu, "OK");
            }

            case HOADON_GET_DOANHTHU_NV_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                Map<String, Double> result =
                        hoaDonService.getDoanhThuNVTheoNam(
                                nam,
                                maNV
                        );

                return new Response(true, result, "OK");
            }

            case HOADON_GET_HOADON_NV_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                List<HoaDon_DTO> list =
                        hoaDonService.getHoaDonNVTheoNam(
                                nam,
                                maNV
                        );

                return new Response(true, list, "OK");
            }

            case HOADON_GET_TONG_DOANHTHU_NV_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                Double tongDoanhThu =
                        hoaDonService.getTongDoanhThuNVTheoNam(
                                nam,
                                maNV
                        );

                return new Response(true, tongDoanhThu, "OK");
            }

            case HOADON_GET_HOADON_NV_THEO_NGAY_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                String maNV = (String) map.get("maNV");

                List<HoaDon_DTO> list =
                        hoaDonService.getHoaDonNVTheoNgayCuThe(
                                dateStart,
                                dateEnd,
                                maNV
                        );

                return new Response(true, list, "OK");
            }

            case HOADON_GET_TONG_DOANHTHU_NV_THEO_NGAY_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                String maNV = (String) map.get("maNV");

                Double tongDoanhThu =
                        hoaDonService.getTongDoanhThuNVTheoNgayCuThe(
                                dateStart,
                                dateEnd,
                                maNV
                        );

                return new Response(true, tongDoanhThu, "OK");
            }

            case HOADON_GET_TONG_DOANHTHU_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");

                Double tongDoanhThu =
                        hoaDonService.getTongDoanhThuTheoThang(
                                thang,
                                nam
                        );

                return new Response(true, tongDoanhThu, "OK");
            }

            case HOADON_GET_TONG_DOANHTHU_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                Double tongDoanhThu =
                        hoaDonService.getTongDoanhThuTheoNam(nam);

                return new Response(true, tongDoanhThu, "OK");
            }

            case HOADON_GET_ALL_HOADON_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");

                List<HoaDon_DTO> list =
                        hoaDonService.getAllHoaDonTheoThang(
                                thang,
                                nam
                        );

                return new Response(true, list, "OK");
            }

            case HOADON_GET_DOANHTHU_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                Map<String, Double> result =
                        hoaDonService.getDoanhThuTheoNam(nam);

                return new Response(true, result, "OK");
            }

            case HOADON_GET_ALL_HOADON_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                List<HoaDon_DTO> list =
                        hoaDonService.getHoaDonTheoNam(nam);

                return new Response(true, list, "OK");
            }

            case HOADON_GET_HOADON_THEO_NGAY_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                List<HoaDon_DTO> list =
                        hoaDonService.getHoaDonTheoNgayCuThe(dateStart, dateEnd);

                return new Response(true, list, "OK");
            }

            case HOADON_GET_TONG_DOANHTHU_THEO_NGAY_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                Double tongDoanhThu =
                        hoaDonService.getTongDoanhThuTheoNgayCuThe(dateStart, dateEnd);

                return new Response(true, tongDoanhThu, "OK");
            }

            case HOADON_GET_ALL -> {

                List<HoaDon_DTO> ds = hoaDonService.getAllHoaDons();

                return Response.builder()
                        .success(true)
                        .data(ds)
                        .message("Lấy danh sách hóa đơn thành công")
                        .build();
            }

            case HOADON_GET_BY_MADATBAN -> {

                String maDatBan = (String) request.getData();

                HoaDon_DTO hd = hoaDonService.getHoaDonTheoMaDatBan(maDatBan);

                return Response.builder()
                        .success(true)
                        .data(hd)
                        .build();
            }

            case HOADON_ADD -> {

                HoaDon_DTO hd = (HoaDon_DTO) request.getData();

                boolean result = hoaDonService.themHoaDon(hd);

                return Response.builder()
                        .success(result)
                        .message(result
                                ? "Thêm hóa đơn thành công"
                                : "Thêm hóa đơn thất bại")
                        .build();
            }

            case HOADON_UPDATE -> {

                HoaDon_DTO hd = (HoaDon_DTO) request.getData();

                boolean result = hoaDonService.capNhat(hd);

                return Response.builder()
                        .success(result)
                        .message(result ? "Cập nhật hóa đơn thành công" : "Cập nhật hóa đơn thất bại")
                        .build();
            }
        }

        return new Response(false, null, "Invalid command");
    }
}
