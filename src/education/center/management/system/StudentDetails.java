// A programcsomag neve, amelyhez a fájl tartozik
package education.center.management.system;

/**
 * A szerző megjelölése (nem kötelező, de hasznos dokumentációhoz)
 * @author Ferenc Szijarto
 */

// Szükséges könyvtárak importálása
import javax.swing.*;              // Swing GUI komponensek
import java.awt.*;                 // Alapvető grafikus kezelő osztályok
import java.sql.*;                 // Adatbázis-kezeléshez szükséges osztályok
import javax.swing.table.DefaultTableModel; // Táblázatmodell-kezelés
import java.awt.event.*;          // Eseménykezelés (pl. gombkattintás)

public class StudentDetails extends JFrame implements ActionListener {

    // GUI elemek deklarálása
    Choice crollno;         // Legördülő lista a hallgatói azonosítókhoz
    JTable table;           // Táblázat az adatok megjelenítéséhez
    JButton search, print, update, add, cancel;  // Műveleti gombok

    // Konstruktor: az ablak és a komponensek inicializálása
    StudentDetails() {
        getContentPane().setBackground(Color.WHITE); // Háttérszín
        setLayout(null); // Egyéni elrendezés (null layout)

        // Címke: utasítás a felhasználónak
        JLabel heading = new JLabel("Keresés hallgatói kód alapján:");
        heading.setBounds(20, 20, 250, 25);
        heading.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(heading);

        // Legördülő lista inicializálása
        crollno = new Choice();
        crollno.setBounds(270, 20, 250, 25);
        add(crollno);

        // Az adatbázisból lekérdezzük az összes hallgatói azonosítót a legördülő listába
        try {
            Conn c = new Conn(); // Egyéni adatbázis kapcsolat osztály
            ResultSet rs = c.getStatement().executeQuery("SELECT rollno FROM student");
            while (rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hiba a neptun kódok betöltésekor.");
        }

        // Táblázat inicializálása
        table = new JTable();
        loadTableData(); // Alapértelmezett teljes adatbetöltés

        // Görgethető panel a táblázathoz
        JScrollPane jsp = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setPreferredScrollableViewportSize(new Dimension(880, 500));

        // Panel a táblázat köré, hogy pozicionálható legyen
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBounds(10, 110, 880, 500);
        tablePanel.add(jsp, BorderLayout.CENTER);
        add(tablePanel);

        // Gombok beállítása
        Font buttonFont = new Font("Tahoma", Font.BOLD, 12);

        search = new JButton("Keresés");
        search.setBounds(20, 60, 100, 30);
        search.setFont(buttonFont);
        search.addActionListener(this);
        add(search);

        print = new JButton("Nyomtatás");
        print.setBounds(130, 60, 110, 30);
        print.setFont(buttonFont);
        print.addActionListener(this);
        add(print);

        add = new JButton("Hozzáadás");
        add.setBounds(250, 60, 120, 30);
        add.setFont(buttonFont);
        add.addActionListener(this);
        add(add);

        update = new JButton("Frissítés");
        update.setBounds(380, 60, 110, 30);
        update.setFont(buttonFont);
        update.addActionListener(this);
        add(update);

        cancel = new JButton("Kilépés");
        cancel.setBounds(500, 60, 110, 30);
        cancel.setFont(buttonFont);
        cancel.addActionListener(this);
        add(cancel);

        // Ablak mérete, helyzete és láthatósága
        setSize(920, 700);
        setLocation(300, 100);
        setVisible(true);
    }

    // Adatok betöltése az adatbázisból a táblázatba
    private void loadTableData() {
        try {
            Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("SELECT * FROM student");

            // Oszlopnevek magyarra fordítása
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            String[] localizedHeaders = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                String name = metaData.getColumnName(i).toLowerCase();
                switch (name) {
                    case "rollno": localizedHeaders[i - 1] = "Hallgatói azonosító"; break;
                    case "name": localizedHeaders[i - 1] = "Név"; break;
                    case "mname": localizedHeaders[i - 1] = "Anyja neve"; break;
                    case "dob": localizedHeaders[i - 1] = "Születési dátum"; break;
                    case "address": localizedHeaders[i - 1] = "Lakcím"; break;
                    case "phone": localizedHeaders[i - 1] = "Telefonszám"; break;
                    case "email": localizedHeaders[i - 1] = "E-mail"; break;
                    case "class_x": localizedHeaders[i - 1] = "10. osztály"; break;
                    case "class_xii": localizedHeaders[i - 1] = "12. osztály"; break;
                    case "ssn": localizedHeaders[i - 1] = "TAJ"; break;
                    case "course": localizedHeaders[i - 1] = "Képzés"; break;
                    case "branch": localizedHeaders[i - 1] = "Szakirány"; break;
                    default: localizedHeaders[i - 1] = name;
                }
            }

            // Táblázatmodell feltöltése
            DefaultTableModel model = new DefaultTableModel(localizedHeaders, 0);
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            table.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hiba a diákok adatainak betöltésekor.");
        }
    }

    // Eseménykezelő metódus
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            // Hallgató keresése az azonosító alapján
            String selectedRoll = crollno.getSelectedItem();
            try {
                Conn c = new Conn();
                ResultSet rs = c.getStatement().executeQuery("SELECT * FROM student WHERE rollno = '" + selectedRoll + "'");
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                String[] localizedHeaders = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    String name = metaData.getColumnName(i).toLowerCase();
                    switch (name) {
                        case "rollno": localizedHeaders[i - 1] = "Hallgatói azonosító"; break;
                        case "name": localizedHeaders[i - 1] = "Név"; break;
                        case "mname": localizedHeaders[i - 1] = "Anyja neve"; break;
                        case "dob": localizedHeaders[i - 1] = "Születési dátum"; break;
                        case "address": localizedHeaders[i - 1] = "Lakcím"; break;
                        case "phone": localizedHeaders[i - 1] = "Telefonszám"; break;
                        case "email": localizedHeaders[i - 1] = "E-mail"; break;
                        case "class_x": localizedHeaders[i - 1] = "10. osztály"; break;
                        case "class_xii": localizedHeaders[i - 1] = "12. osztály"; break;
                        case "ssn": localizedHeaders[i - 1] = "TAJ"; break;
                        case "course": localizedHeaders[i - 1] = "Képzés"; break;
                        case "branch": localizedHeaders[i - 1] = "Szakirány"; break;
                        default: localizedHeaders[i - 1] = name;
                    }
                }

                DefaultTableModel model = new DefaultTableModel(localizedHeaders, 0);
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                }

                table.setModel(model);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Hiba a keresés során.");
            }

        } else if (ae.getSource() == print) {
            // Táblázat nyomtatása
            try {
                table.print();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Nem sikerült nyomtatni.");
            }

        } else if (ae.getSource() == add) {
            // Új hallgató hozzáadása
            setVisible(false);
            new AddStudent(); // Feltételezve, hogy van ilyen osztály

        } else if (ae.getSource() == update) {
            // Frissítés gomb esemény (jelenleg kikommentelve)
            setVisible(false);
            new UpdateStudent();

        } else if (ae.getSource() == cancel) {
            // Kilépés az ablakból
            setVisible(false);
        }
    }

    // Main metódus az osztály futtatásához
    public static void main(String[] args) {
        new StudentDetails();
    }
}
