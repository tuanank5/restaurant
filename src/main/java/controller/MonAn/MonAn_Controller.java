package controller.MonAn;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
import dao.KhuyenMai_DAO;
import dao.impl.KhuyenMai_DAOImpl;
import dao.impl.MonAn_DAOImpl;
import entity.KhuyenMai;
import entity.MonAn;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


public class MonAn_Controller implements Initializable{

	 	private MenuNV_Controller menuController; // Reference đến MenuNV_Controller

	    public void setMenuController(MenuNV_Controller menuController) {
	        this.menuController = menuController;
	    }
	    
	    @FXML private Button btnSua, btnThem, btnThemAnh, btnXoa;
	    @FXML private ComboBox<KhuyenMai> cmbKM;
	    @FXML private ComboBox<String> cmbLoaiMon;
	    @FXML private ImageView img;
	    @FXML private TableView<MonAn> tblMon;
	    @FXML private TableColumn<MonAn, String> colMa, colTen, colLoaiMon;
	    @FXML private TableColumn<MonAn, Double> colDonGia;
	    @FXML private TableColumn<MonAn, KhuyenMai> colKM;
	    @FXML private TextField txtMaMon, txtTenMon, txtDonGia;


	    private String duongDanAnh;
	    
	    private MonAn_DAOImpl monDAO = new MonAn_DAOImpl();
	    private KhuyenMai_DAOImpl kmDAO = new KhuyenMai_DAOImpl();
	    private KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAOImpl();

	    @FXML
	    private void handleThemAnh() {
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Chọn ảnh món ăn");

	        // Chỉ cho chọn file ảnh
	        fileChooser.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
	        );

	        File file = fileChooser.showOpenDialog(null);

