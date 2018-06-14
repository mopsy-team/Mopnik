package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.stream.IntStream;

public class VerticalTitledTable extends TitledTable {
    public VerticalTitledTable(String title, Object[][] data, String[] columnNames, int[] editableRows) {
        super(title, data, columnNames);
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0 && IntStream.of(editableRows).anyMatch(i -> i == row);
            }
        };
        jTable = new JTable(model);
        jTable.setRowHeight(30);
        this.add(jTable);
    }

    public VerticalTitledTable(String title, Object[][] data, String[] columnNames){
        this(title, data, columnNames, IntStream.range(0, data.length).toArray());
    }
}
