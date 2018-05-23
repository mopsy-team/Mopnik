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

        FilePicker osmFilePicker = addFilePicker("Wybierz plik z mapą",
                "Wybierz", ".osm", "Open Street Map");

        FilePicker mopFilePicker = addFilePicker("Wybierz układ MOPów",
                "Wybierz", ".xml", "Spreadsheet");

        FilePicker travelMatricesFilePicker = addFilePicker("Wybierz macierze podróży",
                "Wybierz", ".csv", "CSV file");

        JButton submit = new JButton("Przeprowadź symulację");
        submit.addActionListener(
                e -> {
                    String message = "Proszę wczytać następujące plik:\n";
                    boolean notSet = false;
                    if (osmFilePicker.getSelectedFilePath().equals("")) {
                        message += "plik Open Street Map\n";
                        notSet = true;
                    }
                    if (mopFilePicker.getSelectedFilePath().equals("")) {
                        message += "plik z MOPami \n";
                        notSet = true;
                    }
                    if (travelMatricesFilePicker.getSelectedFilePath().equals("")) {
                        message += "macierze podróży";
                        notSet = true;
                    }
                    if (notSet){
                        JOptionPane.showMessageDialog(this, message);
                    }
                    else {
                        System.out.println(osmFilePicker.getSelectedFilePath());
                        this.setVisible(false);
                    }
                }); // TODO
        this.add(submit);

        this.setVisible(true);
    }

    private FilePicker addFilePicker(String textFieldLabel, String buttonLabel, String extension, String description) {
        FilePicker filePicker = new FilePicker(textFieldLabel, buttonLabel);
        filePicker.setMode(FilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(extension, description);
        this.add(filePicker);
        return filePicker;
    }
}