	        if (file != null) {
	            duongDanAnh = file.getAbsolutePath(); // Lưu đường dẫn ảnh

	            Image image = new Image(file.toURI().toString());
	            img.setImage(image);

	            // Giữ nguyên kích thước đã đặt trong FXML — KHÔNG set lại fitWidth / fitHeight
	            img.setPreserveRatio(true);
	        }
	        
	    }

	    @Override
	    public void initialize(URL url, ResourceBundle rb) {
	    	khoiTaoComboBoxKhuyenMai();
	    	 txtMaMon.setText(sinhMaMon());
	        // --- Cấu hình cột TableView ---
	        colMa.setCellValueFactory(new PropertyValueFactory<>("maMon"));
	        colTen.setCellValueFactory(new PropertyValueFactory<>("tenMon"));
	        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
	        
	        // Cột Khuyến mãi hiển thị tên KM + %
	        colKM.setCellValueFactory(new PropertyValueFactory<>("khuyenMai"));
	        colKM.setCellFactory(column -> new TableCell<MonAn, KhuyenMai>() {
	        	@Override
	            protected void updateItem(KhuyenMai item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty || item == null) {
	                	setText("");
	                } else {
	                    setText(item.getTenKM() + " - " + item.getPhanTramGiamGia() + "%");
	                }
	            }
	        });
	        colLoaiMon.setCellValueFactory(cellData -> 
	        new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLoaiMon())
	    );
	        cmbLoaiMon.getItems().addAll("Món chính", "Tráng miệng", "Nước uống");

	        tblMon.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	            if (newSelection != null) {
	                hienThiChiTietMon(newSelection);
	            }
	        });
	        
	        loadTable();
	        btnThem.setOnAction(e -> handleThemMon());
	        btnSua.setOnAction(e -> handleSuaMon());
	        btnXoa.setOnAction(e -> handleXoaMon());
	    }
	    
	    @FXML
	    private void handleXoaMon() {
	        try {
	            MonAn monChon = tblMon.getSelectionModel().getSelectedItem();
	            if (monChon == null) {
	                Alert alert = new Alert(Alert.AlertType.WARNING);
	                alert.setTitle("Thông báo");
	                alert.setHeaderText(null);
	                alert.setContentText("Vui lòng chọn món ăn để xóa!");
	                alert.showAndWait();
	                return;
	            }

	            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
	            confirm.setTitle("Xác nhận xóa");
	            confirm.setHeaderText(null);
	            confirm.setContentText("Bạn có chắc muốn xóa món: " + monChon.getTenMon() + " ?");
	            
	            // Nếu nhấn Hủy thì thoát
	            if (confirm.showAndWait().get() != ButtonType.OK) {
	                return;
	            }

	            boolean xoaThanhCong = monDAO.xoa(monChon.getMaMon());
	            if (xoaThanhCong) {
	                Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                alert.setTitle("Xóa món ăn");
	                alert.setHeaderText(null);
	                alert.setContentText("Xóa món ăn thành công!");
	                alert.showAndWait();

	                loadTable();      // load lại bảng
	                tblMon.refresh(); // làm mới hiển thị
	                resetForm();      // xóa dữ liệu trên form
	            } else {
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Lỗi");
	                alert.setHeaderText(null);
	                alert.setContentText("Xóa món ăn thất bại!");
	                alert.showAndWait();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    @FXML
	    private void handleSuaMon() {
	        try {
	            MonAn monChon = tblMon.getSelectionModel().getSelectedItem();
	            if (monChon == null) {
	                Alert alert = new Alert(Alert.AlertType.WARNING);
	                alert.setTitle("Thông báo");
	                alert.setHeaderText(null);
	                alert.setContentText("Vui lòng chọn món ăn để sửa!");
	                alert.showAndWait();
	                return;
	            }

	            String tenMon = txtTenMon.getText().trim();
	            String donGiaStr = txtDonGia.getText().trim();
	            KhuyenMai km = cmbKM.getValue();

	            if (tenMon.isEmpty() || donGiaStr.isEmpty() || km == null) {
	                Alert alert = new Alert(Alert.AlertType.WARNING);
	                alert.setTitle("Thông báo");
	                alert.setHeaderText(null);
	                alert.setContentText("Vui lòng nhập đầy đủ thông tin món ăn và chọn khuyến mãi!");
	                alert.showAndWait();
	                return;
	            }

	            double donGia;
	            try {
	                donGia = Double.parseDouble(donGiaStr);
	            } catch (NumberFormatException e) {
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Lỗi nhập dữ liệu");
	                alert.setHeaderText(null);
	                alert.setContentText("Đơn giá phải là số hợp lệ!");
	                alert.showAndWait();
	                return;
	            }

	            // Cập nhật thông tin cho đối tượng chọn
	            monChon.setTenMon(tenMon);
	            monChon.setDonGia(donGia);
	            monChon.setKhuyenMai(km);
	            monChon.setDuongDanAnh(duongDanAnh); // ảnh có thể thay đổi
	            monChon.setLoaiMon(cmbLoaiMon.getValue());
	            
	            boolean suaThanhCong = monDAO.capNhat(monChon); // Gọi phương thức update trong DAO
	            if (suaThanhCong) {
	                Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                alert.setTitle("Sửa món ăn");
	                alert.setHeaderText(null);
	                alert.setContentText("Sửa món ăn thành công!");
	                alert.showAndWait();
	             // Reload TableView
	                loadTable();
	                tblMon.refresh();
	                // Reset form
	                resetForm();
	            } else {
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Lỗi");
	                alert.setHeaderText(null);
	                alert.setContentText("Sửa món ăn thất bại!");
	                alert.showAndWait();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    
	    private void khoiTaoComboBoxKhuyenMai() {
	        // Lấy danh sách Khuyến Mãi từ DB
	    	List<KhuyenMai> danhSachKM = khuyenMaiDAO.getDanhSach("KhuyenMai.list", KhuyenMai.class);

	        if (danhSachKM != null && !danhSachKM.isEmpty()) {
	            // Đưa danh sách vào ComboBox
	            cmbKM.getItems().setAll(danhSachKM);

	            // Hiển thị tên KM + %
	            cmbKM.setCellFactory(lv -> new ListCell<KhuyenMai>() {
	                @Override
	                protected void updateItem(KhuyenMai item, boolean empty) {
	                    super.updateItem(item, empty);
	                    setText(empty || item == null ? "" : item.getTenKM() + " - " + item.getPhanTramGiamGia() + "%");
	                }
	            });

	            cmbKM.setButtonCell(new ListCell<KhuyenMai>() {
	                @Override
	                protected void updateItem(KhuyenMai item, boolean empty) {
	                    super.updateItem(item, empty);
	                    setText(empty || item == null ? "" : item.getTenKM() + " - " + item.getPhanTramGiamGia() + "%");
	                }
	            });
	        } else {
	            cmbKM.getItems().clear(); // Trường hợp danh sách rỗng
	        }
	    }

	    // ------------------- Load dữ liệu từ DB lên TableView -------------------
	    private void loadTable() {
	        List<MonAn> danhSachMon = monDAO.getDanhSachMonAn(); // dùng phương thức DAO chuẩn
	        if (danhSachMon != null && !danhSachMon.isEmpty()) {
	            tblMon.setItems(FXCollections.observableArrayList(danhSachMon));
	        } else {
	            tblMon.setItems(FXCollections.observableArrayList());
	            System.out.println("Danh sách món ăn rỗng hoặc dữ liệu chưa đúng FK KhuyenMai!");
	        }
	    }
	    
	    //---------THÊM MÓN------------
	    @FXML
	    private void handleThemMon() {
	        try {
	            String maMon = txtMaMon.getText().trim();
	            String tenMon = txtTenMon.getText().trim();
	            String donGiaStr = txtDonGia.getText().trim();
	            KhuyenMai km = cmbKM.getValue(); // Lấy KM từ ComboBox

	            // --- Kiểm tra dữ liệu nhập ---
	            if (maMon.isEmpty() || tenMon.isEmpty() || donGiaStr.isEmpty() || km == null) {
	                Alert alert = new Alert(Alert.AlertType.WARNING);
	                alert.setTitle("Thông báo");
	                alert.setHeaderText(null);
	                alert.setContentText("Vui lòng nhập đầy đủ thông tin món ăn và chọn khuyến mãi!");
	                alert.showAndWait();
	                return;
	            }

	            double donGia;
	            try {
	                donGia = Double.parseDouble(donGiaStr);
	            } catch (NumberFormatException e) {
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Lỗi nhập dữ liệu");
	                alert.setHeaderText(null);
	                alert.setContentText("Đơn giá phải là số hợp lệ!");
	                alert.showAndWait();
	                return;
	            }

	            // --- Tạo đối tượng MonAn ---
	            String loaiMon = cmbLoaiMon.getValue();

	            MonAn mon = new MonAn(maMon, tenMon, donGia, km, duongDanAnh, loaiMon);

	            // --- Thêm vào DB ---
	            boolean themThanhCong = monDAO.them(mon);
	            if (themThanhCong) {
	                Alert alert = new Alert(Alert.AlertType.INFORMATION);
	                alert.setTitle("Thêm món ăn");
	                alert.setHeaderText(null);
	                alert.setContentText("Thêm món ăn thành công!");
	                alert.showAndWait();

	                // Cập nhật TableView
	                loadTable();

	                // Reset form và tự sinh mã món mới
	                resetForm();

	            } else {
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Lỗi");
	                alert.setHeaderText(null);
	                alert.setContentText("Thêm món ăn thất bại! Kiểm tra dữ liệu và FK KhuyenMai.");
	                alert.showAndWait();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    //Sinh mã tự động
        private String sinhMaMon() {
            List<MonAn> listMon = monDAO.getDanhSachMonAn();
            int max = 0;
            for (MonAn m : listMon) {
                try {
                    int so = Integer.parseInt(m.getMaMon().substring(1));
                    if (so > max) max = so;
                } catch (Exception e) {
                    // bỏ qua nếu format khác
                }
            }
            return String.format("M%02d", max + 1); // Ví dụ: M01, M02,...
        }

	    private void resetForm() {
	        txtMaMon.setText(sinhMaMon()); // tự sinh mã mới
	        txtTenMon.clear();
	        txtDonGia.clear();
	        cmbKM.getSelectionModel().clearSelection();
	        img.setImage(null);
	        duongDanAnh = null;
	    }

	    private void hienThiChiTietMon(MonAn mon) {
	        txtMaMon.setText(mon.getMaMon());
	        txtTenMon.setText(mon.getTenMon());
	        txtDonGia.setText(String.valueOf(mon.getDonGia()));

	        if (mon.getKhuyenMai() != null) {
	            cmbKM.getSelectionModel().select(mon.getKhuyenMai());
	        } else {
	        	cmbKM.getSelectionModel().clearSelection();
	        }

	        // Hiển thị Loại Món
	        if (mon.getLoaiMon() != null && !mon.getLoaiMon().isEmpty()) {
	            cmbLoaiMon.getSelectionModel().select(mon.getLoaiMon());
	        } else {
	            cmbLoaiMon.getSelectionModel().clearSelection();
	        }
	        
	        // Hiển thị ảnh và lưu đường dẫn vào biến
	        if (mon.getDuongDanAnh() != null && !mon.getDuongDanAnh().isEmpty()) {
	            File file = new File(mon.getDuongDanAnh());
	            if (file.exists()) {
	            	img.setImage(new Image(file.toURI().toString()));
	                duongDanAnh = mon.getDuongDanAnh(); // <-- giữ đường dẫn
	            } else {
	                img.setImage(null);
	                duongDanAnh = null;
	            }
	        } else {
	            img.setImage(null);
	            duongDanAnh = null;
	        }
	    }
	}
	            