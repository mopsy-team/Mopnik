package mop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.kordamp.ikonli.swing.*;
import util.TitledTable;

import java.awt.*;


public class MopInfoDialog extends JDialog {

    public MopInfoDialog(MopInfo mopInfo) {
        super();
        this.setSize(800, 600);
        this.setTitle(mopInfo.getName());
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String[] columnNames = {"", ""};
        Object[][] data = {
                {"Oddział", mopInfo.getBranch()},
                {"Miejscowość", mopInfo.getLocality()},
                {"Nr drogi", mopInfo.getRoad()},
                {"Kierunek", mopInfo.getDirection()}
        };
        this.add(new TitledTable("podstawowe informacje o " + mopInfo.getName(),
                data, columnNames));

        Object[][] spacesData = {
                {"Dla pojazdów osobowych", mopInfo.getParkingSpacesInfo().getCarSpaces()},
                {"Dla pojazdów ciężarowych" , mopInfo.getParkingSpacesInfo().getTruckSpaces()},
                {"Dla pojazdów autobusowych", mopInfo.getParkingSpacesInfo().getBusSpaces()}
        };

        this.add(new TitledTable("Liczba miejsc parkingowych", spacesData, columnNames));

        MopEquipmentInfo eq = mopInfo.getEquipmentInfo();
        Object[][] equipmentData = {
                {"Ochrona", "Ogrodzenie", "Monitoring", "Oświetlenie", "Stacja paliw",
                        "Miejsca dla pojazdów z ładunkiem niebezpiecznym", "Restauracja", "Toalety",
                        "Myjnia", "Warsztat"},
                {mapYesNo(eq.isSecurity()), mapYesNo(eq.isFence()), mapYesNo(eq.isCctv()),
                mapYesNo(eq.isLight()), mapYesNo(eq.isPetrolStation()), mapYesNo(eq.isDangerousGoods()),
                mapYesNo(eq.isRestaurant()), mapYesNo(eq.isAccomodation()), mapYesNo(eq.isToilet()),
                mapYesNo(eq.isCarWash()), mapYesNo(eq.isCarRepairShop())}
        };
        String[] eqColumnNames = {"", "", "", "", "", "", "", "", "", ""};
        this.add(new TitledTable("Wyposażenie", equipmentData, eqColumnNames));
        this.setVisible(true);
    }

    private String mapYesNo(boolean flag) {
        return flag ? "TAK" : "NIE";
    }
}
