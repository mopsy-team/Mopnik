package simulations;

import mopsim.network.NetworkCreator;
import util.AbstractDialog;
import util.FilePicker;
import util.VerticalTitledTable;

import javax.swing.*;
import java.awt.*;

import static config.AppConfig.*;

public class GenerateMapDialog extends AbstractDialog {

    private FilePicker networkPicker;
    private VerticalTitledTable input;
    private JFileChooser fileChooser;

    public GenerateMapDialog() {
        super();

        fileChooser = new JFileChooser();
        Dimension dialogSize = new Dimension(600, 150);

        this.setSize(dialogSize);
        this.setTitle("Generacja siatki drogowej");
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        networkPicker = addFilePicker("Plik z siecią drogową",
                "Wybierz", ".osm",
                getPath(getMapOsmFilename()), "Open Street Map");

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
        JButton submit = new JButton("Generuj");
        submit.addActionListener(
                e -> {
                    input.endEditing();
                    String outPath = input.getValueAt(0, 1).toString();

                    String networkPath = networkPicker.getSelectedFilePath();
                    String message = "";
                    if (outPath.equals("")) {
                        message = "Proszę wybrać nazwę pliku wyjściowego";
                    }
                    if (!message.equals("")) {
                        JOptionPane.showMessageDialog(this, message);
                    } else {
                        if (networkPath.equals("")) {
                            networkPath = getPath(getMapXmlFilename());
                        }

                        final String _networkPath = networkPath;
                        Thread thread = new Thread(() -> {
                            NetworkCreator networkCreator = new NetworkCreator(_networkPath, outPath);
                            networkCreator.loadNetworkFromOsm();
                            networkCreator.write();
                            JOptionPane.showMessageDialog(this,
                                    "Poprawnie wygenerowano mapę.");
                            this.setVisible(false);
                        });
                        thread.start();
                    }
                });
        return submit;
    }

    private FilePicker addFilePicker(String textFieldLabel, String buttonLabel, String extension, String initialValue, String description) {
        return addFilePicker(textFieldLabel, buttonLabel, new String[]{extension}, initialValue, description);
    }
    private FilePicker addFilePicker(String textFieldLabel, String buttonLabel, String[] extensions, String initialValue, String description) {
        FilePicker filePicker = new FilePicker(textFieldLabel, buttonLabel, initialValue, fileChooser);
        filePicker.setMode(FilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(extensions, description);
        this.add(filePicker);
        return filePicker;
    }
}