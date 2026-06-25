/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package controller;

import dao.UserDAO;
import model.User;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class AuthController {

    private static User loggedInUser;
    private final UserDAO userDAO;

    public AuthController() {
        this.userDAO = new UserDAO();
    }


    public LoginResult login(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            return new LoginResult(false, "Username tidak boleh kosong!");
        }
        if (password == null || password.trim().isEmpty()) {
            return new LoginResult(false, "Password tidak boleh kosong!");
        }


        User user = userDAO.findByUsernameAndPassword(username.trim(), password);

        if (user != null) {
            loggedInUser = user;
            return new LoginResult(true, "Login berhasil! Selamat datang, " + user.getNamaLengkap());
        } else {
            return new LoginResult(false, "Username atau password salah!");
        }
    }

    public static void logout() {
        loggedInUser = null;
    }

  
    public static User getLoggedInUser() {
        return loggedInUser;
    }


    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }


    public static class LoginResult {
        private final boolean success;
        private final String  message;

        public LoginResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String  getMessage() { return message; }
    }
}
