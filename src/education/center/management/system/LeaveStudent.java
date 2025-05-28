package education.center.management.system;

/**
 *
 * @author Ferenc Szijarto
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class LeaveStudent extends JFrame implements ActionListener {

    Choice cRollNo, cTime;
    JDateChooser dcDate;
    JButton submitBtn, cancelBtn;
    JLabel lblStudentName;

    LeaveStudent() {
        setTitle("Hallgatói távollét űrlap");
        setSize(500, 550);
        setLocation(550, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Cím
        JLabel heading = new JLabel("Távollét igénylése (hallgató)");
        heading.setBounds(40, 50, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(heading);

        // Neptun-kód választó felirat
        JLabel lblRollNo = new JLabel("Keresés Hallgatói kód alapján");
        lblRollNo.setBounds(60, 100, 250, 20);
        lblRollNo.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(lblRollNo);

        // Neptun-kód választó
        cRollNo = new Choice();
        cRollNo.setBounds(60, 130, 200, 20);
        add(cRollNo);

        // Név címke felirat
        JLabel lblNameTitle = new JLabel("Hallgató neve");
        lblNameTitle.setBounds(60, 160, 200, 20);
        lblNameTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(lblNameTitle);

        // Név megjelenítése
        lblStudentName = new JLabel();
        lblStudentName.setBounds(60, 185, 300, 20);
        lblStudentName.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblStudentName);

        // Hallgatók betöltése adatbázisból
        try {
            Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("SELECT rollno FROM student");
            while (rs.next()) {
                cRollNo.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hiba a hallgatók betöltésekor.");
        }

        // Neptun-kód változásra frissítjük a nevet
        cRollNo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateStudentName();
            }
        });

        // Kezdeti név beállítása
        if (cRollNo.getItemCount() > 0) {
            updateStudentName();
        }

        // Dátum felirat
        JLabel lblDate = new JLabel("Dátum");
        lblDate.setBounds(60, 220, 200, 20);
        lblDate.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(lblDate);

        // Dátum kiválasztó
        dcDate = new JDateChooser();
        dcDate.setBounds(60, 250, 200, 25);
        dcDate.setDateFormatString("yyyy-MM-dd"); // Beállítjuk a formátumot
        add(dcDate);

        // Időtartam felirat
        JLabel lblTime = new JLabel("Időtartam");
        lblTime.setBounds(60, 300, 200, 20);
        lblTime.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(lblTime);

        // Időtartam választó
        cTime = new Choice();
        cTime.setBounds(60, 330, 200, 20);
        cTime.add("Egész nap");
        cTime.add("Fél nap");
        cTime.add("Igazolt távollét");
        cTime.add("Késés");
        add(cTime);

        // Küldés gomb
        submitBtn = new JButton("Küldés");
        submitBtn.setBounds(60, 400, 100, 30);
        submitBtn.setBackground(Color.BLACK);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Tahoma", Font.BOLD, 15));
        submitBtn.addActionListener(this);
        add(submitBtn);

        // Mégse gomb
        cancelBtn = new JButton("Mégse");
        cancelBtn.setBounds(200, 400, 100, 30);
        cancelBtn.setBackground(Color.BLACK);
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Tahoma", Font.BOLD, 15));
        cancelBtn.addActionListener(this);
        add(cancelBtn);

        setVisible(true);
    }

    // Név frissítése az aktuális Hallgatói-kód alapján
    private void updateStudentName() {
        String rollNo = cRollNo.getSelectedItem();
        try {
            Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("SELECT name FROM student WHERE rollno = '" + rollNo + "'");
            if (rs.next()) {
                lblStudentName.setText(rs.getString("name"));
            } else {
                lblStudentName.setText("Név nem található");
            }
        } catch (Exception e) {
            lblStudentName.setText("Hiba a név betöltésekor");
        }
    }

    // Gombesemények kezelése
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitBtn) {
            String rollNo = cRollNo.getSelectedItem();
            String dateInput = ((JTextField) dcDate.getDateEditor().getUiComponent()).getText();
            String duration = cTime.getSelectedItem();

            // Ha nincs dátum kiválasztva
            if (dateInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kérlek, válassz egy dátumot!");
                return;
            }

            // Dátum ellenőrzés és formázás
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);

            java.util.Date parsedDate;
            try {
                parsedDate = sdf.parse(dateInput);
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(this, "Érvénytelen dátumformátum! (pl.: 2025-05-18)");
                return;
            }

            // Adatok mentése az adatbázisba
            String query = "INSERT INTO studentleave (rollno, date, duration) VALUES (?, ?, ?)";
            try {
                Conn c = new Conn();
                PreparedStatement pst = c.getConnection().prepareStatement(query);
                pst.setString(1, rollNo);
                pst.setString(2, sdf.format(parsedDate));
                pst.setString(3, duration);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Távollét sikeresen rögzítve.");
                setVisible(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Hiba a mentés során!");
                e.printStackTrace();
            }

        } else {
            // Mégse gomb
            setVisible(false);
        }
    }

    // Főprogram
    public static void main(String[] args) {
        new LeaveStudent();
    }
}
