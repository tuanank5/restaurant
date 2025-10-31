package controller.Ban;

import java.util.List;
import javafx.scene.control.TextField; 
import config.RestaurantApplication;
import controller.Menu.MenuNV_Controller;
import dao.Ban_DAO;
import dao.LoaiBan_DAO;
import entity.Ban;
import entity.LoaiBan;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class QuanLyBan_Controller {

    private MenuNV_Controller menuController; // Reference đến MenuNV_Controller

    public void setMenuController(MenuNV_Controller menuController) {
        this.menuController = menuController;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button btnSua;

    @FXML
    private Button btnThem;

    @FXML
    private Button btnXoa;

    @FXML
    private TableColumn<Ban, String> colMaBan;

    @FXML
    private TableColumn<Ban, String> colMaLoaiBan;

    @FXML
    private TableColumn<Ban, String> colTrangThai;

    @FXML
    private TableColumn<Ban, String> colViTri;

    @FXML
    private TableView<Ban> tblBan;
    
    @FXML
    private TextField txtMaBan;

    @FXML
    private TextField txtViTri;

    @FXML
    private ComboBox<String> comBoxTrangThai; 

    @FXML
    private ComboBox<LoaiBan> comBoxLoaiBan; 


    private ObservableList<Ban> danhSachBan = FXCollections.observableArrayList();

    private String sinhMaBan() {
        // Lấy danh sách bàn hiện có
        List<Ban> listBan = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .getDanhSach(Ban.class, null);

        int max = 0;
        for (Ban b : listBan) {
            try {
                // Giả sử mã bàn có dạng "B01", "B02", ...
                int so = Integer.parseInt(b.getMaBan().substring(1));
                if (so > max) max = so;
            } catch (Exception e) {
                // bỏ qua nếu format khác
            }
        }
        return String.format("B%02d", max + 1); // Tạo mã mới
    }


    @FXML
    public void initialize() {
        // Thiết lập các cột TableView
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        colViTri.setCellValueFactory(new PropertyValueFactory<>("viTri"));
        
        colTrangThai.setCellValueFactory(cellData -> {
            String trangThai = cellData.getValue().getTrangThai();
            return new SimpleStringProperty(trangThai != null ? trangThai : "Trống");
        });
     // Chú ý: colMaLoaiBan phải là TableColumn<Ban, String>
        colMaLoaiBan.setCellValueFactory(cellData -> {
            LoaiBan loaiBan = cellData.getValue().getLoaiBan();
            String tenLoai = (loaiBan != null) ? loaiBan.getTenLoaiBan() : "";
            return new SimpleStringProperty(tenLoai);
        });

     // ✅ Khởi tạo dữ liệu cho ComboBox trạng thái
        comBoxTrangThai.setItems(FXCollections.observableArrayList(
                "Trống",
                "Đang sử dụng",
                "Đặt trước"
        ));
        comBoxTrangThai.setValue("Trống"); // Giá trị mặc định

     // Load dữ liệu loại bàn từ DB
        List<LoaiBan> listLoaiBan = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(LoaiBan_DAO.class)
                .getDanhSach(LoaiBan.class, null);

        comBoxLoaiBan.getItems().setAll(listLoaiBan);

        // Hiển thị tên loại bàn trên ComboBox
        comBoxLoaiBan.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(LoaiBan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTenLoaiBan());
            }
        });
        comBoxLoaiBan.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(LoaiBan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTenLoaiBan());
            }
        });
        //Sửa
        tblBan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Điền dữ liệu vào các TextField và ComboBox
                txtMaBan.setText(newSelection.getMaBan()); // Mã bàn vẫn giữ, không sửa
                txtViTri.setText(newSelection.getViTri());
                comBoxTrangThai.setValue(newSelection.getTrangThai());
                comBoxLoaiBan.setValue(newSelection.getLoaiBan());
            }
        });

        // Load dữ liệu bàn từ DB
        List<Ban> listBan = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .getDanhSach(Ban.class, null);

        // Đưa dữ liệu vào ObservableList và hiển thị lên TableView
        danhSachBan.setAll(listBan);
        tblBan.setItems(danhSachBan);
        txtMaBan.setText(sinhMaBan());
        
    }
    
    @FXML
    void controller(ActionEvent event) {
        if (event.getSource() == btnThem) {
            themBan();
        } else if (event.getSource() == btnSua) {
            suaBan();
        } else if (event.getSource() == btnXoa) {
            xoaBan();
        }
    }

    private void themBan() {
    	String maBan = txtMaBan.getText().trim();
        String viTri = txtViTri.getText().trim();
        String trangThai = comBoxTrangThai.getValue();
        LoaiBan loaiBan = comBoxLoaiBan.getValue(); // Lấy trực tiếp đối tượng

        if (maBan.isEmpty() || viTri.isEmpty() || trangThai == null || loaiBan == null) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
            return;
        }

        Ban ban = new Ban(maBan, viTri, trangThai, loaiBan);

        boolean check = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .them(ban);

        if (check) {
            danhSachBan.add(ban);
            tblBan.refresh();
            showAlert("Thành công", "Thêm bàn thành công!", Alert.AlertType.INFORMATION);
  
            resetForm();
        } else {
            showAlert("Lỗi", "Không thể thêm bàn. Mã bàn có thể đã tồn tại!", Alert.AlertType.ERROR);
        }


    }




    private void suaBan() {
        Ban banChon = tblBan.getSelectionModel().getSelectedItem();
        if (banChon == null) {
            showAlert("Thông báo", "Vui lòng chọn bàn cần sửa!", Alert.AlertType.WARNING);
            return;
        }

        String viTri = txtViTri.getText().trim();
        String trangThai = comBoxTrangThai.getValue();
        LoaiBan loaiBan = comBoxLoaiBan.getValue();

        if (viTri.isEmpty() || trangThai == null || loaiBan == null) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
            return;
        }

        // Cập nhật thông tin vào đối tượng
        banChon.setViTri(viTri);
        banChon.setTrangThai(trangThai);
        banChon.setLoaiBan(loaiBan);

        // Cập nhật vào database
        boolean check = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .sua(banChon); // giả sử Ban_DAO có phương thức sua()

        if (check) {
            tblBan.refresh(); // Làm mới TableView
            showAlert("Thành công", "Sửa bàn thành công!", Alert.AlertType.INFORMATION);
            resetForm(); // Reset form để nhập bàn khác
        } else {
            showAlert("Lỗi", "Không thể sửa bàn!", Alert.AlertType.ERROR);
        }
    }

    
    private void xoaBan() {
        Ban banChon = tblBan.getSelectionModel().getSelectedItem();
        if (banChon == null) {
            showAlert("Thông báo", "Vui lòng chọn bàn cần xóa!", Alert.AlertType.WARNING);
            return;
        }

        boolean check = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .xoa(banChon);

        if (check) {
            danhSachBan.remove(banChon);
            tblBan.refresh();
            showAlert("Thành công", "Xóa bàn thành công!", Alert.AlertType.INFORMATION);
            resetForm();
        } else {
            showAlert("Lỗi", "Không thể xóa bàn!", Alert.AlertType.ERROR);
        }
    }
    
    private void refreshTable() {
        List<Ban> list = RestaurantApplication.getInstance()
                .getDatabaseContext()
                .newEntity_DAO(Ban_DAO.class)
                .getDanhSach(Ban.class, null);

        danhSachBan.setAll(list);
        tblBan.setItems(danhSachBan);
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    private void resetForm() {
        // Tự sinh mã bàn mới
        txtMaBan.setText(sinhMaBan());
        
        // Xóa textField vị trí
        txtViTri.clear();
        
        // ComboBox trạng thái về mặc định
        comBoxTrangThai.setValue("Trống");
        
        // ComboBox loại bàn chưa chọn
        comBoxLoaiBan.getSelectionModel().clearSelection();
    }

}
    