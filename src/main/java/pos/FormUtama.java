package pos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FormUtama extends JFrame {

    private JTabbedPane tabbedPane;

    private JTextField txtIdBarang, txtNamaBarang, txtHarga, txtStok, txtSatuan;
    private JButton btnSimpanBarang, btnHapusBarang, btnRefreshBarang, btnUbahBarang;
    private JTable tblBarang;
    private DefaultTableModel modelBarang;

    private JTextField txtCariIdBarang, txtJumlahBeli, txtUangBayar;
    private JButton btnTambahKeranjang, btnBayar;
    private JLabel lblTotalHarga;
    private JTable tblKeranjang;
    private DefaultTableModel modelKeranjang;

    private Transaksi transaksiSaatIni;

    public FormUtama() {
        setTitle("Aplikasi POS Warung Bu Wiwi - Kasir: " + KoneksiDatabase.kasirAktif.getNamaKasir());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inisialisasiTransaksiBaru();

        tabbedPane = new JTabbedPane();

        JPanel panelBarang = buatPanelBarang();
        JPanel panelKasir = buatPanelKasir();

        tabbedPane.addTab("Kelola Barang", panelBarang);
        tabbedPane.addTab("Kasir / Penjualan", panelKasir);

        add(tabbedPane);

        loadDataBarang();
    }

    private JPanel buatPanelBarang() {
        JPanel panel = new JPanel(null);

        JLabel lblId = new JLabel("ID Barang:");
        lblId.setBounds(20, 20, 100, 25);
        panel.add(lblId);

        txtIdBarang = new JTextField();
        txtIdBarang.setBounds(130, 20, 150, 25);
        panel.add(txtIdBarang);

        JLabel lblNama = new JLabel("Nama Barang:");
        lblNama.setBounds(20, 50, 100, 25);
        panel.add(lblNama);

        txtNamaBarang = new JTextField();
        txtNamaBarang.setBounds(130, 50, 200, 25);
        panel.add(txtNamaBarang);

        JLabel lblHarga = new JLabel("Harga:");
        lblHarga.setBounds(20, 80, 100, 25);
        panel.add(lblHarga);

        txtHarga = new JTextField();
        txtHarga.setBounds(130, 80, 150, 25);
        panel.add(txtHarga);

        JLabel lblStok = new JLabel("Stok:");
        lblStok.setBounds(20, 110, 100, 25);
        panel.add(lblStok);

        txtStok = new JTextField();
        txtStok.setBounds(130, 110, 100, 25);
        panel.add(txtStok);

        JLabel lblSatuan = new JLabel("Satuan:");
        lblSatuan.setBounds(20, 140, 100, 25);
        panel.add(lblSatuan);

        txtSatuan = new JTextField();
        txtSatuan.setBounds(130, 140, 100, 25);
        panel.add(txtSatuan);

        btnSimpanBarang = new JButton("Simpan");
        btnSimpanBarang.setBounds(20, 180, 90, 30);
        panel.add(btnSimpanBarang);

        btnHapusBarang = new JButton("Hapus");
        btnHapusBarang.setBounds(120, 180, 90, 30);
        panel.add(btnHapusBarang);

        btnUbahBarang = new JButton("Ubah");
        btnUbahBarang.setBounds(220, 180, 90, 30);
        panel.add(btnUbahBarang);

        btnRefreshBarang = new JButton("Refresh");
        btnRefreshBarang.setBounds(320, 180, 90, 30);
        panel.add(btnRefreshBarang);

        String[] kolomBarang = { "ID Barang", "Nama", "Harga", "Stok", "Satuan" };
        modelBarang = new DefaultTableModel(kolomBarang, 0);
        tblBarang = new JTable(modelBarang);

        JScrollPane scrollPane = new JScrollPane(tblBarang);
        scrollPane.setBounds(20, 230, 740, 280);
        panel.add(scrollPane);

        btnSimpanBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpanBarang();
            }
        });

        btnHapusBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapusBarang();
            }
        });

        btnUbahBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ubahBarang();
            }
        });

        btnRefreshBarang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataBarang();
                bersihkanFormBarang();
            }
        });

        tblBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int baris = tblBarang.getSelectedRow();
                if (baris >= 0) {
                    txtIdBarang.setText(modelBarang.getValueAt(baris, 0).toString());
                    txtIdBarang.setEditable(false); 
                    txtNamaBarang.setText(modelBarang.getValueAt(baris, 1).toString());
                    txtHarga.setText(modelBarang.getValueAt(baris, 2).toString());
                    txtStok.setText(modelBarang.getValueAt(baris, 3).toString());
                    txtSatuan.setText(modelBarang.getValueAt(baris, 4).toString());
                }
            }
        });

        return panel;
    }

    private JPanel buatPanelKasir() {
        JPanel panel = new JPanel(null);

        JLabel lblCariId = new JLabel("ID Barang:");
        lblCariId.setBounds(20, 20, 80, 25);
        panel.add(lblCariId);

        txtCariIdBarang = new JTextField();
        txtCariIdBarang.setBounds(100, 20, 150, 25);
        panel.add(txtCariIdBarang);

        JLabel lblJml = new JLabel("Jumlah:");
        lblJml.setBounds(260, 20, 60, 25);
        panel.add(lblJml);

        txtJumlahBeli = new JTextField();
        txtJumlahBeli.setBounds(320, 20, 50, 25);
        panel.add(txtJumlahBeli);

        btnTambahKeranjang = new JButton("Tambah ke Keranjang");
        btnTambahKeranjang.setBounds(390, 20, 180, 25);
        panel.add(btnTambahKeranjang);

        String[] kolomKeranjang = { "Nama Barang", "Harga", "Qty", "Subtotal" };
        modelKeranjang = new DefaultTableModel(kolomKeranjang, 0);
        tblKeranjang = new JTable(modelKeranjang);

        JScrollPane scrollPane = new JScrollPane(tblKeranjang);
        scrollPane.setBounds(20, 60, 740, 300);
        panel.add(scrollPane);

        JLabel lblTotalTeks = new JLabel("Total Harga (Rp) :");
        lblTotalTeks.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalTeks.setBounds(400, 380, 160, 30);
        panel.add(lblTotalTeks);

        lblTotalHarga = new JLabel("0.0");
        lblTotalHarga.setFont(new Font("Arial", Font.BOLD, 24));
        lblTotalHarga.setForeground(Color.RED);
        lblTotalHarga.setBounds(570, 380, 190, 30);
        panel.add(lblTotalHarga);

        JLabel lblBayar = new JLabel("Uang Bayar:");
        lblBayar.setBounds(400, 420, 100, 25);
        panel.add(lblBayar);

        txtUangBayar = new JTextField();
        txtUangBayar.setBounds(480, 420, 150, 25);
        panel.add(txtUangBayar);

        btnBayar = new JButton("BAYAR");
        btnBayar.setBounds(640, 420, 120, 40);
        panel.add(btnBayar);

        btnTambahKeranjang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tambahKeKeranjang();
            }
        });

        btnBayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesPembayaran();
            }
        });

        return panel;
    }

    private void loadDataBarang() {
        modelBarang.setRowCount(0); 
        ArrayList<Barang> list = KoneksiDatabase.ambilSemuaBarang();
        for (Barang b : list) {
            Object[] row = {
                    b.getIdBarang(),
                    b.getNamaBarang(),
                    b.getHarga(),
                    b.getStok(),
                    b.getSatuan()
            };
            modelBarang.addRow(row);
        }
    }

    private void simpanBarang() {
        try {
            Barang b = new Barang(
                    txtIdBarang.getText(),
                    txtNamaBarang.getText(),
                    Double.parseDouble(txtHarga.getText()),
                    Integer.parseInt(txtStok.getText()),
                    txtSatuan.getText());

            if (KoneksiDatabase.tambahBarang(b)) {
                JOptionPane.showMessageDialog(this, "Barang berhasil disimpan!");
                loadDataBarang();
                bersihkanFormBarang();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka!");
        }
    }

    private void ubahBarang() {
        try {
            if (txtIdBarang.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih barang dari tabel terlebih dahulu!");
                return;
            }

            Barang b = new Barang(
                    txtIdBarang.getText(),
                    txtNamaBarang.getText(),
                    Double.parseDouble(txtHarga.getText()),
                    Integer.parseInt(txtStok.getText()),
                    txtSatuan.getText());

            if (KoneksiDatabase.ubahBarang(b)) {
                JOptionPane.showMessageDialog(this, "Barang berhasil diubah!");
                loadDataBarang();
                bersihkanFormBarang();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka!");
        }
    }

    private void hapusBarang() {
        int barisTerpilih = tblBarang.getSelectedRow();
        if (barisTerpilih >= 0) {
            String idBarang = modelBarang.getValueAt(barisTerpilih, 0).toString();
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus barang " + idBarang + "?");
            if (konfirmasi == JOptionPane.YES_OPTION) {
                if (KoneksiDatabase.hapusBarang(idBarang)) {
                    JOptionPane.showMessageDialog(this, "Barang dihapus!");
                    loadDataBarang();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih barang di tabel terlebih dahulu!");
        }
    }

    private void bersihkanFormBarang() {
        txtIdBarang.setText("");
        txtIdBarang.setEditable(true); 
        txtNamaBarang.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        txtSatuan.setText("");
    }

    private void inisialisasiTransaksiBaru() {
        transaksiSaatIni = new Transaksi();
        transaksiSaatIni.setKasir(KoneksiDatabase.kasirAktif);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        transaksiSaatIni.setTanggal(dtf.format(now));
    }

    private void tambahKeKeranjang() {
        String idBarang = txtCariIdBarang.getText();
        String strJml = txtJumlahBeli.getText();

        if (idBarang.isEmpty() || strJml.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Barang dan Jumlah harus diisi!");
            return;
        }

        try {
            int jumlah = Integer.parseInt(strJml);
            Barang barangDitemukan = KoneksiDatabase.cariBarang(idBarang);

            if (barangDitemukan != null) {
                if (barangDitemukan.getStok() < jumlah) {
                    JOptionPane.showMessageDialog(this,
                            "Stok tidak mencukupi! Sisa stok: " + barangDitemukan.getStok());
                    return;
                }

                DetailTransaksi dt = new DetailTransaksi(barangDitemukan, jumlah);
                transaksiSaatIni.getListBelanja().add(dt);

                Object[] row = {
                        barangDitemukan.getNamaBarang(),
                        barangDitemukan.getHarga(),
                        jumlah,
                        dt.getSubtotal()
                };
                modelKeranjang.addRow(row);

                transaksiSaatIni.hitungTotal();
                lblTotalHarga.setText(String.valueOf(transaksiSaatIni.getTotalHarga()));

                txtCariIdBarang.setText("");
                txtJumlahBeli.setText("");
                txtCariIdBarang.requestFocus();

            } else {
                JOptionPane.showMessageDialog(this, "Barang dengan ID " + idBarang + " tidak ditemukan!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah beli harus angka!");
        }
    }

    private void prosesPembayaran() {
        if (transaksiSaatIni.getListBelanja().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja masih kosong!");
            return;
        }

        try {
            double uangBayar = Double.parseDouble(txtUangBayar.getText());
            double total = transaksiSaatIni.getTotalHarga();

            if (uangBayar < total) {
                JOptionPane.showMessageDialog(this, "Uang pembayaran kurang!");
                return;
            }

            double kembalian = uangBayar - total;
            transaksiSaatIni.setUangBayar(uangBayar);
            transaksiSaatIni.setKembalian(kembalian);

            boolean sukses = KoneksiDatabase.simpanTransaksi(transaksiSaatIni);

            if (sukses) {
                StringBuilder struk = new StringBuilder();
                struk.append("===== STRUK BELANJA =====\n");
                struk.append("Tanggal: ").append(transaksiSaatIni.getTanggal()).append("\n");
                struk.append("Kasir: ").append(transaksiSaatIni.getKasir().getNamaKasir()).append("\n");
                struk.append("-------------------------\n");
                for (DetailTransaksi dt : transaksiSaatIni.getListBelanja()) {
                    struk.append(dt.getBarang().getNamaBarang())
                            .append(" x").append(dt.getJumlahBeli())
                            .append("\t: Rp ").append(dt.getSubtotal()).append("\n");
                }
                struk.append("-------------------------\n");
                struk.append("Total   : Rp ").append(total).append("\n");
                struk.append("Bayar   : Rp ").append(uangBayar).append("\n");
                struk.append("Kembali : Rp ").append(kembalian).append("\n");
                struk.append("=========================\n");
                struk.append("Terima Kasih!");

                JOptionPane.showMessageDialog(this, struk.toString(), "Pembayaran Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);

                resetKasir();
                loadDataBarang();
            } else {
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan transaksi ke database!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Uang bayar harus berupa angka!");
        }
    }

    private void resetKasir() {
        modelKeranjang.setRowCount(0);
        lblTotalHarga.setText("0.0");
        txtUangBayar.setText("");
        inisialisasiTransaksiBaru();
    }
}
