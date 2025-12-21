package controller.NhanVien;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import config.RestaurantApplication;
import controller.Menu.MenuNVQL_Controller;
import dao.NhanVien_DAO;
import entity.NhanVien;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import util.ComponentUtil;
import util.ExportExcelUtil;

public class NhanVien_Controller {

    @FXML private BorderPane borderPane;
    @FXML private TableView<NhanVien> tblNV;
    @FXML private TextField txtTimKiem;
    @FXML private HBox hBox;

    @FXML private Button btnTatCa, btnDangLamViec, btnDaNghi;
    @FXML private Button btnThemNV, btnXoa, btnXuatPDF;

    @FXML private TableColumn<NhanVien,String> tblMaNV, tblTenNV, tblChucVu,
            tblEmail, tblDiaChi, tblGioiTinh, tblTrangThai;
    @FXML private TableColumn<NhanVien,Date> tblNamSinh, tblNgayVaoLam;

    private ObservableList<NhanVien> danhSachNhanVien = FXCollections.observableArrayList();
    private List<NhanVien> danhSachNhanVienDB;
    private final int LIMIT = 15;
    private String status = "all";

    @FXML
    private void initialize() {
        setValueTable();
        loadData();
        phanTrang(danhSachNhanVienDB.size());
        btnThemNV.setTooltip(new Tooltip("Thêm nhân viên mới"));
        btnXoa.setTooltip(new Tooltip("Đổi thành nhân viên ngừng làm việc"));
        btnXuatPDF.setTooltip(new Tooltip("Xuất danh sách nhân viên ra file PDF"));
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

        if (source == btnTatCa) status = "all";
        else if (source == btnDangLamViec) status = "active";
        else if (source == btnDaNghi) status = "inactive";

        int soLuong = locTheoTrangThai(status);
        hBox.setVisible(true);
        phanTrang(soLuong);
    }

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        if (event.getSource() == txtTimKiem) {
            timKiem();
        }if (event.getSource() == tblNV) {
        	if (event.getClickCount() == 2) {
	        	    NhanVien nv = tblNV.getSelectionModel().getSelectedItem();
	        	    if (nv == null) return;
	
	        	    FXMLLoader loader = new FXMLLoader(
	        	        getClass().getResource("/view/fxml/NhanVien/ThongTinChiTietNV.fxml")
	        	    );
	
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
        ObservableList<NhanVien> tempList = FXCollections.observableArrayList(danhSachNhanVienDB);
        FilteredList<NhanVien> filteredData = new FilteredList<>(tempList, p -> true);

        hBox.setVisible(false);

        txtTimKiem.textProperty().addListener((obs, oldV, newV) -> {
            filteredData.setPredicate(nv -> {
                if (newV == null || newV.isEmpty()) return true;
                String f = newV.toLowerCase();
                return nv.getMaNV().toLowerCase().contains(f)
                        || nv.getTenNV().toLowerCase().contains(f)
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

        List<NhanVien> filtered = danhSachNhanVienDB;
        if (!status.equals("all")) {
            boolean active = status.equals("active");
            filtered = danhSachNhanVienDB.stream()
                    .filter(nv -> nv.isTrangThai() == active)
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

                List<NhanVien> source = danhSachNhanVienDB;
                if (!status.equals("all")) {
                    boolean active = status.equals("active");
                    source = danhSachNhanVienDB.stream()
                            .filter(nv -> nv.isTrangThai() == active)
                            .collect(Collectors.toList());
                }

                int end = Math.min(skip + LIMIT, source.size());
                loadData(source.subList(skip, end));
            });
        });
    }

    private void phanTrang(FilteredList<NhanVien> list) {
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
        NhanVien nv = tblNV.getSelectionModel().getSelectedItem();
        if (nv == null) return;

        Optional<ButtonType> opt = showAlertConfirm("Bạn có chắc chắn xóa?");
        if (opt.get().getButtonData() == ButtonBar.ButtonData.YES) {
            nv.setTrangThai(false);
            RestaurantApplication.getInstance()
                    .getDatabaseContext()
                    .newEntity_DAO(NhanVien_DAO.class)
                    .capNhat(nv);
            loadData();
        }
    }

    private void xuatExcel() throws IOException {
        Stage stage = (Stage) tblNV.getScene().getWindow();
        tblNV.setItems(FXCollections.observableArrayList(danhSachNhanVienDB));
        ExportExcelUtil.exportTableViewToExcel(tblNV, stage);
        loadData();
    }

    private void loadData() {
        danhSachNhanVienDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(NhanVien_DAO.class)
                .getDanhSach(NhanVien.class, new HashMap<>());
        loadData(danhSachNhanVienDB.subList(0, Math.min(LIMIT, danhSachNhanVienDB.size())));
    }

    private void loadData(List<NhanVien> list) {
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

        tblGioiTinh.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isGioiTinh() ? "Nữ" : "Nam"));

        tblTrangThai.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isTrangThai()
                        ? "Đang làm việc" : "Ngừng làm việc"));
    }

    private void huyChonDong() {
        tblNV.getSelectionModel().clearSelection();
    }

    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(content);
        alert.getButtonTypes().setAll(
                new ButtonType("Có", ButtonBar.ButtonData.YES),
                new ButtonType("Không", ButtonBar.ButtonData.NO),
                new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE)
        );
        return alert.showAndWait();
    }
}
