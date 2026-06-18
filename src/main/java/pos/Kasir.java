package pos;

public class Kasir {
    private int idKasir;
    private String namaKasir;
    private String username;
    private String password;

    public Kasir() {
    }

    public Kasir(int idKasir, String namaKasir, String username, String password) {
        this.idKasir = idKasir;
        this.namaKasir = namaKasir;
        this.username = username;
        this.password = password;
    }

    public int getIdKasir() {
        return idKasir;
    }

    public void setIdKasir(int idKasir) {
        this.idKasir = idKasir;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
