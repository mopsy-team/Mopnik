package mop;

import elements.MainFrame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PointListener extends MouseAdapter {
    private JComponent component;
    private MopInfo mopInfo;
    private MainFrame frame;

    public PointListener(JComponent component, MopInfo mopInfo, MainFrame frame) {
        this.component = component;
        this.mopInfo = mopInfo;
        this.frame = frame;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MopInfoDialog dialog = new MopInfoDialog(mopInfo, frame);
    }
}
