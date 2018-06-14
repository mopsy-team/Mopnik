package mop;

import elements.MainFrame;
import util.AbstractDialog;
import util.HorizontalTitledTable;
import util.TitledTable;
import util.VerticalTitledTable;

import javax.swing.*;


public abstract class MopInfoDialog extends AbstractDialog {

    protected TitledTable information;
    protected TitledTable parkingSpaces;
    protected TitledTable equipmentTable;
    protected TitledTable trafficInfoTable;

    public MopInfoDialog(MopInfo mopInfo, MainFrame frame) {
        super();
        this.setSize(1000, 750);
        this.setTitle(mopInfo.getName());
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String[] columnNames = {"", ""};
        Object[][] data = {
                {"Oddział", mopInfo.getBranch()},
                {"Miejscowość", mopInfo.getLocality()},
                {"Nr drogi", mopInfo.getRoad()},
                {"Kierunek", mopInfo.getDirection()},
                {"Pikietaż", mopInfo.getMileage()}
        };
        information = new VerticalTitledTable("podstawowe informacje o " + mopInfo.getName(),
                data, columnNames);

        Object[][] spacesData = {
                {"Dla pojazdów osobowych", mopInfo.getParkingSpacesInfo().getCarSpaces()},
                {"Dla pojazdów ciężarowych", mopInfo.getParkingSpacesInfo().getTruckSpaces()},
                {"Dla pojazdów autobusowych", mopInfo.getParkingSpacesInfo().getBusSpaces()}
        };

        parkingSpaces = new VerticalTitledTable("Liczba miejsc parkingowych", spacesData, columnNames);

        MopEquipmentInfo eq = mopInfo.getEquipmentInfo();
        Object[][] equipmentData = {
                {"Ochrona", "Ogrodzenie", "Monitoring", "Oświetlenie", "Stacja paliw",
                        "<html> Pojazdy <br> niebezpieczne", "Restauracja", "Toalety",
                        "Myjnia", "Warsztat", "Hotel"},
                {mapYesNo(eq.isSecurity()), mapYesNo(eq.isFence()), mapYesNo(eq.isCctv()),
                        mapYesNo(eq.isLight()), mapYesNo(eq.isPetrolStation()), mapYesNo(eq.isDangerousGoods()),
                        mapYesNo(eq.isRestaurant()), mapYesNo(eq.isAccommodation()), mapYesNo(eq.isToilet()),
                        mapYesNo(eq.isCarWash()), mapYesNo(eq.isCarRepairShop()), mapYesNo(eq.isAccommodation())}
        };
        String[] eqColumnNames = {"", "", "", "", "", "", "", "", "", "", ""};

        equipmentTable = new HorizontalTitledTable("Wyposażenie", equipmentData, eqColumnNames);

        if (mopInfo.getTrafficInfo() != null) {
            Object[][] trafficData = {
                    {"Pojazdy osobowe", mopInfo.getTrafficInfo().getCar()},
                    {"Pojazdy ciężarowe", mopInfo.getTrafficInfo().getTruck()},
                    {"Pojazdy autobusowa", mopInfo.getTrafficInfo().getBus()}
            };

            trafficInfoTable = new HorizontalTitledTable("Średniodobowe natężenie ruchu", trafficData, columnNames);

            /*
            for (Method method : frame.getMethods()) {
                MethodResult methodResult = method.compute(mopInfo.getRoute());
                Object[][] neededSpaces = {{methodResult.getCar(), methodResult.getTruck(), methodResult.getBus()}};
                columnNames = new String[]{"Pojazdy osobowe", "Pojazdy ciążarowe", "Autobusy"};
                this.add(new TitledTable("Potrzebne miejsca parkingowe", neededSpaces, columnNames));
            }
            */
        }

        this.add(information);
        this.add(parkingSpaces);
        this.add(equipmentTable);
        if (mopInfo.getTrafficInfo() != null) {
            this.add(trafficInfoTable);
        }

        this.setVisible(true);
    }

    protected String mapYesNo(boolean flag) {
        return flag ? "TAK" : "NIE";
    }

    protected boolean mapYesNoToBool(String s) {
        return s.equals("TAK");
    }
}
