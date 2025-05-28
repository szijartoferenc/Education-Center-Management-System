package education.center.management.system;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

public class ExaminationDetails extends JFrame implements ActionListener {

    JTextField search;
    JButton submit, cancel;
    JTable table;

    ExaminationDetails() {
        setTitle("Vizsgaeredmények megtekintése");
        setSize(1000, 475);
        setLocation(300, 100);
        setLayout(null);

        getContentPane().setBackground(Color.WHITE);

        // Cím
        JLabel heading = new JLabel("Eredmények megtekintése");
        heading.setBounds(80, 15, 400, 50);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(heading);

        // Keresőmező
        search = new JTextField();
        search.setBounds(80, 90, 200, 30);
        search.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(search);

        // Eredmény lekérés gomb
        submit = new JButton("Eredmény");
        submit.setBounds(300, 90, 120, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(this);
        submit.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(submit);

        // Vissza gomb
        cancel = new JButton("Vissza");
        cancel.setBounds(440, 90, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(cancel);

        // Táblázat a hallgatói adatokhoz
        table = new JTable();
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 130, 1000, 310);
        add(jsp);

        // Alapértelmezetten hallgatók betöltése
        try {
            Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("SELECT rollno AS 'Hallgatói azonosító', name AS 'Név' FROM student");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hiba történt az adatok betöltésekor.");
            e.printStackTrace();
        }

        // Táblázat sorára kattintás – mező kitöltése automatikusan
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int row = table.getSelectedRow();
                search.setText(table.getModel().getValueAt(row, 0).toString()); // 0 = Hallgatói azonosító
            }
        });

        setVisible(true);
    }

    // Gombesemények kezelése
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            try {
                Conn c = new Conn();
                String rollno = search.getText();

                // A hallgató neve, tantárgyai és jegyei lekérdezve
                String query = "SELECT s.rollno AS 'Hallgatói azonosító', " +
                               "st.name AS 'Név', " +
                               "sb.subject1 AS 'Tantárgy 1', sb.subject2 AS 'Tantárgy 2', sb.subject3 AS 'Tantárgy 3', sb.subject4 AS 'Tantárgy 4', sb.subject5 AS 'Tantárgy 5', " +
                               "m.marks1 AS 'Jegy 1', m.marks2 AS 'Jegy 2', m.marks3 AS 'Jegy 3', m.marks4 AS 'Jegy 4', m.marks5 AS 'Jegy 5' " +
                               "FROM student st " +
                               "JOIN subject sb ON st.rollno = sb.rollno " +
                               "JOIN marks m ON st.rollno = m.rollno " +
                               "JOIN student s ON s.rollno = st.rollno " +
                               "WHERE st.rollno = '" + rollno + "'";

                ResultSet rs = c.getStatement().executeQuery(query);

                // Eredmény táblázatba töltése
                table.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Hiba történt a lekérdezés során.");
                e.printStackTrace();
            }

        } else if (ae.getSource() == cancel) {
            setVisible(false); // Vissza gomb
        }
    }

    public static void main(String[] args) {
        new ExaminationDetails();
    }
}
