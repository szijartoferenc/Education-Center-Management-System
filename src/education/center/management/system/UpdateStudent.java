package education.center.management.system;

import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;
import java.sql.*;

public class UpdateStudent extends JFrame implements ActionListener {

    // Mezők
    JTextField tfaddress, tfphone, tfemail, tfcourse, tfbranch;
    JLabel labelrollno, labelname, labelmname, labeldob, labelx, labelxii, labelssn;
    JButton submit, cancel;
    Choice crollno;
    JDateChooser dcdob;

    UpdateStudent() {
        setSize(900, 650);
        setLocation(350, 50);
        setLayout(null);
        setTitle("Hallgatói adatok frissítése");

        JLabel heading = new JLabel("Hallgató adatainak szerkesztése");
        heading.setBounds(50, 10, 500, 50);
        heading.setFont(new Font("serif", Font.ITALIC, 35));
        add(heading);

        JLabel lblrollnumber = new JLabel("Hallgatói kód kiálasztása:");
        lblrollnumber.setBounds(50, 100, 250, 20);
        lblrollnumber.setFont(new Font("serif", Font.PLAIN, 20));
        add(lblrollnumber);

        crollno = new Choice();
        crollno.setBounds(350, 100, 200, 20);
        add(crollno);

        // Hallgatói kódok betöltése
        try (Conn c = new Conn()) {
            ResultSet rs = c.getStatement().executeQuery("SELECT * FROM student");
            while (rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Automatikus adatok betöltése választás után
        crollno.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                loadStudentData();
            }
        });

        // Adatok címkék és mezők
        addField("Név:", 150, labelname = new JLabel(), 50);
        addField("Anyja neve:", 150, labelmname = new JLabel(), 400);
        addField("Hallgatói kód:", 200, labelrollno = new JLabel(), 50);
        addField("Születési dátum:", 200, labeldob = new JLabel(), 400);
        addField("Lakcím:", 250, tfaddress = new JTextField(), 50);
        addField("Telefonszám:", 250, tfphone = new JTextField(), 400);
        addField("Email cím:", 300, tfemail = new JTextField(), 50);
        addField("10. osztály (%):", 300, labelx = new JLabel(), 400);
        addField("12. osztály (%):", 350, labelxii = new JLabel(), 50);
        addField("TAJ szám:", 350, labelssn = new JLabel(), 400);
        addField("Képzés:", 400, tfcourse = new JTextField(), 50);
        addField("Szak:", 400, tfbranch = new JTextField(), 400);

        // Gombok
        submit = new JButton("Frissítés");
        submit.setBounds(250, 500, 120, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Tahoma", Font.BOLD, 15));
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Mégse");
        cancel.setBounds(450, 500, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 15));
        cancel.addActionListener(this);
        add(cancel);

        // Kezdeti adatbetöltés
        if (crollno.getItemCount() > 0) {
            crollno.select(0);
            loadStudentData();
        }

        setVisible(true);
    }

    // Segédfüggvény mezők hozzáadásához
    void addField(String labelText, int y, JComponent comp, int x) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 150, 30);
        label.setFont(new Font("serif", Font.BOLD, 20));
        add(label);

        comp.setBounds(x + 150, y, 150, 30);
        add(comp);
    }

    // Adatok betöltése az adatbázisból
    void loadStudentData() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM student WHERE rollno='" + crollno.getSelectedItem() + "'";
            ResultSet rs = c.getStatement().executeQuery(query);
            if (rs.next()) {
                labelname.setText(rs.getString("name"));
                labelmname.setText(rs.getString("mname"));
                labelrollno.setText(rs.getString("rollno"));
                labeldob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                labelx.setText(rs.getString("class_x"));
                labelxii.setText(rs.getString("class_xii"));
                labelssn.setText(rs.getString("ssn"));
                tfcourse.setText(rs.getString("course"));
                tfbranch.setText(rs.getString("branch"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gombesemények kezelése
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String rollno = labelrollno.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String course = tfcourse.getText();
            String branch = tfbranch.getText();

            // Alapellenőrzések
            if (phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kérlek, tölts ki minden mezőt!");
                return;
            }

            try {
                Conn c = new Conn();
                String query = "UPDATE student SET address='" + address + "', phone='" + phone + "', email='" + email +
                        "', course='" + course + "', branch='" + branch + "' WHERE rollno='" + rollno + "'";

                c.getStatement().executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Adatok sikeresen frissítve.");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Hiba történt a frissítés során.");
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateStudent();
    }
}
