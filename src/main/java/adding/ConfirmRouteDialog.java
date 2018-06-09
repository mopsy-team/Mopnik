package adding;

import elements.MainFrame;
import util.AbstractDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class ConfirmRouteDialog extends AbstractDialog {
    public ConfirmRouteDialog(Point2D p1, Point2D p2, MainFrame mainFrame) {
        super();
        this.setSize(300, 240);
        this.setTitle("Dodaj drogę");
        this.setLocationRelativeTo(null); // center the dialog.
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JTextField name = new JTextField();
        JLabel nameLabel = new JLabel("Wybierz nazwę drogi (np. A1)");
        this.add(nameLabel, BorderLayout.WEST);
        this.add(name, BorderLayout.CENTER);

        JLabel directionLabel = new JLabel("Wybierz kierunki (np. Gdańsk, Katowice)");
        JTextField dir1 = new JTextField();
        JTextField dir2 = new JTextField();
        this.add(directionLabel);
        this.add(dir1);
        this.add(dir2);

        JLabel mileageLabel = new JLabel("Wybierz pikietaż początku i końca");
        JTextField mil1 = new JTextField();
        JTextField mil2 = new JTextField();
        this.add(mileageLabel);
        this.add(mil1);
        this.add(mil2);

        JButton submit = new JButton("Zatwierdź");
        submit.addActionListener(e -> {
            mainFrame.addRoute(name.getText(), p1, p2,
                    Integer.parseInt(mil1.getText()), Integer.parseInt(mil2.getText()),
                    dir1.getText(), dir2.getText());
            this.setVisible(false);
        });
        getRootPane().setDefaultButton(submit);
        this.add(submit);
        this.setVisible(true);
    }
}
