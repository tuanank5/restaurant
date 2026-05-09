package handler;

import java.util.List;

import dto.Ban_DTO;
import dto.DonDatBan_DTO;
import dto.LoaiBan_DTO;
import network.common.CommandHandler;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import service.DonDatBan_Service;
import service.impl.DonDatBan_ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DonDatBanHandler implements CommandHandler {

    private final DonDatBan_Service donDatBanService;

    public DonDatBanHandler() {
        donDatBanService = new DonDatBan_ServiceImpl();
    }

    @Override
    public Response handle(Request request) {

        switch (request.getCommandType()) {

            case DONDATBAN_GET_ALL_DONDATBAN_NV_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                List<DonDatBan_DTO> list =
                        donDatBanService.getAllDonDatBanNVTheoThang(
                                thang,
                                nam,
                                maNV
                        );

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_KHACHHANG_THEO_THANG_NV_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                List<String> list =
                        donDatBanService.getKhachHangTheoThangNVCuThe(
                                thang,
                                nam,
                                maNV
                        );

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_ALL_DONDATBAN_THEO_NAM_NV_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                List<DonDatBan_DTO> list =
                        donDatBanService.getAllDonDatBanTheoNamNVCuThe(
                                nam,
                                maNV
                        );

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_KHACHHANG_THEO_NAM_NV_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                List<String> list =
                        donDatBanService.getKhachHangTheoNamNVCuThe(
                                nam,
                                maNV
                        );

                return new Response(true, list, "OK");
            }

            case DONDATBAN_COUNT_DONDATBAN_THEO_NAM_NV_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");
                String maNV = (String) map.get("maNV");

                Map<String, Integer> result =
                        donDatBanService.countDonDatBanTheoNamNVCuThe(
                                nam,
                                maNV
                        );

                return new Response(true, result, "OK");
            }

            case DONDATBAN_GET_ALL_DONDATBAN_THEO_NGAY_NV_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                String maNV = (String) map.get("maNV");

                List<DonDatBan_DTO> list =
                        donDatBanService.getAllDonDatBanTheoNgayNVCuThe(
                                dateStart,
                                dateEnd,
                                maNV
                        );

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_KHACHHANG_THEO_NGAY_NV_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                String maNV = (String) map.get("maNV");

                List<String> list =
                        donDatBanService.getKhachHangTheoNgayNVCuThe(
                                dateStart,
                                dateEnd,
                                maNV
                        );

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_ALL_DONDATBAN_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");

                List<DonDatBan_DTO> list =
                        donDatBanService.getAllDonDatBanTheoThang(
                                thang,
                                nam
                        );

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_ALL_DONDATBAN_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                List<DonDatBan_DTO> list =
                        donDatBanService.getAllDonDatBanTheoNam(nam);

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_KHACHHANG_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");

                List<String> list =
                        donDatBanService.getKhachHangTheoThang(
                                thang,
                                nam
                        );

                return new Response(true, list, "OK");
            }

            case DONDATBAN_COUNT_LOAIBAN_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");

                Map<LoaiBan_DTO, Integer> result =
                        donDatBanService.countLoaiBanTheoThang(thang, nam);

                return new Response(true, result, "OK");
            }

            case DONDATBAN_GET_KHACHHANG_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                List<String> list =
                        donDatBanService.getKhachHangTheoNam(nam);

                return new Response(true, list, "OK");
            }

            case DONDATBAN_COUNT_LOAIBAN_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                Map<LoaiBan_DTO, Integer> result =
                        donDatBanService.countLoaiBanTheoNam(nam);

                return new Response(true, result, "OK");
            }

            case DONDATBAN_COUNT_DONDATBAN_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                Map<String, Integer> result =
                        donDatBanService.countDonDatBanTheoNam(nam);

                return new Response(true, result, "OK");
            }

            case DONDATBAN_GET_ALL_DONDATBAN_THEO_NGAY_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                List<DonDatBan_DTO> list =
                        donDatBanService.getAllDonDatBanTheoNgayCuThe(dateStart, dateEnd);

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_KHACHHANG_THEO_NGAY_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                List<String> list =
                        donDatBanService.getKhachHangTheoNgayCuThe(dateStart, dateEnd);

                return new Response(true, list, "OK");
            }

            case DONDATBAN_COUNT_LOAIBAN_THEO_NGAY_CUTHE -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                LocalDate dateStart =
                        (LocalDate) map.get("dateStart");

                LocalDate dateEnd =
                        (LocalDate) map.get("dateEnd");

                Map<LoaiBan_DTO, Integer> result =
                        donDatBanService.countLoaiBanTheoNgayCuThe(dateStart, dateEnd);

                return new Response(true, result, "OK");
            }

            case DONDATBAN_GET_ALL -> {

                List<DonDatBan_DTO> ds = donDatBanService.getAllDonDatBan();

                return Response.builder()
                        .success(true)
                        .data(ds)
                        .message("Lấy danh sách đơn đặt bàn thành công")
                        .build();
            }

            case DONDATBAN_GET_BY_BAN -> {
                Ban_DTO banDTO = (Ban_DTO) request.getData();
                List<DonDatBan_DTO> dsDon = donDatBanService.getByBan(banDTO);
                return Response.builder()
                        .success(true)
                        .data(dsDon)
                        .build();
            }

            case DONDATBAN_ADD -> {
                DonDatBan_DTO dto = (DonDatBan_DTO) request.getData();
                boolean result = donDatBanService.them(dto);

                return Response.builder()
                        .success(result)
                        .message(result
                                ? "Thêm đơn đặt bàn thành công"
                                : "Thêm đơn đặt bàn thất bại")
                        .build();
            }

            case DONDATBAN_UPDATE -> {
                DonDatBan_DTO ddb = (DonDatBan_DTO) request.getData();
                boolean result = donDatBanService.sua(ddb);
                return Response.builder()
                        .success(result)
                        .message(result
                                ? "Cập nhật đơn đặt bàn thành công"
                                : "Cập nhật đơn đặt bàn thất bại")
                        .build();
            }
        }

        return new Response(false, null, "Invalid command");
    }
}
