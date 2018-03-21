package util;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TitledTable extends JPanel {
    public TitledTable(String title, Object[][] data, String[] columnNames) {
        JTable jTable = new JTable(data, columnNames);
        this.setLayout(new CardLayout(10, 10));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP));
        jTable.setDefaultEditor(Object.class, null);
        jTable.setRowHeight(30);
        this.add(jTable);

    }
}
