package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HorizontalTitledTable extends TitledTable {
    public HorizontalTitledTable(String title, Object[][] data, String[] columnNames) {
        super(title, data, columnNames);
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return row > 0;
            }
        };
        jTable = new JTable(model);
        jTable.setRowHeight(30);
        this.add(jTable);
    }
}
