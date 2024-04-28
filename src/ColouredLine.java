package src;

import javax.swing.*;
import java.awt.*;

public class ColouredLine extends JComponent {
    private Color colour;
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 100;
    private int y2 = 0;
    private float thickness = 0;

    /**
     * Constructs a line with a default color of black (Color.black).
     */
    public ColouredLine() {
        this.colour = Color.black;
        calcAndSetPreferredSize();
    }

    /**
     * Constructs a line with a default color of black (Color.black) and with
     * user specified thickness.
     */
    public ColouredLine(float thickness) {
        this.colour = Color.black;
        this.thickness = thickness;
        calcAndSetPreferredSize();
    }

    public ColouredLine(Color colour, int x1, int y1, int x2, int y2) {
        this.colour = colour;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        calcAndSetPreferredSize();
    }

    public ColouredLine(Color colour, int x1, int y1, int x2, int y2, float thickness) {
        this.colour = colour;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.thickness = thickness;
        calcAndSetPreferredSize();
    }

    public Color getColour() {
        return colour;
    }

    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    public void setPreferredSize(Dimension dimension) {
        super.setPreferredSize(dimension);
    }

    public void calcAndSetPreferredSize() {
        Dimension prefSize = getPreferredSize();

        if (prefSize.height == 0 && prefSize.width == 0) {
            if (y2 - y1 != 0) {
                setPreferredSize(new Dimension(x2 - x1, y2 - y1));
            } else {
                setPreferredSize(new Dimension(x2 - x1, (int) Math.ceil(thickness)));
            }
        }
    }

    public void setColor(Color colour) {
        super.firePropertyChange("colour", this.colour, colour);
        this.colour = colour;
        invalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (colour != null && thickness == 0) {
            g.setColor(colour);
            g.drawLine(x1, y1, x2, y2);
        } else if (colour != null) {
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(colour);
            g2D.setStroke(new BasicStroke(thickness));
            g2D.drawLine(x1, y1, x2, y2);

        } else {
            throw new NullPointerException("Color was null");
        }
    }
}
