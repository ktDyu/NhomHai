/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.KhachHang;

import util.DBcontext;
import entity.KhachHang.KhachHang;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author my account
 */
public class KhachHangDAO {

    public ArrayList<KhachHang> getAll() {
        String sql = """
                   SELECT [ID]
                         ,[MaKhachHang]
                         ,[HoTen]
                         ,[SDT]
                         ,[DiaChi]
                         ,[NgaySinh]
                         ,[GioiTinh]
                     FROM [dbo].[KhachHang]
                   """;
        ArrayList<KhachHang> list = new ArrayList<>();
        try (Connection con = DBcontext.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setIdKH(rs.getInt(1));
                kh.setMaKhachHang(rs.getString(2));
                kh.setHoTen(rs.getString(3));
                kh.setSDT(rs.getString(4));
                kh.setDiaChi(rs.getString(5));
                kh.setNgaySinh(rs.getString(6));
                kh.setGioiTinh(rs.getBoolean(7));
                list.add(kh);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean add(KhachHang kh) {
        String sql = """
                   INSERT INTO [dbo].[KhachHang]
                              ([MaKhachHang]
                              ,[HoTen]
                              ,[SDT]
                              ,[DiaChi]
                              ,[NgaySinh]
                              ,[GioiTinh])
                        VALUES
                              (?,?,?,?,?,?)
                   """;
        int check = 0;
        try (Connection con = DBcontext.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, kh.getMaKhachHang());
            ps.setObject(2, kh.getHoTen());
            ps.setObject(3, kh.getSDT());
            ps.setObject(4, kh.getDiaChi());
            ps.setObject(5, kh.getNgaySinh());
            ps.setObject(6, kh.isGioiTinh());
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return check > 0;
    }

    public boolean update(int id, KhachHang newKhachHang) {
        String sql = """
                   UPDATE [dbo].[KhachHang]
                      SET [MaKhachHang] = ?
                         ,[HoTen] = ?
                         ,[SDT] =?
                         ,[DiaChi] = ?
                         ,[NgaySinh] = ?
                         ,[GioiTinh] = ?
                    WHERE ID=?
                   """;
        int check = 0;
        try (Connection con = DBcontext.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, newKhachHang.getMaKhachHang());
            ps.setObject(2, newKhachHang.getHoTen());
            ps.setObject(3, newKhachHang.getSDT());
            ps.setObject(4, newKhachHang.getDiaChi());
            ps.setObject(5, newKhachHang.getNgaySinh());
            ps.setObject(6, newKhachHang.isGioiTinh());
            ps.setObject(7, id);
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return check > 0;
    }

    public boolean delete(int id) {
        String sql = """
                   DELETE FROM [dbo].[KhachHang]
                         WHERE ID=?
                   """;
        int check = 0;
        try (Connection con = DBcontext.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, id);
            check = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return check > 0;
    }

    public ArrayList<KhachHang> search(String keyWord) {
        String sql = """
                   Select ID ,MaKhachHang,HoTen,SDT,DiaChi,NgaySinh,GioiTinh
                   from KhachHang
                   where MaKhachHang like ? or HoTen like ? or SDT like ? or DiaChi like ? or NgaySinh like ? or GioiTinh like ? 
                   """;
        ArrayList<KhachHang> list = new ArrayList<>();
        try (Connection con = DBcontext.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String timKiem = "%" + keyWord + "%";
            for (int i = 1; i <= 6; i++) {
                if (i == 6) {
                    if (keyWord.equalsIgnoreCase("Nam")) {
                        ps.setBoolean(6, true);
                    } else if (keyWord.equalsIgnoreCase("Nữ")) {
                        ps.setBoolean(6, false);

                    } else {
                        ps.setObject(i, null);
                    }

                } else {
                    ps.setString(i, timKiem);
                }

            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhachHang kh = new KhachHang();
                kh.setIdKH(rs.getInt(1));
                kh.setMaKhachHang(rs.getString(2));
                kh.setHoTen(rs.getString(3));
                kh.setSDT(rs.getString(4));
                kh.setDiaChi(rs.getString(5));
                kh.setNgaySinh(rs.getString(6));
                kh.setGioiTinh(rs.getBoolean(7));
                list.add(kh);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean checkMa(String ma) {
        String sql = """
                   Select MaKhachHang
                   from KhachHang
                   where MaKhachHang =?
                     
                   """;
        ArrayList<KhachHang> list = new ArrayList<>();
        try (Connection con = DBcontext.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, ma);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;

            }

        } catch (Exception e) {
            return false;

        }

        return false;
    }

    public boolean checkSDT(String sdt) {
        String sql = """
                   Select SDT
                   from KhachHang
                   where SDT =?
                     
                   """;
        ArrayList<KhachHang> list = new ArrayList<>();
        try (Connection con = DBcontext.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, sdt);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;

            }

        } catch (Exception e) {
            return false;

        }

        return false;
    }

    public static void main(String[] args) {
        System.out.println(new KhachHangDAO().getAll());
    }

}
