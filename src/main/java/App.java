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
                    "Coś poszło nie tak. Prawdopodobnie któryś z plików podanych w pliku konfiguracyjnym nie istnieje lub jest niepoprawny.\n" +
                            "Proszę usunąć plik config.txt lub upewnić się, że zawiera on poprawne nazwy plików istniejących w katalogu resources i uruchomić program ponownie.",
                    "Błędny plik konfiguracyjny",
                    JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }
}
