package education.center.management.system;

/**
 *
 * @author Ferenc Szijarto
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class About extends JFrame {

    About() {
        // Ablak mérete és pozíciója
        setSize(700, 500);
        setLocation(400, 150);
        getContentPane().setBackground(Color.WHITE);
        setTitle("Névjegy");

        setLayout(null); // Kézi komponenselrendezés

        // Kép beillesztése és átméretezése
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/about.jpg"));
        Image i2 = i1.getImage().getScaledInstance(300, 200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(370, 0, 300, 200);
        add(image);

        // Cím
        JLabel heading = new JLabel("<html>Oktatási Központ<br/>Irányítási Rendszer</html>");
        heading.setBounds(70, 20, 300, 130);
        heading.setFont(new Font("Tahoma", Font.BOLD, 30));
        add(heading);

        // Fejlesztő neve
        JLabel name = new JLabel("Fejlesztő: Szijártó Ferenc");
        name.setBounds(70, 220, 550, 40);
        name.setFont(new Font("Tahoma", Font.BOLD, 26));
        add(name);

        // Neptun kód / azonosító
        JLabel rollno = new JLabel("Azonosító: N/A");
        rollno.setBounds(70, 280, 550, 40);
        rollno.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(rollno);

        // Kapcsolat / portfólió
        JLabel contact = new JLabel("Kapcsolat: https://portfolio.frankie-webesgin.hu");
        contact.setBounds(70, 340, 550, 40);
        contact.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(contact);

        // Vissza gomb létrehozása
        JButton backButton = new JButton("Vissza");
        backButton.setBounds(280, 400, 120, 35);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(backButton);

        // Gomb eseménykezelő: bezárja az ablakot
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // vagy dispose(); ha végleg le akarjuk zárni az ablakot
            }
        });

        setVisible(true); // Ablak megjelenítése
    }

    public static void main(String[] args) {
        new About();
    }
}
