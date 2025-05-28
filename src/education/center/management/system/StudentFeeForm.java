package education.center.management.system;

/**
 *
 * @author Ferenc Szijarto
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.util.HashMap;

public class StudentFeeForm extends JFrame implements ActionListener {

    Choice crollno;
    JComboBox<String> cbcourse, cbbranch, cbsemester;
    JLabel labeltotal;
    JButton update, pay, back;

    JLabel labelname, labelmname;

    // Térkép a magyar félév nevek és az angol adatbázisoszlop nevek között
    HashMap<String, String> semesterMap;

    StudentFeeForm() {
        setSize(900, 500);
        setLocation(300, 100);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Háttérkép
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/fee.jpg"));
        Image i2 = i1.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(400, 50, 500, 300);
        add(image);

        // Hallgatói kód
        JLabel lblrollnumber = new JLabel("Hallgatói kód kiválasztása:");
        lblrollnumber.setBounds(40, 60, 200, 20);
        lblrollnumber.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblrollnumber);

        crollno = new Choice();
        crollno.setBounds(250, 60, 150, 20);
        add(crollno);

        try {
            Conn c = new Conn();
            ResultSet rs = c.getStatement().executeQuery("select * from student");
            while (rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Név
        JLabel lblname = new JLabel("Név:");
        lblname.setBounds(40, 100, 150, 20);
        lblname.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblname);

        labelname = new JLabel();
        labelname.setBounds(250, 100, 150, 20);
        labelname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(labelname);

        // Anyja neve
        JLabel lblmname = new JLabel("Anyja neve:");
        lblmname.setBounds(40, 140, 150, 20);
        lblmname.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblmname);

        labelmname = new JLabel();
        labelmname.setBounds(250, 140, 150, 20);
        labelmname.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(labelmname);

        crollno.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                try {
                    Conn c = new Conn();
                    String query = "select * from student where rollno='" + crollno.getSelectedItem() + "'";
                    ResultSet rs = c.getStatement().executeQuery(query);
                    if (rs.next()) {
                        labelname.setText(rs.getString("name"));
                        labelmname.setText(rs.getString("fname"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Szak
        JLabel lblcourse = new JLabel("Szak:");
        lblcourse.setBounds(40, 180, 150, 20);
        lblcourse.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblcourse);

        String course[] = {"BSc", "MSc", "MBA", "MCA", "BA", "MA"};
        cbcourse = new JComboBox<>(course);
        cbcourse.setBounds(250, 180, 150, 20);
        cbcourse.setBackground(Color.WHITE);
        add(cbcourse);

        // Tanszék
        JLabel lblbranch = new JLabel("Tanszék:");
        lblbranch.setBounds(40, 220, 150, 20);
        lblbranch.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblbranch);

        String branch[] = {"Számítástechnika", "Elektronika", "Gépészet", "Építőmérnök", "Informatika"};
        cbbranch = new JComboBox<>(branch);
        cbbranch.setBounds(250, 220, 150, 20);
        cbbranch.setBackground(Color.WHITE);
        add(cbbranch);

        // Félév
        JLabel lblsemester = new JLabel("Félév:");
        lblsemester.setBounds(40, 260, 150, 20);
        lblsemester.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblsemester);

        // Magyar feliratok – ezek jelennek meg a JComboBox-ban
        String semestersHU[] = {"1. félév", "2. félév", "3. félév", "4. félév", "5. félév", "6. félév", "7. félév", "8. félév"};
        cbsemester = new JComboBox<>(semestersHU);
        cbsemester.setBounds(250, 260, 150, 20);
        cbsemester.setBackground(Color.WHITE);
        add(cbsemester);

        // Térkép a magyar és angol oszlopnevek között
        semesterMap = new HashMap<>();
        for (int i = 1; i <= 8; i++) {
            semesterMap.put(i + ". félév", "semester" + i);
        }

        // Összeg
        JLabel lbltotal = new JLabel("Fizetendő összeg:");
        lbltotal.setBounds(40, 300, 200, 20);
        lbltotal.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lbltotal);

        labeltotal = new JLabel();
        labeltotal.setBounds(250, 300, 150, 20);
        labeltotal.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(labeltotal);

        // Gombok
        update = new JButton("Frissítés");
        update.setBounds(30, 380, 100, 25);
        update.setBackground(Color.BLACK);
        update.setForeground(Color.WHITE);
        update.addActionListener(this);
        add(update);

        pay = new JButton("Befizetés");
        pay.setBounds(150, 380, 100, 25);
        pay.setBackground(Color.BLACK);
        pay.setForeground(Color.WHITE);
        pay.addActionListener(this);
        add(pay);

        back = new JButton("Vissza");
        back.setBounds(270, 380, 100, 25);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        // Alapértelmezett adatok betöltése
        try {
            Conn c = new Conn();
            String query = "select * from student where rollno='" + crollno.getSelectedItem() + "'";
            ResultSet rs = c.getStatement().executeQuery(query);
            if (rs.next()) {
                labelname.setText(rs.getString("name"));
                labelmname.setText(rs.getString("mname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == update) {
            String course = (String) cbcourse.getSelectedItem();
            String semesterHU = (String) cbsemester.getSelectedItem(); // pl. "1. félév"
            String semesterEN = semesterMap.get(semesterHU); // pl. "semester1"

            try {
                Conn c = new Conn();
                ResultSet rs = c.getStatement().executeQuery("select * from fee where course = '" + course + "'");
                if (rs.next()) {
                    labeltotal.setText(rs.getString(semesterEN));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == pay) {
            String rollno = crollno.getSelectedItem();
            String course = (String) cbcourse.getSelectedItem();
            String semesterHU = (String) cbsemester.getSelectedItem();
            String semesterEN = semesterMap.get(semesterHU);
            String branch = (String) cbbranch.getSelectedItem();
            String total = labeltotal.getText();

            try {
                Conn c = new Conn();
                String query = "insert into collegefee values('" + rollno + "', '" + course + "', '" + branch + "', '" + semesterEN + "', '" + total + "')";
                c.getStatement().executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Befizetés sikeres!");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new StudentFeeForm();
    }
}
