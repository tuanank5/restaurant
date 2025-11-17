package controller.KhachHang;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import config.DatabaseContext;
import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.HangKhachHang_DAO;
import dao.KhachHang_DAO;
import dao.impl.HangKhachHang_DAOImpl;
import entity.HangKhachHang;
import entity.KhachHang;
import entity.LoaiBan;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class ThemKhachHang_Controller {
	@FXML
    private BorderPane borderPane;

    @FXML
    private Button btnLuu;

    @FXML
    private Button btnTroLai;

    @FXML
    private Button btnXoa;

    @FXML
    private ComboBox<String> comBoxHangKH;

    @FXML
    private Label lblDanhSachKhachHang;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtDiemTichLuy;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtKhachHang;

    @FXML
    private TextField txtSDT;

	private String ui;
	
	private KhachHang khachHang;
	
	private List<HangKhachHang> danhSachHangKhachHangDB;
    
    @FXML
    public void initialize() {
        Platform.runLater(() -> txtKhachHang.requestFocus());
        loadData();
    }
    
    public FXMLLoader readyUI(String ui) {
        Parent root = null;
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
            fxmlLoader.setLocation(getClass().getResource("/view/fxml/" + ui + ".fxml"));
            root = fxmlLoader.load();
            borderPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fxmlLoader;
    }
    @FXML
    void controller(ActionEvent event) {
    	Object src = event.getSource();
    	if(src == btnTroLai) {
    		if(!lblDanhSachKhachHang.getText().equalsIgnoreCase("Danh Sách Khách Hàng")) {
    			//xacNhanLuu("DatBan/DatBan");
    			MenuNV_Controller.instance.readyUI("DatBan/DatBan-test");
    		}else {
    			MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
    		}
    	}else if(src ==  btnLuu) {
    		luuLai();
    	} else if(src == btnXoa) {
    		resetAllField();
    	}
    }
    
    @FXML
    void mouseClicked(MouseEvent event) {
        Object source = event.getSource();
        // Kiểm tra nếu người dùng click vào label "Danh Sách Khách Hàng"
        if (source == lblDanhSachKhachHang) {
            if (!lblDanhSachKhachHang.getText().equalsIgnoreCase("Danh Sách Khách Hàng")) {
                // Nếu không phải ở danh sách chính, xác nhận lưu và quay lại giao diện gọi đến
                //xacNhanLuu("DatBan/DatBan");
                MenuNV_Controller.instance.readyUI("DatBan/DatBan-test");
            } else {
                // Nếu đang ở trang danh sách khách hàng chính, quay lại danh sách khách hàng
                //xacNhanLuu("KhachHang/KhachHang");
                MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
            }
        }
    }
    
    @FXML
    void keyPressed(KeyEvent event) throws IOException {
        Object src = event.getSource();
        // Kiểm tra nếu người dùng thao tác trong AnchorPane chính của giao diện
        if (src == borderPane) {
            //Tổ hợp phím Ctrl + S -> Lưu nhân viên
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                luuLai();
            //Nhấn phím ESC -> Xác nhận lưu rồi quay lại giao diện trước
            } else if (event.getCode() == KeyCode.ESCAPE) {
                //Nếu không đang ở màn hình "Danh Sách Nhân Viên"
                if (!lblDanhSachKhachHang.getText().equalsIgnoreCase("Danh Sách Nhân Viên")) {
                    //xacNhanLuu("DatBan/DatBan");
                    MenuNV_Controller.instance.readyUI("DatBan/DatBan-test");// Ví dụ quay về trang chính hoặc form trước đó
                } else {
                    //xacNhanLuu("KhachHang/KhachHang");
                    MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");// Quay lại danh sách nhân viên
                }
            }
        }
    }
    
    // Chức năng xác nhận lưu khách hàng
    private void xacNhanLuu(String ui) {
        KhachHang khachHangNew = getKhachHangNew();
        if (khachHangNew != null) {
            Optional<ButtonType> buttonType = showAlertConfirm("Bạn có muốn lưu thông tin khách hàng không?");
            if (buttonType.isPresent()) {
                if (buttonType.get().getButtonData() == ButtonBar.ButtonData.NO) {
                	MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
                } else if (buttonType.get().getButtonData() == ButtonBar.ButtonData.YES) {
                    // Thực hiện lưu khách hàng mới
                    boolean check = RestaurantApplication.getInstance()
                            .getDatabaseContext()
                            .newEntity_DAO(KhachHang_DAO.class)
                            .them(khachHangNew);
                    if (check) {
                        showAlert("Thông báo", "Thêm khách hàng thành công!", Alert.AlertType.INFORMATION);
                        this.khachHang = khachHangNew;
                        //readyUI(ui); // Quay lại trang trước sau khi lưu
                        MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
                    } else {
                        showAlert("Thông báo", "Thêm khách hàng thất bại!", Alert.AlertType.WARNING);
                    }
                }
            }
        } else {
        	MenuNV_Controller.instance.readyUI("KhachHang/KhachHang");
        }
    }
    
    public void luuLai() {
    	KhachHang khachHangNew = getKhachHangNew();
    	 try {
    	     if (khachHangNew != null) {
    	        boolean check = RestaurantApplication.getInstance()
    	                    .getDatabaseContext()
    	                    .newEntity_DAO(KhachHang_DAO.class)
    	                    .them(khachHangNew);
    	            //Kiểm tra kết quả thêm
    	            if (check) {
    	                showAlert("Thông báo", "Thêm khách hàng thành công!", Alert.AlertType.INFORMATION);
    	                this.khachHang = khachHangNew;
    	                // Nếu không ở form thêm, thì quay lại
    	                if (!lblDanhSachKhachHang.getText().equalsIgnoreCase("Danh Sách Khách Hàng")) {
    	                	readyUI(ui);
    	                }
    	                // Reset lại toàn bộ các ô nhập
    	                resetAllField();
    	            } else {
    	                showAlert("Thông báo", "Thêm khách hàng thất bại!", Alert.AlertType.WARNING);
    	            }
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        showAlert("Lỗi", "Xảy ra lỗi trong quá trình lưu khách hàng.", Alert.AlertType.ERROR);
    	    }
    }
    
    private KhachHang getKhachHangNew() {
        String tenKH = txtKhachHang.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String diemStr = txtDiemTichLuy.getText().trim();
        String hangKH = comBoxHangKH.getValue();

        DatabaseContext databaseContext = RestaurantApplication
                .getInstance()
                .getDatabaseContext();

        //Kiểm tra tên khách hàng
        if (tenKH.isEmpty()) {
            showAlert("Cảnh Báo", "Vui lòng nhập tên khách hàng!", Alert.AlertType.WARNING);
            txtKhachHang.requestFocus();
            return null;
        }
        //Kiểm tra số điện thoại
        if (sdt.isEmpty()) {
            showAlert("Cảnh Báo", "Vui lòng nhập số điện thoại!", Alert.AlertType.WARNING);
            txtSDT.requestFocus();
            return null;
        } else {
            String regexPhone = "(84|0)[987531][0-9]{8,9}\\b";
            if (!sdt.matches(regexPhone)) {
                showAlert("Cảnh Báo", "Số điện thoại không hợp lệ!", Alert.AlertType.WARNING);
                txtSDT.requestFocus();
                return null;
            } else {
                Map<String, Object> filter = new HashMap<>();
                filter.put("sdt", sdt);
                List<KhachHang> listKH = databaseContext
                        .newEntity_DAO(KhachHang_DAO.class)
                        .getDanhSach(KhachHang.class, filter);
                if (!listKH.isEmpty()) {
                    showAlert("Cảnh Báo", "Số điện thoại đã tồn tại!", Alert.AlertType.WARNING);
                    return null;
                }
            }
        }
        //Kiểm tra email
        if (email.isEmpty()) {
            showAlert("Cảnh Báo", "Vui lòng nhập email!", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return null;
        } else if (!email.matches("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$")) {
            showAlert("Cảnh Báo", "Email không hợp lệ!", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return null;
        }
        //Kiểm tra địa chỉ
        if (diaChi.isEmpty()) {
            showAlert("Cảnh Báo", "Vui lòng nhập địa chỉ!", Alert.AlertType.WARNING);
            txtDiaChi.requestFocus();
            return null;
        }
        //Kiểm tra điểm tích lũy
        int diemTichLuy = 0;
        try {
            diemTichLuy = Integer.parseInt(diemStr);
            if (diemTichLuy < 0) {
                showAlert("Cảnh Báo", "Điểm tích lũy không thể âm!", Alert.AlertType.WARNING);
                txtDiemTichLuy.requestFocus();
                return null;
            }
        } catch (NumberFormatException e) {
            showAlert("Cảnh Báo", "Điểm tích lũy phải là số nguyên!", Alert.AlertType.WARNING);
            txtDiemTichLuy.requestFocus();
            return null;
        }
        //Kiểm tra hạng khách hàng
        if (hangKH == null) {
            showAlert("Cảnh Báo", "Vui lòng chọn hạng khách hàng!", Alert.AlertType.WARNING);
            comBoxHangKH.requestFocus();
            return null;
        }
        KhachHang kh = new KhachHang();
        kh.setMaKH(phatSinhMaKH()); // quan trọng
        kh.setTenKH(tenKH);
        kh.setSdt(sdt);
        kh.setEmail(email);
        kh.setDiaChi(diaChi);
        kh.setDiemTichLuy(diemTichLuy);
        // Lấy object HangKhachHang tương ứng
        HangKhachHang hang = danhSachHangKhachHangDB.stream()
            .filter(h -> h.getTenHang().equals(hangKH))
            .findFirst()
            .orElse(null);
        kh.setHangKhachHang(hang);

        return kh;
    }

    private String phatSinhMaKH(){
    	KhachHang_DAO dao = RestaurantApplication.getInstance()
    			.getDatabaseContext()
    			.newEntity_DAO(KhachHang_DAO.class);
    	//Lấy danh sách khách hàng hiện có
    	List<KhachHang> dsKH = dao.getDanhSach(KhachHang.class, new HashMap<>());
    	//Tìm mã KH > nhất hiện tại
    	int max = 0;
    	for(KhachHang kh : dsKH) {
    		String ma = kh.getMaKH();
    		if(ma != null && ma.startsWith("KH")) {
    			try {
    				int num = Integer.parseInt(ma.substring(2));
    				if(num > max) max = num;
    			}catch (NumberFormatException e) {
					// TODO: handle exception
				}
    		}
    	}
    	return String.format("KH%04d", max + 1);
    }
    
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.show();
    }
    
    private Optional<ButtonType> showAlertConfirm(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(content);
        ButtonType buttonLuu = new ButtonType("Có", ButtonBar.ButtonData.YES);
        ButtonType buttonKhongLuu = new ButtonType("Không", ButtonBar.ButtonData.NO);
        ButtonType buttonHuy = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonLuu, buttonKhongLuu, buttonHuy);
        return alert.showAndWait();
    }
    
    private void resetAllField() {
        txtKhachHang.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtDiemTichLuy.setText("");
        comBoxHangKH.setValue(null);
    }
    
    private void loadData() {
        Map<String,Object> filter = new HashMap<>();
        danhSachHangKhachHangDB = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(HangKhachHang_DAO.class)
                .getDanhSach(HangKhachHang.class, filter);
        comBoxHangKH.getItems().clear();
        //comBoxHangKH.getItems().add("Tất cả");
        for(HangKhachHang hang : danhSachHangKhachHangDB) {
            comBoxHangKH.getItems().add(hang.getTenHang());
        }
        comBoxHangKH.getSelectionModel().selectFirst();
    }
    
//    private void loadData() {
//        Map<String,Object> filter = new HashMap<>();
//        danhSachHangKhachHangDB = RestaurantApplication.getInstance()
//                .getDatabaseContext()
//                .newEntity_DAO(HangKhachHang_DAO.class)
//                .getDanhSach(HangKhachHang.class, filter);
//        comBoxHangKH.getItems().clear();
//        // Dùng Set để loại trùng tên hạng
//        Set<String> tenHangSet = new HashSet<>();
//        for (HangKhachHang hang : danhSachHangKhachHangDB) {
//            tenHangSet.add(hang.getTenHang());
//        }
//        ObservableList<String> listTen = FXCollections.observableArrayList(tenHangSet);
//        // Load lên ComboBox
//        comBoxHangKH.setItems(listTen);
//        if (!listTen.isEmpty()) {
//            comBoxHangKH.getSelectionModel().selectFirst();
//        }
//    }

    // Dùng để lấy ra sau đó setText lại cho đường dẫn
    public void setUrl(String nameUrl, String currentPage) {
        lblDanhSachKhachHang.setText(nameUrl);
        this.ui = currentPage;
    }
}
