package util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VerticalTitledTable extends TitledTable {
    protected JScrollPane scroll;

    public VerticalTitledTable(String title, Object[][] data, String[] columnNames, int[] editableRows) {
        super(title, data, columnNames);
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0 && IntStream.of(editableRows).anyMatch(i -> i == row);
            }
            @Override
            public String getColumnName(int index) {
                return columnNames[index];
            }
        };

        model.setColumnIdentifiers(columnNames);
        boolean displayHeader = Stream.of(columnNames).anyMatch(s -> !s.isEmpty());
        jTable = new JTable(model);
        jTable.setRowHeight(30);
        if(! displayHeader) {
            jTable.setTableHeader(null);
        }
        scroll = new JScrollPane(jTable);
        if(displayHeader) {
            scroll.setColumnHeader(new JViewport() {
                @Override
                public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    d.height = 40;
                    return d;
                }
            });
        }
        this.add(scroll);
    }

    public VerticalTitledTable(String title, Object[][] data, String[] columnNames){
        this(title, data, columnNames, IntStream.range(0, data.length).toArray());
    }
}
