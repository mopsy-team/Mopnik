package mop;

import config.AppConfig;
import util.AbstractDialog;
import util.VerticalTitledTable;

import javax.swing.*;

public class SetUrlDialog extends AbstractDialog {
    private VerticalTitledTable input;
    public SetUrlDialog() {
        super();
        this.setSize(800, 160);
        this.setTitle("Konfiguracja aplikacji");
        this.setLocationRelativeTo(null); // center the dialog.

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));


        this.add(inputTable());
        this.add(submitButton());

        this.setVisible(true);
    }
    private JButton submitButton() {
        JButton submit = new JButton("Zapisz");
        submit.addActionListener(
                e -> {
                    input.endEditing();
                    String url = input.getValueAt(0, 1).toString();
                    AppConfig.setMopsUrl(url);
                    AppConfig.save();
                    this.setVisible(false);
                });
        return submit;
    }
    private VerticalTitledTable inputTable() {
        String[] columnNames = {"", ""};
        Object[][] spacesData = {
                {"Url serwera", AppConfig.getMopsUrl()},
        };
        input = new VerticalTitledTable("", spacesData, columnNames);
        return input;
    }
}
