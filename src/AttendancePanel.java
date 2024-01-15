import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AttendancePanel extends JPanel{
    private final int grade;
    private DefaultTableModel attendanceTableModel;
    private JSpinner dateSpinner;
    public HashMap<String, Boolean[]> attendanceRecords;
    AttendancePanel(int num, HashMap<String, Boolean[]> attend) {
        attendanceRecords = attend;
        grade = num;
        initializeAttendancePanel();
    }
    private void initializeAttendancePanel () {
        this.setLayout(new BorderLayout());
        String[] columnNames = {"Student ID", "Name", "Attendance"};
        attendanceTableModel = new DefaultTableModel(SchoolManagementApp.data[grade], columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 2 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        JTable attendanceTable = new JTable(attendanceTableModel);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        attendanceTable.setFillsViewportHeight(true);
        JPanel datePanel = new JPanel();
        JLabel dateLabel = new JLabel("Date:");
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
        dateSpinner.addChangeListener(e -> onDateChanged());
        Calendar calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dateSpinner.setValue(calendar.getTime());
        datePanel.add(dateLabel);
        datePanel.add(dateSpinner);
        this.add(datePanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        loadAttendanceForDate(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
        onDateChanged();
    }
    private String previousDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    private void onDateChanged (){
        String selectedDate = getSelectedDate();
        if (((SpinnerDateModel) dateSpinner.getModel()).getDate().getTime() < SchoolManagementApp.calendar.getTime().getTime() || ((SpinnerDateModel) dateSpinner.getModel()).getDate().getTime() > new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).getTime().getTime()) {
            if(((SpinnerDateModel) dateSpinner.getModel()).getDate().getTime() < SchoolManagementApp.calendar.getTime().getTime()) {
                dateSpinner.setValue(SchoolManagementApp.calendar.getTime());
            } else {
                Calendar calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dateSpinner.setValue(calendar.getTime());
            }
            selectedDate = getSelectedDate();
            saveCurrentAttendance(previousDate);
            saveCurrentAttendance(selectedDate);
        }
        if (previousDate != null && !previousDate.equals(selectedDate)) {
            saveCurrentAttendance(previousDate);
            loadAttendanceForDate(selectedDate);
        }
        previousDate = selectedDate;
    }
    public void saveCurrentAttendance (String date){
        int rowCount = attendanceTableModel.getRowCount();
        Boolean[] attendanceForDate = new Boolean[rowCount];
        for (int i = 0; i < rowCount; i++) {
            attendanceForDate[i] = (Boolean) attendanceTableModel.getValueAt(i, 2);
        }
        attendanceRecords.put(date, attendanceForDate);
    }
    private void loadAttendanceForDate (String date){
        Boolean[] attendanceForDate = attendanceRecords.getOrDefault(date, new Boolean[attendanceTableModel.getRowCount()]);
        for (int i = 0; i < attendanceTableModel.getRowCount(); i++) {
            attendanceTableModel.setValueAt(attendanceForDate[i], i, 2);
        }
    }
    public String getSelectedDate () {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(((SpinnerDateModel) dateSpinner.getModel()).getDate());
    }
    public HashMap<String, Boolean[]> getAttendanceRecords() {
        return attendanceRecords;
    }
    public void getThis() {
        saveCurrentAttendance(getSelectedDate());
    }
}
