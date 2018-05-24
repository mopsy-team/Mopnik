package mop;

import elements.MainFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class MopPoint extends DefaultWaypoint {
    private static final Log log = LogFactory.getLog(MopPoint.class);
    private final String label;
    private final MopInfo mopInfo;
    private JButton button = new JButton();

    /**
     * @param label   the text
     * @param mopInfo information about mop
     */
    public MopPoint(String label, MopInfo mopInfo, MopType mopType, MainFrame frame) {
        super(mopInfo.getGeoPosition());
        this.label = label;
        this.mopInfo = mopInfo;
        Image img = null;
        try {
            if (mopType == MopType.EXISTING) {
                img = ImageIO.read(MopPoint.class.getResource("/images/Parking_icon_16.png"));
            }
            else {
                img = ImageIO.read(MopPoint.class.getResource("/images/Parking_icon_16_red.png"));
            }
        } catch (Exception e) {
            log.warn("Couldn't read icon file");
        }
        if (img != null) {
            this.button = new JButton(new ImageIcon(img));
            // TODO(MG) remove the background insted of making it invisible (cut the button)
            this.button.setBorderPainted(false);
            this.button.setContentAreaFilled(false);
            this.button.setToolTipText(mopInfo.getName());
        }
        this.button.addMouseListener(new PointListener(this.button, mopInfo, frame));
    }

    /**
     * @return the label text
     */
    public String getLabel() {
        return label;
    }

    public MopInfo getMopInfo() {
        return mopInfo;
    }

    public JButton getButton() {
        return button;
    }

}
