-- Chèn dữ liệu vào bảng NhanVien
INSERT INTO [dbo].[NhanVien] (maNV, tenNV, chucVu, email, namSinh, diaChi, gioiTinh, ngayVaoLam, trangThai)
VALUES 
('NV001', N'Nguyễn Văn A', N'Quản lý', 'nv.a@example.com', '1990-01-01', N'Số 1, Đường A, TP.HCM', 1, '2020-01-01', 1),
('NV002', N'Trần Thị B', N'Nhân viên', 'nv.b@example.com', '1992-02-02', N'Số 2, Đường B, Hà Nội', 0, '2021-02-01', 1),
('NV003', N'Lê Văn C', N'Technician', 'nv.c@example.com', '1995-03-03', N'Số 3, Đường C, Đà Nẵng', 1, '2019-03-01', 1);

-- Chèn dữ liệu vào bảng TaiKhoan
INSERT INTO [dbo].[TaiKhoan] (maTaiKhoan, tenTaiKhoan, matKhau, ngayDangNhap, ngayDangXuat, ngaySuaDoi, maNV)
VALUES 
('TK001', 'userA', 'passwordA', '2023-10-01', '2023-10-02', '2023-10-03', 'NV001'),
('TK002', 'userB', 'passwordB', '2023-10-01', '2023-10-02', '2023-10-03', 'NV002'),
('TK003', 'userC', 'passwordC', '2023-10-01', '2023-10-02', '2023-10-03', 'NV003');