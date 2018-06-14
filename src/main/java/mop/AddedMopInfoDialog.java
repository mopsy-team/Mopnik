package mop;

import elements.MainFrame;
import exceptions.ValidationError;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddedMopInfoDialog extends MopInfoDialog {

    public AddedMopInfoDialog(MopInfo mopInfo, MainFrame mainFrame){
        super(mopInfo, mainFrame, true);
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
                parkingSpaces.endEditing();
                equipmentTable.endEditing();
                information.endEditing();

                try {
                    Object[] ps = parkingSpaces.getColumn(1);

                    mopInfo.setParkingSpacesInfo(new MopParkingSpacesInfo(makeInt(ps[0]), makeInt(ps[1]), makeInt(ps[2])));

                    Object[] ei = equipmentTable.getRow(0);
                    boolean[] eiBooleans = new boolean[ei.length];
                    for (int i = 0; i < ei.length; ++i) {
                        eiBooleans[i] = (boolean) ei[i];
                    }
                    MopEquipmentInfo newMopEquipmentInfo;
                    newMopEquipmentInfo = new MopEquipmentInfo(eiBooleans);
                    mopInfo.setEquipmentInfo(newMopEquipmentInfo);

                    Object[] ins = information.getColumn(1);

                    mopInfo.setBranch((String) ins[0]);
                    mopInfo.setLocality((String) ins[1]);

                    mainFrame.repaint();
                    AddedMopInfoDialog.super.dispose();
                }
                catch (ValidationError err){
                    err.alert();
                }
            }
        });
        this.add(submit);

        JButton remove = new JButton("Usuń MOPa");
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.removeMop(mopInfo);
                AddedMopInfoDialog.super.dispose();
            }
        });
        this.add(remove);
    }

    private int makeInt (Object o) throws ValidationError {
        try {
            return Integer.parseInt(o.toString());
        }
        catch (java.lang.NumberFormatException e){
            throw new ValidationError("Liczba miejsc parkingowych musi być liczbą całkowitą dodatnią");
        }
    }
}
