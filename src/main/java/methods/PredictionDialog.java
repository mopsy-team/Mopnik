package methods;

import elements.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.Collection;

public class PredictionDialog extends JDialog {
    public PredictionDialog(Method method, MainFrame frame) {
        super();
        this.setSize(800, 600);
        this.setTitle(method.toString());
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String[] columnNames = new String[]{"Wartość", "Samochody osobowe", "Samochody ciężarowe", "Autobusy"};
        Collection<MethodValue> fields = method.getFields();
        Object[][] data = new Object[fields.size()][];
        int i = 0;
        MethodValue[] f = new MethodValue[fields.size()];
        for (MethodValue field : fields) {
            Object[] row = new Object[]{field.getDescription(), field.getCar(), field.getTruck(), field.getBus()};
            data[i] = row;
            f[i] = field;
            ++i;
        }
        JTable table = new JTable(data, columnNames);
        table.setModel(new DefaultTableModel(data, columnNames) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        });


        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        // TODO (MG) add textfield with explanation
        this.add(scrollPane);

        JButton submit = new JButton("Ustaw parametry");
        submit.addActionListener(event -> {
            for (int j = 0; j < fields.size(); ++j) {
                f[j].setCar(Double.parseDouble(table.getValueAt(j, 1).toString()));
                f[j].setTruck(Double.parseDouble(table.getValueAt(j, 2).toString()));
                f[j].setBus(Double.parseDouble(table.getValueAt(j, 3).toString()));
            }
            method.setFields(Arrays.asList(f));
            frame.addMethod(method);
        });
        this.add(submit);
        this.setVisible(true);
    }
}
