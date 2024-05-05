package src;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a line implementation with user-specified height, such that the line can be coloured.
 */
public class ColouredLine extends JComponent {
    private Color colour;
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 100;
    private int y2 = 0;
    private float thickness = 0;

    /**
     * Constructs a line with a default color of black (Color.black), and default size (x1, y1, x2, y2) = (0, 0, 100, 0).
     */
    public ColouredLine() {
        this.colour = Color.black;
        calcAndSetPreferredSize();
    }

    /**
     * Constructs a line with user specified colour and thickness.
     *
     * @param colour the colour to set this line to
     * @param thickness the thickness (in the y-axis direction) of the line
     */
    public ColouredLine(Color colour, float thickness) {
        this.colour = colour;
        this.thickness = thickness;
        calcAndSetPreferredSize();
    }

    /**
     * Constructs a line with a default color of "colour" and with
     * user specified thickness.
     *
     * @param colour the colour to set this line to
     * @param x1 the x coordinate of the top-left point defining this line's bounds
     * @param x2 the x coordinate of the bottom-right point defining this line's bounds
     * @param y1 the y coordinate of the top-left point defining this line's bounds
     * @param y2 the y coordinate of the bottom-right point defining this line's bounds
     */
    public ColouredLine(Color colour, int x1, int y1, int x2, int y2) {
        this.colour = colour;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        calcAndSetPreferredSize();
    }

    /**
     * Returns this line's current colour.
     * @see java.awt.Color
     * @return the line's current colour
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Returns this line's preferred size
     * @return the line's preferred size
     */
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    /**
     * Private method called by every ColouredLine constructor. This method calculates
     * and sets this line's preferred size based on fields x1, x2, y1, y2 and thickness.
     */
    private void calcAndSetPreferredSize() {
        Dimension prefSize = getPreferredSize();

        // height and width being 0 means preferred size is at default
        if (prefSize.height == 0 && prefSize.width == 0) {
            if (y2 - y1 != 0) { // if y1 and y2 have been set to some non-default value
                setPreferredSize(new Dimension(x2 - x1, y2 - y1));
            } else { // otherwise use thickness
                setPreferredSize(new Dimension(x2 - x1, (int) Math.ceil(thickness)));
            }
        }
    }

    /**
     * Sets this line's current colour to "colour", and makes invalidates and repaints
     * the line. Notifies all property listeners attached to this line of this colour change.
     * @see java.awt.Color
     * @see java.awt.Container#invalidate()
     * @see java.awt.Component#repaint()
     * @param colour
     */
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
