package export;

import elements.MainFrame;
import mop.JSONToMopParser;
import org.json.JSONObject;
import util.AbstractDialog;
import util.JSONParser;
import util.VerticalTitledTable;

import javax.swing.*;
import java.awt.*;


public class ExportMopsToJSONDialog extends AbstractDialog {

    private VerticalTitledTable input;
    private MainFrame mainFrame;

    public ExportMopsToJSONDialog(MainFrame mainFrame) {
        super();

        Dimension dialogSize = new Dimension(600, 150);

        this.setSize(dialogSize);
        this.setTitle("Eksport MOP-ów");
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.mainFrame = mainFrame;
        this.add(inputTable());
        this.add(submitButton());

        this.setVisible(true);
    }

    private VerticalTitledTable inputTable() {
        String[] columnNames = {"", ""};
        Object[][] spacesData = {
                {"Nazwa pliku wyjściowego", ""},
        };
        input = new VerticalTitledTable("", spacesData, columnNames);
        return input;
    }

    private JButton submitButton() {
        JButton submit = new JButton("Eksportuj");
        submit.addActionListener(
                e -> {
                    input.endEditing();
                    String outPath = input.getValueAt(0, 1).toString();
                    String message = "";
                    if (outPath.equals("")) {
                        message = "Proszę wybrać nazwę pliku wejściowego";
                    }
                    if (!message.equals("")) {
                        JOptionPane.showMessageDialog(this, message);
                    } else {
                        JSONObject json = JSONToMopParser.parseSet(mainFrame.getMopInfos());
                        JSONParser.writeJsonToFile(json, outPath);
                        JOptionPane.showMessageDialog(mainFrame.getFrame(),
                                "Poprawnie wyeksportowano dane.");
                        this.setVisible(false);
                    }
                });
        return submit;
    }
}
