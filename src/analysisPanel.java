import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class analysisPanel extends JPanel {
    private final int grade;
    private DefaultTableModel analysisTableModel;
    private final ArrayList<ArrayList<Object[]>> gradesRecords;
    private final ArrayList<HashMap<String, Boolean[]>> attendanceRecords;
    analysisPanel(int num, ArrayList<ArrayList<Object[]>> g, ArrayList<HashMap<String, Boolean[]>> a) {
        gradesRecords = g;
        attendanceRecords = a;
        grade = num;
        initializeAnalysisPanel();
    }
    private void initializeAnalysisPanel () {
        this.setLayout(new BorderLayout());
        String[] columnNames = {"Student ID", "Name", "Average Grade", "Total Attendance"};
        JPanel addAnalysisPanel = new JPanel(new GridLayout(0, 1));
        JButton addAnalysisButton = new JButton("Refresh");
        addAnalysisPanel.add(addAnalysisButton);
        analysisTableModel = new DefaultTableModel(new Object[][] {}, columnNames);
        JTable analysisTable = new JTable(analysisTableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        analysisTable.setAutoCreateRowSorter(true);
        getGrades();
        addAnalysisButton.addActionListener((e -> {
            gradesRecords.set(grade, SchoolManagementApp.gradesPanels.get(grade).getGradesRecords());
            analysisTableModel = new DefaultTableModel(new Object[][] {}, columnNames);
            getGrades();
            analysisTable.setModel(analysisTableModel);
        }));
        JScrollPane scrollPane = new JScrollPane(analysisTable);
        analysisTable.setFillsViewportHeight(true);
        this.add(addAnalysisPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void getGrades() {
        ArrayList<Integer> att = getAttendance();
        for (int i = 0; i<SchoolManagementApp.data[grade].length; i++){
            int avgrade = 0;
            int numgrades = 0;
            for (int j=0; j<(gradesRecords.get(grade)).size(); j++) {
                if ((Integer) SchoolManagementApp.data[grade][i][0] == Integer.parseInt((String) gradesRecords.get(grade).get(j)[0])) {
                    numgrades+=1;
                    avgrade+=Integer.parseInt((String) gradesRecords.get(grade).get(j)[3]);
                }
            }
            int total;
            if (numgrades == 0) {
                total = 0;
            } else {
                total = Math.round((float) avgrade / numgrades);
            }
            analysisTableModel.addRow(new Object[] {SchoolManagementApp.data[grade][i][0], SchoolManagementApp.data[grade][i][1], total, att.get(i)});
        }
    }

    private ArrayList<Integer> getAttendance() {
        SchoolManagementApp.attendancePanels.get(grade).getThis();
        ArrayList<Integer> totals = new ArrayList<>();
        Calendar calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        for (int i = 0; i<SchoolManagementApp.data[grade].length; i++){
            int numthere = 0;
            long numtotal = getDifferenceDays(SchoolManagementApp.calendar.getTime(), calendar.getTime())+1;
            for (String key : attendanceRecords.get(grade).keySet()) {
                if (attendanceRecords.get(grade).get(key)[i] != null && attendanceRecords.get(grade).get(key)[i]) {
                    numthere += 1;
                }
            }
            int total;
            if (numtotal == 0) {
                total = 0;
            } else {
                total = Math.round(100*((float) numthere /numtotal));
            }
            totals.add(total);
        }
        return totals;
    }
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

}
