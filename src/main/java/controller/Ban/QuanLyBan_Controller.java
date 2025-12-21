package controller.Ban;

import java.util.List;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
import util.AutoIDUitl;


public class QuanLyBan_Controller {

    private MenuNV_Controller menuController; // Reference đến MenuNV_Controller

    public void setMenuController(MenuNV_Controller menuController) {
        this.menuController = menuController;
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField txtTimKiem;
    
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
    private TableColumn<Ban, String> colViTri;

    @FXML
    private TableView<Ban> tblBan;
    
    @FXML
    private TextField txtMaBan;

    @FXML
    private TextField txtViTri;

    private ObservableList<Ban> dsBan; 

    @FXML
    private ComboBox<LoaiBan> comBoxLoaiBan; 


    private ObservableList<Ban> danhSachBan = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        // Thiết lập các cột TableView
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));
        colViTri.setCellValueFactory(new PropertyValueFactory<>("viTri"));
        colMaLoaiBan.setCellValueFactory(cellData -> {
            LoaiBan loaiBan = cellData.getValue().getLoaiBan();
            String tenLoai = (loaiBan != null) ? loaiBan.getTenLoaiBan() : "";
            return new SimpleStringProperty(tenLoai);
        });
        
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
        // Mặc định disable nút Sửa/Xóa
        btnSua.setDisable(true);
        btnXoa.setDisable(true);
        //Sửa
        tblBan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
            	// Có dòng được chọn → enable nút Sửa/Xóa
                btnSua.setDisable(false);
                btnXoa.setDisable(false);
                // Điền dữ liệu vào các TextField và ComboBox
                txtMaBan.setText(newSelection.getMaBan()); // Mã bàn vẫn giữ, không sửa
                txtViTri.setText(newSelection.getViTri());
                
                if (newSelection.getLoaiBan() != null) {
                    // tìm loại bàn trong comboBox
                    for (LoaiBan lb : comBoxLoaiBan.getItems()) {
                        if (lb.getMaLoaiBan().equals(newSelection.getLoaiBan().getMaLoaiBan())) {
                            comBoxLoaiBan.setValue(lb);
                            break;
                        }
                    }
                }

            }else {
                // Không có dòng nào được chọn → disable nút Sửa/Xóa
                btnSua.setDisable(true);
                btnXoa.setDisable(true);

                // Reset form
                resetForm();
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
        txtMaBan.setText(AutoIDUitl.sinhMaBan());
        txtTimKiem.setOnKeyReleased(event -> {
            String keyword = txtTimKiem.getText().trim();
            timKiemBan(keyword);
        });
        
        btnSua.setTooltip(new Tooltip("Sửa thông tin bàn đã chọn"));
        btnThem.setTooltip(new Tooltip("Thêm bàn mới vào danh sách"));
        btnXoa.setTooltip(new Tooltip("Xóa bàn đã chọn"));
        txtMaBan.setTooltip(new Tooltip("Nhập mã bàn"));
        txtTimKiem.setTooltip(new Tooltip("Nhập mã bàn hoặc vị trí để tìm kiếm"));
        txtViTri.setTooltip(new Tooltip("Nhập vị trí của bàn trong nhà hàng"));
        comBoxLoaiBan.setTooltip(new Tooltip("Chọn loại bàn (nhỏ, thường, lớn)"));

    }
    
    private void timKiemBan(String keyword) {
        String kw = keyword.toLowerCase();

        if (kw.isEmpty()) {
            tblBan.setItems(danhSachBan); // reset danh sách gốc
            return;
        }

        ObservableList<Ban> kq = FXCollections.observableArrayList();

        for (Ban b : danhSachBan) {
            boolean matchMa = b.getMaBan().toLowerCase().contains(kw);
            boolean matchViTri = b.getViTri().toLowerCase().contains(kw);
            boolean matchLoai = b.getLoaiBan() != null &&
                    b.getLoaiBan().getTenLoaiBan().toLowerCase().contains(kw);

            if (matchMa || matchViTri || matchLoai) {
                kq.add(b);
            }
        }
        tblBan.setItems(kq);
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
        LoaiBan loaiBan = comBoxLoaiBan.getValue(); // Lấy trực tiếp đối tượng

        if (maBan.isEmpty() || viTri.isEmpty() || loaiBan == null) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
            return;
        }

        String trangThaiMacDinh = "Trống";

        Ban ban = new Ban(maBan, viTri, trangThaiMacDinh, 0, loaiBan);


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
        LoaiBan loaiBan = comBoxLoaiBan.getValue();

        if (viTri.isEmpty() || loaiBan == null) {
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!", Alert.AlertType.WARNING);
            return;
        }

        // Cập nhật thông tin vào đối tượng
        banChon.setViTri(viTri);
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
    	txtMaBan.setText(AutoIDUitl.sinhMaBan());
        
        // Xóa textField vị trí
        txtViTri.clear();
        
        // ComboBox loại bàn chưa chọn
        comBoxLoaiBan.getSelectionModel().clearSelection();
    }

}
    