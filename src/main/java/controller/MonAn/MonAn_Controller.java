package controller.MonAn;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import controller.Menu.MenuNV_Controller;
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
	    @FXML private ImageView img;
	    @FXML private TableView<MonAn> tblMon;
	    @FXML private TableColumn<MonAn, String> colMa, colTen;
	    @FXML private TableColumn<MonAn, Double> colDonGia;
	    @FXML private TableColumn<MonAn, KhuyenMai> colKM;
	    @FXML private TextField txtMaMon, txtTenMon, txtDonGia;


	    private String duongDanAnh;
	    
	    private MonAn_DAOImpl monDAO = new MonAn_DAOImpl();
	    private KhuyenMai_DAOImpl kmDAO = new KhuyenMai_DAOImpl();

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

	        loadTable();
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
	            MonAn mon = new MonAn(maMon, tenMon, donGia, km, duongDanAnh);

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

	                // Xóa dữ liệu nhập
	                txtMaMon.clear();
	                txtTenMon.clear();
	                txtDonGia.clear();
	                cmbKM.getSelectionModel().clearSelection();
	                img.setImage(null);
	                duongDanAnh = null;
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


	}
	          