/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package view;

import controller.PembayaranController;
import model.Pembayaran;
import model.Transaksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class PembayaranPanel extends JPanel {

    private final PembayaranController controller;
    private final NumberFormat          nf;

    private JTextField              txtIdPembayaran;
    private JComboBox<TransaksiItem> cmbTransaksi;
    private JTextField              txtNamaPelanggan;
    private JTextField              txtJenisLaundry;
    private JTextField              txtBerat;
    private JTextField              txtTotalTagihan;
    private JTextField              txtUangBayar;
    private JTextField              txtKembalian;
    private JButton                 btnBayar;
    private JButton                 btnCetakStruk;
    private JButton                 btnReset;

    private JTable            tabelRiwayat;
    private DefaultTableModel tabelModel;

    private Pembayaran lastPembayaran;
    private Transaksi  lastTransaksi;

    private static final Color COLOR_BG       = new Color(0x1A1A2E);
    private static final Color COLOR_CARD     = new Color(0x16213E);
    private static final Color COLOR_ACCENT   = new Color(0x0F3460);
    private static final Color COLOR_PRIMARY  = new Color(0x1A73E8);
    private static final Color COLOR_SUCCESS  = new Color(0x2E7D32);
    private static final Color COLOR_WARNING  = new Color(0xF57F17);
    private static final Color COLOR_TEXT     = new Color(0xE0E0E0);
    private static final Color COLOR_SUBTEXT  = new Color(0x9E9E9E);
    private static final Color COLOR_FIELD_BG = new Color(0x0F3460);

    private static final String[] COLUMNS_RIWAYAT = {
        "ID Pembayaran", "ID Transaksi", "Pelanggan", "Jenis", "Total", "Uang", "Kembalian", "Tgl Bayar", "Status"
    };

    public PembayaranPanel() {
        this.controller = new PembayaranController();
        this.nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);

        JPanel form = createPaymentForm();
        JPanel riwayat = createRiwayatPanel();
        setupListeners();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                form, riwayat);
        split.setDividerLocation(480);
        split.setDividerSize(6);
        split.setBorder(null);
        split.setOpaque(false);
        add(split, BorderLayout.CENTER);
    }


    private JPanel createHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);
        h.setBorder(new EmptyBorder(0,0,20,0));
        JLabel lbl = new JLabel("Pembayaran Laundry");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24)); lbl.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Proses pembayaran dan cetak struk");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13)); sub.setForeground(COLOR_SUBTEXT);
        JPanel col = new JPanel(new GridLayout(2,1,0,2)); col.setOpaque(false);
        col.add(lbl); col.add(sub);
        h.add(col, BorderLayout.WEST);
        return h;
    }


    private JPanel createPaymentForm() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(COLOR_BG);
        outer.setBorder(new EmptyBorder(0, 0, 0, 12));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_CARD);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Form Pembayaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(16));

        card.add(createRow("ID Pembayaran:",
            txtIdPembayaran = createReadonlyField(), true));
        card.add(Box.createVerticalStrut(10));

        cmbTransaksi = new JComboBox<>();
        cmbTransaksi.setName("cmbTransaksiPembayaran");
        styleCombo(cmbTransaksi);
        cmbTransaksi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        card.add(createRow("Pilih Transaksi (Belum Bayar):", cmbTransaksi, false));
        card.add(Box.createVerticalStrut(10));

        txtNamaPelanggan = createReadonlyField();
        card.add(createRow("Nama Pelanggan:", txtNamaPelanggan, true));
        card.add(Box.createVerticalStrut(8));

        txtJenisLaundry = createReadonlyField();
        card.add(createRow("Jenis Laundry:", txtJenisLaundry, true));
        card.add(Box.createVerticalStrut(8));

        txtBerat = createReadonlyField();
        card.add(createRow("Berat (Kg):", txtBerat, true));
        card.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_ACCENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));

        txtTotalTagihan = createReadonlyField();
        txtTotalTagihan.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtTotalTagihan.setForeground(new Color(0xFFD54F));
        txtTotalTagihan.setBackground(new Color(0x1A2E3A));
        card.add(createRow("Total Tagihan:", txtTotalTagihan, true));
        card.add(Box.createVerticalStrut(12));

        txtUangBayar = new JTextField();
        txtUangBayar.setName("txtUangBayar");
        txtUangBayar.putClientProperty("JTextField.placeholderText", "Masukkan jumlah uang...");
        styleField(txtUangBayar);
        txtUangBayar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtUangBayar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        card.add(createRow("Uang Bayar (Rp):", txtUangBayar, false));
        card.add(Box.createVerticalStrut(12));

        txtKembalian = createReadonlyField();
        txtKembalian.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtKembalian.setForeground(new Color(0xA5D6A7));
        txtKembalian.setBackground(new Color(0x1B3A1B));
        card.add(createRow("Kembalian:", txtKembalian, true));
        card.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnBayar = new JButton("💳 BAYAR");
        btnBayar.setName("btnBayar");
        styleBtn(btnBayar, COLOR_SUCCESS);

        btnCetakStruk = new JButton("🖨 Cetak Struk");
        btnCetakStruk.setName("btnCetakStruk");
        styleBtn(btnCetakStruk, COLOR_PRIMARY);
        btnCetakStruk.setEnabled(false);

        btnReset = new JButton("↺ Reset");
        btnReset.setName("btnResetPembayaran");
        styleBtn(btnReset, COLOR_ACCENT);

        btnPanel.add(btnBayar);
        btnPanel.add(btnCetakStruk);
        btnPanel.add(btnReset);

        card.add(btnPanel);

        outer.add(card, BorderLayout.CENTER);

        return outer;
    }


    private JPanel createRiwayatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);

        JLabel title = new JLabel("Riwayat Pembayaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));

        tabelModel = new DefaultTableModel(COLUMNS_RIWAYAT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelRiwayat = new JTable(tabelModel);
        tabelRiwayat.setName("tabelRiwayatPembayaran");
        tabelRiwayat.setRowHeight(34);
        tabelRiwayat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelRiwayat.setShowVerticalLines(false);
        tabelRiwayat.setGridColor(new Color(0x2D2D3A));
        tabelRiwayat.setSelectionBackground(COLOR_PRIMARY);
        tabelRiwayat.setSelectionForeground(Color.WHITE);
        tabelRiwayat.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelRiwayat.getTableHeader().setBackground(COLOR_ACCENT);
        tabelRiwayat.getTableHeader().setForeground(COLOR_TEXT);
        tabelRiwayat.getTableHeader().setReorderingAllowed(false);

        tabelRiwayat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelRiwayat.getSelectedRow();
                if (row >= 0) {
                    String idTrx = (String) tabelModel.getValueAt(row, 1);
                    Transaksi t = controller.getTransaksiById(idTrx);
                    if (t != null) {
                        lastTransaksi = t;
                        lastPembayaran = controller.getByTransaksi(idTrx);
                        btnCetakStruk.setEnabled(lastPembayaran != null);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelRiwayat);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT, 1));
        scroll.getViewport().setBackground(new Color(0x16213E));

        panel.add(title,  BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }


    private void setupListeners() {
        // Pilih transaksi → auto-fill
        cmbTransaksi.addActionListener(e -> {
            TransaksiItem item = (TransaksiItem) cmbTransaksi.getSelectedItem();
            if (item != null) {
                Transaksi t = controller.getTransaksiById(item.id);
                if (t != null) {
                    lastTransaksi = t;
                    txtNamaPelanggan.setText(t.getNamaPelanggan());
                    txtJenisLaundry.setText(t.getJenisLaundry());
                    txtBerat.setText(t.getBerat() + " Kg");
                    txtTotalTagihan.setText(nf.format(t.getTotalHarga()));
                    txtUangBayar.setText("");
                    txtKembalian.setText("");
                    txtIdPembayaran.setText(controller.generateId());
                    txtUangBayar.requestFocus();
                }
            }
        });

        // Hitung kembalian real-time
        txtUangBayar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { hitungKembalian(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { hitungKembalian(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });

        btnBayar.addActionListener(e -> doBayar());

        btnCetakStruk.addActionListener(e -> {
            if (lastPembayaran != null && lastTransaksi != null) {
                cetakStruk(lastTransaksi, lastPembayaran);
            } else {
                showError("Tidak ada data struk yang tersedia.");
            }
        });

        btnReset.addActionListener(e -> resetForm());

        txtUangBayar.addActionListener(e -> doBayar());
    }


    private void doBayar() {
        String idPembayaran = txtIdPembayaran.getText().trim();
        TransaksiItem item = (TransaksiItem) cmbTransaksi.getSelectedItem();
        String idTransaksi = item != null ? item.id : "";

        String err = controller.bayar(idPembayaran, idTransaksi, txtUangBayar.getText());
        if (err != null) {
            showError(err);
        } else {
            showSuccess("Pembayaran berhasil! Kembalian: " + txtKembalian.getText());
            lastPembayaran = controller.getByTransaksi(idTransaksi);
            lastTransaksi  = controller.getTransaksiById(idTransaksi);
            btnCetakStruk.setEnabled(true);
            loadData();

            // Tanya cetak struk
            int opt = JOptionPane.showConfirmDialog(this,
                "Pembayaran berhasil disimpan!\nCetak struk sekarang?",
                "Cetak Struk", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (opt == JOptionPane.YES_OPTION && lastPembayaran != null) {
                cetakStruk(lastTransaksi, lastPembayaran);
            }
        }
    }

    private void hitungKembalian() {
        TransaksiItem item = (TransaksiItem) cmbTransaksi.getSelectedItem();
        if (item == null) return;
        Transaksi t = controller.getTransaksiById(item.id);
        if (t == null) return;

        double kembalian = controller.hitungKembalian(txtUangBayar.getText(), t.getTotalHarga());
        if (kembalian >= 0) {
            txtKembalian.setText(nf.format(kembalian));
            txtKembalian.setForeground(new Color(0xA5D6A7));
        } else {
            double uang = 0;
            try { uang = Double.parseDouble(txtUangBayar.getText().replaceAll("[^0-9.]", "")); } catch (Exception ignored) {}
            if (uang > 0) {
                txtKembalian.setText("Kurang: " + nf.format(t.getTotalHarga() - uang));
                txtKembalian.setForeground(new Color(0xEF5350));
            } else {
                txtKembalian.setText("");
            }
        }
    }

 
    private void cetakStruk(Transaksi t, Pembayaran p) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) graphics;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double x = pageFormat.getImageableX();
            double y = pageFormat.getImageableY();
            g2.translate(x, y);

            Font fontBold  = new Font("Monospaced", Font.BOLD, 12);
            Font fontPlain = new Font("Monospaced", Font.PLAIN, 11);
            Font fontTitle = new Font("Monospaced", Font.BOLD, 16);

            int lx = 10, ly = 20, lh = 18;

            g2.setFont(fontTitle);
            g2.drawString("==============================", lx, ly); ly+=lh;
            g2.drawString("      MYLAUNDRY STORE         ", lx, ly); ly+=lh;
            g2.drawString("   Struk Pembayaran Laundry   ", lx, ly); ly+=lh;
            g2.drawString("==============================", lx, ly); ly+=lh;

            g2.setFont(fontPlain);
            String waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            g2.drawString("Tanggal  : " + waktu, lx, ly); ly+=lh;
            g2.drawString("ID Bayar : " + p.getIdPembayaran(), lx, ly); ly+=lh;
            g2.drawString("ID Trx   : " + t.getIdTransaksi(), lx, ly); ly+=lh;
            g2.drawString("Pelanggan: " + t.getNamaPelanggan(), lx, ly); ly+=lh;
            g2.drawString("------------------------------", lx, ly); ly+=lh;
            g2.drawString("Jenis    : " + t.getJenisLaundry(), lx, ly); ly+=lh;
            g2.drawString("Berat    : " + t.getBerat() + " Kg", lx, ly); ly+=lh;
            g2.drawString("Harga/Kg : " + nf.format(t.getHargaPerKg()), lx, ly); ly+=lh;
            g2.drawString("------------------------------", lx, ly); ly+=lh;

            g2.setFont(fontBold);
            g2.drawString("TOTAL    : " + nf.format(p.getTotalBayar()), lx, ly); ly+=lh;
            g2.drawString("DIBAYAR  : " + nf.format(p.getUangBayar()), lx, ly); ly+=lh;
            g2.drawString("KEMBALIAN: " + nf.format(p.getKembalian()), lx, ly); ly+=lh;

            g2.setFont(fontPlain);
            g2.drawString("==============================", lx, ly); ly+=lh;
            g2.drawString("    Terima Kasih Sudah Datang  ", lx, ly); ly+=lh;
            g2.drawString("    Laundry bersih & rapi ^^  ", lx, ly); ly+=lh;
            g2.drawString("==============================", lx, ly);

            return Printable.PAGE_EXISTS;
        });

        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                showError("Gagal mencetak: " + ex.getMessage());
            }
        }
    }


    public void loadData() {

        cmbTransaksi.removeAllItems();
        List<Transaksi> unpaid = controller.getTransaksiUnpaid();
        for (Transaksi t : unpaid) {
            cmbTransaksi.addItem(new TransaksiItem(t.getIdTransaksi(),
                    t.getNamaPelanggan(), t.getTotalHarga()));
        }

 
        SwingWorker<List<Pembayaran>, Void> w = new SwingWorker<>() {
            @Override protected List<Pembayaran> doInBackground() { return controller.getAll(); }
            @Override protected void done() {
                try {
                    tabelModel.setRowCount(0);
                    for (Pembayaran p : get()) {
                        tabelModel.addRow(new Object[]{
                            p.getIdPembayaran(), p.getIdTransaksi(),
                            p.getNamaPelanggan(), p.getJenisLaundry(),
                            nf.format(p.getTotalBayar()), nf.format(p.getUangBayar()),
                            nf.format(p.getKembalian()),
                            p.getTglBayar() != null
                                ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(p.getTglBayar())
                                : "-",
                            p.getStatusBayar()
                        });
                    }
                } catch (Exception ex) { showError(ex.getMessage()); }
            }
        };
        w.execute();

        txtIdPembayaran.setText(controller.generateId());
        resetFormFields();
    }

    private void resetForm() {
        resetFormFields();
        loadData();
        lastPembayaran = null;
        lastTransaksi  = null;
        btnCetakStruk.setEnabled(false);
    }

    private void resetFormFields() {
        txtNamaPelanggan.setText(""); txtJenisLaundry.setText("");
        txtBerat.setText(""); txtTotalTagihan.setText("");
        txtUangBayar.setText(""); txtKembalian.setText("");
    }


    private JPanel createRow(String labelText, JComponent field, boolean readOnly) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(COLOR_SUBTEXT);
        lbl.setPreferredSize(new Dimension(170, 20));

        row.add(lbl,   BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private JTextField createReadonlyField() {
        JTextField f = new JTextField();
        f.setEditable(false);
        f.setBackground(new Color(0x1E2A4A));
        f.setForeground(COLOR_TEXT);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT, 1), new EmptyBorder(5,8,5,8)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return f;
    }

    private void styleField(JTextField f) {
        f.setBackground(COLOR_FIELD_BG); f.setForeground(COLOR_TEXT);
        f.setCaretColor(Color.WHITE); f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT, 1), new EmptyBorder(6,10,6,10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    }

    private void styleCombo(JComboBox<?> c) {
        c.setBackground(COLOR_FIELD_BG); c.setForeground(COLOR_TEXT);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private void styleBtn(JButton b, Color bg) {
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class TransaksiItem {
        final String id, nama;
        final double total;
        TransaksiItem(String id, String nama, double total) {
            this.id=id; this.nama=nama; this.total=total;
        }
        @Override public String toString() {
            return id + " | " + nama + " | Rp " + String.format("%,.0f", total);
        }
    }
}
