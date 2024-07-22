/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.KhachHang;

/**
 *
 * @author my account
 */
public class KhachHang {

    private Integer idKH;
    private String maKhachHang;
    private String hoTen;
    private String SDT;
    private String ngaySinh;
    private boolean gioiTinh;

    private String DiaChi;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String hoTen, String SDT, String ngaySinh, boolean gioiTinh, String DiaChi) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.SDT = SDT;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.DiaChi = DiaChi;
    }

    public KhachHang(Integer idKH, String maKhachHang, String hoTen, String SDT, String ngaySinh, boolean gioiTinh, String DiaChi) {
        this.idKH = idKH;
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.SDT = SDT;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.DiaChi = DiaChi;
    }

    public Integer getIdKH() {
        return idKH;
    }

    public void setIdKH(Integer idKH) {
        this.idKH = idKH;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    @Override
    public String toString() {
        return "KhachHang{" + "idKH=" + idKH + ", maKhachHang=" + maKhachHang + ", hoTen=" + hoTen + ", SDT=" + SDT + ", ngaySinh=" + ngaySinh + ", gioiTinh=" + gioiTinh + ", DiaChi=" + DiaChi + '}';
    }

}
