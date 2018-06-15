package util;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class AbstractDialog extends JDialog {
    public AbstractDialog() {
        super();
        KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        int w = JComponent.WHEN_IN_FOCUSED_WINDOW;
        getRootPane().registerKeyboardAction(e -> this.dispose(), k, w);
    }

}
