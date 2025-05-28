package education.center.management.system;

/**
 *
 * @author Ferenc Szijarto
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener {

    private Image backgroundImage;

    public Dashboard() {
        setTitle("Oktatási Központ - Vezérlőpult");
        setSize(1540, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Háttérkép betöltése
        java.net.URL imgURL = ClassLoader.getSystemResource("icons/background01.jpg");
        if (imgURL != null) {
            backgroundImage = new ImageIcon(imgURL).getImage().getScaledInstance(1540, 850, Image.SCALE_SMOOTH);
        } else {
            System.err.println("Nem található a kép: icons/background01.jpg");
        }

        // Menü létrehozása
        JMenuBar mb = new JMenuBar();

        Color menuColor = new Color(60, 60, 60);
        Color textColor = new Color(255, 255, 255);

        // --- Menük és almenük ---
        JMenu newInformation = createMenu("Új adatfelvétel", menuColor);
        newInformation.add(createMenuItem("Új oktatói adat"));
        newInformation.add(createMenuItem("Új hallgatói adat"));
        mb.add(newInformation);

        JMenu details = createMenu("Adatok megtekintése", menuColor);
        details.add(createMenuItem("Oktatói adatok"));
        details.add(createMenuItem("Hallgatói adatok"));
        mb.add(details);

        JMenu leave = createMenu("Szabadság kérelem", menuColor);
        leave.add(createMenuItem("Oktatói szabadság"));
        leave.add(createMenuItem("Hallgatói hiányzás"));
        mb.add(leave);

        JMenu leaveDetails = createMenu("Hiányzás részletek", menuColor);
        leaveDetails.add(createMenuItem("Oktatói szabadság részletei"));
        leaveDetails.add(createMenuItem("Hallgatói hiányzások"));
        mb.add(leaveDetails);

        JMenu exam = createMenu("Vizsgák", menuColor);
        exam.add(createMenuItem("Vizsgaeredmények"));
        exam.add(createMenuItem("Jegyek rögzítése"));
        mb.add(exam);

        JMenu updateInfo = createMenu("Adatok frissítése", menuColor);
        updateInfo.add(createMenuItem("Oktatói adatok frissítése"));
        updateInfo.add(createMenuItem("Hallgatói adatok frissítése"));
        mb.add(updateInfo);

        JMenu fee = createMenu("Tandíj", menuColor);
        fee.add(createMenuItem("Tandíj szerkezete"));
        fee.add(createMenuItem("Hallgatói tandíj űrlap"));
        mb.add(fee);

        JMenu utility = createMenu("Segédeszközök", menuColor);
        utility.add(createMenuItem("Jegyzettömb"));
        utility.add(createMenuItem("Számológép"));
        mb.add(utility);

        JMenu about = createMenu("Névjegy", menuColor);
        about.add(createMenuItem("Névjegy"));
        mb.add(about);

        JMenu exit = createMenu("Kilépés", menuColor);
        exit.add(createMenuItem("Kilépés"));
        mb.add(exit);

        setJMenuBar(mb);

        // Tartalom panel beállítása háttérképpel
        setContentPane(new BackgroundPanel());
        setVisible(true);
    }

    // Menü létrehozása
    private JMenu createMenu(String title, Color fgColor) {
        JMenu menu = new JMenu(title);
        menu.setForeground(fgColor.brighter());
        return menu;
    }

    // Menüelem létrehozása
    private JMenuItem createMenuItem(String title) {
        JMenuItem item = new JMenuItem(title);
        item.setBackground(Color.WHITE);
        item.addActionListener(this);
        return item;
    }

    // Háttérkép panel
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();

        switch (msg) {
            case "Kilépés":
                setVisible(false);
                break;
            case "Számológép":
                try {
                    Runtime.getRuntime().exec("calc.exe");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Jegyzettömb":
                try {
                    Runtime.getRuntime().exec("notepad.exe");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Új oktatói adat":
                new AddTeacher();
                break;
            case "Új hallgatói adat":
                new AddStudent();
                break;
            case "Oktatói adatok":
                new TeacherDetails();
                break;
            case "Hallgatói adatok":
                new StudentDetails();
                break;
            case "Oktatói szabadság":
                new LeaveTeacher();
                break;
            case "Hallgatói hiányzás":
                new LeaveStudent();
                break;
            case "Oktatói szabadság részletei":
                new TeacherLeaveDetails();
                break;
            case "Hallgatói hiányzások":
                new StudentLeaveDetails();
                break;
            case "Oktatói adatok frissítése":
                new UpdateTeacher();
                break;
            case "Hallgatói adatok frissítése":
                new UpdateStudent();
                break;
            case "Jegyek rögzítése":
                new EnterMarks();
                break;
            case "Vizsgaeredmények":
                new ExaminationDetails();
                break;
            case "Tandíj szerkezete":
                new FeeStructure();
                break;
            case "Hallgatói tandíj űrlap":
                new StudentFeeForm();
                break;
            case "Névjegy":
                new About();
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
