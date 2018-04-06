package way;

import util.TitledTable;

import javax.swing.*;

public class TrafficInfoDialog extends JDialog {
    private Route route;

    public TrafficInfoDialog(Route route) {
        super();
        this.route = route;
        this.setSize(1000, 750);
        Integer sdrCar = 0, sdrTruck = 0, sdrBus = 0;
        if (route != null) {
            this.setTitle(route.getName() + " " + route.getMileageBegin() + " - " + route.getMileageEnd());
            sdrCar = route.getTrafficInfo().getCar() / 2;
            sdrTruck = route.getTrafficInfo().getTruck() / 2;
            sdrBus = route.getTrafficInfo().getBus() / 2;
        }
        else {
            this.setTitle("A1 od do");
        }
        this.setLocationRelativeTo(null); // center the dialog.
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String[] columnNames = {"", "", "", ""};
        Object[][] data = {
                {"Rodzaj pojazdu", "Średniodobowe natężenie ruchu",
                        "Liczba miejsc", "Liczba potrzebnych miejsc"},
                {"Osobowe", sdrCar, " ", ""},
                {"Ciężarowe", sdrTruck, " ", ""},
                {"Autobusy", sdrBus, " ", ""},
        };
        this.add(new TitledTable("Kierunek: Gdańsk",
                data, columnNames));

        columnNames = new String[]{"", "", "", ""};
        data = new Object[][]{
                {"Rodzaj pojazdu", "Średniodobowe natężenie ruchu",
                        "Liczba miejsc parkingowych", "Liczba potrzebnych miejsc parkingowych"},
                {"Osobowe", sdrCar, "", ""},
                {"Ciężarowe", sdrTruck, "", ""},
                {"Autobusy", sdrBus, "", ""},
        };
        this.add(new TitledTable("Kierunek: Katowice",
                data, columnNames));

        this.setVisible(true);
    }

}
