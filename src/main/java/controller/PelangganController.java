/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package controller;

import dao.PelangganDAO;
import model.Pelanggan;

import java.util.List;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class PelangganController {

    private final PelangganDAO dao;

    public PelangganController() {
        this.dao = new PelangganDAO();
    }

    public String generateId() {
        return dao.generateId();
    }

    public String simpan(String idPelanggan, String nama, String noHp, String alamat) {
        String validasi = validasiInput(nama, noHp, alamat);
        if (validasi != null) return validasi;

        Pelanggan p = new Pelanggan();
        p.setIdPelanggan(idPelanggan);
        p.setNama(nama.trim());
        p.setNoHp(noHp.trim());
        p.setAlamat(alamat.trim());

        boolean ok = dao.save(p);
        return ok ? null : "Gagal menyimpan data pelanggan ke database!";
    }


    public List<Pelanggan> getAll() {
        return dao.getAll();
    }


    public Pelanggan getById(String idPelanggan) {
        return dao.getById(idPelanggan);
    }


    public List<Pelanggan> cari(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return dao.getAll();
        }
        return dao.search(keyword.trim());
    }


    public String update(String idPelanggan, String nama, String noHp, String alamat) {
        if (idPelanggan == null || idPelanggan.trim().isEmpty()) {
            return "Pilih pelanggan yang akan diedit terlebih dahulu!";
        }
        String validasi = validasiInput(nama, noHp, alamat);
        if (validasi != null) return validasi;

        Pelanggan p = new Pelanggan();
        p.setIdPelanggan(idPelanggan.trim());
        p.setNama(nama.trim());
        p.setNoHp(noHp.trim());
        p.setAlamat(alamat.trim());

        boolean ok = dao.update(p);
        return ok ? null : "Gagal memperbarui data pelanggan!";
    }


    public String hapus(String idPelanggan) {
        if (idPelanggan == null || idPelanggan.trim().isEmpty()) {
            return "Pilih pelanggan yang akan dihapus terlebih dahulu!";
        }
        boolean ok = dao.delete(idPelanggan.trim());
        return ok ? null : "Gagal menghapus pelanggan! Mungkin masih memiliki transaksi aktif.";
    }


    public int getTotalPelanggan() {
        return dao.countAll();
    }

 
    private String validasiInput(String nama, String noHp, String alamat) {
        if (nama == null || nama.trim().isEmpty()) {
            return "Nama pelanggan tidak boleh kosong!";
        }
        if (noHp == null || noHp.trim().isEmpty()) {
            return "Nomor HP tidak boleh kosong!";
        }
        if (!noHp.trim().matches("^[0-9]{8,15}$")) {
            return "Nomor HP harus berupa angka (8-15 digit)!";
        }
        if (alamat == null || alamat.trim().isEmpty()) {
            return "Alamat tidak boleh kosong!";
        }
        return null;
    }
}
