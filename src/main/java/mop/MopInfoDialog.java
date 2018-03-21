package mop;

import elements.MainFrame;
import methods.Method;
import methods.MethodResult;
import util.TitledTable;

import javax.swing.*;


public class MopInfoDialog extends JDialog {

    public MopInfoDialog(MopInfo mopInfo, MainFrame frame) {
        super();
        this.setSize(1000, 750);
        this.setTitle(mopInfo.getName());
        this.setLocationRelativeTo(null); // center the dialog.
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String[] columnNames = {"", ""};
        Object[][] data = {
                {"Oddział", mopInfo.getBranch()},
                {"Miejscowość", mopInfo.getLocality()},
                {"Nr drogi", mopInfo.getRoad()},
                {"Kierunek", mopInfo.getDirection()},
                {"Pikietaż", mopInfo.getMileage()}
        };
        this.add(new TitledTable("podstawowe informacje o " + mopInfo.getName(),
                data, columnNames));

        Object[][] spacesData = {
                {"Dla pojazdów osobowych", mopInfo.getParkingSpacesInfo().getCarSpaces()},
                {"Dla pojazdów ciężarowych", mopInfo.getParkingSpacesInfo().getTruckSpaces()},
                {"Dla pojazdów autobusowych", mopInfo.getParkingSpacesInfo().getBusSpaces()}
        };

        this.add(new TitledTable("Liczba miejsc parkingowych", spacesData, columnNames));

        MopEquipmentInfo eq = mopInfo.getEquipmentInfo();
        Object[][] equipmentData = {
                {"Ochrona", "Ogrodzenie", "Monitoring", "Oświetlenie", "Stacja paliw",
                        "<html> Pojazdy <br> niebezpieczne", "Restauracja", "Toalety",
                        "Myjnia", "Warsztat"},
                {mapYesNo(eq.isSecurity()), mapYesNo(eq.isFence()), mapYesNo(eq.isCctv()),
                        mapYesNo(eq.isLight()), mapYesNo(eq.isPetrolStation()), mapYesNo(eq.isDangerousGoods()),
                        mapYesNo(eq.isRestaurant()), mapYesNo(eq.isAccomodation()), mapYesNo(eq.isToilet()),
                        mapYesNo(eq.isCarWash()), mapYesNo(eq.isCarRepairShop())}
        };
        String[] eqColumnNames = {"", "", "", "", "", "", "", "", "", ""};
        this.add(new TitledTable("Wyposażenie", equipmentData, eqColumnNames));

        if (mopInfo.getTrafficInfo() != null) {
            Object[][] trafficData = {
                    {"Pojazdy osobowe", mopInfo.getTrafficInfo().getCar()},
                    {"Pojazdy ciężarowe", mopInfo.getTrafficInfo().getTruck()},
                    {"Pojazdy autobusowa", mopInfo.getTrafficInfo().getBus()}
            };

            this.add(new TitledTable("Średniodobowe natężenie ruchu", trafficData, columnNames));


            System.out.println(frame.getMethods().size());
            for (Method method : frame.getMethods()) {
                MethodResult methodResult = method.compute(mopInfo.getTrafficInfo());
                Object[][] neededSpaces = {{methodResult.getCar(), methodResult.getTruck(), methodResult.getBus()}};
                columnNames = new String[]{"Pojazdy osobowe", "Pojazdy ciążarowe", "Autobusy"};
                this.add(new TitledTable("Potrzebne miejsca parkingowe", neededSpaces, columnNames));
            }
        }

        this.setVisible(true);
    }

    private String mapYesNo(boolean flag) {
        return flag ? "TAK" : "NIE";
    }
}
