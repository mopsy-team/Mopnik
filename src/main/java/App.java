import elements.MainFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        MainFrame mn = new MainFrame();
        try {
            mn.show();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mn.getFrame(),
                    "Któryś z plików podanych w pliku wejściowym nie istnieje lub jest niepoprawny.\n" +
                            "Proszę usunąć plik config.txt i uruchomić ponownie",
                    "Błędny plik konfiguracyjny",
                    JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }
}
