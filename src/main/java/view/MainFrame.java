/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package view;

import config.DBConnection;
import controller.AuthController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class MainFrame extends JFrame {

    private JPanel          cardPanel;
    private CardLayout      cardLayout;

    private JMenuItem       menuDashboard;
    private JMenuItem       menuPelanggan;
    private JMenuItem       menuTransaksi;
    private JMenuItem       menuPembayaran;
    private JMenuItem       menuLaporan;
    private JMenuItem       menuLogout;
    private JMenuItem       menuExit;

    private JLabel          lblStatusUser;
    private JLabel          lblStatusTime;
    private Timer           clockTimer;

    private DashboardPanel  dashboardPanel;
    private PelangganPanel  pelangganPanel;
    private TransaksiPanel  transaksiPanel;
    private PembayaranPanel pembayaranPanel;
    private LaporanPanel    laporanPanel;

    private JButton         btnNavDashboard;
    private JButton         btnNavPelanggan;
    private JButton         btnNavTransaksi;
    private JButton         btnNavPembayaran;
    private JButton         btnNavLaporan;
    private JButton         activeNavBtn;

    private static final Color COLOR_BG        = new Color(0x1A1A2E);
    private static final Color COLOR_SIDEBAR    = new Color(0x16213E);
    private static final Color COLOR_ACCENT     = new Color(0x0F3460);
    private static final Color COLOR_PRIMARY    = new Color(0x1A73E8);
    private static final Color COLOR_TEXT       = new Color(0xE0E0E0);
    private static final Color COLOR_SUBTEXT    = new Color(0x9E9E9E);
    private static final Color COLOR_NAV_HOVER  = new Color(0x0F3460);
    private static final Color COLOR_NAV_ACTIVE = new Color(0x1A73E8);

    public static final String PANEL_DASHBOARD  = "DASHBOARD";
    public static final String PANEL_PELANGGAN  = "PELANGGAN";
    public static final String PANEL_TRANSAKSI  = "TRANSAKSI";
    public static final String PANEL_PEMBAYARAN = "PEMBAYARAN";
    public static final String PANEL_LAPORAN    = "LAPORAN";

    public MainFrame() {
        initPanels();
        initComponents();
        setupFrame();
        setupListeners();
        startClock();
        navigateTo(PANEL_DASHBOARD, btnNavDashboard);
    }

    private void initPanels() {
        dashboardPanel  = new DashboardPanel(this);
        pelangganPanel  = new PelangganPanel();
        transaksiPanel  = new TransaksiPanel();
        pembayaranPanel = new PembayaranPanel();
        laporanPanel    = new LaporanPanel();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        add(createMenuBar_JPanel(), BorderLayout.NORTH);

        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_BG);
        cardPanel.add(dashboardPanel,  PANEL_DASHBOARD);
        cardPanel.add(pelangganPanel,  PANEL_PELANGGAN);
        cardPanel.add(transaksiPanel,  PANEL_TRANSAKSI);
        cardPanel.add(pembayaranPanel, PANEL_PEMBAYARAN);
        cardPanel.add(laporanPanel,    PANEL_LAPORAN);
        add(cardPanel, BorderLayout.CENTER);

        add(createStatusBar(), BorderLayout.SOUTH);
    }

 
    private JPanel createMenuBar_JPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_SIDEBAR);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_ACCENT),
            new EmptyBorder(12, 20, 12, 20)
        ));
        header.setPreferredSize(new Dimension(0, 62));


        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        JLabel lblLogo = new JLabel("🧺");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel lblAppName = new JLabel("MyLaundry");
        lblAppName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblAppName.setForeground(Color.WHITE);
        JLabel lblVersion = new JLabel("v1.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(COLOR_SUBTEXT);
        left.add(lblLogo);
        left.add(lblAppName);
        left.add(lblVersion);


        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        User user = AuthController.getLoggedInUser();
        String nama = user != null ? user.getNamaLengkap() : "Unknown";
        String role = user != null ? user.getRole().toUpperCase() : "";
        JLabel lblUser = new JLabel( nama + "  [" + role + "]");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(COLOR_TEXT);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setName("btnLogout");
        btnLogout.setBackground(new Color(0xC62828));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> doLogout());

        right.add(lblUser);
        right.add(btnLogout);

        header.add(left,  BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }


    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_ACCENT),
            new EmptyBorder(16, 0, 16, 0)
        ));
        sidebar.setPreferredSize(new Dimension(200, 0));

        JLabel lblMenu = new JLabel("  NAVIGASI");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblMenu.setForeground(COLOR_SUBTEXT);
        lblMenu.setBorder(new EmptyBorder(0, 16, 12, 16));
        sidebar.add(lblMenu);

        btnNavDashboard  = createNavButton("🏠", "Dashboard",  PANEL_DASHBOARD);
        btnNavPelanggan  = createNavButton("👥", "Pelanggan",  PANEL_PELANGGAN);
        btnNavTransaksi  = createNavButton("🧾", "Transaksi",  PANEL_TRANSAKSI);
        btnNavPembayaran = createNavButton("💳", "Pembayaran", PANEL_PEMBAYARAN);
        btnNavLaporan    = createNavButton("📊", "Laporan",    PANEL_LAPORAN);

        sidebar.add(btnNavDashboard);
        sidebar.add(btnNavPelanggan);
        sidebar.add(btnNavTransaksi);
        sidebar.add(btnNavPembayaran);
        sidebar.add(btnNavLaporan);

        sidebar.add(Box.createVerticalGlue());


        JLabel lblVer = new JLabel("  MyLaundry v1.0");
        lblVer.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVer.setForeground(new Color(0x424242));
        sidebar.add(lblVer);

        return sidebar;
    }

    private JButton createNavButton(String icon, String text, String panelKey) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setName("btnNav" + text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(COLOR_SIDEBAR);
        btn.setForeground(COLOR_TEXT);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setPreferredSize(new Dimension(200, 48));
        btn.setBorder(new EmptyBorder(0, 16, 0, 16));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn != activeNavBtn) btn.setBackground(COLOR_NAV_HOVER);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn != activeNavBtn) btn.setBackground(COLOR_SIDEBAR);
            }
        });

        btn.addActionListener(e -> navigateTo(panelKey, btn));
        return btn;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(COLOR_ACCENT);
        statusBar.setBorder(new EmptyBorder(4, 16, 4, 16));

        lblStatusUser = new JLabel("● Terhubung ke database");
        lblStatusUser.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatusUser.setForeground(new Color(0x66BB6A));

        lblStatusTime = new JLabel(getCurrentTime());
        lblStatusTime.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatusTime.setForeground(COLOR_SUBTEXT);

        statusBar.add(lblStatusUser, BorderLayout.WEST);
        statusBar.add(lblStatusTime, BorderLayout.EAST);
        return statusBar;
    }

   
    public void navigateTo(String panelKey, JButton navBtn) {
        cardLayout.show(cardPanel, panelKey);

        if (activeNavBtn != null) {
            activeNavBtn.setBackground(COLOR_SIDEBAR);
            activeNavBtn.setForeground(COLOR_TEXT);
            activeNavBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        }

        activeNavBtn = navBtn;
        if (activeNavBtn != null) {
            activeNavBtn.setBackground(COLOR_NAV_ACTIVE);
            activeNavBtn.setForeground(Color.WHITE);
            activeNavBtn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        }

        switch (panelKey) {
            case PANEL_DASHBOARD  -> dashboardPanel.refreshStats();
            case PANEL_PELANGGAN  -> pelangganPanel.loadData();
            case PANEL_TRANSAKSI  -> transaksiPanel.loadData();
            case PANEL_PEMBAYARAN -> pembayaranPanel.loadData();
            case PANEL_LAPORAN    -> {}
        }
    }

    private void setupFrame() {
        setTitle("MyLaundry — Sistem Manajemen Laundry                                                                                                                                                        Riki Ramadhan 231011400777");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                doExit();
            }
        });
    }

    private void setupListeners() {

        KeyStroke ksDash = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK);
        KeyStroke ksPel  = KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.ALT_DOWN_MASK);
        KeyStroke ksTrx  = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK);
        KeyStroke ksBay  = KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.ALT_DOWN_MASK);
        KeyStroke ksLap  = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ksDash, "dash");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ksPel,  "pel");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ksTrx,  "trx");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ksBay,  "bay");
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ksLap,  "lap");

        getRootPane().getActionMap().put("dash", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { navigateTo(PANEL_DASHBOARD, btnNavDashboard); }
        });
        getRootPane().getActionMap().put("pel", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { navigateTo(PANEL_PELANGGAN, btnNavPelanggan); }
        });
        getRootPane().getActionMap().put("trx", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { navigateTo(PANEL_TRANSAKSI, btnNavTransaksi); }
        });
        getRootPane().getActionMap().put("bay", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { navigateTo(PANEL_PEMBAYARAN, btnNavPembayaran); }
        });
        getRootPane().getActionMap().put("lap", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { navigateTo(PANEL_LAPORAN, btnNavLaporan); }
        });
    }

   
    private void startClock() {
        clockTimer = new Timer(1000, e -> lblStatusTime.setText(getCurrentTime()));
        clockTimer.start();
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy  HH:mm:ss"));
    }

   
    private void doLogout() {
        int opt = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            if (clockTimer != null) clockTimer.stop();
            AuthController.logout();
            DBConnection.closeConnection();

            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            this.dispose();
        }
    }

  
    private void doExit() {
        int opt = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar dari aplikasi?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            if (clockTimer != null) clockTimer.stop();
            DBConnection.closeConnection();
            System.exit(0);
        }
    }
}
