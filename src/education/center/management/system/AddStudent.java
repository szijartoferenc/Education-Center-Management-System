package education.center.management.system;

/**
 *
 * @author Ferenc Szijarto
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;

public class AddStudent extends JFrame implements ActionListener {

    // Beviteli mezők különböző adatokhoz
    JTextField tfname, tfmname, tfaddress, tfphone, tfemail, tfx, tfxii, tfssn;

    // Automatikusan generált hallgatói azonosító címke
    JLabel labelrollno;

    // Dátumválasztó születési dátumhoz
    JDateChooser dcdob;

    // Legördülő listák a képzéshez és szakhoz
    JComboBox<String> cbcourse, cbbranch;

    // Gombok
    JButton submit, cancel;

    // Véletlenszám generálása a hallgatói kódhoz
    Random ran = new Random();
    long first4 = Math.abs((ran.nextLong() % 9000L) + 1000L);

    AddStudent() {

        // Ablak méretének és pozíciójának beállítása
        setSize(900, 700);
        setLocation(350, 50);
        setLayout(null);
        setTitle("Hallgatói Adatfelvétel");

        // Cím
        JLabel heading = new JLabel("Új hallgató adatainak felvétele");
        heading.setBounds(250, 30, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 30));
        add(heading);

        // Név
        JLabel lblname = new JLabel("Név");
        lblname.setBounds(50, 150, 100, 30);
        lblname.setFont(new Font("serif", Font.BOLD, 20));
        add(lblname);

        tfname = new JTextField();
        tfname.setBounds(200, 150, 150, 30);
        add(tfname);

        // Anyja neve
        JLabel lblmname = new JLabel("Anyja neve");
        lblmname.setBounds(400, 150, 200, 30);
        lblmname.setFont(new Font("serif", Font.BOLD, 20));
        add(lblmname);

        tfmname = new JTextField();
        tfmname.setBounds(600, 150, 150, 30);
        add(tfmname);

        // Hallgatói kód
        JLabel lblrollno = new JLabel("Hallgatói kód");
        lblrollno.setBounds(50, 200, 200, 30);
        lblrollno.setFont(new Font("serif", Font.BOLD, 20));
        add(lblrollno);

        labelrollno = new JLabel("EC" + first4);
        labelrollno.setBounds(200, 200, 200, 30);
        labelrollno.setFont(new Font("serif", Font.BOLD, 20));
        labelrollno.setForeground(new Color(0, 102, 204)); 
        add(labelrollno);

        // Születési dátum
        JLabel lbldob = new JLabel("Születési dátum");
        lbldob.setBounds(400, 200, 200, 30);
        lbldob.setFont(new Font("serif", Font.BOLD, 20));
        add(lbldob);

        dcdob = new JDateChooser();
        dcdob.setBounds(600, 200, 150, 30);
        add(dcdob);

        // Lakcím
        JLabel lbladdress = new JLabel("Lakcím");
        lbladdress.setBounds(50, 250, 200, 30);
        lbladdress.setFont(new Font("serif", Font.BOLD, 20));
        add(lbladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(200, 250, 150, 30);
        add(tfaddress);

        // Telefonszám
        JLabel lblphone = new JLabel("Telefonszám");
        lblphone.setBounds(400, 250, 200, 30);
        lblphone.setFont(new Font("serif", Font.BOLD, 20));
        add(lblphone);

        tfphone = new JTextField();
        tfphone.setBounds(600, 250, 150, 30);
        add(tfphone);

        // Email cím
        JLabel lblemail = new JLabel("Email cím");
        lblemail.setBounds(50, 300, 200, 30);
        lblemail.setFont(new Font("serif", Font.BOLD, 20));
        add(lblemail);

        tfemail = new JTextField();
        tfemail.setBounds(200, 300, 150, 30);
        add(tfemail);

        // 10. osztályos eredmény
        JLabel lblx = new JLabel("10. osztály");
        lblx.setBounds(400, 300, 200, 30);
        lblx.setFont(new Font("serif", Font.BOLD, 20));
        add(lblx);

        tfx = new JTextField();
        tfx.setBounds(600, 300, 150, 30);
        add(tfx);

        // 12. osztályos eredmény
        JLabel lblxii = new JLabel("12. osztály");
        lblxii.setBounds(50, 350, 200, 30);
        lblxii.setFont(new Font("serif", Font.BOLD, 20));
        add(lblxii);

        tfxii = new JTextField();
        tfxii.setBounds(200, 350, 150, 30);
        add(tfxii);

        // TAJ szám
        JLabel lblssn = new JLabel("TAJ szám");
        lblssn.setBounds(400, 350, 200, 30);
        lblssn.setFont(new Font("serif", Font.BOLD, 20));
        add(lblssn);

        tfssn = new JTextField();
        tfssn.setBounds(600, 350, 150, 30);
        add(tfssn);

        // Képzés típusa (pl. BSc, MSc)
        JLabel lblcourse = new JLabel("Képzés");
        lblcourse.setBounds(50, 400, 200, 30);
        lblcourse.setFont(new Font("serif", Font.BOLD, 20));
        add(lblcourse);

        String[] course = {"BSc", "MSc", "BA", "MA", "MBA", "PhD"};
        cbcourse = new JComboBox<>(course);
        cbcourse.setBounds(200, 400, 150, 30);
        cbcourse.setBackground(Color.WHITE);
        add(cbcourse);

        // Szak
        JLabel lblbranch = new JLabel("Szak");
        lblbranch.setBounds(400, 400, 200, 30);
        lblbranch.setFont(new Font("serif", Font.BOLD, 20));
        add(lblbranch);

        String[] branch = {"Programtervező", "Villamosmérnök", "Gépészmérnök", "Építőmérnök", "Informatikus"};
        cbbranch = new JComboBox<>(branch);
        cbbranch.setBounds(600, 400, 150, 30);
        cbbranch.setBackground(Color.WHITE);
        add(cbbranch);

        // Rögzítés gomb
        submit = new JButton("Rögzítés");
        submit.setBounds(250, 550, 120, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(this);
        submit.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(submit);

        // Mégse gomb
        cancel = new JButton("Mégse");
        cancel.setBounds(450, 550, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        cancel.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(cancel);

        // Ablak megjelenítése
        setVisible(true);
    }

    // Gombok eseménykezelése
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            // Adatok begyűjtése
            String name = tfname.getText().trim();
            String mname = tfmname.getText().trim();
            String rollno = labelrollno.getText();
            String dob = ((JTextField) dcdob.getDateEditor().getUiComponent()).getText().trim();
            String address = tfaddress.getText().trim();
            String phone = tfphone.getText().trim();
            String email = tfemail.getText().trim();
            String x = tfx.getText().trim();
            String xii = tfxii.getText().trim();
            String ssn = tfssn.getText().trim();
            String course = (String) cbcourse.getSelectedItem();
            String branch = (String) cbbranch.getSelectedItem();

            // Alapvető ellenőrzések
            if (name.isEmpty() || mname.isEmpty() || dob.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kérlek, tölts ki minden kötelező mezőt!");
                return;
            }

            phone = phone.trim();

            if (!phone.matches("^\\+\\d{10,15}$")) {
                JOptionPane.showMessageDialog(null, "A telefonszámnak + jellel kell kezdődnie, majd 10–15 számjegyből kell állnia.\nPélda: +36201234567");
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(null, "Hibás email formátum!");
                return;
            }

            try {
                // SQL lekérdezés létrehozása és futtatása
                String query = "INSERT INTO student VALUES('" + name + "', '" + mname + "', '" + rollno + "', '" + dob + "', '" + address + "', '" + phone + "', '" + email + "', '" + x + "', '" + xii + "', '" + ssn + "', '" + course + "', '" + branch + "')";

                Conn con = new Conn();
                con.getStatement().executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Hallgatói adatok sikeresen rögzítve.");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Hiba történt az adatbázis mentés során.");
            }
        } else {
            // Mégse gomb megnyomásakor bezárjuk az ablakot
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new AddStudent();
    }
}
