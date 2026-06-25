/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package view;

import controller.AuthController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class LoginFrame extends JFrame {

    private JTextField      txtUsername;
    private JPasswordField  txtPassword;
    private JButton         btnLogin;
    private JButton         btnExit;
    private JLabel          lblStatus;
    private JCheckBox       chkShowPassword;

    private final AuthController authController;

    private static final Color COLOR_BG          = new Color(0x1A1A2E);
    private static final Color COLOR_CARD         = new Color(0x16213E);
    private static final Color COLOR_ACCENT       = new Color(0x0F3460);
    private static final Color COLOR_PRIMARY      = new Color(0x1A73E8);
    private static final Color COLOR_PRIMARY_HOVER= new Color(0x1557B0);
    private static final Color COLOR_TEXT         = new Color(0xE0E0E0);
    private static final Color COLOR_SUBTEXT      = new Color(0x9E9E9E);
    private static final Color COLOR_ERROR        = new Color(0xEF5350);
    private static final Color COLOR_SUCCESS      = new Color(0x66BB6A);
    private static final Color COLOR_FIELD_BG     = new Color(0x0F3460);
    private static final Color COLOR_FIELD_BORDER = new Color(0x1A73E8);

    public LoginFrame() {
        this.authController = new AuthController();
        initComponents();
        setupFrame();
        setupListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_BG);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_ACCENT, 1),
            new EmptyBorder(50, 60, 50, 60)
        ));
        card.setPreferredSize(new Dimension(420, 520));

        JLabel lblIcon = new JLabel("🧺", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("MyLaundry", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Titip Cucian, Bawa Pulang Senyuman", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(COLOR_SUBTEXT);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_ACCENT);
        sep.setMaximumSize(new Dimension(300, 2));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsername = createFormLabel("Username");

        txtUsername = new JTextField();
        txtUsername.setName("txtUsername");
        styleTextField(txtUsername);
        txtUsername.putClientProperty("JTextField.placeholderText", "Masukkan username...");

        JLabel lblPassword = createFormLabel("Password");

        txtPassword = new JPasswordField();
        txtPassword.setName("txtPassword");
        styleTextField(txtPassword);
        txtPassword.putClientProperty("JTextField.placeholderText", "Masukkan password...");

        chkShowPassword = new JCheckBox("Tampilkan Password");
        chkShowPassword.setBackground(COLOR_CARD);
        chkShowPassword.setForeground(COLOR_SUBTEXT);
        chkShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkShowPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.setName("chkShowPassword");

        lblStatus = new JLabel(" ", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(COLOR_ERROR);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin = createPrimaryButton("  Login  ");
        btnLogin.setName("btnLogin");

        btnExit = new JButton("Keluar");
        btnExit.setName("btnExit");
        styleSecondaryButton(btnExit);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        btnPanel.setBackground(COLOR_CARD);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);

        JLabel lblInfo = new JLabel("<html><center><font color='#9E9E9E' size='2'>"
                + "</font></center></html>", SwingConstants.CENTER);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblIcon);
        card.add(Box.createVerticalStrut(8));
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSubtitle);
        card.add(Box.createVerticalStrut(24));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));
        card.add(lblUsername);
        card.add(Box.createVerticalStrut(6));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(16));
        card.add(lblPassword);
        card.add(Box.createVerticalStrut(6));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(8));
        card.add(chkShowPassword);
        card.add(Box.createVerticalStrut(12));
        card.add(lblStatus);
        card.add(Box.createVerticalStrut(12));
        card.add(btnPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(lblInfo);

        mainPanel.add(card);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(COLOR_BG);
        footer.setBorder(new EmptyBorder(0, 0, 12, 0));
        JLabel lblFooter = new JLabel("© 2026 MyLaundry App by.Riki Ramadhan v1.0");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(COLOR_SUBTEXT);
        footer.add(lblFooter);

        add(mainPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private void setupFrame() {
        setTitle("MyLaundry — Login       Riki Ramadhan 231011400777");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 640);
        setMinimumSize(new Dimension(420, 560));
        setLocationRelativeTo(null);
        setResizable(true);

        try {
            setIconImage(Toolkit.getDefaultToolkit()
                    .createImage(getClass().getResource("/icons/logo.png")));
        } catch (Exception ignored) {}
    }

    private void setupListeners() {

        btnLogin.addActionListener(e -> doLogin());


        btnExit.addActionListener(e -> {
            int opt = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin keluar?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        txtUsername.addActionListener(e -> txtPassword.requestFocus());

        txtPassword.addActionListener(e -> doLogin());

        chkShowPassword.addItemListener(e -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('●');
            }
        });
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        btnLogin.setEnabled(false);
        btnLogin.setText("Memproses...");
        lblStatus.setForeground(COLOR_SUBTEXT);
        lblStatus.setText("Memverifikasi...");

        SwingWorker<AuthController.LoginResult, Void> worker = new SwingWorker<>() {
            @Override
            protected AuthController.LoginResult doInBackground() {
                return authController.login(username, password);
            }

            @Override
            protected void done() {
                try {
                    AuthController.LoginResult result = get();
                    if (result.isSuccess()) {
                        lblStatus.setForeground(COLOR_SUCCESS);
                        lblStatus.setText(result.getMessage());

                        Timer delay = new Timer(500, evt -> {
                            MainFrame mainFrame = new MainFrame();
                            mainFrame.setVisible(true);
                            LoginFrame.this.dispose();
                        });
                        delay.setRepeats(false);
                        delay.start();
                    } else {
                        lblStatus.setForeground(COLOR_ERROR);
                        lblStatus.setText("⚠ " + result.getMessage());
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                        btnLogin.setEnabled(true);
                        btnLogin.setText("  Login  ");

                        shakeComponent(txtPassword);
                    }
                } catch (Exception ex) {
                    lblStatus.setForeground(COLOR_ERROR);
                    lblStatus.setText("⚠ Error: " + ex.getMessage());
                    btnLogin.setEnabled(true);
                    btnLogin.setText("  Login  ");
                }
            }
        };
        worker.execute();
    }

   
    private void shakeComponent(JComponent comp) {
        final Point original = comp.getLocation();
        Timer timer = new Timer(50, null);
        final int[] count = {0};
        final int[] dir = {1};
        timer.addActionListener(e -> {
            if (count[0] >= 8) {
                comp.setLocation(original);
                ((Timer) e.getSource()).stop();
            } else {
                comp.setLocation(original.x + dir[0] * 5, original.y);
                dir[0] *= -1;
                count[0]++;
            }
        });
        timer.start();
    }


    private JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(COLOR_FIELD_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_FIELD_BORDER, 1),
            new EmptyBorder(10, 14, 10, 14)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (field instanceof JPasswordField) {
            ((JPasswordField) field).setEchoChar('●');
        }
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 46));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY_HOVER);
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY);
            }
        });
        return btn;
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(new Color(0x37474F));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 46));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0x455A64));
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0x37474F));
            }
        });
    }
}
