package adding;

import elements.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AddMopPanel extends JPanel {
    public AddMopPanel(MainFrame mainFrame) {
        super();
        setBackground(new Color(255, 255, 255, 90));
        mainFrame.getFrame().setGlassPane(this);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                mainFrame.getFrame().remove(AddMopPanel.this);
                int x = e.getX();
                int y = e.getY();
                mainFrame.addMop("Nowy mop", x+5, y-15);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void setVisible() {
        super.setVisible(true);
    }
}
