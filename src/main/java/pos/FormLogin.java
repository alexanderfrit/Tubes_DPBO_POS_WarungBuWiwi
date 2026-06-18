package pos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public FormLogin() {
        setTitle("Login POS Warung Bu Wiwi");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitle = new JLabel("Silakan Login");
        lblTitle.setBounds(120, 20, 150, 25);
        add(lblTitle);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(40, 70, 80, 25);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(130, 70, 150, 25);
        add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(40, 110, 80, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 110, 150, 25);
        add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(130, 160, 100, 30);
        add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosesLogin();
            }
        });
    }

    private void prosesLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!");
            return;
        }

        boolean berhasil = KoneksiDatabase.cekLogin(username, password);

        if (berhasil) {
            JOptionPane.showMessageDialog(this,
                    "Login Berhasil! Selamat Datang " + KoneksiDatabase.kasirAktif.getNamaKasir());
            this.dispose();
            new FormUtama().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Login Gagal! Username atau Password salah.");
        }
    }
}
