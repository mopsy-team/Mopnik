package adding;

import elements.MainFrame;
import mop.MopParkingSpacesInfo;
import org.jxmapviewer.viewer.GeoPosition;
import util.AbstractDialog;
import way.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Map;

public class ConfirmMopPositionDialog extends AbstractDialog {
    public ConfirmMopPositionDialog(Route route, GeoPosition gp, MainFrame mainFrame) {
        super();
        this.setSize(300, 240);
        this.setTitle("Potwierdź dodanie MOP-a");

        this.setLocationRelativeTo(null); // center the dialog.
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JTextArea textArea = new JTextArea("Wybrano drogę " + route.getName() + " i pikietaż: " +
                (route.getMileageEnd() + route.getMileageBegin())/2 + ". \nWybierz kierunek:");
        textArea.setEditable(false);
        this.add(textArea);

        ButtonGroup group = new ButtonGroup();
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        for ( Map.Entry<String, MopParkingSpacesInfo> entry: route.getSpacesByDirection().entrySet()) {
            JRadioButton radioButton = new JRadioButton(entry.getKey());
            radioPanel.add(radioButton);
            group.add(radioButton);
        }
        final JButton submit = new JButton("Zatwierdź");
        submit.addActionListener(e -> {
            String direction = "";
            Enumeration<AbstractButton> enumeration = group.getElements();
            while (enumeration.hasMoreElements()) {
                AbstractButton b = enumeration.nextElement();
                if (b.isSelected()) {
                    direction = b.getText();
                    break;
                }
            }
            mainFrame.addMop("Nowy mop", gp, route, direction);
            this.setVisible(false);
        });
        getRootPane().setDefaultButton(submit);
        this.add(radioPanel);
        this.add(submit);
        this.setVisible(true);
    }

}
