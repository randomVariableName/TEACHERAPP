import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class gradesPanel extends JPanel{
    private int grades;
    public DefaultTableModel gradesTableModel;
    public ArrayList<Object[]> gradesRecords;
    gradesPanel(int num, ArrayList<Object[]> thing) {
        grades = num;
        gradesRecords = thing;
        initializeGradesPanel();
    }
    private boolean isValidStudent (String studentId, String studentName){
        for (Object[] student : SchoolManagementApp.data[grades]) {
            if (studentId.equals(student[0].toString()) && studentName.equals(student[1])) {
                return true;
            }
        }
        return false;
    }
    private void initializeGradesPanel () {
        this.setLayout(new BorderLayout());
        String[] gradeColumnNames = {"Student ID", "Name", "Test", "Grade"};
        gradesTableModel = new DefaultTableModel(gradeColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable gradesTable = new JTable(gradesTableModel);
        gradesTable.setAutoCreateRowSorter(true);
        JScrollPane gradesScrollPane = new JScrollPane(gradesTable);
        gradesTable.setFillsViewportHeight(true);
        JPanel addGradePanel = new JPanel(new GridLayout(0, 4));
        JTextField studentIdField = new JTextField();
        JTextField studentNameField = new JTextField();
        JTextField testField = new JTextField();
        JTextField gradeField = new JTextField();
        studentIdField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    studentIdField.transferFocus();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        studentNameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    studentNameField.transferFocus();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        testField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    testField.transferFocus();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        gradeField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    String studentId = studentIdField.getText();
                    String studentName = studentNameField.getText();
                    String test = testField.getText();
                    String grade = gradeField.getText();
                    if (isValidStudent(studentId, studentName) && !test.isEmpty() && !grade.isEmpty()) {
                        gradesTableModel.addRow(new Object[]{studentId, studentName, test, grade});
                        gradesRecords.add(new Object[]{studentId, studentName, test, grade});
                        studentIdField.setText("");
                        studentNameField.setText("");
                        testField.setText("");
                        gradeField.setText("");
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        JButton addGradeButton = new JButton("Add Grade");
        for (int i = 0; i<gradesRecords.size(); i++) {
            gradesTableModel.addRow(gradesRecords.get(i));
        }
        addGradeButton.addActionListener((e -> {
            String studentId = studentIdField.getText();
            String studentName = studentNameField.getText();
            String test = testField.getText();
            String grade = gradeField.getText();
            if (isValidStudent(studentId, studentName) && !test.isEmpty() && !grade.isEmpty()) {
                gradesTableModel.addRow(new Object[]{studentId, studentName, test, grade});
                gradesRecords.add(new Object[]{studentId, studentName, test, grade});
                studentIdField.setText("");
                studentNameField.setText("");
                testField.setText("");
                gradeField.setText("");
            }
        }));
        gradesTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 8) {
                    gradesRecords.remove(gradesTable.getSelectedRow());
                    gradesTableModel.removeRow(gradesTable.getSelectedRow());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        addGradePanel.add(new JLabel("Student ID"));
        addGradePanel.add(studentIdField);
        addGradePanel.add(new JLabel("Name"));
        addGradePanel.add(studentNameField);
        addGradePanel.add(new JLabel("Test"));
        addGradePanel.add(testField);
        addGradePanel.add(new JLabel("Grade (%)"));
        addGradePanel.add(gradeField);
        addGradePanel.add(addGradeButton);
        this.add(addGradePanel, BorderLayout.PAGE_START);
        this.add(gradesScrollPane, BorderLayout.CENTER);
    }
    public ArrayList<Object[]> getGradesRecords() {
        return gradesRecords;
    }
}
