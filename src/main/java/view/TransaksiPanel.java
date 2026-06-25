/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package view;

import controller.TransaksiController;
import model.Pelanggan;
import model.Transaksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class TransaksiPanel extends JPanel {

    private final TransaksiController controller;
    private final NumberFormat         nf;

    private JTextField  txtId;
    private JComboBox<ComboItem> cmbPelanggan;
    private JComboBox<String>    cmbJenis;
    private JTextField  txtBerat;
    private JTextField  txtHargaPerKg;
    private JTextField  txtTotalHarga;
    private JSpinner    spTglMasuk;
    private JSpinner    spTglSelesai;
    private JComboBox<String> cmbStatus;
    private JTextArea   txtCatatan;
    private JTextField  txtCari;
    private JComboBox<String> cmbFilterStatus;

    private JButton btnTambah, btnSimpan, btnEdit, btnHapus, btnReset, btnCari;

    private JTable             tabel;
    private DefaultTableModel  tabelModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private boolean isEditMode = false;

    private static final Color COLOR_BG       = new Color(0x1A1A2E);
    private static final Color COLOR_CARD     = new Color(0x16213E);
    private static final Color COLOR_ACCENT   = new Color(0x0F3460);
    private static final Color COLOR_PRIMARY  = new Color(0x1A73E8);
    private static final Color COLOR_SUCCESS  = new Color(0x43A047);
    private static final Color COLOR_WARNING  = new Color(0xF57F17);
    private static final Color COLOR_DANGER   = new Color(0xC62828);
    private static final Color COLOR_TEXT     = new Color(0xE0E0E0);
    private static final Color COLOR_SUBTEXT  = new Color(0x9E9E9E);
    private static final Color COLOR_FIELD_BG = new Color(0x0F3460);

    private static final String[] JENIS_LAUNDRY = {
        Transaksi.JENIS_CUCI_KERING, Transaksi.JENIS_CUCI_SETRIKA,
        Transaksi.JENIS_SETRIKA_SAJA, Transaksi.JENIS_EXPRESS
    };

    private static final String[] STATUS_LIST = {
        Transaksi.STATUS_PROSES,    Transaksi.STATUS_DICUCI,
        Transaksi.STATUS_DISETRIKA, Transaksi.STATUS_SELESAI,
        Transaksi.STATUS_SUDAH_DIAMBIL
    };

    private static final String[] COLUMNS = {
        "ID Transaksi", "Pelanggan", "Jenis", "Berat (Kg)",
        "Harga/Kg", "Total", "Tgl Masuk", "Tgl Selesai", "Status"
    };

    private static final double[] HARGA_DEFAULT = {5000, 7000, 4000, 15000};

    public TransaksiPanel() {
        this.controller = new TransaksiController();
        this.nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(createHeader(), BorderLayout.NORTH);

        JPanel form = createFormPanel();
        JPanel table = createTablePanel();
        setupFormListeners();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                form, table);
        split.setDividerLocation(340);
        split.setDividerSize(6);
        split.setBorder(null);
        split.setOpaque(false);
        add(split, BorderLayout.CENTER);
    }


    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = new JLabel("Transaksi Laundry");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Kelola transaksi laundry pelanggan");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(COLOR_SUBTEXT);
        JPanel titleCol = new JPanel(new GridLayout(2, 1, 0, 2));
        titleCol.setOpaque(false);
        titleCol.add(lblTitle); titleCol.add(lblSub);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setOpaque(false);

        txtCari = new JTextField(16);
        txtCari.setName("txtCariTransaksi");
        txtCari.putClientProperty("JTextField.placeholderText", "Cari nama / ID...");
        styleField(txtCari);
        txtCari.setPreferredSize(new Dimension(180, 34));

        cmbFilterStatus = new JComboBox<>(new String[]{"Semua", Transaksi.STATUS_PROSES,
            Transaksi.STATUS_DICUCI, Transaksi.STATUS_DISETRIKA,
            Transaksi.STATUS_SELESAI, Transaksi.STATUS_SUDAH_DIAMBIL});
        cmbFilterStatus.setName("cmbFilterStatus");
        styleCombo(cmbFilterStatus);
        cmbFilterStatus.setPreferredSize(new Dimension(140, 34));

        btnCari = createButton("Filter", COLOR_ACCENT, "btnCariTransaksi");

        searchPanel.add(new JLabel("<html><font color='#9E9E9E'>Filter:</font></html>"));
        searchPanel.add(cmbFilterStatus);
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);

        header.add(titleCol,    BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);
        return header;
    }


    private JPanel createFormPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(new EmptyBorder(16, 16, 12, 16));

        JLabel lblFormTitle = new JLabel("Form Transaksi Laundry");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFormTitle.setForeground(Color.WHITE);
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(4, 6, 4, 6);
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.anchor  = GridBagConstraints.WEST;

        gbc(g, 0,0,0); formGrid.add(createLabel("ID Transaksi:"), g);
        gbc(g, 1,0,0.2);
        txtId = new JTextField(); txtId.setName("txtIdTransaksi");
        txtId.setEditable(false);
        txtId.setBackground(new Color(0x1E2A4A)); txtId.setForeground(new Color(0x64B5F6));
        txtId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtId.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT,1), new EmptyBorder(5,8,5,8)));
        formGrid.add(txtId, g);

        gbc(g, 2,0,0); formGrid.add(createLabel("Pelanggan:"), g);
        gbc(g, 3,0,0.3);
        cmbPelanggan = new JComboBox<>(); cmbPelanggan.setName("cmbPelanggan");
        styleCombo(cmbPelanggan);
        formGrid.add(cmbPelanggan, g);

        gbc(g, 4,0,0); formGrid.add(createLabel("Status:"), g);
        gbc(g, 5,0,0.2);
        cmbStatus = new JComboBox<>(STATUS_LIST); cmbStatus.setName("cmbStatus");
        styleCombo(cmbStatus);
        formGrid.add(cmbStatus, g);

        gbc(g, 0,1,0); formGrid.add(createLabel("Jenis Laundry:"), g);
        gbc(g, 1,1,0.2);
        cmbJenis = new JComboBox<>(JENIS_LAUNDRY); cmbJenis.setName("cmbJenis");
        styleCombo(cmbJenis);
        formGrid.add(cmbJenis, g);

        gbc(g, 2,1,0); formGrid.add(createLabel("Berat (Kg):"), g);
        gbc(g, 3,1,0.15);
        txtBerat = new JTextField(); txtBerat.setName("txtBerat");
        txtBerat.putClientProperty("JTextField.placeholderText","Contoh: 2.5");
        styleField(txtBerat); formGrid.add(txtBerat, g);

        gbc(g, 4,1,0); formGrid.add(createLabel("Harga/Kg (Rp):"), g);
        gbc(g, 5,1,0.15);
        txtHargaPerKg = new JTextField(); txtHargaPerKg.setName("txtHargaPerKg");
        txtHargaPerKg.putClientProperty("JTextField.placeholderText","Contoh: 7000");
        styleField(txtHargaPerKg); formGrid.add(txtHargaPerKg, g);

        gbc(g, 0,2,0); formGrid.add(createLabel("Total Harga (Rp):"), g);
        gbc(g, 1,2,0.2);
        txtTotalHarga = new JTextField(); txtTotalHarga.setName("txtTotalHarga");
        txtTotalHarga.setEditable(false);
        txtTotalHarga.setBackground(new Color(0x1B5E20)); txtTotalHarga.setForeground(new Color(0xA5D6A7));
        txtTotalHarga.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTotalHarga.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x2E7D32),1), new EmptyBorder(5,8,5,8)));
        formGrid.add(txtTotalHarga, g);

        gbc(g, 2,2,0); formGrid.add(createLabel("Tanggal Masuk:"), g);
        gbc(g, 3,2,0.15);
        spTglMasuk = createDateSpinner(); spTglMasuk.setName("spTglMasuk");
        formGrid.add(spTglMasuk, g);

        gbc(g, 4,2,0); formGrid.add(createLabel("Tanggal Selesai:"), g);
        gbc(g, 5,2,0.15);
        spTglSelesai = createDateSpinner(); spTglSelesai.setName("spTglSelesai");
        formGrid.add(spTglSelesai, g);


        gbc(g, 0,3,0); formGrid.add(createLabel("Catatan:"), g);
        g.gridx=1; g.gridy=3; g.weightx=0.8; g.gridwidth=5; g.fill=GridBagConstraints.BOTH;
        txtCatatan = new JTextArea(2,30); txtCatatan.setName("txtCatatan");
        txtCatatan.setLineWrap(true); txtCatatan.setWrapStyleWord(true);
        txtCatatan.setBackground(COLOR_FIELD_BG); txtCatatan.setForeground(COLOR_TEXT);
        txtCatatan.setFont(new Font("Segoe UI",Font.PLAIN,13)); txtCatatan.setCaretColor(Color.WHITE);
        txtCatatan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT,1), new EmptyBorder(5,8,5,8)));
        JScrollPane catScroll = new JScrollPane(txtCatatan); catScroll.setBorder(null);
        formGrid.add(catScroll, g);
        g.gridwidth=1; g.fill=GridBagConstraints.HORIZONTAL;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnTambah = createButton("➕ Tambah",  COLOR_PRIMARY,  "btnTambahTransaksi");
        btnSimpan = createButton("💾 Simpan",  COLOR_SUCCESS,  "btnSimpanTransaksi");
        btnEdit   = createButton("✏ Edit",    COLOR_WARNING,  "btnEditTransaksi");
        btnHapus  = createButton("🗑 Hapus",   COLOR_DANGER,   "btnHapusTransaksi");
        btnReset  = createButton("↺ Reset",   COLOR_ACCENT,   "btnResetTransaksi");

        btnSimpan.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

        btnPanel.add(btnTambah); btnPanel.add(btnSimpan);
        btnPanel.add(btnEdit);   btnPanel.add(btnHapus);
        btnPanel.add(btnReset);

        card.add(lblFormTitle, BorderLayout.NORTH);
        card.add(formGrid,     BorderLayout.CENTER);
        card.add(btnPanel,     BorderLayout.SOUTH);

        return card;
    }

    private void gbc(GridBagConstraints g, int x, int y, double wx) {
        g.gridx=x; g.gridy=y; g.weightx=wx;
    }


    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(12,0,0,0));

        tabelModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabel = new JTable(tabelModel);
        tabel.setName("tabelTransaksi");
        tabel.setRowHeight(36);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.setShowVerticalLines(false);
        tabel.setGridColor(new Color(0x2D2D3A));
        tabel.setSelectionBackground(COLOR_PRIMARY);
        tabel.setSelectionForeground(Color.WHITE);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(COLOR_ACCENT);
        tabel.getTableHeader().setForeground(COLOR_TEXT);
        tabel.getTableHeader().setReorderingAllowed(false);
        tabel.setAutoCreateRowSorter(true);


        int[] widths = {150, 150, 110, 80, 90, 110, 90, 90, 110};
        for (int i = 0; i < widths.length; i++) {
            tabel.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        sorter = new TableRowSorter<>(tabelModel);
        tabel.setRowSorter(sorter);

        tabel.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    int modelRow = t.convertRowIndexToModel(row);
                    String status = (String) tabelModel.getValueAt(modelRow, 8);
                    c.setBackground(switch (status != null ? status : "") {
                        case Transaksi.STATUS_PROSES    -> new Color(0x1A2E3A);
                        case Transaksi.STATUS_DICUCI    -> new Color(0x1A2A3E);
                        case Transaksi.STATUS_DISETRIKA -> new Color(0x2A1A3E);
                        case Transaksi.STATUS_SELESAI   -> new Color(0x1A3A1A);
                        case Transaksi.STATUS_SUDAH_DIAMBIL -> new Color(0x2D2D3A);
                        default                         -> new Color(0x16213E);
                    });
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT, 1));
        scroll.getViewport().setBackground(new Color(0x16213E));

        JLabel lblTotal = new JLabel();
        lblTotal.setForeground(COLOR_SUBTEXT);
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTotal.setBorder(new EmptyBorder(4,0,0,0));
        tabelModel.addTableModelListener(e -> lblTotal.setText("Total: " + tabelModel.getRowCount() + " transaksi"));

        panel.add(scroll,    BorderLayout.CENTER);
        panel.add(lblTotal,  BorderLayout.SOUTH);
        return panel;
    }

    private void setupFormListeners() {

        btnTambah.addActionListener(e -> {
            resetForm();
            txtId.setText(controller.generateId());
            isEditMode = false;
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
            loadComboData();
            cmbPelanggan.requestFocus();
        });

        btnSimpan.addActionListener(e -> {
            if (isEditMode) doUpdate();
            else doSimpan();
        });

        btnEdit.addActionListener(e -> {
            isEditMode = true;
            setFormEditable(true);
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
        });

        btnHapus.addActionListener(e -> doHapus());

        btnReset.addActionListener(e -> { resetForm(); loadData(); });

        btnCari.addActionListener(e -> doCari());
        txtCari.addActionListener(e -> doCari());
        cmbFilterStatus.addActionListener(e -> doCari());

        javax.swing.event.DocumentListener autoHitung = new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { hitungTotal(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { hitungTotal(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        };
        txtBerat.getDocument().addDocumentListener(autoHitung);
        txtHargaPerKg.getDocument().addDocumentListener(autoHitung);

        cmbJenis.addActionListener(e -> {
            int idx = cmbJenis.getSelectedIndex();
            if (idx >= 0 && idx < HARGA_DEFAULT.length) {
                txtHargaPerKg.setText(String.valueOf((int) HARGA_DEFAULT[idx]));
            }
        });

        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabel.getSelectedRow();
                if (row >= 0) {
                    int modelRow = tabel.convertRowIndexToModel(row);
                    String id = (String) tabelModel.getValueAt(modelRow, 0);
                    Transaksi t = controller.getById(id);
                    if (t != null) tampilkanDiForm(t);
                }
            }
        });
    }


    private void doSimpan() {
        ComboItem pi = (ComboItem) cmbPelanggan.getSelectedItem();
        String idPelanggan = pi != null ? pi.id : "";
        String err = controller.simpan(
            txtId.getText(), idPelanggan,
            (String) cmbJenis.getSelectedItem(),
            txtBerat.getText(), txtHargaPerKg.getText(),
            getDateFromSpinner(spTglMasuk),
            getDateFromSpinner(spTglSelesai),
            (String) cmbStatus.getSelectedItem(),
            txtCatatan.getText()
        );
        if (err != null) showError(err);
        else { showSuccess("Transaksi berhasil disimpan!"); resetForm(); loadData(); }
    }

    private void doUpdate() {
        ComboItem pi = (ComboItem) cmbPelanggan.getSelectedItem();
        String idPelanggan = pi != null ? pi.id : "";
        String err = controller.update(
            txtId.getText(), idPelanggan,
            (String) cmbJenis.getSelectedItem(),
            txtBerat.getText(), txtHargaPerKg.getText(),
            getDateFromSpinner(spTglMasuk),
            getDateFromSpinner(spTglSelesai),
            (String) cmbStatus.getSelectedItem(),
            txtCatatan.getText()
        );
        if (err != null) showError(err);
        else { showSuccess("Transaksi berhasil diperbarui!"); resetForm(); loadData(); }
    }

    private void doHapus() {
        if (txtId.getText().isEmpty()) { showError("Pilih transaksi yang akan dihapus!"); return; }
        int opt = JOptionPane.showConfirmDialog(this,
            "Hapus transaksi " + txtId.getText() + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            String err = controller.hapus(txtId.getText());
            if (err != null) showError(err);
            else { showSuccess("Transaksi berhasil dihapus!"); resetForm(); loadData(); }
        }
    }

    private void doCari() {
        String keyword = txtCari.getText().trim();
        String status  = (String) cmbFilterStatus.getSelectedItem();
        List<Transaksi> hasil;
        if (!keyword.isEmpty()) {
            hasil = controller.cari(keyword);
        } else {
            hasil = controller.filterByStatus(status);
        }
        isiTabel(hasil);
    }

    private void hitungTotal() {
        double total = controller.hitungTotalHarga(txtBerat.getText(), txtHargaPerKg.getText());
        if (total > 0) {
            txtTotalHarga.setText(nf.format(total));
        } else {
            txtTotalHarga.setText("");
        }
    }


    public void loadData() {
        loadComboData();
        SwingWorker<List<Transaksi>, Void> w = new SwingWorker<>() {
            @Override protected List<Transaksi> doInBackground() { return controller.getAll(); }
            @Override protected void done() {
                try { isiTabel(get()); } catch (Exception ex) { showError(ex.getMessage()); }
            }
        };
        w.execute();
    }

    private void loadComboData() {
        cmbPelanggan.removeAllItems();
        List<Pelanggan> list = controller.getAllPelanggan();
        for (Pelanggan p : list) {
            cmbPelanggan.addItem(new ComboItem(p.getIdPelanggan(), p.getNama()));
        }
    }

    private void isiTabel(List<Transaksi> list) {
        tabelModel.setRowCount(0);
        for (Transaksi t : list) {
            tabelModel.addRow(new Object[]{
                t.getIdTransaksi(),
                t.getNamaPelanggan(),
                t.getJenisLaundry(),
                String.format("%.2f", t.getBerat()),
                nf.format(t.getHargaPerKg()),
                nf.format(t.getTotalHarga()),
                t.getTglMasuk() != null ? t.getTglMasuk().toString() : "-",
                t.getTglSelesai() != null ? t.getTglSelesai().toString() : "-",
                t.getStatus()
            });
        }
    }

    private void tampilkanDiForm(Transaksi t) {
        txtId.setText(t.getIdTransaksi());

        for (int i = 0; i < cmbPelanggan.getItemCount(); i++) {
            if (cmbPelanggan.getItemAt(i).id.equals(t.getIdPelanggan())) {
                cmbPelanggan.setSelectedIndex(i); break;
            }
        }
        cmbJenis.setSelectedItem(t.getJenisLaundry());
        txtBerat.setText(String.valueOf(t.getBerat()));
        txtHargaPerKg.setText(String.valueOf((int) t.getHargaPerKg()));
        txtTotalHarga.setText(nf.format(t.getTotalHarga()));
        if (t.getTglMasuk() != null)
            spTglMasuk.setValue(new java.util.Date(t.getTglMasuk().getTime()));
        if (t.getTglSelesai() != null)
            spTglSelesai.setValue(new java.util.Date(t.getTglSelesai().getTime()));
        cmbStatus.setSelectedItem(t.getStatus());
        txtCatatan.setText(t.getCatatan() != null ? t.getCatatan() : "");
        setFormEditable(false);
        isEditMode = false;
        btnSimpan.setEnabled(false);
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    private void resetForm() {
        txtId.setText(""); txtBerat.setText(""); txtHargaPerKg.setText(""); txtTotalHarga.setText("");
        txtCatatan.setText(""); txtCari.setText("");
        cmbPelanggan.setSelectedIndex(-1);
        cmbJenis.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        spTglMasuk.setValue(new java.util.Date());
        spTglSelesai.setValue(new java.util.Date());
        setFormEditable(true);
        isEditMode = false;
        btnSimpan.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        tabel.clearSelection();
        if (sorter != null) sorter.setRowFilter(null);
    }

    private void setFormEditable(boolean editable) {
        txtBerat.setEditable(editable);
        txtHargaPerKg.setEditable(editable);
        txtCatatan.setEditable(editable);
        cmbPelanggan.setEnabled(editable);
        cmbJenis.setEnabled(editable);
        cmbStatus.setEnabled(editable);
        spTglMasuk.setEnabled(editable);
        spTglSelesai.setEnabled(editable);
    }

    private JSpinner createDateSpinner() {
        JSpinner sp = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(sp, "dd/MM/yyyy");
        sp.setEditor(editor);
        sp.setBackground(COLOR_FIELD_BG);
        sp.setForeground(COLOR_TEXT);
        return sp;
    }

    private Date getDateFromSpinner(JSpinner sp) {
        java.util.Date d = (java.util.Date) sp.getValue();
        return new Date(d.getTime());
    }


    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(COLOR_TEXT);
        return l;
    }

    private void styleField(JTextField f) {
        f.setBackground(COLOR_FIELD_BG); f.setForeground(COLOR_TEXT);
        f.setCaretColor(Color.WHITE); f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT, 1), new EmptyBorder(5,8,5,8)));
    }

    private void styleCombo(JComboBox<?> cmb) {
        cmb.setBackground(COLOR_FIELD_BG); cmb.setForeground(COLOR_TEXT);
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private JButton createButton(String text, Color bg, String name) {
        JButton btn = new JButton(text);
        btn.setName(name); btn.setBackground(bg); btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 34));
        return btn;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

 
    private static class ComboItem {
        final String id, nama;
        ComboItem(String id, String nama) { this.id=id; this.nama=nama; }
        @Override public String toString() { return id + " - " + nama; }
    }
}
