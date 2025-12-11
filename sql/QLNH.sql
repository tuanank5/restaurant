CREATE DATABASE QLNH

USE QLNH

CREATE TABLE LoaiBan
(
	maLoaiBan VARCHAR(20) PRIMARY KEY,
	tenLoaiBan NVARCHAR(50),
	soLuong INT CHECK (soLuong >= 0)
);

CREATE TABLE Ban
(
	maBan VARCHAR(20) PRIMARY KEY,
	viTri NVARCHAR(100),
	trangThai NVARCHAR(50),
	maLoaiBan VARCHAR(20) REFERENCES LoaiBan(maLoaiBan) ON DELETE CASCADE 
);



CREATE TABLE Coc
(
	maCoc VARCHAR(20) PRIMARY KEY,
	loaiCoc BIT,
	phanTramCoc INT CHECK (phanTramCoc BETWEEN 0 AND 100),
	soTienCoc DECIMAL(10,2) CHECK (soTienCoc > 0)
);

CREATE TABLE HangKhachHang
(
	maHang VARCHAR(20) PRIMARY KEY,
	tenHang NVARCHAR(100),
	diemHang INT CHECK (diemHang >= 0),
	giamGia DECIMAL(5,2) CHECK (giamGia BETWEEN 0 AND 100),
	moTa NVARCHAR(200)
);

CREATE TABLE KhachHang
(
	maKH VARCHAR(20) PRIMARY KEY,
	tenKH NVARCHAR(100) NOT NULL,
	sdt VARCHAR(20),
	email VARCHAR(100) NULL UNIQUE,
	diaChi NVARCHAR(200),
	diemTichLuy INT DEFAULT 0 CHECK (diemTichLuy >= 0),
	maHang VARCHAR(20) NULL,
	FOREIGN KEY (maHang) REFERENCES HangKhachHang(maHang) ON DELETE SET NULL
);

CREATE TABLE DonDatBan
(
	maDatBan VARCHAR(20) PRIMARY KEY,
	ngayGio DATETIME DEFAULT GETDATE(),
	soLuong INT CHECK (soLuong > 0),
	maBan VARCHAR(20) REFERENCES Ban(maBan) ON DELETE CASCADE,
	gioBatDau TIME NOT NULL,
	trangThai NVARCHAR(20)
);
 
CREATE TABLE DonLapDoiHuyBan
(
	maDatBan VARCHAR(20) PRIMARY KEY,
	lyDo NVARCHAR(200),
	ngayGioLapDon DATETIME DEFAULT GETDATE(),
	tienHoanTra DECIMAL(10,2) CHECK (tienHoanTra > 0),
	FOREIGN KEY (maDatBan) REFERENCES DonDatBan(maDatBan) ON DELETE CASCADE
);


CREATE TABLE KhuyenMai
(
	maKM VARCHAR(20) PRIMARY KEY,
	tenKM NVARCHAR(100),
	loaiKM NVARCHAR(50),
	sanPhamKM NVARCHAR(50),
	ngayBatDau DATE,
	ngayKetThuc DATE,
	phanTramGiamGia INT CHECK (phanTramGiamGia BETWEEN 0 AND 100)
);

CREATE TABLE MonAn
(
	maMon VARCHAR(20) PRIMARY KEY,
	tenMon NVARCHAR(100),
	donGia DECIMAL(10,2) CHECK (donGia >= 0),
	maKM VARCHAR(20) NULL REFERENCES KhuyenMai(maKM),
	duongDanAnh NVARCHAR(255) NULL,
	loaiMon NVARCHAR(100) NULL
);

CREATE TABLE NhanVien
(
	maNV VARCHAR(20) PRIMARY KEY,
	tenNV NVARCHAR(100) NOT NULL,
	chucVu NVARCHAR(50),
	email VARCHAR(100) UNIQUE,
	namSinh DATE,
	diaChi NVARCHAR(200),
	gioiTinh BIT,
	ngayVaoLam DATE,
	trangThai BIT DEFAULT 1
);

CREATE TABLE HoaDon
(
	maHD VARCHAR(20) PRIMARY KEY,
	ngayLap DATE DEFAULT GETDATE(),
	tongTien DECIMAL(12,2) DEFAULT 0 CHECK (tongTien >= 0),
	thue DECIMAL(12,2) DEFAULT 0 CHECK (thue >= 0),
	trangThai NVARCHAR(50),
	kieuThanhToan NVARCHAR(50),
	tienNhan DECIMAL(12,2) DEFAULT 0 CHECK(tienNhan >= 0),
	tienThua DECIMAL(12,2) DEFAULT 0 CHECK(tienThua >= 0),
	maKH VARCHAR(20) NOT NULL REFERENCES KhachHang(maKH) ON DELETE CASCADE,
	maKM VARCHAR(20) NULL REFERENCES KhuyenMai(maKM) ON DELETE CASCADE,
	maNV VARCHAR(20) NOT NULL REFERENCES NhanVien(maNV) ON DELETE CASCADE,
	maDatBan VARCHAR(20),
	maCoc VARCHAR(20) NULL REFERENCES Coc(maCoc) ON DELETE CASCADE,
	FOREIGN KEY (maDatBan) REFERENCES DonDatBan(maDatBan)
);

CREATE TABLE ChiTietHoaDon
(
	maHD VARCHAR(20),
	maMon VARCHAR(20),
	soLuong INT CHECK (soLuong > 0),
	thanhTien DECIMAL(12,2) CHECK (thanhTien >= 0), -- ko có
	PRIMARY KEY (maHD, maMon),
	FOREIGN KEY (maHD) REFERENCES HoaDon(maHD) ON DELETE CASCADE,
	FOREIGN KEY (maMon) REFERENCES MonAn(maMon) ON DELETE CASCADE
);


CREATE TABLE TaiKhoan
(
	maTaiKhoan VARCHAR(20) PRIMARY KEY,
	tenTaiKhoan VARCHAR(20),
	matKhau VARCHAR(20),
	ngayDangNhap DATE,
	ngayDangXuat DATE,
	ngaySuaDoi DATE,
	maNV VARCHAR(20) REFERENCES NhanVien(maNV) ON DELETE CASCADE
);

CREATE TABLE CaLamViec
(
	maCa VARCHAR(20) PRIMARY KEY,
	gioMoCa DATE CHECK (gioMoCa >= GETDATE()),
	gioKetCa DATE CHECK (gioKetCa <= GETDATE()),
	ghiChu NVARCHAR(50)
);

CREATE TABLE PhanCong
(
	maCa VARCHAR(20),
	maNV VARCHAR(20),
	ngayLam DATE CHECK (ngayLam >= GETDATE()),
	PRIMARY KEY (maCa, maNV),
	FOREIGN KEY (maCa) REFERENCES CaLamViec(maCa) ON DELETE CASCADE,
	FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE
);
