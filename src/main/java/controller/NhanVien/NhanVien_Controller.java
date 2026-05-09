package controller.NhanVien;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controller.Menu.MenuNVQL_Controller;
import dto.NhanVien_DTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import network.Client;
import network.common.CommandType;
import network.common.Request;
import network.common.Response;
import util.AlertUtil;
import util.ComponentUtil;
import util.ExportExcelUtil;

public class NhanVien_Controller {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<NhanVien_DTO> tblNV;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private HBox hBox;
    @FXML
    private Button btnTatCa, btnDangLamViec, btnDaNghi;
    @FXML
    private Button btnThemNV, btnXoa, btnXuatPDF;
    @FXML
    private TableColumn<NhanVien_DTO, String> tblMaNV, tblTenNV, tblChucVu, tblEmail, tblDiaChi, tblGioiTinh,
            tblTrangThai;
    @FXML
    private TableColumn<NhanVien_DTO, Date> tblNamSinh, tblNgayVaoLam;

    private ObservableList<NhanVien_DTO> danhSachNhanVien = FXCollections.observableArrayList();
    private List<NhanVien_DTO> danhSachNhanVienDB = List.of();
    private final int LIMIT = 15;
    private String status = "all";

    private Client client;

    @FXML
    private void initialize() {
        setValueTable();

        client = Client.tryCreate();

        loadData();
        if (danhSachNhanVienDB != null) {
            phanTrang(danhSachNhanVienDB.size());
        }
        btnThemNV.setTooltip(new Tooltip("Thêm nhân viên mới"));
        btnXoa.setTooltip(new Tooltip("Đổi thành nhân viên ngừng làm việc"));
        btnXuatPDF.setTooltip(new Tooltip("Xuất danh sách nhân viên ra file"));
        btnTatCa.setTooltip(new Tooltip("Hiển thị tất cả nhân viên"));
        btnDaNghi.setTooltip(new Tooltip("Lọc danh sách nhân viên đã nghỉ việc"));
        btnDangLamViec.setTooltip(new Tooltip("Lọc danh sách nhân viên đang làm việc"));
        txtTimKiem.setTooltip(new Tooltip("Nhập tên hoặc mã nhân viên để tìm kiếm"));
    }

