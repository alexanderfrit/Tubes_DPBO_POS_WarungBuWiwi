package pos;

public class DetailTransaksi {
    private Barang barang;
    private int jumlahBeli;
    private double subtotal;

    public DetailTransaksi(Barang barang, int jumlahBeli) {
        this.barang = barang;
        this.jumlahBeli = jumlahBeli;
        this.subtotal = hitungSubtotal();
    }

    public double hitungSubtotal() {
        if (barang != null) {
            return barang.getHarga() * jumlahBeli;
        }
        return 0;
    }

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
        this.subtotal = hitungSubtotal();
    }

    public int getJumlahBeli() {
        return jumlahBeli;
    }

    public void setJumlahBeli(int jumlahBeli) {
        this.jumlahBeli = jumlahBeli;
        this.subtotal = hitungSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
