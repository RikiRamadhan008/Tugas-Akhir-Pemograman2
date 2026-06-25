/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package view;

import controller.PelangganController;
import controller.TransaksiController;
import model.Transaksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class DashboardPanel extends JPanel {

    private final PelangganController pelangganCtrl;
    private final TransaksiController transaksiCtrl;
    private final MainFrame           parentFrame;

    private JLabel lblTotalPelanggan;
    private JLabel lblTotalTransaksi;
    private JLabel lblTotalPendapatan;
    private JLabel lblProses;
    private JLabel lblSelesai;
    private JLabel lblSudahDiambil;

    private static final Color COLOR_BG     = new Color(0x1A1A2E);
    private static final Color COLOR_CARD   = new Color(0x16213E);
    private static final Color COLOR_TEXT   = new Color(0xE0E0E0);
    private static final Color COLOR_SUB    = new Color(0x9E9E9E);

    private static final Color[] CARD_COLORS = {
        new Color(0x1A73E8),   
        new Color(0x00ACC1),   
        new Color(0x43A047),   
        new Color(0xF4511E),   
        new Color(0x7B1FA2),   
        new Color(0x37474F),   
    };

    public DashboardPanel(MainFrame parentFrame) {
        this.parentFrame   = parentFrame;
        this.pelangganCtrl = new PelangganController();
        this.transaksiCtrl = new TransaksiController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel lblTitle = new JLabel("Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Ringkasan data MyLaundry");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(COLOR_SUB);

        JButton btnRefresh = new JButton(" Refresh");
        btnRefresh.setName("btnRefresh");
        btnRefresh.setBackground(new Color(0x0F3460));
        btnRefresh.setForeground(COLOR_TEXT);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> refreshStats());

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);


        JPanel contentPanel = new JPanel(new GridLayout(1, 1));
        contentPanel.setOpaque(false);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);

        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 16, 16));
        statsGrid.setOpaque(false);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        lblTotalPelanggan = new JLabel("0");
        statsGrid.add(createStatCard("👥", "Total Pelanggan", lblTotalPelanggan,
                CARD_COLORS[0], "Klik untuk kelola pelanggan",
                e -> parentFrame.navigateTo(MainFrame.PANEL_PELANGGAN, null)));

      
        lblTotalTransaksi = new JLabel("0");
        statsGrid.add(createStatCard("🧾", "Total Transaksi", lblTotalTransaksi,
                CARD_COLORS[1], "Klik untuk lihat transaksi",
                e -> parentFrame.navigateTo(MainFrame.PANEL_TRANSAKSI, null)));

      
        lblTotalPendapatan = new JLabel("Rp 0");
        statsGrid.add(createStatCard("💰", "Total Pendapatan", lblTotalPendapatan,
                CARD_COLORS[2], "Total dari seluruh transaksi", null));


        lblProses = new JLabel("0");
        statsGrid.add(createStatCard("⏳", "Sedang Diproses", lblProses,
                CARD_COLORS[3], "Laundry dalam status Proses/Dicuci/Disetrika", null));

    
        lblSelesai = new JLabel("0");
        statsGrid.add(createStatCard("✅", "Selesai", lblSelesai,
                CARD_COLORS[4], "Laundry siap diambil", null));

       
        lblSudahDiambil = new JLabel("0");
        statsGrid.add(createStatCard("📦", "Sudah Diambil", lblSudahDiambil,
                CARD_COLORS[5], "Laundry yang sudah diambil pelanggan", null));

        scrollContent.add(statsGrid);
        scrollContent.add(Box.createVerticalStrut(24));

   
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        bottomPanel.setOpaque(false);

     
        bottomPanel.add(createInfoCard());
 
        bottomPanel.add(createQuickActionCard());

        scrollContent.add(bottomPanel);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }

   
    private JPanel createStatCard(String icon, String title, JLabel valueLabel,
                                   Color accentColor, String tooltip,
                                   java.awt.event.ActionListener clickAction) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setToolTipText(tooltip);

        // Icon + Title
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topRow.setOpaque(false);
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(COLOR_SUB);
        topRow.add(lblIcon);
        topRow.add(lblTitle);


        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);


        JPanel bottomLine = new JPanel();
        bottomLine.setBackground(accentColor);
        bottomLine.setPreferredSize(new Dimension(0, 2));
        bottomLine.setOpaque(true);

        card.add(topRow,      BorderLayout.NORTH);
        card.add(valueLabel,  BorderLayout.CENTER);
        card.add(bottomLine,  BorderLayout.SOUTH);

        if (clickAction != null) {
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                Color original = COLOR_CARD;
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    clickAction.actionPerformed(new java.awt.event.ActionEvent(card, 0, "click"));
                }
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    card.setBackground(new Color(0x1E2A4A));
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    card.setBackground(original);
                }
            });
        }

        return card;
    }

   
    private JPanel createInfoCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(new EmptyBorder(20, 20, 100, 20));

        JLabel lblTitle = new JLabel("Informasi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        String infoHtml = "<html><font color='#9E9E9E' size='3'>"
                + ""
                + ""
                + ""
                + ""
                + ""
                + ""
                + "<b>Harga Laundry:</b><br><br>"
                + "<table>"
                + "<tr><td>Cuci Kering</td><td>: Rp 5.000/Kg</td></tr>"
                + "<tr><td>Cuci Setrika</td><td>: Rp 7.000/Kg</td></tr>"
                + "<tr><td>Setrika</td><td>: Rp 4.000/Kg</td></tr>"
                + "<tr><td>Express</td><td>: Rp 15.000/Kg</td></tr>"
                + "</table>"
                + "</font></html>";
        JLabel lblInfo = new JLabel(infoHtml);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblInfo,  BorderLayout.CENTER);
        return card;
    }

  
    private JPanel createQuickActionCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Akses Cepat");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(new EmptyBorder(0, 0, 16, 0));
        card.add(lblTitle, BorderLayout.NORTH);

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        btnGrid.setOpaque(false);

        btnGrid.add(createQuickBtn("➕ Pelanggan Baru", new Color(0x1A73E8),
                e -> parentFrame.navigateTo(MainFrame.PANEL_PELANGGAN, null)));
        btnGrid.add(createQuickBtn("🧾 Transaksi Baru", new Color(0x00ACC1),
                e -> parentFrame.navigateTo(MainFrame.PANEL_TRANSAKSI, null)));
        btnGrid.add(createQuickBtn("💳 Pembayaran", new Color(0x43A047),
                e -> parentFrame.navigateTo(MainFrame.PANEL_PEMBAYARAN, null)));
        btnGrid.add(createQuickBtn("📊 Laporan", new Color(0x7B1FA2),
                e -> parentFrame.navigateTo(MainFrame.PANEL_LAPORAN, null)));

        card.add(btnGrid, BorderLayout.CENTER);
        return card;
    }

    private JButton createQuickBtn(String text, Color color, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    public void refreshStats() {
        SwingWorker<int[], Void> worker = new SwingWorker<>() {
            int totalPelanggan, totalTransaksi, proses, selesai, sudahDiambil;
            double totalPendapatan;

            @Override
            protected int[] doInBackground() {
                totalPelanggan  = pelangganCtrl.getTotalPelanggan();
                totalTransaksi  = transaksiCtrl.getTotalTransaksi();
                totalPendapatan = transaksiCtrl.getTotalPendapatan();
                proses          = transaksiCtrl.countByStatus(Transaksi.STATUS_PROSES)
                                + transaksiCtrl.countByStatus(Transaksi.STATUS_DICUCI)
                                + transaksiCtrl.countByStatus(Transaksi.STATUS_DISETRIKA);
                selesai         = transaksiCtrl.countByStatus(Transaksi.STATUS_SELESAI);
                sudahDiambil    = transaksiCtrl.countByStatus(Transaksi.STATUS_SUDAH_DIAMBIL);
                return null;
            }

            @Override
            protected void done() {
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                lblTotalPelanggan.setText(String.valueOf(totalPelanggan));
                lblTotalTransaksi.setText(String.valueOf(totalTransaksi));
                lblTotalPendapatan.setText(nf.format(totalPendapatan));
                lblProses.setText(String.valueOf(proses));
                lblSelesai.setText(String.valueOf(selesai));
                lblSudahDiambil.setText(String.valueOf(sudahDiambil));
            }
        };
        worker.execute();
    }
}
