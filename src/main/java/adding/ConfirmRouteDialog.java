package adding;

import elements.MainFrame;
import exceptions.ValidationError;
import org.jxmapviewer.viewer.GeoPosition;
import util.AbstractDialog;
import way.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

import static util.Validator.makeUnsignedDouble;

public class ConfirmRouteDialog extends AbstractDialog {
    public ConfirmRouteDialog(Point2D p1, Point2D p2, MainFrame mainFrame) {
        super();
        this.setSize(400, 300);
        this.setTitle("Dodaj drogę");
        this.setLocationRelativeTo(null); // center the dialog.

        GeoPosition gpBeg = mainFrame.getMapViewer().convertPointToGeoPosition(p1);
        GeoPosition gpEnd = mainFrame.getMapViewer().convertPointToGeoPosition(p2);

        Route routeBeg = mainFrame.findNearestRoute(gpBeg);
        Route routeEnd = mainFrame.findNearestRoute(gpEnd);
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
            try {
                Double milBeg = makeUnsignedDouble(mil1.getText(), "Pikietaż");
                Double milEnd = makeUnsignedDouble(mil1.getText(), "Pikietaż");

                mainFrame.addRoute(name.getText(), gpBeg, gpEnd,
                        milBeg, milEnd, dir1.getText(), dir2.getText());
                this.dispose();
            }
            catch (ValidationError validationError) {
                validationError.alert();
            }
        });
        getRootPane().setDefaultButton(submit);
        this.add(submit);
        this.setVisible(true);
    }
}
