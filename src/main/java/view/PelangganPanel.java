/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package view;

import controller.PelangganController;
import model.Pelanggan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class PelangganPanel extends JPanel {

    private final PelangganController controller;

    private JTextField txtId;
    private JTextField txtNama;
    private JTextField txtNoHp;
    private JTextArea  txtAlamat;
    private JTextField txtCari;

    private JButton btnTambah;
    private JButton btnSimpan;
    private JButton btnEdit;
    private JButton btnHapus;
    private JButton btnReset;
    private JButton btnCari;

    private JTable           tabel;
    private DefaultTableModel tabelModel;
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

    private static final String[] COLUMNS = {
        "ID Pelanggan", "Nama", "No. HP", "Alamat", "Tgl. Daftar"
    };

    public PelangganPanel() {
        this.controller = new PelangganController();
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

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                form, table);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        splitPane.setBackground(COLOR_BG);
        add(splitPane, BorderLayout.CENTER);
    }


    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = new JLabel("Data Pelanggan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Kelola data pelanggan laundry");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(COLOR_SUBTEXT);

        JPanel titleCol = new JPanel(new GridLayout(2, 1, 0, 2));
        titleCol.setOpaque(false);
        titleCol.add(lblTitle);
        titleCol.add(lblSub);

        JPanel cariPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        cariPanel.setOpaque(false);

        txtCari = new JTextField(20);
        txtCari.setName("txtCariPelanggan");
        txtCari.putClientProperty("JTextField.placeholderText", "Cari nama / ID / HP...");
        styleField(txtCari);
        txtCari.setPreferredSize(new Dimension(220, 36));

        btnCari = createButton("Cari", COLOR_ACCENT, "btnCariPelanggan");

        cariPanel.add(txtCari);
        cariPanel.add(btnCari);

        header.add(titleCol,  BorderLayout.WEST);
        header.add(cariPanel, BorderLayout.EAST);
        return header;
    }


    private JPanel createFormPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lblFormTitle = new JLabel("Form Data Pelanggan");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFormTitle.setForeground(Color.WHITE);
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(4, 4, 4, 4);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0;
        formGrid.add(createLabel("ID Pelanggan:"), gbc);
        gbc.gridx=1; gbc.gridy=0; gbc.weightx=0.4;
        txtId = new JTextField();
        txtId.setName("txtIdPelanggan");
        txtId.setEditable(false);
        txtId.setBackground(new Color(0x1E2A4A));
        txtId.setForeground(new Color(0x64B5F6));
        txtId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtId.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT, 1),
            new EmptyBorder(6, 10, 6, 10)));
        formGrid.add(txtId, gbc);

        gbc.gridx=2; gbc.gridy=0; gbc.weightx=0;
        formGrid.add(Box.createHorizontalStrut(16), gbc);
        gbc.gridx=3; gbc.gridy=0; gbc.weightx=0;
        formGrid.add(createLabel("Nomor HP:"), gbc);
        gbc.gridx=4; gbc.gridy=0; gbc.weightx=0.4;
        txtNoHp = new JTextField();
        txtNoHp.setName("txtNoHp");
        txtNoHp.putClientProperty("JTextField.placeholderText", "08xxxxxxxxxx");
        styleField(txtNoHp);
        formGrid.add(txtNoHp, gbc);

        gbc.gridx=0; gbc.gridy=1; gbc.weightx=0;
        formGrid.add(createLabel("Nama Pelanggan:"), gbc);
        gbc.gridx=1; gbc.gridy=1; gbc.weightx=0.4;
        txtNama = new JTextField();
        txtNama.setName("txtNamaPelanggan");
        txtNama.putClientProperty("JTextField.placeholderText", "Nama lengkap pelanggan");
        styleField(txtNama);
        formGrid.add(txtNama, gbc);

        gbc.gridx=3; gbc.gridy=1; gbc.weightx=0;
        formGrid.add(createLabel("Alamat:"), gbc);
        gbc.gridx=4; gbc.gridy=1; gbc.weightx=0.4; gbc.gridheight=2;
        gbc.fill = GridBagConstraints.BOTH;
        txtAlamat = new JTextArea(3, 20);
        txtAlamat.setName("txtAlamat");
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        txtAlamat.setBackground(COLOR_FIELD_BG);
        txtAlamat.setForeground(COLOR_TEXT);
        txtAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtAlamat.setCaretColor(Color.WHITE);
        txtAlamat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT, 1),
            new EmptyBorder(6, 10, 6, 10)));
        JScrollPane alamatScroll = new JScrollPane(txtAlamat);
        alamatScroll.setBorder(null);
        formGrid.add(alamatScroll, gbc);
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        btnTambah = createButton("➕ Tambah",  COLOR_PRIMARY,  "btnTambahPelanggan");
        btnSimpan = createButton("💾 Simpan",  COLOR_SUCCESS,  "btnSimpanPelanggan");
        btnEdit   = createButton("✏ Edit",    COLOR_WARNING,  "btnEditPelanggan");
        btnHapus  = createButton("🗑 Hapus",   COLOR_DANGER,   "btnHapusPelanggan");
        btnReset  = createButton("↺ Reset",   COLOR_ACCENT,   "btnResetPelanggan");

        btnSimpan.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

        btnPanel.add(btnTambah);
        btnPanel.add(btnSimpan);
        btnPanel.add(btnEdit);
        btnPanel.add(btnHapus);
        btnPanel.add(btnReset);

        card.add(lblFormTitle, BorderLayout.NORTH);
        card.add(formGrid,     BorderLayout.CENTER);
        card.add(btnPanel,     BorderLayout.SOUTH);

        return card;
    }


    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        tabelModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabel = new JTable(tabelModel);
        tabel.setName("tabelPelanggan");
        tabel.setRowHeight(36);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabel.setShowVerticalLines(false);
        tabel.setGridColor(new Color(0x2D2D3A));
        tabel.setSelectionBackground(new Color(0x1A73E8, true));
        tabel.setSelectionForeground(Color.WHITE);
        tabel.setIntercellSpacing(new Dimension(0, 1));
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabel.getTableHeader().setBackground(COLOR_ACCENT);
        tabel.getTableHeader().setForeground(COLOR_TEXT);
        tabel.getTableHeader().setReorderingAllowed(false);
        tabel.setAutoCreateRowSorter(true);

        tabel.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(300);
        tabel.getColumnModel().getColumn(4).setPreferredWidth(150);

        sorter = new TableRowSorter<>(tabelModel);
        tabel.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(tabel);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT, 1));
        scrollPane.getViewport().setBackground(new Color(0x16213E));

        JLabel lblTotal = new JLabel();
        lblTotal.setForeground(COLOR_SUBTEXT);
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTotal.setBorder(new EmptyBorder(4, 0, 0, 0));

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(lblTotal,   BorderLayout.SOUTH);

        tabelModel.addTableModelListener(e -> {
            lblTotal.setText("Total: " + tabelModel.getRowCount() + " pelanggan");
        });

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
            txtNama.requestFocus();
        });


        btnSimpan.addActionListener(e -> {
            if (isEditMode) {
                doUpdate();
            } else {
                doSimpan();
            }
        });

 
        btnEdit.addActionListener(e -> {
            isEditMode = true;
            txtNama.setEditable(true);
            txtNoHp.setEditable(true);
            txtAlamat.setEditable(true);
            btnSimpan.setEnabled(true);
            btnEdit.setEnabled(false);
            txtNama.requestFocus();
        });


        btnHapus.addActionListener(e -> doHapus());


        btnReset.addActionListener(e -> {
            resetForm();
            loadData();
        });


        btnCari.addActionListener(e -> doCari());
        txtCari.addActionListener(e -> doCari());


        txtCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { doCariRealtime(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { doCariRealtime(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { doCariRealtime(); }
        });

        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabel.getSelectedRow();
                if (row >= 0) {
                    int modelRow = tabel.convertRowIndexToModel(row);
                    String id = (String) tabelModel.getValueAt(modelRow, 0);
                    Pelanggan p = controller.getById(id);
                    if (p != null) tampilkanDiForm(p);
                }
            }
        });
    }


    private void doSimpan() {
        String err = controller.simpan(
            txtId.getText(), txtNama.getText(), txtNoHp.getText(), txtAlamat.getText()
        );
        if (err != null) {
            showError(err);
        } else {
            showSuccess("Pelanggan berhasil disimpan!");
            resetForm();
            loadData();
        }
    }

    private void doUpdate() {
        String err = controller.update(
            txtId.getText(), txtNama.getText(), txtNoHp.getText(), txtAlamat.getText()
        );
        if (err != null) {
            showError(err);
        } else {
            showSuccess("Data pelanggan berhasil diperbarui!");
            resetForm();
            loadData();
        }
    }

    private void doHapus() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            showError("Pilih pelanggan yang akan dihapus terlebih dahulu!");
            return;
        }
        int opt = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus pelanggan:\n" + txtNama.getText() + " (" + id + ")?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            String err = controller.hapus(id);
            if (err != null) {
                showError(err);
            } else {
                showSuccess("Pelanggan berhasil dihapus!");
                resetForm();
                loadData();
            }
        }
    }

    private void doCari() {
        String keyword = txtCari.getText().trim();
        List<Pelanggan> hasil = controller.cari(keyword);
        isiTabel(hasil);
    }

    private void doCariRealtime() {
        String keyword = txtCari.getText().trim();
        if (sorter != null) {
            if (keyword.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
            }
        }
    }

    public void loadData() {
        SwingWorker<List<Pelanggan>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Pelanggan> doInBackground() {
                return controller.getAll();
            }
            @Override
            protected void done() {
                try {
                    isiTabel(get());
                } catch (Exception ex) {
                    showError("Gagal memuat data: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void isiTabel(List<Pelanggan> list) {
        tabelModel.setRowCount(0);
        for (Pelanggan p : list) {
            tabelModel.addRow(new Object[]{
                p.getIdPelanggan(),
                p.getNama(),
                p.getNoHp(),
                p.getAlamat(),
                p.getCreatedAt() != null
                    ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(p.getCreatedAt())
                    : "-"
            });
        }
    }

    private void tampilkanDiForm(Pelanggan p) {
        txtId.setText(p.getIdPelanggan());
        txtNama.setText(p.getNama());
        txtNoHp.setText(p.getNoHp());
        txtAlamat.setText(p.getAlamat());
        txtNama.setEditable(false);
        txtNoHp.setEditable(false);
        txtAlamat.setEditable(false);
        isEditMode = false;
        btnSimpan.setEnabled(false);
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    private void resetForm() {
        txtId.setText("");
        txtNama.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        txtNama.setEditable(true);
        txtNoHp.setEditable(true);
        txtAlamat.setEditable(true);
        isEditMode = false;
        btnSimpan.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        tabel.clearSelection();
        txtCari.setText("");
        if (sorter != null) sorter.setRowFilter(null);
    }


    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT);
        return lbl;
    }

    private void styleField(JTextField f) {
        f.setBackground(COLOR_FIELD_BG);
        f.setForeground(COLOR_TEXT);
        f.setCaretColor(Color.WHITE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT, 1),
            new EmptyBorder(6, 10, 6, 10)));
    }

    private JButton createButton(String text, Color bg, String name) {
        JButton btn = new JButton(text);
        btn.setName(name);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
        return btn;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }
}
