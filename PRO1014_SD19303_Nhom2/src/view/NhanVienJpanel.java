/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import entity.ChucVu;
import entity.NhanVien;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.NhanVien.ChucVuRepository;
import service.NhanVien.NhanVienRepository;
import utilView.TableActionCellEditer;
import utilView.TableActionCellRender;
import utilView.TableActionEvent;

/**
 *
 * @author ADMIN
 */
public class NhanVienJpanel extends javax.swing.JPanel {

    private DefaultComboBoxModel dcbb;

    private ChucVuRepository repoCV;

    private DefaultTableModel dtm;

    private NhanVienRepository repoNV;

    private String hinhAnh;

    //private int selectedIndex;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private static final String DEFAULT_DIRECTORY = "C:\\Users\\ADMIN\\Desktop\\excel";

    private TableState tableState = TableState.INITIAL;

    public NhanVienJpanel() {
        initComponents();
        dcbb = (DefaultComboBoxModel) cbbChucVu.getModel();
        dtm = (DefaultTableModel) tbNhanVien.getModel();
        repoCV = new ChucVuRepository();
        repoNV = new NhanVienRepository();
        showDataTable(repoNV.getAll());
        showComboBoxChucVu();
        showComboBoxLocChucVu();
        locDuLieu();

        tbNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        TableActionEvent event = new TableActionEvent() {
            private Component rootPane;

            @Override
            public void onDelete(int row) {

                if (tbNhanVien.isEditing()) {
                    tbNhanVien.getCellEditor().stopCellEditing();
                }

                List<NhanVien> listNV = null;

                switch (tableState) {
                    case INITIAL:
                        listNV = repoNV.getAll();
                        break;
                    case FILTERED:
                        String gioiTinhSelected = cbbLocGioiTinh.getSelectedItem() != null ? cbbLocGioiTinh.getSelectedItem().toString() : "Tất cả";
                        String chucVuSelected = cbbLocChucVu.getSelectedItem() != null ? cbbLocChucVu.getSelectedItem().toString() : "Tất cả";
                        String trangThaiSelected = cbbLocTrangThai.getSelectedItem() != null ? cbbLocTrangThai.getSelectedItem().toString() : "Tất cả";
                        listNV = repoNV.locDuLieu(gioiTinhSelected, chucVuSelected, trangThaiSelected);
                        break;
                    case SEARCHED:
                        listNV = repoNV.search(txtTimKiem.getText());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tableState);
                }

                int index = tbNhanVien.getSelectedRow();
                if (index >= 0 && index < listNV.size()) {

                    NhanVien selectedNV = listNV.get(index);
                    String trangThai = selectedNV.getTrangThai();

                    if ("No Active".equals(trangThai)) {
                        JOptionPane.showMessageDialog(rootPane, "Mục này có trạng thái 'No Active'. Không thể xóa.");
                        return;
                    }

                    int hoiXoa = JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc chắn muốn xóa không?", "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (hoiXoa == JOptionPane.YES_OPTION) {
                        int id = selectedNV.getId();
                        repoNV.remove(id);
                        showDataTable(getDataBasedOnState());
                        JOptionPane.showMessageDialog(rootPane, "Đã cập nhật sang trạng thái No Active");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Chưa chọn hàng để xóa hoặc chỉ số hàng không hợp lệ");

                }
            }

            @Override
            public void onView(int row) {
                if (row < 0) {
                    JOptionPane.showMessageDialog(rootPane, "Chọn dòng hợp lệ để xem chi tiết.");
                    return;
                }
                List<NhanVien> listNV = null;
                switch (tableState) {
                    case INITIAL:
                        listNV = repoNV.getAll();
                        break;
                    case FILTERED:
                        String gioiTinhSelected = cbbLocGioiTinh.getSelectedItem() != null ? cbbLocGioiTinh.getSelectedItem().toString() : "Tất cả";
                        String chucVuSelected = cbbLocChucVu.getSelectedItem() != null ? cbbLocChucVu.getSelectedItem().toString() : "Tất cả";
                        String trangThaiSelected = cbbLocTrangThai.getSelectedItem() != null ? cbbLocTrangThai.getSelectedItem().toString() : "Tất cả";
                        listNV = repoNV.locDuLieu(gioiTinhSelected, chucVuSelected, trangThaiSelected);
                        break;
                    case SEARCHED:
                        listNV = repoNV.search(txtTimKiem.getText());
                        break;
                    default:
                        JOptionPane.showMessageDialog(rootPane, "Trạng thái bảng không hợp lệ.");
                        return;
                }
                if (listNV != null) {
                    if (row >= listNV.size()) {
                        JOptionPane.showMessageDialog(rootPane, "Chỉ số hàng vượt quá kích thước danh sách.");
                        return;
                    }
                    NhanVien nv = listNV.get(row);
                    if (nv != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Mã NV: ").append(nv.getMaNV()).append("\n");
                        sb.append("Họ Tên: ").append(nv.getHoTen()).append("\n");
                        sb.append("Ngày Sinh: ").append(nv.getNgaySinh()).append("\n");
                        sb.append("Giới Tính: ").append(nv.getGioiTinh() ? "Nam" : "Nữ").append("\n");
                        sb.append("SĐT: ").append(nv.getSdt()).append("\n");
                        sb.append("Địa Chỉ: ").append(nv.getDiaChi()).append("\n");
                        sb.append("Tài Khoản: ").append(nv.getTaiKhoan()).append("\n");
                        sb.append("Mật Khẩu: ").append(nv.getMatKhau()).append("\n");
                        sb.append("Chức Vụ: ").append(nv.getChucVu().getTenCV()).append("\n");
                        sb.append("Ảnh: ").append(nv.getAnh()).append("\n");
                        JOptionPane.showMessageDialog(rootPane, sb.toString(), "Chi Tiết Nhân Viên", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Dữ liệu không hợp lệ.");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Dữ liệu không hợp lệ hoặc chỉ số hàng không hợp lệ.");
                }

            }
        };

        tbNhanVien.getColumnModel().getColumn(11).setCellRenderer(new TableActionCellRender());
        tbNhanVien.getColumnModel().getColumn(11).setCellEditor(new TableActionCellEditer(event));
    }

    private void showDataTable(List<NhanVien> list) {
        dtm.setRowCount(0);
        for (NhanVien nv : list) {
            dtm.addRow(new Object[]{
                nv.getMaNV(), nv.getHoTen(), nv.getNgaySinh(), nv.getGioiTinh() ? "Nam" : "Nữ", nv.getSdt(),
                nv.getDiaChi(), nv.getTaiKhoan(), nv.getMatKhau(), nv.getTrangThai(), nv.getChucVu().getTenCV(), nv.getAnh()

            });

        }
    }

    private void showComboBoxChucVu() {
        dcbb.removeAllElements();
        for (ChucVu cv : repoCV.getAll()) {
            dcbb.addElement(cv.getTenCV());
        }
    }

    private void showComboBoxLocChucVu() {
        List<String> chucVuList = repoCV.loadChucVu();
        cbbLocChucVu.removeAllItems();
        cbbLocChucVu.addItem("Tất cả");
        for (String chucVu : chucVuList) {
            cbbLocChucVu.addItem(chucVu);
        }
    }

    private void locDuLieu() {
        String gioiTinhSelected = cbbLocGioiTinh.getSelectedItem() != null ? cbbLocGioiTinh.getSelectedItem().toString() : "Tất cả";
        String chucVuSelected = cbbLocChucVu.getSelectedItem() != null ? cbbLocChucVu.getSelectedItem().toString() : "Tất cả";
        String trangThaiSelected = cbbLocTrangThai.getSelectedItem() != null ? cbbLocTrangThai.getSelectedItem().toString() : "Tất cả";
        List<NhanVien> listDL = repoNV.locDuLieu(gioiTinhSelected, chucVuSelected, trangThaiSelected);
        showDataTable(listDL);
        tableState = TableState.FILTERED;
    }

    public void exportTableToExcel(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Xuất file Excel");
        fileChooser.setCurrentDirectory(new File(DEFAULT_DIRECTORY));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
        fileChooser.setSelectedFile(new File(""));
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.endsWith(".xlsx")) {
            filePath += ".xlsx";
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách nhân viên");
        TableModel model = table.getModel();
        int numRows = model.getRowCount();
        int numCols = model.getColumnCount();
        Set<Integer> columnsToRemove = new HashSet<>();
        columnsToRemove.add(11);
        Row headerRow = sheet.createRow(0);
        int colIndex = 0;
        for (int i = 0; i < numCols; i++) {
            if (!columnsToRemove.contains(i)) {
                Cell cell = headerRow.createCell(colIndex++);
                cell.setCellValue(model.getColumnName(i));
            }
        }
        for (int row = 0; row < numRows; row++) {
            Row dataRow = sheet.createRow(row + 1);
            colIndex = 0;
            for (int col = 0; col < numCols; col++) {
                if (!columnsToRemove.contains(col)) {
                    Cell cell = dataRow.createCell(colIndex++);
                    Object value = model.getValueAt(row, col);
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
        }
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Xuất file thành công");
    }

    private enum TableState {
        INITIAL,
        FILTERED,
        SEARCHED
    }

    private List<NhanVien> getDataBasedOnState() {
        switch (tableState) {
            case INITIAL:
                return repoNV.getAll();
            case FILTERED:
                String gioiTinhSelected = cbbLocGioiTinh.getSelectedItem() != null ? cbbLocGioiTinh.getSelectedItem().toString() : "Tất cả";
                String chucVuSelected = cbbLocChucVu.getSelectedItem() != null ? cbbLocChucVu.getSelectedItem().toString() : "Tất cả";
                String trangThaiSelected = cbbLocTrangThai.getSelectedItem() != null ? cbbLocTrangThai.getSelectedItem().toString() : "Tất cả";
                return repoNV.locDuLieu(gioiTinhSelected, chucVuSelected, trangThaiSelected);
            case SEARCHED:
                return repoNV.search(txtTimKiem.getText());
            default:
                throw new IllegalStateException("Unexpected value: " + tableState);
        }
    }

    private void detail(int index) {
        String gioiTinhSelected = cbbLocGioiTinh.getSelectedItem() != null ? cbbLocGioiTinh.getSelectedItem().toString() : "Tất cả";
        String chucVuSelected = cbbLocChucVu.getSelectedItem() != null ? cbbLocChucVu.getSelectedItem().toString() : "Tất cả";
        String trangThaiSelected = cbbLocTrangThai.getSelectedItem() != null ? cbbLocTrangThai.getSelectedItem().toString() : "Tất cả";
        NhanVien nv = repoNV.locDuLieu(gioiTinhSelected, chucVuSelected, trangThaiSelected).get(index);
        txtMaNV.setText(nv.getMaNV());
        txtHoTen.setText(nv.getHoTen());
        jdcNgaySinh.setDate(nv.getNgaySinh());
        rdNam.setSelected(nv.getGioiTinh() == true);
        rdNu.setSelected(!nv.getGioiTinh() == true);
        txtSDT.setText(nv.getSdt());
        txtDiaChi.setText(nv.getDiaChi());
        txtTaiKhoan.setText(nv.getTaiKhoan());
        txtMatKhau.setText(nv.getMatKhau());
        rdActive.setSelected(nv.getTrangThai().equals("Active"));
        rdNoActive.setSelected(!nv.getTrangThai().equals("Active"));
        cbbChucVu.setSelectedItem(nv.getChucVu().getTenCV());
        lblAnh.setToolTipText(nv.getAnh());
        this.lblAnh.setPreferredSize(new Dimension(177, 178));
        this.lblAnh.setMinimumSize(new Dimension(177, 178));
        this.lblAnh.setMaximumSize(new Dimension(177, 178));
        ImageIcon defaultIcon = new ImageIcon(new BufferedImage(177, 178, BufferedImage.TYPE_INT_ARGB));
        if (nv.getAnh() != null && !nv.getAnh().trim().isEmpty()) {
            this.lblAnh.setText("");
            ImageIcon imgHinhAnh = new ImageIcon("D:\\DuAn1\\PRO1014_SD19303_Nhom2\\src\\img/" + nv.getAnh());
            Image img = imgHinhAnh.getImage();
            this.hinhAnh = nv.getAnh();
            this.lblAnh.setIcon(new ImageIcon(img.getScaledInstance(177, 178, Image.SCALE_SMOOTH)));
        } else {
            this.lblAnh.setText("");
            this.lblAnh.setIcon(defaultIcon);
        }
        boolean isActive = nv.getTrangThai().equals("Active");
        btnSua.setEnabled(isActive);
        btnXoa.setEnabled(isActive);
        btnThem.setEnabled(true);
    }

    private void detailActive(int index) {
        NhanVien nv = repoNV.getAll().get(index);
        txtMaNV.setText(nv.getMaNV());
        txtHoTen.setText(nv.getHoTen());
        jdcNgaySinh.setDate(nv.getNgaySinh());
        rdNam.setSelected(nv.getGioiTinh() == true);
        rdNu.setSelected(!nv.getGioiTinh() == true);
        txtSDT.setText(nv.getSdt());
        txtDiaChi.setText(nv.getDiaChi());
        txtTaiKhoan.setText(nv.getTaiKhoan());
        txtMatKhau.setText(nv.getMatKhau());
        rdActive.setSelected(nv.getTrangThai().equals("Active"));
        rdNoActive.setSelected(!nv.getTrangThai().equals("Active"));
        cbbChucVu.setSelectedItem(nv.getChucVu().getTenCV());
        lblAnh.setToolTipText(nv.getAnh());
        this.lblAnh.setPreferredSize(new Dimension(177, 178));
        this.lblAnh.setMinimumSize(new Dimension(177, 178));
        this.lblAnh.setMaximumSize(new Dimension(177, 178));
        ImageIcon defaultIcon = new ImageIcon(new BufferedImage(177, 178, BufferedImage.TYPE_INT_ARGB));
        if (nv.getAnh() != null && !nv.getAnh().trim().isEmpty()) {
            this.lblAnh.setText("");
            ImageIcon imgHinhAnh = new ImageIcon("D:\\DuAn1\\PRO1014_SD19303_Nhom2\\src\\img/" + nv.getAnh());
            Image img = imgHinhAnh.getImage();
            this.hinhAnh = nv.getAnh();
            this.lblAnh.setIcon(new ImageIcon(img.getScaledInstance(177, 178, Image.SCALE_SMOOTH)));
        } else {
            this.lblAnh.setText("");
            this.lblAnh.setIcon(defaultIcon);
        }
        boolean isActive = nv.getTrangThai().equals("Active");
        btnSua.setEnabled(isActive);
        btnXoa.setEnabled(isActive);
        btnThem.setEnabled(true);
    }

    private void detailTimKiem(int index) {
        String timKiem = txtTimKiem.getText();
        NhanVien nv = repoNV.search(timKiem).get(index);
        txtMaNV.setText(nv.getMaNV());
        txtHoTen.setText(nv.getHoTen());
        jdcNgaySinh.setDate(nv.getNgaySinh());
        rdNam.setSelected(nv.getGioiTinh() == true);
        rdNu.setSelected(!nv.getGioiTinh() == true);
        txtSDT.setText(nv.getSdt());
        txtDiaChi.setText(nv.getDiaChi());
        txtTaiKhoan.setText(nv.getTaiKhoan());
        txtMatKhau.setText(nv.getMatKhau());
        rdActive.setSelected(nv.getTrangThai().equals("Active"));
        rdNoActive.setSelected(!nv.getTrangThai().equals("Active"));
        cbbChucVu.setSelectedItem(nv.getChucVu().getTenCV());

        lblAnh.setToolTipText(nv.getAnh());
        this.lblAnh.setPreferredSize(new Dimension(177, 178));
        this.lblAnh.setMinimumSize(new Dimension(177, 178));
        this.lblAnh.setMaximumSize(new Dimension(177, 178));
        ImageIcon defaultIcon = new ImageIcon(new BufferedImage(177, 178, BufferedImage.TYPE_INT_ARGB));
        if (nv.getAnh() != null && !nv.getAnh().trim().isEmpty()) {
            this.lblAnh.setText("");
            ImageIcon imgHinhAnh = new ImageIcon("D:\\DuAn1\\PRO1014_SD19303_Nhom2\\src\\img/" + nv.getAnh());
            Image img = imgHinhAnh.getImage();
            this.hinhAnh = nv.getAnh();
            this.lblAnh.setIcon(new ImageIcon(img.getScaledInstance(177, 178, Image.SCALE_SMOOTH)));
        } else {
            this.lblAnh.setText("");
            this.lblAnh.setIcon(defaultIcon);
        }

        boolean isActive = nv.getTrangThai().equals("Active");
        btnSua.setEnabled(isActive);
        btnXoa.setEnabled(isActive);
        btnThem.setEnabled(true);
    }

    private NhanVien getFormData() {
        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMaNV.getText());
        nv.setHoTen(txtHoTen.getText());
        nv.setNgaySinh(jdcNgaySinh.getDate());
        nv.setGioiTinh(rdNam.isSelected());
        nv.setSdt(txtSDT.getText());
        nv.setDiaChi(txtDiaChi.getText());
        nv.setTaiKhoan(txtTaiKhoan.getText());
        nv.setMatKhau(txtMatKhau.getText());
        nv.setTrangThai(rdActive.isSelected() == true ? "Active" : "No Active");
        ChucVu cv = repoCV.getChucVuByTen(cbbChucVu.getSelectedItem().toString());
        nv.setChucVu(cv);
        nv.setAnh(lblAnh.getToolTipText());
        return nv;
    }

    void selectImage() {
        try {
            JFileChooser jfc = new JFileChooser("D:\\DuAn1\\PRO1014_SD19303_Nhom2\\src\\img/");
            jfc.showOpenDialog(null);
            File file = jfc.getSelectedFile();
            this.hinhAnh = file.getName();
            Image img = ImageIO.read(file);
            this.lblAnh.setText("");
            this.lblAnh.setIcon(new ImageIcon(img.getScaledInstance(177, 178, 0)));
            this.lblAnh.setToolTipText(hinhAnh);
        } catch (Exception e) {
            return;
        }

    }

    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        rdNam.isSelected();
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtTaiKhoan.setText("");
        txtMatKhau.setText("");
        lblAnh.setToolTipText("");
    }

    private boolean check() {
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống");
            return false;
        } else if (txtHoTen.getText().trim().startsWith(" ")) {
            JOptionPane.showMessageDialog(this, "Họ tên không được cách đầu dòng");
            return false;
        } else if (!txtHoTen.getText().trim().matches("^[\\p{L}]+( [\\p{L}]+)*$")) {
            JOptionPane.showMessageDialog(this, "Họ tên không được chứa số hoặc kí tự đặc biệt, chỉ cách 1 lần ở giữa dòng");
            return false;

        } else if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống");
            return false;
        } else if (!txtSDT.getText().trim().matches("^0\\d{9,10}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là một dãy số từ 10 đến 11 chữ số và bắt đầu bằng số 0");
            return false;
        } else if (repoNV.checkSDT(txtSDT.getText())) {
            JOptionPane.showMessageDialog(this, "Số điện thoại đã có người sử dụng");
            return false;
        } else if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống");
            return false;
        } else if (txtDiaChi.getText().trim().startsWith(" ")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được cách đầu dòng");
            return false;
        } else if (!txtDiaChi.getText().trim().matches("^[\\p{L}]+( [\\p{L}]+)*$")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được chứa số hoặc kí tự đặc biệt, chỉ cách 1 lần ở giữa dòng, chỉ nhập tên tỉnh/thành phố");
            return false;
        } else if (txtTaiKhoan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tài khoản không được để trống");
            return false;
        } else if (!txtTaiKhoan.getText().matches("^[a-zA-Z0-9]{8,16}$")) {
            JOptionPane.showMessageDialog(this, "Tài khoản phải là 1 chuỗi liền nhau từ 8 - 16 kí tự, không có kí tự đặc biệt");
            return false;
        } else if (txtMatKhau.getText()
                .trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống");
            return false;
        } else if (!txtMatKhau.getText()
                .matches("^[\\p{Print}&&[^\\s]]{8,20}$")) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải là 1 chuỗi liền nhau từ 8 - 16 kí tự");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkSua() {
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống");
            return false;
        } else if (txtHoTen.getText().trim().startsWith(" ")) {
            JOptionPane.showMessageDialog(this, "Họ tên không được cách đầu dòng");
            return false;
        } else if (!txtHoTen.getText().trim().matches("^[\\p{L}]+( [\\p{L}]+)*$")) {
            JOptionPane.showMessageDialog(this, "Họ tên không được chứa số hoặc kí tự đặc biệt, chỉ cách 1 lần ở giữa dòng");
            return false;
        } else if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống");
            return false;
        } else if (!txtSDT.getText().matches("^0\\d{9,10}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là một dãy số từ 10 đến 11 chữ số và bắt đầu bằng số 0");
            return false;
        } else if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống");
            return false;
        } else if (txtDiaChi.getText().trim().startsWith(" ")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được cách đầu dòng");
            return false;
        } else if (!txtDiaChi.getText().matches("^[\\p{L}]+( [\\p{L}]+)*$")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được chứa số hoặc kí tự đặc biệt, chỉ cách 1 lần ở giữa dòng, chỉ nhập tên tỉnh/thành phố");
            return false;
        } else if (txtTaiKhoan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tài khoản không được để trống");
            return false;
        } else if (!txtTaiKhoan.getText().matches("^[a-zA-Z0-9]{8,16}$")) {
            JOptionPane.showMessageDialog(this, "Tài khoản phải là 1 chuỗi liền nhau từ 8 - 16 kí tự, không có kí tự đặc biệt");
            return false;
        } else if (txtMatKhau.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống");
            return false;
        } else if (!txtMatKhau.getText().matches("^[\\p{Print}&&[^\\s]]{8,20}$")) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải là 1 chuỗi liền nhau từ 8 - 16 kí tự");
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        rdNam = new javax.swing.JRadioButton();
        rdNu = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        txtTaiKhoan = new javax.swing.JTextField();
        txtMatKhau = new javax.swing.JTextField();
        cbbChucVu = new javax.swing.JComboBox<>();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnClearForm = new javax.swing.JButton();
        lblAnh = new javax.swing.JLabel();
        btnXuatExcel = new javax.swing.JButton();
        btnLoadData = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        rdActive = new javax.swing.JRadioButton();
        rdNoActive = new javax.swing.JRadioButton();
        jdcNgaySinh = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbNhanVien = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        cbbLocGioiTinh = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        cbbLocChucVu = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        cbbLocTrangThai = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 204));
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin nhân viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Ảnh:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Mã NV:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Họ tên:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Ngày sinh:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Giới tính:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("SDT:");

        txtMaNV.setEditable(false);
        txtMaNV.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 255)));
        txtMaNV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        txtHoTen.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 255)));
        txtHoTen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        txtSDT.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 255)));
        txtSDT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        rdNam.setText("Nam");
        rdNam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        rdNu.setText("Nữ");
        rdNu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Địa chỉ:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Tài khoản:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Mật khẩu:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Chức vụ:");

        txtDiaChi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 255)));
        txtDiaChi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        txtTaiKhoan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 255)));
        txtTaiKhoan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        txtMatKhau.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 255)));
        txtMatKhau.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        cbbChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbChucVu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnThem.setBackground(new java.awt.Color(51, 153, 0));
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add-user.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setBorder(null);
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 204, 0));
        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 51, 51));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/deletebutton.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnClearForm.setBackground(new java.awt.Color(0, 153, 255));
        btnClearForm.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClearForm.setForeground(new java.awt.Color(255, 255, 255));
        btnClearForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/clearform.png"))); // NOI18N
        btnClearForm.setText("Clear Form");
        btnClearForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFormActionPerformed(evt);
            }
        });

        lblAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblAnh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhMouseClicked(evt);
            }
        });

        btnXuatExcel.setBackground(new java.awt.Color(0, 102, 204));
        btnXuatExcel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXuatExcel.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/excel.png"))); // NOI18N
        btnXuatExcel.setText("Xuất Excel");
        btnXuatExcel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXuatExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatExcelActionPerformed(evt);
            }
        });

        btnLoadData.setBackground(new java.awt.Color(0, 153, 153));
        btnLoadData.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLoadData.setForeground(new java.awt.Color(255, 255, 255));
        btnLoadData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/loaddata.png"))); // NOI18N
        btnLoadData.setText("Load Data");
        btnLoadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadDataActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Trạng thái:");

        rdActive.setText("Active");

        rdNoActive.setText("No Active");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaNV)
                            .addComponent(txtHoTen)
                            .addComponent(txtSDT)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(rdNam, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdNu, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jdcNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                        .addGap(114, 114, 114)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtDiaChi, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                    .addComponent(txtTaiKhoan)
                                    .addComponent(txtMatKhau)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(rdActive, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(rdNoActive, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(360, 360, 360)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnClearForm)
                        .addGap(18, 18, 18)
                        .addComponent(btnLoadData)
                        .addGap(18, 18, 18)
                        .addComponent(btnXuatExcel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8)))
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(txtTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel5)
                                .addGap(28, 28, 28))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jdcNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(rdNam)
                            .addComponent(rdNu)
                            .addComponent(jLabel11)
                            .addComponent(rdActive)
                            .addComponent(rdNoActive))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel12)
                            .addComponent(cbbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClearForm, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXuatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLoadData, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tbNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tbNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã NV", "Họ tên", "Ngày sinh", "Giới tính", "SDT", "Địa chỉ", "Tài khoản", "Mật khẩu", "Trạng thái", "Chức vụ", "Ảnh", "Hành động"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbNhanVien.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tbNhanVien.setFocusable(false);
        tbNhanVien.setRowHeight(35);
        tbNhanVien.setSelectionBackground(new java.awt.Color(56, 138, 112));
        tbNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbNhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbNhanVien);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/magnifying-glass.png"))); // NOI18N

        txtTimKiem.setForeground(new java.awt.Color(102, 102, 102));
        txtTimKiem.setText("Nhập mã nhân viên, họ tên, ngày sinh... để tìm kiếm");
        txtTimKiem.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 204, 255)));
        txtTimKiem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        txtTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKiemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKiemFocusLost(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 632, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bộ lọc", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Giới tính:");

        cbbLocGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Nam", "Nữ" }));
        cbbLocGioiTinh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbbLocGioiTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLocGioiTinhActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Chức vụ:");

        cbbLocChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbLocChucVu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbbLocChucVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLocChucVuActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Trạng thái:");

        cbbLocTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Active", "No Active" }));
        cbbLocTrangThai.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cbbLocTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLocTrangThaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbLocGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbLocChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbbLocTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbLocGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbLocChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbLocTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(543, 543, 543)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        if (check()) {
            String gioiTinhSelected = cbbLocGioiTinh.getSelectedItem() != null ? cbbLocGioiTinh.getSelectedItem().toString() : "Tất cả";
            String chucVuSelected = cbbLocChucVu.getSelectedItem() != null ? cbbLocChucVu.getSelectedItem().toString() : "Tất cả";
            String trangThaiSelected = cbbLocTrangThai.getSelectedItem() != null ? cbbLocTrangThai.getSelectedItem().toString() : "Tất cả";
            repoNV.add(getFormData());
            switch (tableState) {
                case INITIAL:
                    showDataTable(repoNV.getAll());
                    break;
                case FILTERED:
                    showDataTable(repoNV.locDuLieu(gioiTinhSelected, chucVuSelected, trangThaiSelected));
                    break;
                case SEARCHED:
                    showDataTable(repoNV.search(txtTimKiem.getText()));
                    break;
            }

            JOptionPane.showMessageDialog(this, "Thêm thành công");
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (checkSua()) {
            int hoiSua = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn sửa không?", "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (hoiSua == JOptionPane.YES_OPTION) {
                int index = tbNhanVien.getSelectedRow();
                if (index >= 0) {
                    int id = -1;
                    NhanVien updatedNV = getFormData();

                    List<NhanVien> listNV = null;
                    switch (tableState) {
                        case INITIAL:
                            listNV = repoNV.getAll();
                            break;
                        case FILTERED:
                            String gioiTinhSelected = cbbLocGioiTinh.getSelectedItem() != null ? cbbLocGioiTinh.getSelectedItem().toString() : "Tất cả";
                            String chucVuSelected = cbbLocChucVu.getSelectedItem() != null ? cbbLocChucVu.getSelectedItem().toString() : "Tất cả";
                            String trangThaiSelected = cbbLocTrangThai.getSelectedItem() != null ? cbbLocTrangThai.getSelectedItem().toString() : "Tất cả";
                            listNV = repoNV.locDuLieu(gioiTinhSelected, chucVuSelected, trangThaiSelected);
                            break;
                        case SEARCHED:
                            listNV = repoNV.search(txtTimKiem.getText());
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + tableState);
                    }

                    if (index < listNV.size()) {
                        id = listNV.get(index).getId();

                        updatedNV.setId(id);
                        repoNV.update(id, updatedNV);

                        showDataTable(getDataBasedOnState());

                        JOptionPane.showMessageDialog(this, "Sửa thành công");
                    } else {
                        JOptionPane.showMessageDialog(this, "Chỉ số hàng không hợp lệ");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Chưa chọn hàng để sửa");
                }
            }
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int hoiXoa = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa không?", "Thông báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (hoiXoa == JOptionPane.YES_OPTION) {
            int index = tbNhanVien.getSelectedRow();
            if (index >= 0) {
                int id = -1;
                List<NhanVien> listNV = null;
                switch (tableState) {
                    case INITIAL:
                        listNV = repoNV.getAll();
                        break;
                    case FILTERED:
                        String gioiTinhSelected = cbbLocGioiTinh.getSelectedItem() != null ? cbbLocGioiTinh.getSelectedItem().toString() : "Tất cả";
                        String chucVuSelected = cbbLocChucVu.getSelectedItem() != null ? cbbLocChucVu.getSelectedItem().toString() : "Tất cả";
                        String trangThaiSelected = cbbLocTrangThai.getSelectedItem() != null ? cbbLocTrangThai.getSelectedItem().toString() : "Tất cả";
                        listNV = repoNV.locDuLieu(gioiTinhSelected, chucVuSelected, trangThaiSelected);
                        break;
                    case SEARCHED:
                        listNV = repoNV.search(txtTimKiem.getText());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tableState);
                }
                if (index < listNV.size()) {
                    id = listNV.get(index).getId();
                    repoNV.remove(id);
                    showDataTable(getDataBasedOnState());
                    JOptionPane.showMessageDialog(this, "Đã cập nhật sang trạng thái No Active");
                } else {
                    JOptionPane.showMessageDialog(this, "Chỉ số hàng không hợp lệ");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chưa chọn hàng để xóa");
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnClearFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFormActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearFormActionPerformed

    private void lblAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhMouseClicked
        this.selectImage();
    }//GEN-LAST:event_lblAnhMouseClicked

    private void btnXuatExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatExcelActionPerformed
        NhanVienJpanel exporter = new NhanVienJpanel();
        exporter.exportTableToExcel(tbNhanVien);
    }//GEN-LAST:event_btnXuatExcelActionPerformed

    private void btnLoadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadDataActionPerformed
        List<NhanVien> listDL = repoNV.getAll();
        showDataTable(listDL);
        tableState = TableState.INITIAL;
    }//GEN-LAST:event_btnLoadDataActionPerformed

    private void tbNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbNhanVienMouseClicked
        int index = tbNhanVien.getSelectedRow();
        if (index >= 0) {
            switch (tableState) {
                case INITIAL:
                    detailActive(index);
                    break;
                case FILTERED:
                    detail(index);
                    break;
                case SEARCHED:
                    detailTimKiem(index);
                    break;
            }
        }
    }//GEN-LAST:event_tbNhanVienMouseClicked

    private void txtTimKiemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKiemFocusGained
        if (txtTimKiem.getText().equals("Nhập mã nhân viên, họ tên, ngày sinh... để tìm kiếm")) {
            txtTimKiem.setText("");
            txtTimKiem.setForeground(Color.black);
        }
    }//GEN-LAST:event_txtTimKiemFocusGained

    private void txtTimKiemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKiemFocusLost
        if (txtTimKiem.getText().length() == 0) {
            txtTimKiem.setText("Nhập mã nhân viên, họ tên, ngày sinh... để tìm kiếm");
        }
    }//GEN-LAST:event_txtTimKiemFocusLost

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(dtm);
        tbNhanVien.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter("(?i)" + txtTimKiem.getText(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        tableState = TableState.SEARCHED;
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void cbbLocGioiTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLocGioiTinhActionPerformed
        locDuLieu();
    }//GEN-LAST:event_cbbLocGioiTinhActionPerformed

    private void cbbLocChucVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLocChucVuActionPerformed
        locDuLieu();
    }//GEN-LAST:event_cbbLocChucVuActionPerformed

    private void cbbLocTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLocTrangThaiActionPerformed
        locDuLieu();
    }//GEN-LAST:event_cbbLocTrangThaiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearForm;
    private javax.swing.JButton btnLoadData;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXuatExcel;
    private javax.swing.JComboBox<String> cbbChucVu;
    private javax.swing.JComboBox<String> cbbLocChucVu;
    private javax.swing.JComboBox<String> cbbLocGioiTinh;
    private javax.swing.JComboBox<String> cbbLocTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcNgaySinh;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JRadioButton rdActive;
    private javax.swing.JRadioButton rdNam;
    private javax.swing.JRadioButton rdNoActive;
    private javax.swing.JRadioButton rdNu;
    private javax.swing.JTable tbNhanVien;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTaiKhoan;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
