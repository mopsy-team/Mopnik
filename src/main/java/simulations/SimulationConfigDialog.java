package simulations;

import mopsim.MOPSimRun;
import mopsim.config_group.MOPSimConfigGroup;
import util.AbstractDialog;
import util.FilePicker;
import util.VerticalTitledTable;

import javax.swing.*;
import java.awt.*;

import static config.AppConfig.*;

public class SimulationConfigDialog extends AbstractDialog {

    private FilePicker networkPicker;
    private FilePicker mopPicker;
    private FilePicker carPicker;
    private FilePicker truckPicker;
    private FilePicker busPicker;
    private VerticalTitledTable input;
    private JFileChooser fileChooser;

    public SimulationConfigDialog() {
        super();

        fileChooser = new JFileChooser();
        Dimension dialogSize = new Dimension(800, 480);

        this.setSize(dialogSize);
        this.setTitle("Ustal dane wejściowe symulacji");
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        networkPicker = addFilePicker("Plik z siecią drogową",
                "Wybierz", ".xml",
                getPath(getMapXmlFilename()), "Mapa drogowa");
        mopPicker = addFilePicker("Układ MOPów",
                "Wybierz", ".json",
                getPath(getMopJSONFilename()), "Plik JSON");
        carPicker = addFilePicker("Macierz samochodów osobowych",
                "Wybierz", ".csv",
                getPath(getCarMatrixFilename()), "Plik CSV");
        truckPicker = addFilePicker("Macierz samochodów ciężarowych",
                "Wybierz", ".csv",
                getPath(getTruckMatrixFilename()), "Plik CSV");
        busPicker = addFilePicker("Macierz autobusów",
                "Wybierz", ".csv",
                getPath(getBusMatrixFilename()), "Plik CSV");

        this.add(inputTable());
        this.add(submitButton());

        this.setVisible(true);
    }


    private JButton submitButton() {
        JButton submit = new JButton("Przeprowadź symulację");
        submit.addActionListener(
                e -> {
                    input.endEditing();
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
                    if (!message.equals("")) {
                        JOptionPane.showMessageDialog(this, message);
                    } else {
                        if (mopPath.equals("")) {
                            mopPath = getPath(getMopJSONFilename());
                        }
                        if (networkPath.equals("")) {
                            networkPath = getPath(getMapXmlFilename());
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
                        mopsimConfig.setThreadNr(threadsNr);

                        Thread thread = new Thread(() -> {
                            MOPSimRun.run(mopsimConfig);
                            JOptionPane.showMessageDialog(this,
                                    "Zakończono przeprowadzanie symulacji.");
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

    private int makeInt(Object o) {
        int ret = 0;
        try {
            ret = Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            ret = 0; //TODO
        }
        return ret;
    }

    private VerticalTitledTable inputTable() {
        String[] columnNames = {"", ""};
        Object[][] spacesData = {
                {"Liczba pojazdów osobowych", ""},
                {"Liczba pojazdów ciężarowych", ""},
                {"Liczba pojazdów autobusowych", ""},
                {"Id symulacji", ""},
                {"Liczba wątków", "1"}
        };
        input = new VerticalTitledTable("Stałe użyte w symulacji", spacesData, columnNames);
        return input;
    }
}


