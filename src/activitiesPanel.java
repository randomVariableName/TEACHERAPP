import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class activitiesPanel extends JPanel {
    private int grade;
    private DefaultTableModel activitiesTableModel;
    public ArrayList<Object[]> activitiesRecords;
    activitiesPanel(int num, ArrayList<Object[]> thing) {
        grade = num;
        activitiesRecords = thing;
        initializeActivitiesPanel();
    }
    private boolean isValidStudent (String studentId, String studentName){
        for (Object[] student : SchoolManagementApp.data[grade]) {
            if (studentId.equals(student[0].toString()) && studentName.equals(student[1])) {
                return true;
            }
        }
        return false;
    }
    private void initializeActivitiesPanel() {
        this.setLayout(new BorderLayout());
        String[] activitiesColumnNames = {"Student ID", "Name", "Note"};
        activitiesTableModel = new DefaultTableModel(activitiesColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable activitiesTable = new JTable(activitiesTableModel);
        activitiesTable.setAutoCreateRowSorter(true);
        JScrollPane activitiesScrollPanel = new JScrollPane(activitiesTable);
        activitiesTable.setFillsViewportHeight(true);
        JPanel addActivitiesPanel = new JPanel(new GridLayout(0, 4));
        JTextField studentIdField = new JTextField();
        JTextField studentNameField = new JTextField();
        JTextField noteField = new JTextField();
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
        noteField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    String studentId = studentIdField.getText();
                    String studentName = studentNameField.getText();
                    String note = noteField.getText();
                    if (isValidStudent(studentId, studentName) && !note.isEmpty()) {
                        activitiesTableModel.addRow(new Object[]{studentId, studentName, note});
                        activitiesRecords.add(new Object[]{studentId, studentName, note});
                        studentIdField.setText("");
                        studentNameField.setText("");
                        noteField.setText("");
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        JButton addNoteButton = new JButton("Add Note");
        for (int i = 0; i<activitiesRecords.size(); i++) {
            activitiesTableModel.addRow(activitiesRecords.get(i));
        }
        addNoteButton.addActionListener((e -> {
            String studentId = studentIdField.getText();
            String studentName = studentNameField.getText();
            String note = noteField.getText();
            if (isValidStudent(studentId, studentName) && !note.isEmpty()) {
                activitiesTableModel.addRow(new Object[]{studentId, studentName, note});
                activitiesRecords.add(new Object[]{studentId, studentName, note});
                studentIdField.setText("");
                studentNameField.setText("");
                noteField.setText("");
            }
        }));
        activitiesTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 8) {
                    activitiesRecords.remove(activitiesTable.getSelectedRow());
                    activitiesTableModel.removeRow(activitiesTable.getSelectedRow());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        addActivitiesPanel.add(new JLabel("Student ID"));
        addActivitiesPanel.add(studentIdField);
        addActivitiesPanel.add(new JLabel("Name"));
        addActivitiesPanel.add(studentNameField);
        addActivitiesPanel.add(new JLabel("Note"));
        addActivitiesPanel.add(noteField);
        addActivitiesPanel.add(new JLabel(""));
        addActivitiesPanel.add(new JLabel(""));
        addActivitiesPanel.add(addNoteButton);
        this.add(addActivitiesPanel, BorderLayout.PAGE_START);
        this.add(activitiesScrollPanel, BorderLayout.CENTER);
    }
}
