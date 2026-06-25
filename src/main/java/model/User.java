/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package model;

/**
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class User {

    private int    id;
    private String username;
    private String password;
    private String namaLengkap;
    private String role;

 
    public User() {}


    public User(int id, String username, String password, String namaLengkap, String role) {
        this.id          = id;
        this.username    = username;
        this.password    = password;
        this.namaLengkap = namaLengkap;
        this.role        = role;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', namaLengkap='" + namaLengkap
                + "', role='" + role + "'}";
    }
}
