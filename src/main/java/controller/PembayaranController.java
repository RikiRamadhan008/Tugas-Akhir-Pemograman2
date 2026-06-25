/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package controller;

import dao.PembayaranDAO;
import dao.TransaksiDAO;
import model.Pembayaran;
import model.Transaksi;

import java.util.List;

/**
 *
 * @author Riki Ramadhan 231011400777
 * @version 1.0
 */
public class PembayaranController {

    private final PembayaranDAO pembayaranDAO;
    private final TransaksiDAO  transaksiDAO;

    public PembayaranController() {
        this.pembayaranDAO = new PembayaranDAO();
        this.transaksiDAO  = new TransaksiDAO();
    }


    public String generateId() {
        return pembayaranDAO.generateId();
    }


    public String bayar(String idPembayaran, String idTransaksi, String uangBayarStr) {

        if (idTransaksi == null || idTransaksi.trim().isEmpty()) {
            return "Pilih transaksi yang akan dibayar!";
        }


        if (pembayaranDAO.isTransaksiSudahDibayar(idTransaksi)) {
            return "Transaksi ini sudah dibayar sebelumnya!";
        }

   
        Transaksi transaksi = transaksiDAO.getById(idTransaksi);
        if (transaksi == null) {
            return "Data transaksi tidak ditemukan!";
        }


        if (uangBayarStr == null || uangBayarStr.trim().isEmpty()) {
            return "Uang bayar tidak boleh kosong!";
        }

        double uangBayar;
        try {
            uangBayar = Double.parseDouble(uangBayarStr.trim().replace(",", ".").replace(".", "")
                    .replaceAll("[^0-9]", ""));

            uangBayar = Double.parseDouble(uangBayarStr.trim().replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return "Uang bayar harus berupa angka!";
        }

        double totalBayar = transaksi.getTotalHarga();

        if (uangBayar < totalBayar) {
            return String.format("Uang bayar kurang! Total tagihan: Rp %,.0f, Uang: Rp %,.0f",
                    totalBayar, uangBayar);
        }

        double kembalian = uangBayar - totalBayar;

   
        Pembayaran p = new Pembayaran();
        p.setIdPembayaran(idPembayaran);
        p.setIdTransaksi(idTransaksi);
        p.setTotalBayar(totalBayar);
        p.setUangBayar(uangBayar);
        p.setKembalian(kembalian);
        p.setStatusBayar("Lunas");

        boolean ok = pembayaranDAO.save(p);
        if (!ok) return "Gagal menyimpan data pembayaran!";

 
        if (!Transaksi.STATUS_SELESAI.equals(transaksi.getStatus())
                && !Transaksi.STATUS_SUDAH_DIAMBIL.equals(transaksi.getStatus())) {
            transaksiDAO.updateStatus(idTransaksi, Transaksi.STATUS_SELESAI);
        }

        return null;
    }

    public List<Pembayaran> getAll() {
        return pembayaranDAO.getAll();
    }


    public Pembayaran getByTransaksi(String idTransaksi) {
        return pembayaranDAO.getByTransaksi(idTransaksi);
    }

 
    public List<Transaksi> getTransaksiUnpaid() {
        return transaksiDAO.getUnpaid();
    }

 
    public Transaksi getTransaksiById(String idTransaksi) {
        return transaksiDAO.getById(idTransaksi);
    }

   
    public double hitungKembalian(String uangBayarStr, double totalBayar) {
        try {
            double uangBayar = Double.parseDouble(uangBayarStr.trim().replaceAll("[^0-9.]", ""));
            double kembalian = uangBayar - totalBayar;
            return kembalian < 0 ? -1 : kembalian;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public double getTotalPendapatanBulan(int tahun, int bulan) {
        return pembayaranDAO.getTotalPendapatanBulan(tahun, bulan);
    }
}
