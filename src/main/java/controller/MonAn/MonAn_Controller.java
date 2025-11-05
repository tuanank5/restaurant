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
	}
	          