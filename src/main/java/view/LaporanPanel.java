/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package view;

import config.DBConnection;
import controller.PembayaranController;
import controller.PelangganController;
import controller.TransaksiController;
import model.Pelanggan;
import model.Transaksi;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

/**
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class LaporanPanel extends JPanel {

    private final TransaksiController  transaksiCtrl;
    private final PelangganController  pelangganCtrl;
    private final PembayaranController pembayaranCtrl;
    private final NumberFormat         nf;

    private JComboBox<String> cmbJenisLaporan;
    private JComboBox<String> cmbBulan;
    private JComboBox<Integer> cmbTahun;
    private JButton           btnGenerate;
    private JButton           btnExportPdf;
    private JButton           btnPrint;

    private JTable            tabelPreview;
    private DefaultTableModel previewModel;
    private JLabel            lblInfo;

    private JasperPrint lastJasperPrint;

    private static final Color COLOR_BG      = new Color(0x1A1A2E);
    private static final Color COLOR_CARD    = new Color(0x16213E);
    private static final Color COLOR_ACCENT  = new Color(0x0F3460);
    private static final Color COLOR_PRIMARY = new Color(0x1A73E8);
    private static final Color COLOR_SUCCESS = new Color(0x43A047);
    private static final Color COLOR_TEXT    = new Color(0xE0E0E0);
    private static final Color COLOR_SUBTEXT = new Color(0x9E9E9E);
    private static final Color COLOR_FIELD   = new Color(0x0F3460);

    private static final String[] JENIS_LAPORAN = {
        "Laporan Semua Transaksi",
        "Laporan Pendapatan per Bulan",
        "Laporan Data Pelanggan"
    };

    private static final String[] NAMA_BULAN = {
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    public LaporanPanel() {
        this.transaksiCtrl  = new TransaksiController();
        this.pelangganCtrl  = new PelangganController();
        this.pembayaranCtrl = new PembayaranController();
        this.nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(),  BorderLayout.NORTH);
        add(createControl(), BorderLayout.WEST);
        add(createPreview(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);
        h.setBorder(new EmptyBorder(0,0,20,0));
        JLabel lbl = new JLabel("Laporan");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24)); lbl.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Buat dan ekspor laporan data laundry");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13)); sub.setForeground(COLOR_SUBTEXT);
        JPanel col = new JPanel(new GridLayout(2,1,0,2)); col.setOpaque(false);
        col.add(lbl); col.add(sub);
        h.add(col, BorderLayout.WEST);
        return h;
    }


    private JPanel createControl() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_CARD);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(250, 0));

        JLabel title = new JLabel("Pengaturan Laporan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(20));

        card.add(createSectionLabel("Jenis Laporan"));
        card.add(Box.createVerticalStrut(6));
        cmbJenisLaporan = new JComboBox<>(JENIS_LAPORAN);
        cmbJenisLaporan.setName("cmbJenisLaporan");
        styleCombo(cmbJenisLaporan);
        cmbJenisLaporan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cmbJenisLaporan.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cmbJenisLaporan);
        card.add(Box.createVerticalStrut(16));

        card.add(createSectionLabel("Filter Bulan & Tahun"));
        card.add(Box.createVerticalStrut(6));
        cmbBulan = new JComboBox<>(NAMA_BULAN);
        cmbBulan.setName("cmbBulan");
        styleCombo(cmbBulan);
        cmbBulan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cmbBulan.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbBulan.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
        card.add(cmbBulan);
        card.add(Box.createVerticalStrut(8));

        int tahunSekarang = Calendar.getInstance().get(Calendar.YEAR);
        Integer[] tahunList = new Integer[6];
        for (int i = 0; i < 6; i++) tahunList[i] = tahunSekarang - i;
        cmbTahun = new JComboBox<>(tahunList);
        cmbTahun.setName("cmbTahun");
        styleCombo(cmbTahun);
        cmbTahun.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cmbTahun.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cmbTahun);
        card.add(Box.createVerticalStrut(24));

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_ACCENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createVerticalStrut(20));

        btnGenerate = createBtn("📊  Buat Laporan", COLOR_PRIMARY, "btnGenerate");
        btnGenerate.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(btnGenerate);
        card.add(Box.createVerticalStrut(10));

        btnExportPdf = createBtn("📄  Export PDF", new Color(0xC62828), "btnExportPdf");
        btnExportPdf.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnExportPdf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnExportPdf.setEnabled(false);
        card.add(btnExportPdf);
        card.add(Box.createVerticalStrut(10));

        btnPrint = createBtn("🖨  Print Laporan", new Color(0x37474F), "btnPrint");
        btnPrint.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPrint.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnPrint.setEnabled(false);
        card.add(btnPrint);

        card.add(Box.createVerticalGlue());


        setupListeners();
        return card;
    }


    private JPanel createPreview() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(0, 16, 0, 0));

 
        JPanel previewHeader = new JPanel(new BorderLayout());
        previewHeader.setOpaque(false);
        previewHeader.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel lblPreview = new JLabel("Preview Laporan");
        lblPreview.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPreview.setForeground(Color.WHITE);

        lblInfo = new JLabel("Pilih jenis laporan dan klik 'Buat Laporan'");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(COLOR_SUBTEXT);

        previewHeader.add(lblPreview, BorderLayout.WEST);
        previewHeader.add(lblInfo,    BorderLayout.EAST);

        previewModel = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelPreview = new JTable(previewModel);
        tabelPreview.setName("tabelPreviewLaporan");
        tabelPreview.setRowHeight(34);
        tabelPreview.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelPreview.setShowVerticalLines(false);
        tabelPreview.setGridColor(new Color(0x2D2D3A));
        tabelPreview.setSelectionBackground(COLOR_PRIMARY);
        tabelPreview.setSelectionForeground(Color.WHITE);
        tabelPreview.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelPreview.getTableHeader().setBackground(COLOR_ACCENT);
        tabelPreview.getTableHeader().setForeground(COLOR_TEXT);

        JScrollPane scroll = new JScrollPane(tabelPreview);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT, 1));
        scroll.getViewport().setBackground(new Color(0x16213E));

        panel.add(previewHeader, BorderLayout.NORTH);
        panel.add(scroll,        BorderLayout.CENTER);
        return panel;
    }


    private void setupListeners() {
        btnGenerate.addActionListener(e -> generateLaporan());
        btnExportPdf.addActionListener(e -> exportPdf());
        btnPrint.addActionListener(e -> printLaporan());

        cmbJenisLaporan.addActionListener(e -> {
            boolean isPendapatan = cmbJenisLaporan.getSelectedIndex() == 1;
            cmbBulan.setEnabled(isPendapatan);
            cmbTahun.setEnabled(isPendapatan);
        });
        cmbBulan.setEnabled(false);
        cmbTahun.setEnabled(false);
    }


    private void generateLaporan() {
        int jenis = cmbJenisLaporan.getSelectedIndex();
        btnGenerate.setEnabled(false);
        btnGenerate.setText("⏳ Memuat...");

        SwingWorker<Void, Void> w = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                switch (jenis) {
                    case 0 -> generateLaporanTransaksi();
                    case 1 -> generateLaporanPendapatan();
                    case 2 -> generateLaporanPelanggan();
                }
                return null;
            }

            @Override
            protected void done() {
                btnGenerate.setEnabled(true);
                btnGenerate.setText("📊  Buat Laporan");
            }
        };
        w.execute();
    }

    private void generateLaporanTransaksi() {
        List<Transaksi> list = transaksiCtrl.getAll();
        String[] cols = {"ID Transaksi", "Pelanggan", "Jenis", "Berat(Kg)", "Harga/Kg", "Total", "Tgl Masuk", "Status"};
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            Transaksi t = list.get(i);
            data[i] = new Object[]{
                t.getIdTransaksi(), t.getNamaPelanggan(), t.getJenisLaundry(),
                t.getBerat(), nf.format(t.getHargaPerKg()),
                nf.format(t.getTotalHarga()),
                t.getTglMasuk() != null ? t.getTglMasuk().toString() : "-",
                t.getStatus()
            };
        }
        SwingUtilities.invokeLater(() -> {
            isiPreview(cols, data);
            lblInfo.setText("Total: " + list.size() + " transaksi | " +
                "Pendapatan: " + nf.format(transaksiCtrl.getTotalPendapatan()));
            btnExportPdf.setEnabled(true);
            btnPrint.setEnabled(true);
        });
        tryGenerateJasper("/reports/LaporanTransaksi.jrxml", null, new JRBeanCollectionDataSource(list));
    }

    private void generateLaporanPendapatan() {
        int bulan = cmbBulan.getSelectedIndex() + 1;
        int tahun = (Integer) cmbTahun.getSelectedItem();
        double total = pembayaranCtrl.getTotalPendapatanBulan(tahun, bulan);

        List<Transaksi> all  = transaksiCtrl.getAll();
        List<Transaksi> list = new ArrayList<>();
        for (Transaksi t : all) {
            if (t.getTglMasuk() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(t.getTglMasuk());
                if (cal.get(Calendar.MONTH)+1 == bulan && cal.get(Calendar.YEAR) == tahun) {
                    list.add(t);
                }
            }
        }
        String[] cols = {"ID Transaksi", "Pelanggan", "Jenis", "Berat(Kg)", "Total", "Status"};
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            Transaksi t = list.get(i);
            data[i] = new Object[]{t.getIdTransaksi(), t.getNamaPelanggan(),
                t.getJenisLaundry(), t.getBerat(), nf.format(t.getTotalHarga()), t.getStatus()};
        }
        SwingUtilities.invokeLater(() -> {
            isiPreview(cols, data);
            lblInfo.setText(NAMA_BULAN[bulan-1] + " " + tahun + " | " +
                list.size() + " transaksi | Pendapatan: " + nf.format(total));
            btnExportPdf.setEnabled(true);
            btnPrint.setEnabled(true);
        });
    }

    private void generateLaporanPelanggan() {
        List<Pelanggan> list = pelangganCtrl.getAll();
        String[] cols = {"ID Pelanggan", "Nama", "No. HP", "Alamat", "Tgl Daftar"};
        Object[][] data = new Object[list.size()][cols.length];
        for (int i = 0; i < list.size(); i++) {
            Pelanggan p = list.get(i);
            data[i] = new Object[]{p.getIdPelanggan(), p.getNama(), p.getNoHp(), p.getAlamat(),
                p.getCreatedAt() != null
                    ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getCreatedAt()) : "-"};
        }
        SwingUtilities.invokeLater(() -> {
            isiPreview(cols, data);
            lblInfo.setText("Total: " + list.size() + " pelanggan terdaftar");
            btnExportPdf.setEnabled(true);
            btnPrint.setEnabled(true);
        });
        tryGenerateJasper("/reports/LaporanPelanggan.jrxml", null, new JRBeanCollectionDataSource(list));
    }


    private void tryGenerateJasper(String jrxmlPath, Map<String, Object> params, JRDataSource ds) {
        try {
            InputStream is = getClass().getResourceAsStream(jrxmlPath);
            if (is == null) return; // File tidak ada, skip silently
            JasperReport jr = JasperCompileManager.compileReport(is);
            lastJasperPrint = JasperFillManager.fillReport(jr,
                    params != null ? params : new HashMap<>(), ds);
        } catch (JRException e) {
            System.err.println("[Laporan] Gagal generate Jasper: " + e.getMessage());
        }
    }

    private void exportPdf() {
        if (lastJasperPrint == null) {
            showError("Buat laporan terlebih dahulu menggunakan tombol 'Buat Laporan'!\n" +
                    "Catatan: File .jrxml harus ada di resources/reports/");
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("Laporan_MyLaundry.pdf"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                JasperExportManager.exportReportToPdfFile(lastJasperPrint,
                        fc.getSelectedFile().getAbsolutePath());
                showSuccess("Laporan berhasil diekspor ke:\n" + fc.getSelectedFile().getAbsolutePath());
            } catch (JRException e) {
                showError("Gagal export PDF: " + e.getMessage());
            }
        }
    }

    private void printLaporan() {
        if (lastJasperPrint == null) {
            showError("Buat laporan terlebih dahulu!\nCatatan: File .jrxml dibutuhkan untuk print.");
            return;
        }
        try {
            JasperPrintManager.printReport(lastJasperPrint, true);
        } catch (JRException e) {
            showError("Gagal mencetak: " + e.getMessage());
        }
    }


    private void isiPreview(String[] cols, Object[][] data) {
        previewModel.setColumnIdentifiers(cols);
        previewModel.setRowCount(0);
        for (Object[] row : data) previewModel.addRow(row);
    }

    private JLabel createSectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(COLOR_SUBTEXT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleCombo(JComboBox<?> c) {
        c.setBackground(COLOR_FIELD); c.setForeground(COLOR_TEXT);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton createBtn(String text, Color bg, String name) {
        JButton b = new JButton(text);
        b.setName(name); b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }
}
