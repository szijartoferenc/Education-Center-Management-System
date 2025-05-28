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

public class LeaveTeacher extends JFrame implements ActionListener {

    Choice cempId, cTime;
    JDateChooser dcDate;
    JButton submitBtn, cancelBtn;
    JLabel lblTeacherName;

    LeaveTeacher() {
        setTitle("Oktatói távollét űrlap");
        setSize(500, 550);
        setLocation(550, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Cím
        JLabel heading = new JLabel("Távollét igénylése (oktató)");
        heading.setBounds(40, 50, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(heading);

        // Neptun-kód választó felirat
        JLabel lblRollNo = new JLabel("Keresés Oktatói kód alapján");
        lblRollNo.setBounds(60, 100, 250, 20);
        lblRollNo.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(lblRollNo);

        // Neptun-kód választó
        cempId = new Choice();
        cempId.setBounds(60, 130, 200, 20);
        add(cempId);

        // Név címke felirat
        JLabel lblNameTitle = new JLabel("Oktató neve");
        lblNameTitle.setBounds(60, 160, 200, 20);
        lblNameTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(lblNameTitle);

        // Név megjelenítése
        lblTeacherName = new JLabel();
        lblTeacherName.setBounds(60, 185, 300, 20);
        lblTeacherName.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblTeacherName);

        // Hallgatók betöltése adatbázisból
        try {
            Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("SELECT empId FROM teacher");
            while (rs.next()) {
                cempId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hiba a oktatók betöltésekor.");
        }

        // Neptun-kód változásra frissítjük a nevet
        cempId.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateTeacherName();
            }
        });

        // Kezdeti név beállítása
        if (cempId.getItemCount() > 0) {
            updateTeacherName();
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
    private void updateTeacherName() {
        String empId = cempId.getSelectedItem();
        try {
            Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("SELECT name FROM teacher WHERE empid = '" + empId + "'");
            if (rs.next()) {
                lblTeacherName.setText(rs.getString("name"));
            } else {
                lblTeacherName.setText("Név nem található");
            }
        } catch (Exception e) {
            lblTeacherName.setText("Hiba a név betöltésekor");
        }
    }

    // Gombesemények kezelése
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitBtn) {
            String empId = cempId.getSelectedItem();
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
            String query = "INSERT INTO teacherleave (empid, date, duration) VALUES (?, ?, ?)";
            try {
                Conn c = new Conn();
                PreparedStatement pst = c.getConnection().prepareStatement(query);
                pst.setString(1, empId);
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
        new LeaveTeacher();
    }
}
