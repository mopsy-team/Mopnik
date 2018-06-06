package mop;

import elements.MainFrame;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddedMopInfoDialog extends MopInfoDialog {

    public AddedMopInfoDialog(MopInfo mopInfo, MainFrame mainFrame){
        super(mopInfo, mainFrame);
        information.setNotEditable();
        if (trafficInfoTable != null) {
            trafficInfoTable.setNotEditable();
        }

        JTextArea textArea = new JTextArea("To jest MOP dodany przez użytkownika. " +
                "Możesz zmieniać informacje o nim klikając na poszczególne pola lub go usunąć");
        textArea.setEditable(false);
        this.add(textArea);

        JButton submit = new JButton("Zatwierdź informacje");
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object[] ps = parkingSpaces.getColumn(1);

                mopInfo.setParkingSpacesInfo(new MopParkingSpacesInfo(makeInt(ps[0]), makeInt(ps[1]), makeInt(ps[2])));

                Object[] ei = equipmentTable.getRow(1);
                boolean[] eiBooleans = new boolean[ei.length];
                for (int i = 0; i < ei.length; ++i){
                    eiBooleans[i] = mapYesNoToBool((String)ei[i]);
                }
                MopEquipmentInfo newMopEquipmentInfo;
                newMopEquipmentInfo = new MopEquipmentInfo(eiBooleans);
                mopInfo.setEquipmentInfo(newMopEquipmentInfo);

            }
        });
        this.add(submit);

        JButton remove = new JButton("Usuń MOPa");
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.removeMop(mopInfo);
            }
        });
        this.add(remove);
    }

    private int makeInt (Object o) {
        return Integer.parseInt(o.toString());
    }
}