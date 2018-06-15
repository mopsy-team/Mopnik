package adding;

import elements.MainFrame;
import org.jxmapviewer.viewer.GeoPosition;
import way.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AddMopPanel extends JPanel {
    private MainFrame mainFrame;

    public AddMopPanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        setBackground(new Color(255, 255, 255, 90));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                mainFrame.getFrame().remove(AddMopPanel.this);
                int x = e.getX();
                int y = e.getY() - 20;
                GeoPosition gp = mainFrame.getMapViewer().convertPointToGeoPosition(new Point(x, y));
                Route route = mainFrame.findNearestRoute(gp);
                double mileage = route.findMileage(gp);
                if (route != null) {
                    new ConfirmMopPositionDialog(route, gp, mainFrame, mileage);
                } else {
                    mainFrame.addMop("Nowy mop", gp, route, "");
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
