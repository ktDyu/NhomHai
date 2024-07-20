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
);

-- Create table HoaDon
CREATE TABLE HoaDon (
    ID INT PRIMARY KEY IDENTITY,
    ID_NhanVien INT,
    ID_KhachHang INT,
    MaHoaDon VARCHAR(40),
    TongTien DECIMAL(18, 2),
	DiaChi NVARCHAR(40),
    NgayThanhToan DATE,
    TrangThai BIT,
    MoTa NVARCHAR(255),
    FOREIGN KEY (ID_NhanVien) REFERENCES NhanVien(ID),
    FOREIGN KEY (ID_KhachHang) REFERENCES KhachHang(ID)
);


-- Create table Size
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
    MoTa VARCHAR(50),
    DonGia DECIMAL(18, 2),
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
    DonGia DECIMAL(18, 2),
    FOREIGN KEY (ID_SanPhamChiTiet) REFERENCES SanPhamChiTiet(ID),
    FOREIGN KEY (ID_HoaDon) REFERENCES HoaDon(ID)
);
GO


-- Insert data into ChucVu
INSERT INTO ChucVu (TenChucVu) VALUES
('Manager'),
('Cashier'),
('Salesperson'),
('Stock Clerk'),
('Customer Service');

-- Insert data into NhanVien
INSERT INTO NhanVien (MaNhanVien, HoTen, SDT, NgaySinh, GioiTinh, DiaChi, Anh, TrangThai, TaiKhoan, MatKhau, ID_ChucVu) VALUES
('NV001', N'Nguyen Van A', '0901234567', '1985-01-01', 1, N'Hanoi', 'image1.jpg', N'Active', 'nguyenvana', 'password1', 1),
('NV002', N'Tran Thi B', '0902345678', '1990-02-01', 0, N'Ho Chi Minh', 'image2.jpg', N'Active', 'tranthib', 'password2', 2),
('NV003', N'Le Van C', '0903456789', '1995-03-01', 1, N'Da Nang', 'image3.jpg', N'Active', 'levanc', 'password3', 3),
('NV004', N'Pham Thi D', '0904567890', '2000-04-01', 0, N'Hue', 'image4.jpg', N'Active', 'phamthid', 'password4', 4),
('NV005', N'Hoang Van E', '0905678901', '1980-05-01', 1, N'Can Tho', 'image5.jpg', N'Active', 'hoangvane', 'password5', 5);

-- Insert data into KhachHang
INSERT INTO KhachHang (MaKhachHang, HoTen, SDT, DiaChi, NgaySinh, GioiTinh) VALUES
('KH001', N'Nguyen Thi F', '0911234567', N'Hanoi', '1985-06-01', 0),
('KH002', N'Tran Van G', '0912345678', N'Ho Chi Minh', '1990-07-01', 1),
('KH003', N'Le Thi H', '0913456789', N'Da Nang', '1995-08-01', 0),
('KH004', N'Pham Van I', '0914567890', N'Hue', '2000-09-01', 1),
('KH005', N'Hoang Thi J', '0915678901', N'Can Tho', '1980-10-01', 0);

-- Insert data into HoaDon
INSERT INTO HoaDon (ID_NhanVien, ID_KhachHang, MaHoaDon, TongTien, NgayThanhToan, TrangThai, MoTa) VALUES
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
('Action'),
('Drama'),
('Comedy'),
('Horror'),
('Sci-Fi');

-- Insert data into MauSac
INSERT INTO MauSac (TenMauSac) VALUES
(N'Red'),
(N'Blue'),
(N'Green'),
(N'Yellow'),
(N'Black');

-- Insert data into ChatLieu
INSERT INTO ChatLieu (TenChatLieu) VALUES
(N'Cotton'),
(N'Polyester'),
(N'Silk'),
(N'Denim'),
(N'Wool');

-- Insert data into SanPhamChiTiet
INSERT INTO SanPhamChiTiet (ID_TheLoai, ID_ChatLieu, ID_MauSac, ID_KichCo,TenSP, MaSP, Anh, MoTa, DonGia, SoLuong) VALUES
(1, 1, 1, 1, 'SP001',  'San Pham 1','', N'Sample Product 1', 100000.00, 10),
(1, 1, 1, 1, 'SP001',  'San Pham 1','product1.jpg', N'Sample Product 1', 100000.00, 10),
(2, 2, 2, 2, 'SP002', 'San Pham 2', 'product2.jpg', N'Sample Product 2', 200000.00, 20),
(3, 3, 3, 3, 'SP003', 'San Pham 3', 'product3.jpg', N'Sample Product 3', 300000.00, 30),
(4, 4, 4, 4, 'SP004', 'San Pham 4', 'product4.jpg', N'Sample Product 4', 400000.00, 40),
(5, 5, 5, 5, 'SP005', 'San Pham 5',  'product5.jpg', N'Sample Product 5', 500000.00, 50);

ALTER TABLE SanPhamChiTiet
DROP COLUMN TrangThai
-- Insert data into HoaDonChiTiet
INSERT INTO HoaDonChiTiet (ID_SanPhamChiTiet, ID_HoaDon, SoLuong, DonGia) VALUES
(1, 1, 1, 100000.00),
(2, 2, 2, 200000.00),
(3, 3, 3, 300000.00),
(4, 4, 4, 400000.00),
(5, 5, 5, 500000.00);


select MaSP,TenSP,Anh,MoTa,DonGia,SoLuong,b.TenTheLoai as TheLoai, c.TenChatLieu as ChatLieu, d.TenMauSac as MauSac, e.TenKichCo as KichCo   from SanPhamChiTiet a  
inner join TheLoai b on  a.ID_TheLoai = b.ID 
inner join ChatLieu c on a.ID_ChatLieu = c.ID 
inner join MauSac d on a.ID_MauSac = d.ID 
inner join KichCo e on a.ID_KichCo = e.ID

INSERT INTO SanPhamChiTiet(ID_TheLoai, ID_ChatLieu, ID_MauSac, ID_KichCo, MaSP,TenSP,Anh,MoTa,DonGia,SoLuong) VALUES (?,?,?,?,?,?,?,?,?,?)

SELECT COUNT(*) AS total FROM SanPhamChiTiet

delete from SanPhamChiTiet where ID = ?














