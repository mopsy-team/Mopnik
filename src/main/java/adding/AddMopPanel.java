package adding;

import elements.MainFrame;
import org.jxmapviewer.viewer.GeoPosition;
import way.Route;
import way.SearchInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AddMopPanel extends JPanel {
    private MainFrame mainFrame;

    public AddMopPanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        setOpaque(false);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                mainFrame.getFrame().remove(AddMopPanel.this);
                int x = e.getX();
                int y = e.getY();
                SearchInfo searchInfo = mainFrame.findNearRouteOrNull(new Point(x, y));
                if (searchInfo != null) {
                    double mileage = searchInfo.getMileage();
                    Route route = searchInfo.getRoute();
                    GeoPosition gp = searchInfo.getGeoPosition();
                    new ConfirmMopPositionDialog(route, gp, mainFrame, mileage);
                }
                else {
                    GeoPosition gp = mainFrame.getMapViewer().convertPointToGeoPosition(new Point(x, y));
                    mainFrame.addMop("Nowy mop", gp, new Route(), "");
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
