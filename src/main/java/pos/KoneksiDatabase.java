package pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class KoneksiDatabase {

    private static final String URL_DB = "jdbc:sqlite:warung.db";
    
    public static Kasir kasirAktif = null;

    public static Connection getKoneksi() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL_DB);
        } catch (Exception e) {
            System.out.println("Gagal koneksi ke database: " + e.getMessage());
        }
        return conn;
    }

    public static void inisialisasiDatabase() {
        String tabelKasir = "CREATE TABLE IF NOT EXISTS kasir ("
                + "id_kasir INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama_kasir TEXT NOT NULL,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL"
                + ");";

        String tabelBarang = "CREATE TABLE IF NOT EXISTS barang ("
                + "id_barang TEXT PRIMARY KEY,"
                + "nama_barang TEXT NOT NULL,"
                + "harga REAL NOT NULL,"
                + "stok INTEGER NOT NULL,"
                + "satuan TEXT NOT NULL"
                + ");";

        String tabelTransaksi = "CREATE TABLE IF NOT EXISTS transaksi ("
                + "id_transaksi INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tanggal TEXT NOT NULL,"
                + "id_kasir INTEGER,"
                + "total_harga REAL,"
                + "uang_bayar REAL,"
                + "kembalian REAL,"
                + "FOREIGN KEY(id_kasir) REFERENCES kasir(id_kasir)"
                + ");";

        String tabelDetail = "CREATE TABLE IF NOT EXISTS detail_transaksi ("
                + "id_detail INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_transaksi INTEGER,"
                + "id_barang TEXT,"
                + "jumlah_beli INTEGER,"
                + "subtotal REAL,"
                + "FOREIGN KEY(id_transaksi) REFERENCES transaksi(id_transaksi),"
                + "FOREIGN KEY(id_barang) REFERENCES barang(id_barang)"
                + ");";

        try (Connection conn = getKoneksi(); Statement stmt = conn.createStatement()) {
            stmt.execute(tabelKasir);
            stmt.execute(tabelBarang);
            stmt.execute(tabelTransaksi);
            stmt.execute(tabelDetail);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM kasir");
            if (rs.next() && rs.getInt("total") == 0) {
                stmt.execute("INSERT INTO kasir (nama_kasir, username, password) VALUES ('Admin Toko', 'admin', 'admin')");
                System.out.println("Kasir default berhasil ditambahkan (admin/admin).");
            }
        } catch (SQLException e) {
            System.out.println("Gagal inisialisasi database: " + e.getMessage());
        }
    }

    public static boolean cekLogin(String user, String pass) {
        String sql = "SELECT * FROM kasir WHERE username = ? AND password = ?";
        try (Connection conn = getKoneksi(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                kasirAktif = new Kasir(
                        rs.getInt("id_kasir"),
                        rs.getString("nama_kasir"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error saat login: " + e.getMessage());
        }
        return false;
    }

    public static ArrayList<Barang> ambilSemuaBarang() {
        ArrayList<Barang> listBarang = new ArrayList<>();
        String sql = "SELECT * FROM barang";

        try (Connection conn = getKoneksi(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Barang b = new Barang(
                        rs.getString("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getDouble("harga"),
                        rs.getInt("stok"),
                        rs.getString("satuan")
                );
                listBarang.add(b);
            }
        } catch (SQLException e) {
            System.out.println("Gagal ambil data barang: " + e.getMessage());
        }
        return listBarang;
    }

    public static Barang cariBarang(String idBarang) {
        String sql = "SELECT * FROM barang WHERE id_barang = ?";
        try (Connection conn = getKoneksi(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idBarang);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Barang(
                        rs.getString("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getDouble("harga"),
                        rs.getInt("stok"),
                        rs.getString("satuan")
                );
            }
        } catch (SQLException e) {
            System.out.println("Gagal cari barang: " + e.getMessage());
        }
        return null;
    }

    public static boolean tambahBarang(Barang b) {
        String sql = "INSERT INTO barang (id_barang, nama_barang, harga, stok, satuan) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getKoneksi(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b.getIdBarang());
            pstmt.setString(2, b.getNamaBarang());
            pstmt.setDouble(3, b.getHarga());
            pstmt.setInt(4, b.getStok());
            pstmt.setString(5, b.getSatuan());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal tambah barang: " + e.getMessage());
            return false;
        }
    }

    public static boolean ubahBarang(Barang b) {
        String sql = "UPDATE barang SET nama_barang = ?, harga = ?, stok = ?, satuan = ? WHERE id_barang = ?";
        try (Connection conn = getKoneksi(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b.getNamaBarang());
            pstmt.setDouble(2, b.getHarga());
            pstmt.setInt(3, b.getStok());
            pstmt.setString(4, b.getSatuan());
            pstmt.setString(5, b.getIdBarang());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal ubah barang: " + e.getMessage());
            return false;
        }
    }

    public static boolean hapusBarang(String idBarang) {
        String sql = "DELETE FROM barang WHERE id_barang = ?";
        try (Connection conn = getKoneksi(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idBarang);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Gagal hapus barang: " + e.getMessage());
            return false;
        }
    }

    public static boolean simpanTransaksi(Transaksi t) {
        Connection conn = getKoneksi();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false);

            String sqlTransaksi = "INSERT INTO transaksi (tanggal, id_kasir, total_harga, uang_bayar, kembalian) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmtTrans = conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);
            pstmtTrans.setString(1, t.getTanggal());
            pstmtTrans.setInt(2, t.getKasir().getIdKasir());
            pstmtTrans.setDouble(3, t.getTotalHarga());
            pstmtTrans.setDouble(4, t.getUangBayar());
            pstmtTrans.setDouble(5, t.getKembalian());
            pstmtTrans.executeUpdate();

            ResultSet rsKeys = pstmtTrans.getGeneratedKeys();
            int idTransaksiBaru = -1;
            if (rsKeys.next()) {
                idTransaksiBaru = rsKeys.getInt(1);
            }

            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_barang, jumlah_beli, subtotal) VALUES (?, ?, ?, ?)";
            String sqlUpdateStok = "UPDATE barang SET stok = stok - ? WHERE id_barang = ?";

            PreparedStatement pstmtDetail = conn.prepareStatement(sqlDetail);
            PreparedStatement pstmtStok = conn.prepareStatement(sqlUpdateStok);

            for (DetailTransaksi dt : t.getListBelanja()) {
                pstmtDetail.setInt(1, idTransaksiBaru);
                pstmtDetail.setString(2, dt.getBarang().getIdBarang());
                pstmtDetail.setInt(3, dt.getJumlahBeli());
                pstmtDetail.setDouble(4, dt.getSubtotal());
                pstmtDetail.executeUpdate();

                pstmtStok.setInt(1, dt.getJumlahBeli());
                pstmtStok.setString(2, dt.getBarang().getIdBarang());
                pstmtStok.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Gagal simpan transaksi: " + e.getMessage());
            try {
                conn.rollback(); 
            } catch (SQLException ex) {
                System.out.println("Gagal rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                System.out.println("Gagal tutup koneksi: " + e.getMessage());
            }
        }
    }
}
