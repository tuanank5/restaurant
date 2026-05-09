package handler;

import dto.DonDatBan_DTO;
import dto.LoaiBan_DTO;
import network.common.CommandHandler;
import network.common.Request;
import network.common.Response;
import service.DonDatBan_Service;
import service.impl.DonDatBan_ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DonDatBanHandler implements CommandHandler {

    private final DonDatBan_Service service = new DonDatBan_ServiceImpl();

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
                        service.getAllDonDatBanNVTheoThang(
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
                        service.getKhachHangTheoThangNVCuThe(
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
                        service.getAllDonDatBanTheoNamNVCuThe(
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
                        service.getKhachHangTheoNamNVCuThe(
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
                        service.countDonDatBanTheoNamNVCuThe(
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
                        service.getAllDonDatBanTheoNgayNVCuThe(
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
                        service.getKhachHangTheoNgayNVCuThe(
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
                        service.getAllDonDatBanTheoThang(
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
                        service.getAllDonDatBanTheoNam(nam);

                return new Response(true, list, "OK");
            }

            case DONDATBAN_GET_KHACHHANG_THEO_THANG -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int thang = (int) map.get("thang");
                int nam = (int) map.get("nam");

                List<String> list =
                        service.getKhachHangTheoThang(
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
                        service.countLoaiBanTheoThang(thang, nam);

                return new Response(true, result, "OK");
            }

            case DONDATBAN_GET_KHACHHANG_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                List<String> list =
                        service.getKhachHangTheoNam(nam);

                return new Response(true, list, "OK");
            }

            case DONDATBAN_COUNT_LOAIBAN_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                Map<LoaiBan_DTO, Integer> result =
                        service.countLoaiBanTheoNam(nam);

                return new Response(true, result, "OK");
            }

            case DONDATBAN_COUNT_DONDATBAN_THEO_NAM -> {

                Map<String, Object> map =
                        (Map<String, Object>) request.getData();

                int nam = (int) map.get("nam");

                Map<String, Integer> result =
                        service.countDonDatBanTheoNam(nam);

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
                        service.getAllDonDatBanTheoNgayCuThe(dateStart, dateEnd);

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
                        service.getKhachHangTheoNgayCuThe(dateStart, dateEnd);

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
                        service.countLoaiBanTheoNgayCuThe(dateStart, dateEnd);

                return new Response(true, result, "OK");
            }
        }

        return new Response(false, null, "Invalid command");
    }
}
