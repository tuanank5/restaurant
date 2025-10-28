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
INSERT INTO HangKhachHang (maHang, tenHang, diemHang, giamGia, moTa)
VALUES
('H1', N'Thường', 0, 0, N'Hạng thường - không giảm giá'),
('H2', N'Bạc', 100, 5, N'Hạng Bạc - giảm 5%'),
('H3', N'Vàng', 300, 10, N'Hạng Vàng - giảm 10%'),
('H4', N'Kim Cương', 600, 15, N'Hạng Kim Cương - giảm 15%');

--Chèn dữ liệu vào bảng HangKhac
INSERT INTO KhachHang (maKH, tenKH, sdt, diaChi, maHang, diemTichLuy)
VALUES ('KH001', N'Nguyễn Văn A', '0901111111', N'123 Lê Lợi, TP.HCM', 'H1', 50);
