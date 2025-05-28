package education.center.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * Ez az ablak a hallgatók távolléteinek megtekintésére, keresésére és kezelésére szolgál.
 * A felhasználó kiválaszthatja a hallgatót a kódja alapján, és megnézheti a távolléti adatokat.
 * Emellett lehetőség van új távollét hozzáadására vagy az adatok nyomtatására.
 * 
 * @author Ferenc
 */
public class StudentLeaveDetails extends JFrame implements ActionListener {

    Choice crollno;                 // Legördülő lista a hallgatói azonosítókhoz
    JLabel studentNameLabel;       // Címke a kiválasztott hallgató nevének megjelenítéséhez
    JTable table;                  // Táblázat az adatok megjelenítéséhez
    JButton search, print, update, add, cancel; // Funkciógombok

    // Konstruktor - Az ablak inicializálása
    StudentLeaveDetails() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Címke: keresés hallgatói kód alapján
        JLabel heading = new JLabel("Keresés hallgatói kód alapján:");
        heading.setBounds(20, 20, 250, 25);
        heading.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(heading);

        // Legördülő lista létrehozása a hallgatói azonosítókhoz
        crollno = new Choice();
        crollno.setBounds(270, 20, 250, 25);
        crollno.addItemListener(e -> updateStudentName());
        add(crollno);

        // A kiválasztott hallgató neve
        studentNameLabel = new JLabel("Név: ");
        studentNameLabel.setBounds(540, 20, 300, 25);
        studentNameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(studentNameLabel);

        // Hallgatói kódok betöltése az adatbázisból
        try (Conn c = new Conn()) {
            ResultSet rs = c.getStatement().executeQuery("SELECT rollno FROM student");
            while (rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hiba a hallgatói kódok betöltésekor.");
        }

        // Táblázat létrehozása és adatok betöltése
        table = new JTable();
        loadTableDataFromDB(); // Az összes távolléti adat betöltése

        // Görgetősáv a táblázat körül
        JScrollPane jsp = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        jsp.setBounds(10, 110, 880, 500);
        add(jsp);

        setTableColumnWidths(); // Oszlopszélességek beállítása

        // Gombok stílusa
        Font buttonFont = new Font("Tahoma", Font.BOLD, 12);

        // Keresés gomb
        search = new JButton("Keresés");
        search.setBounds(20, 60, 100, 30);
        search.setFont(buttonFont);
        search.addActionListener(this);
        add(search);

        // Nyomtatás gomb
        print = new JButton("Nyomtatás");
        print.setBounds(130, 60, 110, 30);
        print.setFont(buttonFont);
        print.addActionListener(this);
        add(print);

        // Hozzáadás gomb (új távollét)
        add = new JButton("Hozzáadás");
        add.setBounds(250, 60, 120, 30);
        add.setFont(buttonFont);
        add.addActionListener(this);
        add(add);

        // Kilépés gomb
        cancel = new JButton("Kilépés");
        cancel.setBounds(500, 60, 110, 30);
        cancel.setFont(buttonFont);
        cancel.addActionListener(this);
        add(cancel);

        // Ablak mérete és megjelenítése
        setSize(920, 700);
        setLocation(300, 100);
        setVisible(true);

        updateStudentName(); // Az első hallgató nevének betöltése
    }

    // Oszlopszélességek beállítása
    private void setTableColumnWidths() {
        int[] widths = {200, 200, 200}; // szélességek pixelben
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    // Adatok betöltése a megadott ResultSet-ből a táblázatba
    private void loadTableData(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Oszlopnevek lokalizálása
        String[] headers = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            String name = metaData.getColumnName(i).toLowerCase();
            switch (name) {
                case "rollno": headers[i - 1] = "Hallgatói azonosító"; break;
                case "date": headers[i - 1] = "Dátum"; break;
                case "duration": headers[i - 1] = "Időtartam"; break;
                default: headers[i - 1] = name;
            }
        }

        // Táblázatmodell feltöltése
        DefaultTableModel model = new DefaultTableModel(headers, 0);
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        table.setModel(model);
        setTableColumnWidths(); // újratöltés után is beállítjuk a szélességeket
    }

    // Teljes táblaadatok betöltése az adatbázisból
    private void loadTableDataFromDB() {
        try (Conn c = new Conn()) {
            String query = "SELECT * FROM studentleave";
            PreparedStatement pst = c.getConnection().prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            loadTableData(rs);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hiba az adatok betöltésekor.");
        }
    }

    // Kiválasztott hallgató nevének frissítése az azonosító alapján
    private void updateStudentName() {
        String selectedRoll = crollno.getSelectedItem();
        try (Conn c = new Conn()) {
            String query = "SELECT name FROM student WHERE rollno = ?";
            PreparedStatement pst = c.getConnection().prepareStatement(query);
            pst.setString(1, selectedRoll);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                studentNameLabel.setText("Név: " + rs.getString("name"));
            } else {
                studentNameLabel.setText("Név: Ismeretlen");
            }
        } catch (Exception e) {
            studentNameLabel.setText("Név: (hiba)");
        }
    }

    // Gombok eseménykezelése
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            // Hallgató keresése az adatbázisban
            String selectedRoll = crollno.getSelectedItem();
            try (Conn c = new Conn()) {
                String query = "SELECT * FROM studentleave WHERE rollno = ?";
                PreparedStatement pst = c.getConnection().prepareStatement(query);
                pst.setString(1, selectedRoll);
                ResultSet rs = pst.executeQuery();
                loadTableData(rs);
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
            // Új ablak megnyitása távollét hozzáadásához
            setVisible(false);
            new LeaveStudent();

        } else if (ae.getSource() == cancel) {
            // Kilépés az ablakból
            setVisible(false);
        }
    }

    // Főmetódus az ablak futtatásához
    public static void main(String[] args) {
        new StudentLeaveDetails();
    }
}
