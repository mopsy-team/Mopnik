package adding;

import elements.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

public class AddRoutePanel extends JPanel {
    private MainFrame mainFrame;

    public AddRoutePanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        setOpaque(false);
        addMouseListener(new MouseListener() {
            private int counter = 0;
            private int x1, x2, y1, y2;

            @Override
            public void mouseClicked(MouseEvent e) {
                counter++;
                if (counter == 1) {
                    x1 = e.getX();
                    y1 = e.getY();
                    System.out.println(x1);
                }
                if (counter == 2) {
                    x2 = e.getX();
                    y2 = e.getY();
                    System.out.println(x2);
                    Point p1 = new Point(x1, y1);
                    Point p2 = new Point(x2, y2);
                    new ConfirmRouteDialog(p1, p2, mainFrame);
                    counter = 0;
                    setVisible(false);
                }
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
        mainFrame.getFrame().setGlassPane(this);
        super.setVisible(true);
    }
}
