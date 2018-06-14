package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;



public class HorizontalTitledTableWithChechboxes extends TitledTable {
    protected JScrollPane scroll;

    public HorizontalTitledTableWithChechboxes(String title, Object[][] data, String[] columnNames, boolean editable) {
        super(title, data, columnNames);
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editable;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return Boolean.class;
            }
            @Override
            public String getColumnName(int index) {
                return columnNames[index];
            }
        };
        model.setColumnIdentifiers(columnNames);
        jTable = new JTable(model);
        jTable.setRowHeight(30);
        scroll = new JScrollPane(jTable);
        scroll.setColumnHeader(new JViewport() {
            @Override public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.height = 40;
                return d;
            }
        });
        this.add(scroll);
    }

}
