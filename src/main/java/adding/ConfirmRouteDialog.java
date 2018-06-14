package adding;

import elements.MainFrame;
import org.jxmapviewer.viewer.GeoPosition;
import util.AbstractDialog;
import way.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class ConfirmRouteDialog extends AbstractDialog {
    public ConfirmRouteDialog(Point2D p1, Point2D p2, MainFrame mainFrame) {
        super();
        this.setSize(400, 300);
        this.setTitle("Dodaj drogę");
        this.setLocationRelativeTo(null); // center the dialog.

        GeoPosition gpBeg = mainFrame.getMapViewer().convertPointToGeoPosition(p1);
        GeoPosition gpEnd = mainFrame.getMapViewer().convertPointToGeoPosition(p2);

        Route routeBeg = mainFrame.getRoutesMap().findRouteByGeoPosition(gpBeg);
        Route routeEnd = mainFrame.getRoutesMap().findRouteByGeoPosition(gpEnd);
        JLabel routeLabel = new JLabel("Wybrany odcinek łączy drogi " + routeBeg.getName() + " oraz " +
                routeEnd.getName() + ".");
        JLabel routeLabel2 = new JLabel("Jeśli chodziło o inny odcinek, spróbuj ponownie.");
        this.add(routeLabel);
        this.add(routeLabel2);
        this.add(new JSeparator(SwingConstants.HORIZONTAL));

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JTextField name = new JTextField();
        JLabel nameLabel = new JLabel("Wybierz nazwę drogi (np. A1)");
        this.add(nameLabel, BorderLayout.WEST);
        this.add(name, BorderLayout.CENTER);

        JLabel directionLabel = new JLabel("Wybierz kierunki (np. Gdańsk, Katowice)");
        JTextField dir1 = new JTextField();
        JTextField dir2 = new JTextField();
        this.add(directionLabel, BorderLayout.WEST);
        this.add(dir1);
        this.add(dir2);

        JLabel mileageLabel = new JLabel("Wybierz pikietaż początku i końca");
        JTextField mil1 = new JTextField();
        JTextField mil2 = new JTextField();
        this.add(mileageLabel, BorderLayout.WEST);
        this.add(mil1);
        this.add(mil2);

        JButton submit = new JButton("Zatwierdź");
        submit.addActionListener(e -> {
            mainFrame.addRoute(name.getText(), gpBeg, gpEnd,
                    Integer.parseInt(mil1.getText()), Integer.parseInt(mil2.getText()),
                    dir1.getText(), dir2.getText());
            this.setVisible(false);
        });
        getRootPane().setDefaultButton(submit);
        this.add(submit);
        this.setVisible(true);
    }
}