    @FXML
    void controller(ActionEvent event) throws IOException {
        Object source = event.getSource();

        if (source == btnThemNV) {
            MenuNVQL_Controller.instance.readyUI("NhanVien/ThemNhanVien");
            return;
        }

        if (source == btnXuatPDF) {
            xuatExcel();
            return;
        }

        if (source == btnXoa) {
            xoa();
            return;
        }

        if (source == btnTatCa)
            status = "all";
        else if (source == btnDangLamViec)
            status = "active";
        else if (source == btnDaNghi)
            status = "inactive";

        int soLuong = locTheoTrangThai(status);
        hBox.setVisible(true);
        phanTrang(soLuong);
    }

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        if (event.getSource() == txtTimKiem) {
            timKiem();
        }
        if (event.getSource() == tblNV) {
            if (event.getClickCount() == 2) {
                NhanVien_DTO nv = tblNV.getSelectionModel().getSelectedItem();
                if (nv == null)
                    return;

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/view/fxml/NhanVien/ThongTinChiTietNV.fxml"));

                BorderPane pane = loader.load();

                ThongTinChiTietNV_Controller controller = loader.getController();
                controller.setNhanVien(nv);

                MenuNVQL_Controller.instance.setCenterUI(pane);
            }
        } else if (event.getSource() == borderPane) {
            huyChonDong();
        }
    }

    @FXML
    void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            huyChonDong();
        }
    }

    private void timKiem() {
        ObservableList<NhanVien_DTO> tempList = FXCollections.observableArrayList(danhSachNhanVienDB);
        FilteredList<NhanVien_DTO> filteredData = new FilteredList<>(tempList, p -> true);

        hBox.setVisible(false);

        txtTimKiem.textProperty().addListener((obs, oldV, newV) -> {
            filteredData.setPredicate(nv -> {
                if (newV == null || newV.isEmpty())
                    return true;
                String f = newV.toLowerCase();
                return nv.getMaNV().toLowerCase().contains(f) || nv.getTenNV().toLowerCase().contains(f)
                        || nv.getEmail().toLowerCase().contains(f);
            });

            hBox.setVisible(true);
            phanTrang(filteredData);

            if (!hBox.getChildren().isEmpty()) {
                ((Button) hBox.getChildren().get(0)).fire();
            }
        });

        tblNV.setItems(filteredData);
    }

    private int locTheoTrangThai(String status) {
        danhSachNhanVien.clear();

        List<NhanVien_DTO> filtered = danhSachNhanVienDB;
        if (!status.equals("all")) {
            boolean active = status.equals("active");
            filtered = danhSachNhanVienDB.stream().filter(nv -> nv.isTrangThai() == active)
                    .collect(Collectors.toList());
        }

        danhSachNhanVien.addAll(filtered.subList(0, Math.min(LIMIT, filtered.size())));
        tblNV.setItems(danhSachNhanVien);
        return filtered.size();
    }

    private void phanTrang(int total) {
        hBox.getChildren().clear();
        loadCountPage(total);

        hBox.getChildren().forEach(btn -> {
            ((Button) btn).setOnAction(e -> {
                int page = Integer.parseInt(((Button) btn).getText()) - 1;
                int skip = page * LIMIT;

                List<NhanVien_DTO> source = danhSachNhanVienDB;
                if (!status.equals("all")) {
                    boolean active = status.equals("active");
                    source = danhSachNhanVienDB.stream().filter(nv -> nv.isTrangThai() == active)
                            .collect(Collectors.toList());
                }

                int end = Math.min(skip + LIMIT, source.size());
                loadData(source.subList(skip, end));
            });
        });
    }

    private void phanTrang(FilteredList<NhanVien_DTO> list) {
        hBox.getChildren().clear();
        loadCountPage(list.size());

        hBox.getChildren().forEach(btn -> {
            ((Button) btn).setOnAction(e -> {
                int page = Integer.parseInt(((Button) btn).getText()) - 1;
                int skip = page * LIMIT;
                int end = Math.min(skip + LIMIT, list.size());
                loadData(list.subList(skip, end));
            });
        });
    }

    private void loadCountPage(int total) {
        int pages = (int) Math.ceil((double) total / LIMIT);
        for (int i = 1; i <= pages; i++) {
            hBox.getChildren().add(ComponentUtil.createButton(String.valueOf(i), 14));
        }
    }

    private void xoa() {
        NhanVien_DTO nv = tblNV.getSelectionModel().getSelectedItem();
        if (nv == null)
            return;

        Optional<ButtonType> opt = AlertUtil.showAlertConfirm("Bạn có chắc chắn xóa?");
        if (opt.get().getButtonData() == ButtonBar.ButtonData.YES) {
            nv.setTrangThai(false);
            if (capNhatNhanVien(nv)) {
                loadData();
            }
        }
    }

    private void xuatExcel() throws IOException {
        Stage stage = (Stage) tblNV.getScene().getWindow();
        tblNV.setItems(FXCollections.observableArrayList(danhSachNhanVienDB));
        ExportExcelUtil.exportTableViewToExcel(tblNV, stage);
        loadData();
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            Request request = Request.builder().commandType(CommandType.NHANVIEN_GET_ALL).build();
            Response response = client == null ? null : client.send(request);
            Object data = response == null ? null : response.getData();

            if (!(data instanceof List<?> rawList)) {
                danhSachNhanVienDB = List.of();
                loadData(List.of());
                return;
            }

            List<NhanVien_DTO> list = (List<NhanVien_DTO>) rawList;
            danhSachNhanVienDB = list == null ? List.of() : list;

            loadData(danhSachNhanVienDB.subList(0, Math.min(LIMIT, danhSachNhanVienDB.size())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(List<NhanVien_DTO> list) {
        danhSachNhanVien.setAll(list);
        tblNV.setItems(danhSachNhanVien);
    }

    private void setValueTable() {
        tblMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        tblTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        tblChucVu.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        tblEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tblDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        tblNamSinh.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
        tblNgayVaoLam.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));

        tblGioiTinh.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isGioiTinh() ? "Nữ" : "Nam"));

        tblTrangThai.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().isTrangThai() ? "Đang làm việc" : "Ngừng làm việc"));
    }

    private void huyChonDong() {
        tblNV.getSelectionModel().clearSelection();
    }

    private boolean capNhatNhanVien(NhanVien_DTO dto) {
        try {
            Request request = Request.builder().commandType(CommandType.NHANVIEN_UPDATE).data(dto).build();
            Response response = client == null ? null : client.send(request);
            return response != null && response.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
