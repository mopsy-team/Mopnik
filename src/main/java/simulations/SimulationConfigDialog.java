package simulations;

import mopsim.MOPSimRun;
import mopsim.config_group.MOPSimConfigGroup;
import util.AbstractDialog;
import util.FilePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static config.AppConfig.getMapFilename;
import static config.AppConfig.getMopFilename;
import static config.AppConfig.getPath;

public class SimulationConfigDialog extends AbstractDialog {

    private FilePicker networkPicker;
    private FilePicker mopPicker;
    private FilePicker carPicker;
    private FilePicker truckPicker;
    private FilePicker busPicker;
    private JTable input;
    private JFileChooser fileChooser;
    public SimulationConfigDialog() {
        super();

        fileChooser = new JFileChooser();
        Dimension dialogSize = new Dimension(800, 480);

        this.setSize(dialogSize);
        this.setTitle("Ustal dane wejściowe symulacji");
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        networkPicker = addFilePicker("Plik z siecią drogową",
                "Wybierz", new String[]{".xml", ".osm"}, "Open Street Map");
        mopPicker = addFilePicker("Układ MOPów",
                "Wybierz", ".json", "json");
        carPicker = addFilePicker("Macierz samochodów osobowych",
                "Wybierz", ".csv", "CSV file");
        truckPicker = addFilePicker("Macierz samochodów ciężarowych",
                "Wybierz", ".csv", "CSV file");
        busPicker = addFilePicker("Macierz autobusów",
                "Wybierz", ".csv", "CSV file");

        this.add(inputTable());
        this.add(submitButton());

        this.setVisible(true);
    }


    private JButton submitButton() {
        JButton submit = new JButton("Przeprowadź symulację");
        submit.addActionListener(
                e -> {
                    int carNr = makeInt(input.getValueAt(0, 1));
                    int truckNr = makeInt(input.getValueAt(1, 1));
                    int busNr = makeInt(input.getValueAt(2, 1));
                    String simulationId = input.getValueAt(3, 1).toString();
                    int threadsNr = makeInt(input.getValueAt(4, 1));


                    String networkPath = networkPicker.getSelectedFilePath();
                    String mopPath = mopPicker.getSelectedFilePath();
                    String carPath = carPicker.getSelectedFilePath();
                    String truckPath = truckPicker.getSelectedFilePath();
                    String busPath = busPicker.getSelectedFilePath();

                    String message = "";
                    if (carPath.equals("") && carNr > 0) {
                        message = "Liczba samochodów osobowych jest dodatnia, " +
                                "ale nie wybrano macierzy samochodów osobowych";
                    }
                    if (truckPath.equals("") && truckNr > 0) {
                        message = "Liczba samochodów ciężarowych jest dodatnia, " +
                                "ale nie wybrano macierzy samochodów ciężarowych";
                    }
                    if (busPath.equals("") && busNr > 0) {
                        message = "Liczba autobusów jest dodatnia, " +
                                "ale nie wybrano macierzy autobusów";
                    }
                    System.out.println(carNr + " " + truckNr + " " + busNr + simulationId +
                    " " + carPath + " " + truckPath + " " + busPath + " " + mopPath);
                    if (!message.equals("")) {
                        JOptionPane.showMessageDialog(this, message);
                    }
                    else {
                        if (mopPath.equals("")) {
                            mopPath = getPath(getMopFilename());
                        }
                        if (networkPath.equals("")) {
                            networkPath = getPath(getMapFilename());
                        }
                        MOPSimConfigGroup mopsimConfig = new MOPSimConfigGroup();
                        mopsimConfig.setCarNr(carNr);
                        mopsimConfig.setTruckNr(truckNr);
                        mopsimConfig.setBusNr(busNr);

                        mopsimConfig.setCarPath(carPath);
                        mopsimConfig.setTruckPath(truckPath);
                        mopsimConfig.setBusPath(busPath);
                        mopsimConfig.setMopData(mopPath);
                        mopsimConfig.setMapPath(networkPath);
                        mopsimConfig.setSimulationId(simulationId);
                        //mopsimConfig.setThreadsNr(threadsNr);
                        MOPSimRun.run(mopsimConfig);
                        this.setVisible(false);
                    }
                });
        return submit;
    }

    private FilePicker addFilePicker(String textFieldLabel, String buttonLabel, String extension, String description) {
        String[] extenstions = {extension};
        return addFilePicker(textFieldLabel, buttonLabel, extenstions, description);
    }


    private FilePicker addFilePicker(String textFieldLabel, String buttonLabel, String[] extensions, String description) {
        FilePicker filePicker = new FilePicker(textFieldLabel, buttonLabel, fileChooser);
        filePicker.setMode(FilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(extensions, description);
        this.add(filePicker);
        return filePicker;
    }

    private int makeInt(Object o) {
        int ret = 0;
        try {
            ret = Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            ret = 0; //TODO
        }
        return ret;
    }

    private JTable inputTable() {
        String[] columnNames = {"", ""};
        Object[][] spacesData = {
                {"Liczba pojazdów osobowych", ""},
                {"Liczba pojazdów ciężarowych", ""},
                {"Liczba pojazdów autobusowych", ""},
                {"Id symulacji", ""},
                {"Liczba wątków", "1"}
        };
        DefaultTableModel model = new DefaultTableModel(spacesData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };
        input = new JTable(model);
        return input;
    }
}


