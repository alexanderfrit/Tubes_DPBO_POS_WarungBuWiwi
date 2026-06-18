package pos;

public class Main {
    public static void main(String[] args) {
        System.out.println("Memulai aplikasi POS...");
        KoneksiDatabase.inisialisasiDatabase();

        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
        } catch (Exception ex) {
            System.err.println("Gagal memuat tema FlatLaf: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormLogin().setVisible(true);
            }
        });
    }
}
