package handler;

import dto.HoaDon_DTO;
import network.common.CommandHandler;
import network.common.Request;
import network.common.Response;
import service.HoaDon_Service;
import service.impl.HoaDon_ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class HoaDonHandler implements CommandHandler {

    private final HoaDon_Service service = new HoaDon_ServiceImpl();

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
                        service.getAllHoaDonNVTheoThang(thang, nam, maNV);

                return new Response(true, list, "OK");
            }

            case HOADON_GET_TONG_DOANHTHU_NV_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                Double tongDoanhThu =
                        service.getTongDoanhThuNVTheoThang(
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
                        service.getDoanhThuNVTheoNam(
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
                        service.getHoaDonNVTheoNam(
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
                        service.getTongDoanhThuNVTheoNam(
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
                        service.getHoaDonNVTheoNgayCuThe(
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
                        service.getTongDoanhThuNVTheoNgayCuThe(
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
                        service.getTongDoanhThuTheoThang(
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
                        service.getTongDoanhThuTheoNam(nam);

                return new Response(true, tongDoanhThu, "OK");
            }

            case HOADON_GET_ALL_HOADON_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");

                List<HoaDon_DTO> list =
                        service.getAllHoaDonTheoThang(
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
                        service.getDoanhThuTheoNam(nam);

                return new Response(true, result, "OK");
            }

            case HOADON_GET_ALL_HOADON_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                List<HoaDon_DTO> list =
                        service.getHoaDonTheoNam(nam);

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
                        service.getHoaDonTheoNgayCuThe(dateStart, dateEnd);

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
                        service.getTongDoanhThuTheoNgayCuThe(dateStart, dateEnd);

                return new Response(true, tongDoanhThu, "OK");
            }
        }

        return new Response(false, null, "Invalid command");
    }
}
