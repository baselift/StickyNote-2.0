package src;

import javax.swing.*;
import java.awt.*;

public class CommonUtils {
    /**
     * Gets the icon at path iconPath, and returns an Image object representing the icon.
     * @param iconPath the path that specifies the icon
     * @return Image representing the icon
     */
    public static Image getIcon(String iconPath) {
        return new ImageIcon(iconPath).getImage();
    }

    /**
     * Gets the icon at path iconPath, and returns an Image object scaled to width "width" and height
     * "height" representing the icon. <br>
     * Valid parameter for "hints" are the Image constants: <br>
     * {@link Image#SCALE_SMOOTH}, {@link Image#SCALE_DEFAULT}, {@link Image#SCALE_FAST}, {@link Image#SCALE_REPLICATE},
     * {@link Image#SCALE_AREA_AVERAGING}
     * @param iconPath the path that specifies the icon
     * @param width the width of the scaled icon
     * @param height the height of the scaled icon
     * @param hints hints that indicate how the image should be scaled
     * @return Image representing the scaled icon
     */
    public static Image getScaledIcon(String iconPath, int width, int height, int hints) {
        return new ImageIcon(iconPath).getImage().getScaledInstance(width, height, hints);
    }

    public static Color getScaledColour(Color c, double factor) {
        return new Color(Math.max((int)(c.getRed() * factor), 0),
                Math.max((int)(c.getGreen()* factor), 0),
                Math.max((int)(c.getBlue() * factor), 0),
                c.getAlpha());
    }

    public static void addToolbarAction(JToolBar tb, Action action) {
        JButton bttn = tb.add(action);
        bttn.setBorder(null);
        bttn.setOpaque(false);
    }
}
