package util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.JTable.*;
import java.awt.*;

public class TitledTable extends JPanel {
    private JTable jTable;
    public TitledTable(String title, Object[][] data, String[] columnNames) {
        jTable = new JTable(data, columnNames);
        this.setLayout(new CardLayout(10, 10));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP));
        jTable.setRowHeight(30);
        this.add(jTable);

        System.out.println(jTable.getDefaultEditor(Object.class).getClass());

    }

    public void setNotEditable() {
        jTable.setDefaultEditor(Object.class, null);
    }

    public void setEditable() {
        jTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));
    }

    public Object[] getRow (int n) {
        Object[] res = new Object[jTable.getColumnCount()];
        for (int i = 0; i < jTable.getColumnCount(); ++i) {
            res[i] = jTable.getValueAt(n, i);
        }
        return res;
    }

    public Object[] getColumn (int n) {
        Object[] res = new Object[jTable.getRowCount()];
        for (int i = 0; i < jTable.getRowCount(); ++i) {
            res[i] = jTable.getValueAt(i, n);
        }
        return res;
    }

}
