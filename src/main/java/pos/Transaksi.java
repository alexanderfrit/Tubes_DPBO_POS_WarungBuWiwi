package pos;

import java.util.ArrayList;

public class Transaksi {
    private int idTransaksi;
    private String tanggal;
    private Kasir kasir;
    private ArrayList<DetailTransaksi> listBelanja;
    private double totalHarga;
    private double uangBayar;
    private double kembalian;

    public Transaksi() {
        this.listBelanja = new ArrayList<>(); 
    }

    public void hitungTotal() {
        double total = 0;
        for (DetailTransaksi dt : listBelanja) {
            total += dt.getSubtotal();
        }
        this.totalHarga = total;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public Kasir getKasir() {
        return kasir;
    }

    public void setKasir(Kasir kasir) {
        this.kasir = kasir;
    }

    public ArrayList<DetailTransaksi> getListBelanja() {
        return listBelanja;
    }

    public void setListBelanja(ArrayList<DetailTransaksi> listBelanja) {
        this.listBelanja = listBelanja;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public double getUangBayar() {
        return uangBayar;
    }

    public void setUangBayar(double uangBayar) {
        this.uangBayar = uangBayar;
    }

    public double getKembalian() {
        return kembalian;
    }

    public void setKembalian(double kembalian) {
        this.kembalian = kembalian;
    }
}
