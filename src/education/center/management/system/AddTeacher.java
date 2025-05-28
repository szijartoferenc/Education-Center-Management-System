package education.center.management.system;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;

/**
 * Oktató adatainak hozzáadása felület
 * @author Ferenc Szijarto
 */
public class AddTeacher extends JFrame implements ActionListener {

    // Szövegmezők
    JTextField tfName, tfMotherName, tfAddress, tfPhone, tfEmail, tfXII, tfDegree, tfSSN;
    JLabel labelEmpId;
    JDateChooser dcDOB;
    JComboBox<String> cbCourse, cbBranch;
    JButton btnSubmit, btnCancel;

    // Egyedi azonosító generálás
    Random ran = new Random();
    long uniqueId = Math.abs((ran.nextLong() % 9000L) + 1000L);

    public AddTeacher() {
        setSize(900, 700);
        setLocation(350, 50);
        setLayout(null); // Megőrizve az abszolút pozicionálás

        addLabel("Új oktató adatai", 150, 30, 500, 50, 30, true);

        tfName = addLabeledTextField("Név", 50, 150);
        tfMotherName = addLabeledTextField("Anyja neve", 400, 150);

        addLabel("Oktatói azonosító", 50, 200);
        labelEmpId = new JLabel("101" + uniqueId);
        labelEmpId.setBounds(230, 200, 200, 30);
        labelEmpId.setFont(new Font("serif", Font.BOLD, 20));
        labelEmpId.setForeground(new Color(0, 102, 204)); 
        add(labelEmpId);

        addLabel("Születési dátum", 400, 200);
        dcDOB = new JDateChooser();
        dcDOB.setBounds(550, 200, 150, 30);
        add(dcDOB);

        tfAddress = addLabeledTextField("Lakcím", 50, 250);
        tfPhone = addLabeledTextField("Telefonszám", 400, 250);
        tfEmail = addLabeledTextField("Email cím", 50, 300);
        tfXII = addLabeledTextField("Alapdiploma", 400, 300);
        tfDegree = addLabeledTextField("Mesterdiploma", 50, 350);
        tfSSN = addLabeledTextField("TAJ szám", 400, 350);

        addLabel("Végzettség", 50, 400);
        String[] courses = {"B.Tech", "BBA", "BCA", "Bsc", "Msc", "MBA", "MCA", "MCom", "MA", "BA"};
        cbCourse = new JComboBox<>(courses);
        cbCourse.setBounds(200, 400, 150, 30);
        cbCourse.setBackground(Color.WHITE);
        add(cbCourse);

        addLabel("Tanszék", 400, 400);
        String[] branches = {"Computer Science", "Electronics", "Mechanical", "Civil", "IT"};
        cbBranch = new JComboBox<>(branches);
        cbBranch.setBounds(550, 400, 150, 30);
        cbBranch.setBackground(Color.WHITE);
        add(cbBranch);

        btnSubmit = addButton("Mentés", 250, 550);
        btnCancel = addButton("Mégse", 450, 550);

        setVisible(true);
    }

    // Címkék hozzáadása
    private void addLabel(String text, int x, int y) {
        addLabel(text, x, y, 200, 30, 20, false);
    }

    private void addLabel(String text, int x, int y, int w, int h, int fontSize, boolean center) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);
        label.setFont(new Font("serif", Font.BOLD, fontSize));
        if (center) label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label);
    }

    // Szövegmező + címke együtt
    private JTextField addLabeledTextField(String label, int x, int y) {
        addLabel(label, x, y);
        JTextField tf = new JTextField();
        tf.setBounds(x + 150, y, 150, 30);
        add(tf);
        return tf;
    }

    // Gomb hozzáadása
    private JButton addButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 120, 30);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Tahoma", Font.BOLD, 15));
        btn.addActionListener(this);
        add(btn);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSubmit) {
            String name = tfName.getText();
            String mname = tfMotherName.getText();
            String empId = labelEmpId.getText();
            String dob = ((JTextField) dcDOB.getDateEditor().getUiComponent()).getText();
            String address = tfAddress.getText();
            String phone = tfPhone.getText();
            String email = tfEmail.getText();
            String xii = tfXII.getText();
            String degree = tfDegree.getText();
            String ssn = tfSSN.getText();
            String course = (String) cbCourse.getSelectedItem();
            String branch = (String) cbBranch.getSelectedItem();

            // Alapellenőrzés
            if (name.isEmpty() || mname.isEmpty() || dob.isEmpty() || address.isEmpty() ||
                phone.isEmpty() || email.isEmpty() || xii.isEmpty() || degree.isEmpty() || ssn.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Minden mezőt ki kell tölteni");
                return;
            }

            phone = phone.trim();

            if (!phone.matches("^\\+\\d{10,15}$")) {
                JOptionPane.showMessageDialog(null, "A telefonszámnak + jellel kell kezdődnie, majd 10–15 számjegyből kell állnia.\nPélda: +36201234567");
                return;
            }

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(null, "Érvénytelen email formátum");
                return;
            }

            // Adatok mentése
            try {
                String query = "INSERT INTO teacher VALUES('" + name + "', '" + mname + "', '" + empId + "', '" + dob + "', '" +
                        address + "', '" + phone + "', '" + email + "', '" + xii + "', '" + degree + "', '" +
                        ssn + "', '" + course + "', '" + branch + "')";

                Conn con = new Conn();
                con.getStatement().executeUpdate(query);

                JOptionPane.showMessageDialog(null, "Oktató adatai sikeresen elmentve");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (ae.getSource() == btnCancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new AddTeacher();
    }
}
