CREATE DATABASE QLBA
go
-- Create table ChucVu
CREATE TABLE ChucVu (
    ID INT PRIMARY KEY IDENTITY,
    TenChucVu NVARCHAR(50)
);

-- Create table NhanVien
CREATE TABLE NhanVien (
    ID INT PRIMARY KEY IDENTITY,
    MaNhanVien VARCHAR(20),
    HoTen NVARCHAR(100),
    SDT VARCHAR(20),
    NgaySinh DATE,
    GioiTinh BIT,
	DiaChi NVARCHAR(50),
    Anh VARCHAR(40),
    TrangThai NVARCHAR(50),
    TaiKhoan VARCHAR(50),
    MatKhau VARCHAR(50),
    ID_ChucVu INT,
    FOREIGN KEY (ID_ChucVu) REFERENCES ChucVu(ID)
);

-- Create table KhachHang
CREATE TABLE KhachHang (
    ID INT PRIMARY KEY IDENTITY,
    MaKhachHang VARCHAR(50),
    HoTen NVARCHAR(100),
    SDT VARCHAR(20),
	DiaChi NVARCHAR(50),
    NgaySinh DATE,
    GioiTinh BIT,
	TrangThai BIT,
);

-- Create table HoaDon
CREATE TABLE HoaDon (
    ID INT PRIMARY KEY IDENTITY,
    ID_NhanVien INT,
    ID_KhachHang INT,
    MaHoaDon VARCHAR(40),
    TongTien DECIMAL(18, 2),
    NgayThanhToan DATE,
    TrangThai bit,
    MoTa NVARCHAR(255),
    FOREIGN KEY (ID_NhanVien) REFERENCES NhanVien(ID),
    FOREIGN KEY (ID_KhachHang) REFERENCES KhachHang(ID)
);


-- Create table KichCo
CREATE TABLE KichCo (
    ID INT PRIMARY KEY IDENTITY,
    TenKichCo VARCHAR(50)
);

-- Create table TheLoai
CREATE TABLE TheLoai (
    ID INT PRIMARY KEY IDENTITY,
    TenTheLoai NVARCHAR(100)
);
-- Create table MauSac
CREATE TABLE MauSac (
    ID INT PRIMARY KEY IDENTITY,
    TenMauSac NVARCHAR(100)
);
-- Create table ChatLieu
CREATE TABLE ChatLieu (
    ID INT PRIMARY KEY IDENTITY,
    TenChatLieu NVARCHAR(100)
);


-- Create table SanPhamChiTiet
CREATE TABLE SanPhamChiTiet (
    ID INT PRIMARY KEY IDENTITY,
    ID_TheLoai INT,
    ID_ChatLieu INT,
    ID_MauSac INT,
    ID_KichCo INT,
	MaSP VARCHAR(30),
	TenSP NVARCHAR(50),
    Anh VARCHAR(50),
    MoTa NVARCHAR(50),
    DonGia Float,
    SoLuong INT,
    FOREIGN KEY (ID_TheLoai) REFERENCES TheLoai(ID),
    FOREIGN KEY (ID_MauSac) REFERENCES MauSac(ID),
	FOREIGN KEY (ID_ChatLieu) REFERENCES ChatLieu(ID),
    FOREIGN KEY (ID_KichCo) REFERENCES KichCo(ID)
);
-- Create table HoaDonChiTiet
CREATE TABLE HoaDonChiTiet (
    ID INT PRIMARY KEY IDENTITY,
    ID_SanPhamChiTiet INT,
    ID_HoaDon INT,
    SoLuong INT,
    DonGia Float,
    FOREIGN KEY (ID_SanPhamChiTiet) REFERENCES SanPhamChiTiet(ID),
    FOREIGN KEY (ID_HoaDon) REFERENCES HoaDon(ID)
);
GO


-- Insert data into ChucVu
INSERT INTO ChucVu (TenChucVu) VALUES
(N'Quản Lý'),
(N'Nhân Viên')

