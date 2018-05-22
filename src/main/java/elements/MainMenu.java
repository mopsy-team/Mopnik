package elements;

import methods.CustomMethod;
import methods.Method;
import methods.PredictionDialog;
import way.TrafficInfoParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class MainMenu {

    public static void create(MainFrame mainFrame) {
        //Where the GUI is created:
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;
        Dimension dialogSize = new Dimension(800, 600);

//Create the menu bar.
        menuBar = new JMenuBar();

//Build the first menu.
        menu = new JMenu("Dodaj dane z pliku");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

//a group of JMenuItems
        menuItem = new JMenuItem("Średniodobowe natężenie ruchu",
                KeyEvent.VK_T);
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

        menuItem = new JMenuItem("Układ MOP-ów",
                KeyEvent.VK_T);
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

        menu = new JMenu("Predykcje");
        menuBar.add(menu);

        JMenu mopPredictions = new JMenu("Zajętości MOP-ów");

        JMenuItem item = new JMenuItem("Domyślna metodyka");
        Method method = new CustomMethod();
        item.addActionListener(event -> {
            new PredictionDialog(method, mainFrame);
        });
        mopPredictions.add(item);
        menu.add(mopPredictions);

        //Build the download data from server menu.
        menu = new JMenu("Dodaj dane z serwera");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        menuItem = new JMenuItem("Układ MOP-ów",
                KeyEvent.VK_T);
        menuItem.addActionListener(event -> {
            mainFrame.setMopPointsFromServer();
        });
        menu.add(menuItem);
        mainFrame.getFrame().setJMenuBar(menuBar);
    }

}
