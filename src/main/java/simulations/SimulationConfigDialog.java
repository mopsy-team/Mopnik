package simulations;

import util.FilePicker;

import javax.swing.*;
import java.awt.*;

public class SimulationConfigDialog extends JDialog {

    public SimulationConfigDialog() {
        super();
        Dimension dialogSize = new Dimension(800, 600);
        this.setSize(dialogSize);
        this.setTitle("Ustal dane wejściowe symulacji");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String osmFilePath = addFilePicker("Wybierz plik z mapą",
                "Wybierz", ".osm", "Open Street Map");

        String mopSettingPath = addFilePicker("Wybierz układ MOPów",
                "Wybierz", ".xml", "Spreadsheet");

        String travelMatricesPath = addFilePicker("Wybierz macierze podróży",
                "Wybierz", ".csv", "CSV file");

        JButton submit = new JButton("Przeprowadź symulację");
        submit.addActionListener(
                e -> System.out.println("start simulation")); // TODO
        this.add(submit);

        this.setVisible(true);
    }

    private String addFilePicker(String textFieldLabel, String buttonLabel, String extension, String description) {
        FilePicker filePicker = new FilePicker(textFieldLabel, buttonLabel);
        filePicker.setMode(FilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(extension, description);
        String filePath = filePicker.getSelectedFilePath();
        this.add(filePicker);
        return filePath;
    }
}


