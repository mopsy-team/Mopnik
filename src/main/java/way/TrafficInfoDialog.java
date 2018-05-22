package way;

import methods.CustomMethod;
import methods.Method;
import methods.MethodResult;
import mop.MopInfo;
import util.TitledTable;

import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

        Method m = new CustomMethod();
        MethodResult methodResult = m.compute(route);

        Map<String, MethodResult> spacesByDirection = route.getSpacesByDirection();

        for (Map.Entry<String, MethodResult> entry : spacesByDirection.entrySet()){
            String dir = entry.getKey();
            MethodResult mr = entry.getValue();
            String[] columnNames = {"", "", "", ""};
            Object[][] data = {
                    {"Rodzaj pojazdu", "Średniodobowe natężenie ruchu",
                            "Liczba miejsc", "Liczba potrzebnych miejsc"},
                    {"Osobowe", sdrCar, mr.getCar(), methodResult.getCar()},
                    {"Ciężarowe", sdrTruck, mr.getTruck(), methodResult.getTruck()},
                    {"Autobusy", sdrBus, mr.getBus(), methodResult.getBus()},
            };
            this.add(new TitledTable("Kierunek: " + dir,
                    data, columnNames));
        }
        this.setVisible(true);
    }

}
