package elements;

import methods.CustomMethod;
import methods.Method;
import methods.PredictionDialog;
import simulations.SimulationConfigDialog;
import way.TrafficInfoParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class MainMenu {

    private Dimension dialogSize;
    private MainFrame mainFrame;

    public MainMenu(MainFrame _mainFrame) {
        //Where the GUI is created:
        //JRadioButtonMenuItem rbMenuItem;
        //JCheckBoxMenuItem cbMenuItem;
        mainFrame = _mainFrame;
        dialogSize = new Dimension(800, 600);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(addFromFileMenu());
        menuBar.add(predictionsMenu());
        menuBar.add(addFromServerMenu());
        menuBar.add(simulationMenu());
        mainFrame.getFrame().setJMenuBar(menuBar);
    }

    private JMenu addFromFileMenu() {

        JMenu menu = new JMenu("Dodaj dane z pliku");
        menu.setMnemonic(KeyEvent.VK_D);

        JMenuItem menuItem;
        menuItem = new JMenuItem("Średniodobowe natężenie ruchu",
                KeyEvent.VK_S);
        menuItem.addActionListener(event -> {
            final JFileChooser fc = new JFileChooser();
            fc.setPreferredSize(dialogSize);
            int returnVal = fc.showOpenDialog(fc);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (TrafficInfoParser.assignRoutes(mainFrame, file) == -1) {
                    JOptionPane.showMessageDialog(mainFrame.getFrame(),
                            "Wskazany plik nie istnieje lub jest w złym formacie.",
                            "Zły format pliku",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Układ MOPów",
                KeyEvent.VK_U);
        menuItem.addActionListener(event -> {
            final JFileChooser fc = new JFileChooser();
            fc.setPreferredSize(dialogSize);
            int returnVal = fc.showOpenDialog(fc);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mainFrame.setMopPointsFromFile(file);
            }
        });
        menu.add(menuItem);
        return menu;
    }

    private JMenu predictionsMenu() {
        JMenu menu = new JMenu("Predykcje");
        menu.setMnemonic(KeyEvent.VK_P);

        JMenu mopPredictions = new JMenu("Zajętości MOP-ów");

        JMenuItem item = new JMenuItem("Domyślna metodyka");
        Method method = new CustomMethod();
        item.addActionListener(event -> {
            new PredictionDialog(method, mainFrame);
        });
        mopPredictions.add(item);
        menu.add(mopPredictions);
        return menu;
    }

    private JMenu addFromServerMenu() {
        JMenu menu = new JMenu("Dodaj dane z serwera");
        menu.setMnemonic(KeyEvent.VK_A);

        JMenuItem menuItem = new JMenuItem("Układ MOP-ów", KeyEvent.VK_U);
        menuItem.addActionListener(event -> {
            mainFrame.setMopPointsFromServer();
        });
        menu.add(menuItem);
        return menu;
    }

    private JMenu simulationMenu() {

        JMenu menu = new JMenu("Symulacje");
        menu.setMnemonic(KeyEvent.VK_S);
        menu.addActionListener(event -> {
            new SimulationConfigDialog();
        });
        return menu;
    }
}
