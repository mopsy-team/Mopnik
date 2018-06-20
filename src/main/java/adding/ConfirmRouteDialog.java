package adding;

import elements.MainFrame;
import exceptions.ValidationError;
import org.jxmapviewer.viewer.GeoPosition;
import util.AbstractDialog;
import way.Route;
import way.SearchInfo;

import javax.swing.*;
import java.awt.*;

import static util.Validator.makeRoadNumber;
import static util.Validator.makeUnsignedDouble;

public class ConfirmRouteDialog extends AbstractDialog {

    private MainFrame mainFrame;

    public ConfirmRouteDialog(Point p1, Point p2, MainFrame mainFrame) {
        super();
        this.setSize(400, 300);
        this.setTitle("Dodaj drogę");
        this.setLocationRelativeTo(null); // center the dialog.

        SearchInfo searchInfoBeg = mainFrame.findNearRouteOrNull(p1);
        SearchInfo searchInfoEnd = mainFrame.findNearRouteOrNull(p2);

        Route routeBeg, routeEnd;
        GeoPosition gpBeg = mainFrame.getMapViewer().convertPointToGeoPosition(p1);
        GeoPosition gpEnd = mainFrame.getMapViewer().convertPointToGeoPosition(p2);

        if (searchInfoBeg != null){
            routeBeg = searchInfoBeg.getRoute();
            gpBeg = searchInfoBeg.getGeoPosition();
            this.add(new JLabel("Początek drogi zaczepiony w: " + routeBeg.getName()));

        }
        if (searchInfoEnd != null) {
            routeEnd = searchInfoEnd.getRoute();
            gpEnd = searchInfoEnd.getGeoPosition();
            this.add(new JLabel("Koniec drogi zaczepiony w: " + routeEnd.getName()));
        }

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

        final GeoPosition gpBegFinal = gpBeg;
        final GeoPosition gpEndFinal = gpEnd;

        JButton submit = new JButton("Zatwierdź");
        submit.addActionListener(e -> {
            try {
                Double milBeg = makeUnsignedDouble(mil1.getText(), "Pikietaż");
                Double milEnd = makeUnsignedDouble(mil2.getText(), "Pikietaż");

                String roadName = name.getText();
                makeRoadNumber(roadName);

                mainFrame.addRoute(roadName, gpBegFinal, gpEndFinal,
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
