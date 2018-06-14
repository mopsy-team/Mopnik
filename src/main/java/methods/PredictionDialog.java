package methods;

import elements.MainFrame;
import exceptions.ValidationError;
import util.AbstractDialog;
import util.TitledTable;
import util.VerticalTitledTable;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;

import static util.Validator.makeUnsignedDouble;

public class PredictionDialog extends AbstractDialog {
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
        TitledTable table = new VerticalTitledTable("Parametry metodyki", data, columnNames);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        // TODO (MG) add textfield with explanation
        this.add(scrollPane);

        JButton submit = new JButton("Ustaw parametry");
        submit.addActionListener(event -> {
            table.endEditing();
            try {
                for (int j = 0; j < fields.size(); ++j) {
                    f[j].setCar(makeUnsignedDouble(table.getValueAt(j, 1), "Parametr metodyki"));
                    f[j].setTruck(makeUnsignedDouble(table.getValueAt(j, 2), "Parametr metodyki"));
                    f[j].setBus(makeUnsignedDouble(table.getValueAt(j, 3), "Parametr metodyki"));
                }
                method.setFields(Arrays.asList(f));
                frame.changeMethod(method);
                this.dispose();
            }
            catch (ValidationError err){
                err.alert();
            }
        });
        this.add(submit);
        this.setVisible(true);
    }
}
