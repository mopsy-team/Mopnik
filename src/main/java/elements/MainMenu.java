package elements;

import adding.AddMopPanel;
import config.AppConfig;
import export.ExportMopsToJSONDialog;
import methods.CustomMethod;
import methods.Method;
import methods.PredictionDialog;
import mop.SetUrlDialog;
import simulations.GenerateMapDialog;
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
        mainFrame = _mainFrame;
        dialogSize = new Dimension(800, 600);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(addFromFileMenu());
        menuBar.add(predictionsMenu());
        menuBar.add(addFromServerMenu());
        menuBar.add(simulationMenu());
        menuBar.add(addingMenu());
        menuBar.add(exportToFileMenu());
        mainFrame.getFrame().setJMenuBar(menuBar);
    }

    private JMenu exportToFileMenu() {
        JMenu menu = new JMenu("Wyeksportuj do pliku");
        menu.setMnemonic(KeyEvent.VK_W);

        JMenuItem menuItem;
        menuItem = new JMenuItem("Układ MOPów",
                KeyEvent.VK_U);
        menuItem.addActionListener(event -> {
            new ExportMopsToJSONDialog(mainFrame);
        });

        menu.add(menuItem);
        return menu;
    }
    private JMenu addFromFileMenu() {

        JMenu menu = new JMenu("Wczytaj dane z pliku");
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
                AppConfig.save();
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

        menuItem = new JMenuItem("Mapa drogowa",
                KeyEvent.VK_M);
        menuItem.addActionListener(event -> {
            final JFileChooser fc = new JFileChooser();
            fc.setPreferredSize(dialogSize);
            int returnVal = fc.showOpenDialog(fc);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                mainFrame.setMapFromFile(file);
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
        item.addActionListener(event -> new PredictionDialog(method, mainFrame));
        mopPredictions.add(item);
        menu.add(mopPredictions);
        return menu;
    }

    private JMenu addFromServerMenu() {
        JMenu menu = new JMenu("Dane z serwera");
        menu.setMnemonic(KeyEvent.VK_A);

        JMenuItem menuItem = new JMenuItem("Dodaj układ MOP-ów", KeyEvent.VK_U);
        menuItem.addActionListener(event -> mainFrame.setMopPointsFromServer());
        menu.add(menuItem);
        menuItem = new JMenuItem("Zmien adres serwera", KeyEvent.VK_Z);
        menuItem.addActionListener(event -> new SetUrlDialog());
        menu.add(menuItem);
        return menu;
    }

    private JMenu simulationMenu() {
        JMenu menu = new JMenu("Symulacje");
        menu.setMnemonic(KeyEvent.VK_S);

        JMenuItem menuItem = new JMenuItem("Przeprowadź symulację", KeyEvent.VK_R);
        menuItem.addActionListener(event -> new SimulationConfigDialog(mainFrame.getMopsimConfig()));
        menu.add(menuItem);

        menuItem = new JMenuItem("Generuj siatkę drogową", KeyEvent.VK_G);
        menuItem.addActionListener(event -> new GenerateMapDialog());
        menu.add(menuItem);

        return menu;
    }

    private JMenu addingMenu() {
        JMenu menu = new JMenu("Dodaj...");
        JMenuItem menuItemMop = new JMenuItem("MOP");
        AddMopPanel addMopPanel = new AddMopPanel(mainFrame);
        menuItemMop.addActionListener(event -> addMopPanel.setVisible());
        JMenuItem menuItemRoute = new JMenuItem("Drogę");
        menu.add(menuItemMop);
        menu.add(menuItemRoute);
        return menu;
    }
}
