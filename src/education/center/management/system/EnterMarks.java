package education.center.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EnterMarks extends JFrame implements ActionListener {

    // GUI mezők
    private Choice crollno;
    private JComboBox<String> cbsemester;
    private JTextField[] tfSubjects = new JTextField[5];
    private JTextField[] tfMarks = new JTextField[5];
    private JButton btnSave, btnBack;
    private JLabel lblStudentName; // Hallgató nevének kijelzése

    // Konstruktor - GUI felépítése
    public EnterMarks() {
        setTitle("Jegyek rögzítése");
        setSize(1000, 500);
        setLocation(300, 150);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Kép hozzáadása
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icons/exam.jpg"));
        Image scaledImg = icon.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
        imageLabel.setBounds(500, 40, 400, 300);
        add(imageLabel);

        // Cím
        JLabel lblTitle = new JLabel("Hallgató jegyeinek rögzítése");
        lblTitle.setBounds(50, 0, 500, 50);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(lblTitle);

        // Hallgatói kód választó
        JLabel lblRoll = new JLabel("Hallgatói kód kiválasztása");
        lblRoll.setBounds(50, 70, 150, 20);
        add(lblRoll);

        crollno = new Choice();
        crollno.setBounds(200, 70, 150, 20);
        add(crollno);

        // Hallgató neve címke
        JLabel lblName = new JLabel("Hallgató neve:");
        lblName.setBounds(50, 100, 150, 20);
        add(lblName);

        lblStudentName = new JLabel("");
        lblStudentName.setBounds(200, 100, 300, 20);
        lblStudentName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        add(lblStudentName);

        // Félév választó
        JLabel lblSemester = new JLabel("Félév kiválasztása");
        lblSemester.setBounds(50, 130, 150, 20);
        add(lblSemester);

        String[] semesters = {
            "1. félév", "2. félév", "3. félév", "4. félév",
            "5. félév", "6. félév", "7. félév", "8. félév"
        };
        cbsemester = new JComboBox<>(semesters);
        cbsemester.setBounds(200, 130, 150, 20);
        cbsemester.setBackground(Color.WHITE);
        add(cbsemester);

        // Címkék a táblázathoz
        JLabel lblTantargy = new JLabel("Tantárgy megadása");
        lblTantargy.setBounds(100, 170, 200, 40);
        add(lblTantargy);

        JLabel lblJegy = new JLabel("Jegy megadása");
        lblJegy.setBounds(320, 170, 200, 40);
        add(lblJegy);

        // 5 tantárgy és jegy mező létrehozása
        for (int i = 0; i < 5; i++) {
            tfSubjects[i] = new JTextField();
            tfSubjects[i].setBounds(50, 220 + i * 30, 200, 20);
            add(tfSubjects[i]);

            tfMarks[i] = new JTextField();
            tfMarks[i].setBounds(250, 220 + i * 30, 200, 20);
            add(tfMarks[i]);
        }

        // Mentés gomb
        btnSave = new JButton("Mentés");
        btnSave.setBounds(70, 380, 150, 25);
        btnSave.setBackground(Color.BLACK);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnSave.addActionListener(this);
        add(btnSave);

        // Vissza gomb
        btnBack = new JButton("Vissza");
        btnBack.setBounds(280, 380, 150, 25);
        btnBack.setBackground(Color.BLACK);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnBack.addActionListener(this);
        add(btnBack);

        // Hallgatók betöltése + név frissítés
        try (Conn c = new Conn()) {
            ResultSet rs = c.getStatement().executeQuery("SELECT * FROM student");
            while (rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Eseményfigyelő: név lekérdezése a kiválasztott Neptun kód alapján
        crollno.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateStudentName();
            }
        });

        // Az első hallgató nevének betöltése
        if (crollno.getItemCount() > 0) {
            crollno.select(0);
            updateStudentName();
        }

        setVisible(true);
    }

    // Hallgató nevének lekérdezése és megjelenítése
    private void updateStudentName() {
        String selectedRoll = crollno.getSelectedItem();
        try (Conn c = new Conn()) {
            PreparedStatement ps = c.getConnection().prepareStatement(
                "SELECT name FROM student WHERE rollno = ?"
            );
            ps.setString(1, selectedRoll);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblStudentName.setText(rs.getString("name"));
            } else {
                lblStudentName.setText("Nincs adat");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblStudentName.setText("Hiba");
        }
    }

    // Eseménykezelés
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            try (Conn c = new Conn()) {
                // Előkészített lekérdezések
                String subjectQuery = "INSERT INTO subject VALUES (?, ?, ?, ?, ?, ?, ?)";
                String marksQuery = "INSERT INTO marks VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (
                    PreparedStatement ps1 = c.getConnection().prepareStatement(subjectQuery);
                    PreparedStatement ps2 = c.getConnection().prepareStatement(marksQuery)
                ) {
                    String roll = crollno.getSelectedItem();
                    String semester = cbsemester.getSelectedItem().toString();

                    // Subject adatok beszúrása
                    ps1.setString(1, roll);
                    ps1.setString(2, semester);
                    for (int i = 0; i < 5; i++) {
                        ps1.setString(i + 3, tfSubjects[i].getText());
                    }
                    ps1.executeUpdate();

                    // Marks adatok beszúrása
                    ps2.setString(1, roll);
                    ps2.setString(2, semester);
                    for (int i = 0; i < 5; i++) {
                        ps2.setString(i + 3, tfMarks[i].getText());
                    }
                    ps2.executeUpdate();

                    JOptionPane.showMessageDialog(null, "A jegyek sikeresen elmentve.");
                    setVisible(false);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Hiba történt a mentés során.");
            }
        } else if (e.getSource() == btnBack) {
            setVisible(false);
        }
    }

    // Program belépési pontja
    public static void main(String[] args) {
        new EnterMarks();
    }
}