-- Insert data into NhanVien
INSERT INTO NhanVien (MaNhanVien, HoTen, SDT, NgaySinh, GioiTinh, DiaChi, Anh, TrangThai, TaiKhoan, MatKhau, ID_ChucVu) VALUES
('NV001', N'Nguyễn Văn An', '0901234567', '1985-01-01', 1, N'Hà Nội', 'image1.jpg', N'Active', 'nguyenvana', 'password1', 1),
('NV002', N'Trần Thị Bình', '0902345678', '1990-02-01', 0, N'Hồ Chí Minh', 'image2.jpg', N'Active', 'tranthib', 'password2', 2),
('NV003', N'Lê Văn Cảnh', '0903456789', '1995-03-01', 1, N'Đà Nẵng', 'image3.jpg', N'Active', 'levancanh', 'password3', 3),
('NV004', N'Phạm Thi Dung', '0904567890', '2000-04-01', 0, N'Huế', 'image4.jpg', N'No Active', 'phamthidung', 'password4', 4),
('NV005', N'Nguyễn Quốc Đạt', '0905678901', '1980-05-01', 1, N'Cần Thơ', 'image5.jpg', N'Active', 'nguyenquocdat', 'password5', 5),
('NV006', N'Hoàng Văn Em', '0345127965', '1984-10-01', 1, N'Hà Nội', 'image6.jpg', N'Active', 'hoangvanem', 'vanem1984', 2),
('NV007', N'Võ Phúc Anh', '0910232567', '1995-09-05', 1, N'Hà Nội', 'image9.jpg', N'No Active', 'vophucanh', 'phucanh1995', 1),
('NV008', N'Vũ Văn Trường', '0758694038', '1992-12-11', 1, N'Hồ Chí Minh', 'img11.jpg', N'No Active', 'vuvantruong', 'truongvv92', 1),
('NV009', N'Nguyễn Thùy Linh', '0390675894', '2000-08-16', 0, N'Cần Thơ', 'image7.jpg', N'Active', 'thuylinhng', 'linhnt2000', 2),
('NV010', N'Lê Văn Đạt', '0849056432', '1989-03-07', 1, N'Đà Nẵng', 'img22.jpg', N'Active', 'levandat', 'datle0307', 3),
('NV011', N'Nguyễn Quang Anh', '0323978605', '1999-10-12', 1, N'Hà Nam', 'img23.jpg', N'Active', 'nguyenquanganh', 'quanganh99!', 4),
('NV012', N'Phạm Văn Đồng', '0586970654', '2000-09-12', 1, N'Hà Nam', 'img24.jpg', N'No Active', 'phamvandong', 'dongpham12', 3),
('NV013', N'Nguyễn Thanh Chúc', '0167059456', '1982-05-11', 0, N'Huế', 'img8.jpg', N'Active', 'thanhchucc', 'chucnguyen82', 1),
('NV014', N'Nguyễn Tiến Dũng', '0259067432', '1993-02-20', 1, N'Đà Nẵng', 'img21.jpg', N'No Active', 'nguyendung', 'dung19933', 5),
('NV015', N'Nguyễn Gia Hân', '0789605432', '1997-04-01', 0, N'Đà Nẵng', 'img14.jpg', N'Active', 'hannguyen', 'hannie97', 2),
('NV016', N'Lê Đức Thiện', '0804395768', '2001-01-02', 1, N'Hà Nội', 'img20.jpg', N'No Active', 'leducthien', 'thienle2001', 3),
('NV017', N'Trần Thị Trang', '0690865748', '1986-11-03', 0, N'Hồ Chí Minh', 'img15.jpg', N'Active', 'trantrangg', 'trangtran03#', 4),
('NV018', N'Phan Đức Tài', '0567908432', '1991-06-09', 1, N'Cần Thơ', 'img19.jpg', N'No Active', 'phanductai', 'taiphanduc', 1),
('NV019', N'Trần Thị Quỳnh', '0795068432', '1997-01-03', 0, N'Hà Nội', 'img18.jpg', N'Active', 'tranthiquynh', 'quynhtran97', 2),
('NV020', N'Nguyễn Tấn Việt', '0290453652', '1989-03-07', 1, N'Hồ Chí Minh', 'img17.jpg', N'No Active', 'nguyentanviet', 'vietnguyen89', 3),
('NV021', N'Nguyễn Quỳnh Hoa', '0844900976', '1998-11-12', 0, N'Huế', 'imagenu.jpg', N'Active', 'quynhhoaaa', 'hoanguyen1112', 4),
('NV022', N'Mai Đức Trọng', '0980342456', '2000-05-09', 1, N'Hà Nam', 'img16.jpg', N'No Active', 'maiductrong', 'trongmai09', 5)

