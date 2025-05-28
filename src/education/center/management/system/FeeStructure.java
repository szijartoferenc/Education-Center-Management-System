package education.center.management.system;

/**
 *
 * @author Ferenc Szijarto
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class FeeStructure extends JFrame {

    // Konstruktor - GUI felépítése
    public FeeStructure() {
        setTitle("Tandíjstruktúra");
        setSize(1000, 700);
        setLocation(250, 50);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Cím
        JLabel heading = new JLabel("Tandíjstruktúra");
        heading.setBounds(50, 10, 400, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 30));
        add(heading);

        // Táblázat
        JTable table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 60, 1000, 600);
        add(jsp);

        // Adatok lekérdezése és betöltése egyéni oszlopnevekkel
        try {
            Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("SELECT * FROM fee");

            // Táblamodell létrehozása egyéni (magyar) oszlopnevekkel
            String[] columnNames = {
                "Szak", "1. félév", "2. félév", "3. félév", "4. félév",
                "5. félév", "6. félév", "7. félév", "8. félév"
            };

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                String[] row = new String[9];
                row[0] = rs.getString("course");
                row[1] = rs.getString("semester1");
                row[2] = rs.getString("semester2");
                row[3] = rs.getString("semester3");
                row[4] = rs.getString("semester4");
                row[5] = rs.getString("semester5");
                row[6] = rs.getString("semester6");
                row[7] = rs.getString("semester7");
                row[8] = rs.getString("semester8");
                model.addRow(row);
            }

            table.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hiba történt az adatok betöltésekor.");
        }

        setVisible(true);
    }

    // Program belépési pontja
    public static void main(String[] args) {
        new FeeStructure();
    }
}
