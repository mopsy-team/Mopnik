package way;

import methods.MethodResult;
import mop.MopParkingSpacesInfo;
import util.AbstractDialog;
import util.TitledTable;
import util.VerticalTitledTable;

import javax.swing.*;
import java.util.Map;

public class TrafficInfoDialog extends AbstractDialog {
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
        this.setLocationRelativeTo(null); // center the dialog.
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        MethodResult methodResult = route.getSpacesNeeded();

        Map<String, MopParkingSpacesInfo> spacesByDirection = route.getSpacesByDirection();

        for (Map.Entry<String, MopParkingSpacesInfo> entry : spacesByDirection.entrySet()) {
            String dir = entry.getKey();
            MopParkingSpacesInfo mr = entry.getValue();
            String[] columnNames = {"Rodzaj pojazdu", "Średniodobowe natężenie ruchu",
                    "Liczba miejsc", "Liczba potrzebnych miejsc"};
            Object[][] data = {
                    {"Osobowe", sdrCar, mr.getCarSpaces(), methodResult.getCar()},
                    {"Ciężarowe", sdrTruck, mr.getTruckSpaces(), methodResult.getTruck()},
                    {"Autobusy", sdrBus, mr.getBusSpaces(), methodResult.getBus()},
            };

            TitledTable table = new VerticalTitledTable("Kierunek: " + dir,
                    data, columnNames);
            table.setNotEditable();
            this.add(table);
        }
        this.setVisible(true);
    }

}
