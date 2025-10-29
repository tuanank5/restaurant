-- Chèn dữ liệu vào bảng NhanVien
INSERT INTO [dbo].[NhanVien] (maNV, tenNV, chucVu, email, namSinh, diaChi, gioiTinh, ngayVaoLam, trangThai)
VALUES 
('NV001', N'Nguyễn Văn A', N'QL', 'nv.a@example.com', '1990-01-01', N'Số 1, Đường A, TP.HCM', 1, '2020-01-01', 1),
('NV002', N'Trần Thị B', N'NV', 'nv.b@example.com', '1992-02-02', N'Số 2, Đường B, Hà Nội', 0, '2021-02-01', 1),
('NV003', N'Lê Văn C', N'NV', 'nv.c@example.com', '1995-03-03', N'Số 3, Đường C, Đà Nẵng', 1, '2019-03-01', 1);

-- Chèn dữ liệu vào bảng TaiKhoan
INSERT INTO [dbo].[TaiKhoan] (maTaiKhoan, tenTaiKhoan, matKhau, ngayDangNhap, ngayDangXuat, ngaySuaDoi, maNV)
VALUES 
('TK001', 'sa', '1', '2023-10-01', '2023-10-02', '2023-10-03', 'NV001'),
('TK002', 'nv', '1', '2023-10-01', '2023-10-02', '2023-10-03', 'NV002'),
('TK003', 'userC', 'passwordC', '2023-10-01', '2023-10-02', '2023-10-03', 'NV003');


--Chèn dữ liệu vào bảng HangKhachHang
INSERT INTO HangKhachHang (maHang, tenHang, diemHang, giamGia, moTa) VALUES
('H1', N'Hang Dong', 0, 0.00, N'Khách hàng mới, chưa có ưu đãi'),
('H2', N'Hang Bac', 1000, 5.00, N'Khách hàng thân thiết, được giảm 5%'),
('H3', N'Hang Vang', 3000, 10.00, N'Khách hàng VIP, giảm 10% trên hóa đơn');

--Chèn dữ liệu vào bảng HangKhachHang
INSERT INTO KhachHang (maKH, tenKH, sdt, email, diaChi, diemTichLuy, maHang) VALUES
('KH001', N'Nguyễn Văn An', '0901234567', 'an.nguyen@example.com', N'123 Trần Hưng Đạo, Q1, TP.HCM', 500, 'H1'),
('KH002', N'Lê Thị Bình', '0912345678', 'binh.le@example.com', N'45 Nguyễn Huệ, Q1, TP.HCM', 1200, 'H2'),
('KH003', N'Trần Quốc Cường', '0987654321', 'cuong.tran@example.com', N'56 Lê Lợi, Q3, TP.HCM', 3500, 'H3');

--Chèn dữ liệu cho loaiBan
INSERT INTO [dbo].[LoaiBan] (maLoaiBan, tenLoaiBan, soLuong) VALUES
('LB01', 'Bàn nhỏ', 10),
('LB02', 'Bàn thường', 20),
('LB03', 'Bàn lớn', 15);

-- Thêm dữ liệu cho Ban
INSERT INTO [dbo].[Ban] (maBan, viTri, trangThai, maLoaiBan) VALUES
('B01', '1', 1, 'LB01'),
('B02', '2', 0, 'LB01'),
('B03', '3', 1, 'LB02'),
('B04', '4', 0, 'LB02'),
('B05', '5', 1, 'LB03'),
('B06', '6', 0, 'LB03'),
('B07', '7', 1, 'LB02'),
('B08', '8', 1, 'LB01'),
('B09', '9', 0, 'LB03'),
('B10', '10', 1, 'LB02');

INSERT INTO KhuyenMai (maKM, tenKM, loaiKM, sanPhamKM, ngayBatDau, ngayKetThuc, phanTramGiamGia)
VALUES
('KM001', N'Giảm 1%', N'Theo %', N'SP', '2025-01-01', '2025-01-15', 1),
('KM002', N'Giảm 2%', N'Theo %', N'SP', '2025-02-01', '2025-02-10', 2),
('KM003', N'Giảm 3%', N'Theo %', N'SP', '2025-11-25', '2025-11-30', 3);