-- Insert data into KhachHang
INSERT INTO KhachHang (MaKhachHang, HoTen, SDT, DiaChi, NgaySinh, GioiTinh,TrangThai)
 VALUES('KH001', N'Nguyễn Thị Ngọc Mai', '0911234567', N'Hà Nội', '2005-06-01', 0,1),
       ('KH002', N'Vũ Thị Lan Anh', '0912345678', N'Hồ Chí Minh', '2003-07-01', 0,1),
       ('KH003', N'Bùi Thị Mỵ','0913456789', N'Đà Nẵng', '1999-08-01', 0,1),
       ('KH004', N'Phạm Văn Đông', '0914567890', N'Huế', '2000-09-01', 1,1),
       ('KH005', N'Nguyễn Minh Hoàng', '0915678901', N'Cần Thơ', '2002-10-01', 1,1);


-- Insert data into HoaDon
INSERT INTO HoaDon (ID_NhanVien, ID_KhachHang, MaHoaDon, TongTien, NgayTao, TrangThai, MoTa) VALUES
(1, 1, 'HD001', 100000.00, '2024-01-01', 1, N'Paid'),
(2, 2, 'HD002', 200000.00, '2024-02-01', 1, N'Paid'),
(3, 3, 'HD003', 300000.00, '2024-03-01', 1, N'Paid'),
(4, 4, 'HD004', 400000.00, '2024-04-01', 1, N'Paid'),
(5, 5, 'HD005', 500000.00, '2024-05-01', 1, N'Paid');

-- Insert data into Size
INSERT INTO KichCo (TenKichCo) VALUES
('Small'),
('Medium'),
('Large'),
('X-Large'),
('XX-Large');

-- Insert data into TheLoai
INSERT INTO TheLoai (TenTheLoai) VALUES
(N'Áo phông cổ tròn'),
(N'Áo phông thể thao'),
(N'Áo phông có cổ'),
(N'Áo phông dài tay')

-- Insert data into MauSac
INSERT INTO MauSac (TenMauSac) VALUES
(N'Đỏ'),
(N'Xanh Nước Biển'),
(N'Xanh Lục'),
(N'Vàng'),
(N'Đen');

-- Insert data into ChatLieu
INSERT INTO ChatLieu (TenChatLieu) VALUES
(N'Cotton'),
(N'Polyester'),
(N'Silk'),
(N'Denim'),
(N'Wool');

-- Insert data into SanPhamChiTiet
INSERT INTO SanPhamChiTiet (ID_TheLoai, ID_ChatLieu, ID_MauSac, ID_KichCo, MaSP, TenSP, Anh, MoTa, DonGia, SoLuong)
VALUES
(1, 1, 1, 1, 'SP001', N'Áo Phông Trắng Cổ Tròn', '1.jpg', N'Áo phông trắng cổ tròn', 150000, 50),
(1, 2, 2, 2, 'SP002', N'Áo Phông Đen Cổ Tròn', 'ao_phong_den_co_tron.jpg', N'Áo phông đen cổ tròn', 160000, 40),
(1, 1, 3, 3, 'SP003', N'Áo Phông Xanh Cổ Trụ', 'ao_phong_xanh_co_tru.jpg', N'Áo phông xanh cổ trụ', 170000, 30),
(1, 3, 1, 4, 'SP004', N'Áo Phông Đỏ Cổ V', 'ao_phong_do_co_v.jpg', N'Áo phông đỏ cổ V', 180000, 20),
(1, 2, 4, 1, 'SP005', N'Áo Phông Vàng Cổ Tròn', 'ao_phong_vang_co_tron.jpg', N'Áo phông vàng cổ tròn', 155000, 25);

-- Insert data into HoaDonChiTiet
INSERT INTO HoaDonChiTiet (ID_SanPhamChiTiet, ID_HoaDon, SoLuong, DonGia) VALUES
(1, 1, 1, 100000),
(2, 2, 2, 200000),
(3, 3, 3, 300000),
(4, 4, 4, 400000),
(5, 5, 5, 500000);


