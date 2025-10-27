package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExportExcelUtil {
	private static void setTableViewToExcel(TableView<?> tableView, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook(); // Tạo workbook Excel
        Sheet sheet = workbook.createSheet("Table Data"); // Tạo sheet Excel
        // Tạo một Font với kiểu chữ in đậm
        Row headerRow = sheet.createRow(0);
        ObservableList<? extends TableColumn<?, ?>> columns = tableView.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i).getText()); // Đặt tiêu đề là tên cột
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            cell.setCellStyle(style);
        }
        // Thêm dữ liệu từ TableView vào sheet
        ObservableList<?> items = tableView.getItems();
        for (int rowIndex = 0; rowIndex < items.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex + 1);
            Object rowData =  items.get(rowIndex);

            for (int colIndex = 0; colIndex < columns.size(); colIndex++) {
                TableColumn<?, ?> column = columns.get(colIndex);
                Object cellData = column.getCellObservableValue(rowIndex).getValue(); // Lấy dữ liệu từ TableView
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(cellData != null ? cellData.toString() : ""); // Chuyển dữ liệu thành chuỗi
            }
        }

        // Tự động căn chỉnh độ rộng cột
        for (int i = 0; i < columns.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi workbook ra file Excel
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            workbook.close();
            System.out.println("Xuất file Excel thành công!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }

    }

    public static void exportTableViewToExcel(TableView<?> tableView, Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file Excel");

        // Đặt bộ lọc cho loại file (chỉ cho phép lưu file Excel)
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );

        // Đặt tên mặc định cho file
        fileChooser.setInitialFileName("data.xlsx");

        // Hiển thị File Explorer
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            // Gọi hàm export dữ liệu ra file Excel
            setTableViewToExcel(tableView, file.getAbsolutePath());
            System.out.println("File đã được lưu tại: " + file.getAbsolutePath());
        } else {
            System.out.println("Người dùng đã hủy việc lưu file.");
        }
    }
}
