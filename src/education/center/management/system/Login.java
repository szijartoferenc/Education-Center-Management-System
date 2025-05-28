package education.center.management.system;

/**
 *
 * @author Ferenc Szijarto
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener {

    // Gombok és mezők deklarálása osztályszinten, hogy minden metódus elérje őket
    private JButton login, cancel;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JCheckBox showPassword;

    public Login() {
        // Alap beállítások az ablakhoz
        setTitle("Bejelentkezés");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocation(500, 250);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Felhasználónév címke
        JLabel lblUsername = new JLabel("Felhasználónév");
        lblUsername.setBounds(40, 20, 100, 20);
        add(lblUsername);

        // Felhasználónév beviteli mező
        tfUsername = new JTextField();
        tfUsername.setBounds(150, 20, 150, 20);
        add(tfUsername);

        // Jelszó címke
        JLabel lblPassword = new JLabel("Jelszó");
        lblPassword.setBounds(40, 70, 100, 20);
        add(lblPassword);

        // Jelszó beviteli mező (rejtett karakterek)
        tfPassword = new JPasswordField();
        tfPassword.setBounds(150, 70, 150, 20);
        add(tfPassword);

        // "Jelszó megjelenítése" jelölőnégyzet
        showPassword = new JCheckBox("Jelszó megjelenítése");
        showPassword.setBounds(150, 100, 180, 20);
        showPassword.setBackground(Color.WHITE);
        showPassword.addActionListener(this);
        add(showPassword);

        // Bejelentkezés gomb
        login = new JButton("Bejelentkezés");
        login.setBounds(40, 140, 120, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Tahoma", Font.BOLD, 12));
        login.addActionListener(this);
        add(login);

        // Mégse gomb
        cancel = new JButton("Mégse");
        cancel.setBounds(180, 140, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 12));
        cancel.addActionListener(this);
        add(cancel);

        // Admin kép betöltése és megjelenítése
        java.net.URL imgURL = ClassLoader.getSystemResource("icons/admin.jpg");
        if (imgURL != null) {
            ImageIcon i1 = new ImageIcon(imgURL);
            Image i2 = i1.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel image = new JLabel(i3);
            image.setBounds(350, 0, 200, 200);
            add(image);
        } else {
            System.err.println("Nem található a kép: icons/admin.jpg");
        }

        // Ablak megjelenítése
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // Bejelentkezés gombra kattintás
        if (ae.getSource() == login) {
            String username = tfUsername.getText();
            String password = new String(tfPassword.getPassword());

            // Egyszerű bejelentkezési ellenőrzés (helyettesíthető adatbázissal később)
            if (username.equals("admin") && password.equals("12345")) {
                JOptionPane.showMessageDialog(this, "Sikeres bejelentkezés!");
                setVisible(false);
                // Itt lehet megnyitni a következő képernyőt, *Dashboard*
                new Dashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Hibás felhasználónév vagy jelszó.");
            }

        // Mégse gombra kattintás
        } else if (ae.getSource() == cancel) {
            setVisible(false);

        // "Jelszó megjelenítése" jelölőnégyzet állapotváltozás
        } else if (ae.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                tfPassword.setEchoChar((char) 0); // látható jelszó
            } else {
                tfPassword.setEchoChar('●'); // visszaállít rejtett jelszóra
            }
        }
    }

    public static void main(String[] args) {
        // Biztonságos GUI indítás az EDT szálon
        SwingUtilities.invokeLater(() -> new Login());
    }
}
