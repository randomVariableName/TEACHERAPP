import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.*;

public class SchoolManagementApp implements java.io.Serializable{
    public ArrayList<attendancePanel> attendancePanels;
    public static Calendar calendar = new GregorianCalendar(2023, Calendar.DECEMBER, 7);
    public static ArrayList<gradesPanel> gradesPanels;
    public ArrayList<activitiesPanel> activitiesPanels;
    public ArrayList<analysisPanel> analysisPanels;
    private JFrame loginFrame;
    private JFrame frame;
    private JPanel loginPanel;
    public Object[] datakey;
    private boolean first = true;
    public ArrayList<ArrayList<Object[]>> gradesRecords = new ArrayList<>();
    public ArrayList<ArrayList<Object[]>> activitiesRecords = new ArrayList<>();
    public ArrayList<HashMap<String, Boolean[]>> attendanceRecords = new ArrayList<>(4);
    public static Object[][][] data;
    public SchoolManagementApp() {
        try {
            FileInputStream fii = new FileInputStream("datakey.txt");
            ObjectInputStream oii = new ObjectInputStream(fii);
            datakey = (Object[]) oii.readObject();
            oii.close();
            fii.close();
            FileInputStream fi = new FileInputStream("data.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            data = (Object[][][]) oi.readObject();
            oi.close();
            fi.close();
            FileInputStream f = new FileInputStream("attendance.txt");
            ObjectInputStream o = new ObjectInputStream(f);
            attendanceRecords = (ArrayList<HashMap<String, Boolean[]>>) o.readObject();
            o.close();
            f.close();
            FileInputStream a = new FileInputStream("grades.txt");
            ObjectInputStream b = new ObjectInputStream(a);
            gradesRecords = (ArrayList<ArrayList<Object[]>>) b.readObject();
            b.close();
            a.close();
            FileInputStream aa = new FileInputStream("activities.txt");
            ObjectInputStream bb = new ObjectInputStream(aa);
            activitiesRecords = (ArrayList<ArrayList<Object[]>>) bb.readObject();
            bb.close();
            aa.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error initializing stream");
        }
        attendancePanels = new ArrayList<>();
        gradesPanels = new ArrayList<>();
        activitiesPanels = new ArrayList<>();
        analysisPanels = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            attendancePanels.add(new attendancePanel(i, attendanceRecords.get(i)));
            gradesPanels.add(new gradesPanel(i, gradesRecords.get(i)));
            activitiesPanels.add(new activitiesPanel(i, activitiesRecords.get(i)));
            analysisPanels.add(new analysisPanel(i, gradesRecords, attendanceRecords));
        }
        initializeLoginUI();
    }
    private void initializeLoginUI() {
        loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginPanel = new JPanel();
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("Login", loginPanel);
        initializeLoginPanel();
        loginFrame.add(pane, BorderLayout.CENTER);
        loginFrame.setLocationRelativeTo(null); 
        loginFrame.setVisible(true);
    }
    private void initializeLoginPanel() {
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = getjButton(usernameField, passwordField);
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);
    }
    private JButton getjButton(JTextField usernameField, JPasswordField passwordField) {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("thompson") && password.equals("password")) {
                loginFrame.dispose();
                if (first) {
                    initializeMainAppUI();
                    first = false;
                } else {
                    frame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Incorrect Login", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });
        return loginButton;
    }
    private void initializeMainAppUI() {
        frame = new JFrame("School Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        JComboBox select = new JComboBox(datakey);
        JButton logout = new JButton();
        logout.setText("Logout");
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Attendance", attendancePanels.get(0));
        tabbedPane.addTab("Grades", gradesPanels.get(0));
        tabbedPane.addTab("Notes", activitiesPanels.get(0));
        tabbedPane.addTab("Analysis", analysisPanels.get(0));
        JPanel things = new JPanel(new BorderLayout());
        things.add(select, BorderLayout.CENTER);
        things.add(logout, BorderLayout.LINE_END);
        frame.add(things, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        loginFrame.setVisible(false);
        frame.addWindowListener(new WindowAdapter() {
            
        });
        logout.addActionListener(e -> {
            frame.setVisible(false);
            initializeLoginUI();
        });
        select.addActionListener(l -> {
            tabbedPane.removeAll();
            tabbedPane.addTab("Attendance", attendancePanels.get(select.getSelectedIndex()));
            tabbedPane.addTab("Grades", gradesPanels.get(select.getSelectedIndex()));
            tabbedPane.addTab("Notes", activitiesPanels.get(select.getSelectedIndex()));
            tabbedPane.addTab("Analysis", analysisPanels.get(select.getSelectedIndex()));
        });
        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                for (int i=0; i<data.length;i++) {
                    attendancePanels.get(i).saveCurrentAttendance(attendancePanels.get(i).getSelectedDate());
                    attendanceRecords.add(attendancePanels.get(i).getAttendanceRecords());
                    gradesRecords.add(gradesPanels.get(i).gradesRecords);
                    activitiesRecords.add(activitiesPanels.get(i).activitiesRecords);
                }
                try {
                    FileOutputStream fii = new FileOutputStream("attendance.txt");
                    ObjectOutputStream oii = new ObjectOutputStream(fii);
                    oii.writeObject(attendanceRecords);
                    oii.close();
                    fii.close();
                    FileOutputStream f = new FileOutputStream("grades.txt");
                    ObjectOutputStream o = new ObjectOutputStream(f);
                    o.writeObject(gradesRecords);
                    o.close();
                    f.close();
                    FileOutputStream fi = new FileOutputStream("activities.txt");
                    ObjectOutputStream oi = new ObjectOutputStream(fi);
                    oi.writeObject(activitiesRecords);
                    oi.close();
                    fi.close();
                } catch (FileNotFoundException e) {
                    System.out.println("File not found");
                } catch (IOException e) {
                    System.out.println("Error initializing stream");
                }
            }
        };
        frame.addWindowListener(listener);

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SchoolManagementApp::new);
    }
}