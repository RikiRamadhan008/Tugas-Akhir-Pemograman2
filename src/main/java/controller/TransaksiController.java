/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package controller;

import dao.PelangganDAO;
import dao.TransaksiDAO;
import model.Pelanggan;
import model.Transaksi;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class TransaksiController {

    private final TransaksiDAO transaksiDAO;
    private final PelangganDAO pelangganDAO;

    public TransaksiController() {
        this.transaksiDAO = new TransaksiDAO();
        this.pelangganDAO = new PelangganDAO();
    }

 
    public String generateId() {
        return transaksiDAO.generateId();
    }

   
    public String simpan(String idTransaksi, String idPelanggan, String jenisLaundry,
                         String beratStr, String hargaPerKgStr,
                         Date tglMasuk, Date tglSelesai,
                         String status, String catatan) {

        String validasi = validasiInput(idPelanggan, jenisLaundry, beratStr, hargaPerKgStr, tglMasuk);
        if (validasi != null) return validasi;

        double berat      = Double.parseDouble(beratStr.trim().replace(",", "."));
        double hargaPerKg = Double.parseDouble(hargaPerKgStr.trim().replace(",", "."));
        double totalHarga = berat * hargaPerKg;

        Transaksi t = new Transaksi();
        t.setIdTransaksi(idTransaksi);
        t.setIdPelanggan(idPelanggan);
        t.setJenisLaundry(jenisLaundry);
        t.setBerat(berat);
        t.setHargaPerKg(hargaPerKg);
        t.setTotalHarga(totalHarga);
        t.setTglMasuk(tglMasuk);
        t.setTglSelesai(tglSelesai);
        t.setStatus(status != null ? status : Transaksi.STATUS_PROSES);
        t.setCatatan(catatan);

        boolean ok = transaksiDAO.save(t);
        return ok ? null : "Gagal menyimpan transaksi ke database!";
    }


    public List<Transaksi> getAll() {
        return transaksiDAO.getAll();
    }

 
    public Transaksi getById(String idTransaksi) {
        return transaksiDAO.getById(idTransaksi);
    }

   
    public List<Transaksi> getUnpaid() {
        return transaksiDAO.getUnpaid();
    }

  
    public List<Transaksi> cari(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return transaksiDAO.getAll();
        }
        return transaksiDAO.search(keyword.trim());
    }

  
    public List<Transaksi> filterByStatus(String status) {
        if (status == null || status.isEmpty() || status.equals("Semua")) {
            return transaksiDAO.getAll();
        }
        return transaksiDAO.filterByStatus(status);
    }

    
    public List<Pelanggan> getAllPelanggan() {
        return pelangganDAO.getAll();
    }

    
    public String update(String idTransaksi, String idPelanggan, String jenisLaundry,
                         String beratStr, String hargaPerKgStr,
                         Date tglMasuk, Date tglSelesai,
                         String status, String catatan) {

        if (idTransaksi == null || idTransaksi.trim().isEmpty()) {
            return "Pilih transaksi yang akan diedit!";
        }

        String validasi = validasiInput(idPelanggan, jenisLaundry, beratStr, hargaPerKgStr, tglMasuk);
        if (validasi != null) return validasi;

        double berat      = Double.parseDouble(beratStr.trim().replace(",", "."));
        double hargaPerKg = Double.parseDouble(hargaPerKgStr.trim().replace(",", "."));
        double totalHarga = berat * hargaPerKg;

        Transaksi t = new Transaksi();
        t.setIdTransaksi(idTransaksi.trim());
        t.setIdPelanggan(idPelanggan);
        t.setJenisLaundry(jenisLaundry);
        t.setBerat(berat);
        t.setHargaPerKg(hargaPerKg);
        t.setTotalHarga(totalHarga);
        t.setTglMasuk(tglMasuk);
        t.setTglSelesai(tglSelesai);
        t.setStatus(status);
        t.setCatatan(catatan);

        boolean ok = transaksiDAO.update(t);
        return ok ? null : "Gagal memperbarui transaksi!";
    }

    
    public String updateStatus(String idTransaksi, String status) {
        if (idTransaksi == null || idTransaksi.trim().isEmpty()) {
            return "ID transaksi tidak valid!";
        }
        boolean ok = transaksiDAO.updateStatus(idTransaksi, status);
        return ok ? null : "Gagal memperbarui status transaksi!";
    }

    
    public String hapus(String idTransaksi) {
        if (idTransaksi == null || idTransaksi.trim().isEmpty()) {
            return "Pilih transaksi yang akan dihapus!";
        }
        boolean ok = transaksiDAO.delete(idTransaksi.trim());
        return ok ? null : "Gagal menghapus transaksi! Mungkin sudah memiliki data pembayaran.";
    }


    public double hitungTotalHarga(String beratStr, String hargaPerKgStr) {
        try {
            double berat      = Double.parseDouble(beratStr.trim().replace(",", "."));
            double hargaPerKg = Double.parseDouble(hargaPerKgStr.trim().replace(",", "."));
            return berat * hargaPerKg;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

  
    public int getTotalTransaksi() {
        return transaksiDAO.getTotalTransaksi();
    }

 
    public double getTotalPendapatan() {
        return transaksiDAO.getTotalPendapatan();
    }


    public int countByStatus(String status) {
        return transaksiDAO.countByStatus(status);
    }

    private String validasiInput(String idPelanggan, String jenisLaundry,
                                  String beratStr, String hargaPerKgStr,
                                  Date tglMasuk) {
        if (idPelanggan == null || idPelanggan.trim().isEmpty()) {
            return "Pelanggan harus dipilih!";
        }
        if (jenisLaundry == null || jenisLaundry.trim().isEmpty()) {
            return "Jenis laundry harus dipilih!";
        }
        if (beratStr == null || beratStr.trim().isEmpty()) {
            return "Berat tidak boleh kosong!";
        }
        try {
            double berat = Double.parseDouble(beratStr.trim().replace(",", "."));
            if (berat <= 0) return "Berat harus lebih dari 0 Kg!";
        } catch (NumberFormatException e) {
            return "Berat harus berupa angka (contoh: 2.5)!";
        }
        if (hargaPerKgStr == null || hargaPerKgStr.trim().isEmpty()) {
            return "Harga per Kg tidak boleh kosong!";
        }
        try {
            double harga = Double.parseDouble(hargaPerKgStr.trim().replace(",", "."));
            if (harga <= 0) return "Harga per Kg harus lebih dari 0!";
        } catch (NumberFormatException e) {
            return "Harga per Kg harus berupa angka!";
        }
        if (tglMasuk == null) {
            return "Tanggal masuk harus diisi!";
        }
        return null;
    }
}
