/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package aplikasimylaundry;

import com.formdev.flatlaf.FlatDarkLaf;
import view.LoginFrame;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        try {
            FlatDarkLaf.setup();

            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));

            UIManager.put("Button.background", new Color(0x1A73E8));
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.hoverBackground", new Color(0x1557B0));
            UIManager.put("Button.pressedBackground", new Color(0x0D47A1));

            UIManager.put("Table.alternateRowColor", new Color(0x2D2D3A));
            UIManager.put("Table.selectionBackground", new Color(0x1A73E8));
            UIManager.put("Table.gridColor", new Color(0x3A3A4A));

        } catch (Exception e) {
            System.err.println("Gagal mengatur Look and Feel: " + e.getMessage());
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
