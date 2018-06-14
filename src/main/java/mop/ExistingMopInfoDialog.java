package mop;

import elements.MainFrame;

public class ExistingMopInfoDialog extends MopInfoDialog {
    public ExistingMopInfoDialog(MopInfo mopInfo, MainFrame mainFrame) {
        super(mopInfo, mainFrame, false);
        information.setNotEditable();
        parkingSpaces.setNotEditable();
        if (trafficInfoTable != null) {
            trafficInfoTable.setNotEditable();
        }
    }
}
