import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class PieChartPanel extends JPanel implements TableCellRenderer {

    private int[] values;

    public PieChartPanel() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        this.values = (int[]) value;
        return this;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color[] colors = {Color.RED, Color.BLUE};
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int chartDiameter = Math.min(panelWidth, panelHeight) - 100;
        int x = (panelWidth - chartDiameter) / 2;
        int y = (panelHeight - chartDiameter) / 2;
        int startAngle = 0;
        for (int i = 0; i < values.length; i++) {
            g.setColor(colors[i]);
            g.fillArc(x, y, chartDiameter, chartDiameter, startAngle, values[i]);
            startAngle += values[i];
        }
    }

}
