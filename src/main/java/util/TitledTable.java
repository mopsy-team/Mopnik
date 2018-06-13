package util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public abstract class TitledTable extends JPanel {
    protected JTable jTable;
    public TitledTable(String title, Object[][] data, String[] columnNames) {
        this.setLayout(new CardLayout(10, 10));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP));
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

    public Object getValueAt(int x, int y) {
        return jTable.getValueAt(x, y);
    }

    public void endEditing() {
        if(jTable.isEditing()){
            jTable.getCellEditor().stopCellEditing();
        }
    }
}
