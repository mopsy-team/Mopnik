package mop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PointListener extends MouseAdapter {
    private JComponent component;
    private MopInfo mopInfo;

    public PointListener(JComponent component, MopInfo mopInfo) {
        this.component = component;
        this.mopInfo = mopInfo;

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MopInfoDialog dialog = new MopInfoDialog(mopInfo);
    }
}
