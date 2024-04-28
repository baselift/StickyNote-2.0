package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/*
This is part of someone else's code. Credits to https://stackoverflow.com/a/13867349 where I got this code from.
However, I made some minor adjustments to it.
*/
class RoundedButton extends Component {

    ActionListener actionListener;     // Post action events to listeners
    String label;                      // The Button's text
    protected boolean pressed = false; // true if the button is detented.

    /**
     * Constructs a RoundedButton with no label.
     */
    public RoundedButton() {
        this("");
    }

    /**
     * Constructs a RoundedButton with the specified label with the default font.
     *
     * @param label the label of the button
     */
    public RoundedButton(String label) {
        this.label = label;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * Constructs a RoundedButton with the specified label with the default font.
     * Use Font class constants for style.
     *
     * @param label the label of the button
     * @param fontFamily the font family
     * @param style Bold, Italic, or plain (use Font class constants)
     * @param size the size of the text/font
     */
    public RoundedButton(String label, String fontFamily, int style, int size) {
        setFont(new Font(fontFamily, style, size));
        this.label = label;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * gets the label
     *
     *
     */
    public String getLabel() {
        return label;
    }

    /**
     * sets the label
     *
     *
     */
    public void setLabel(String label) {
        this.label = label;
        invalidate();
        repaint();
    }

    /**
     * paints the RoundedButton
     */
    @Override
    public void paint(Graphics g) {
        // paint the interior of the button
        if (pressed) {
            g.setColor(getBackground().darker().darker());
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        // draw the perimeter of the button
        g.setColor(getBackground().darker().darker().darker());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        // draw the label centered in the button
        Font f = getFont();
        if (f != null) {
            FontMetrics fm = getFontMetrics(f);
            Rectangle2D labelBounds = fm.getStringBounds(label, g);
            g.setColor(getForeground());

            /*
            (getWidth() - (int) labelBounds.getWidth()) / 2: Set the x coordinate to the value that is width of the
            button minus width of the string divided by 2, in order to have equal left and right padding

            See https://docs.oracle.com/javase/tutorial/2d/overview/coordinate.html for understanding coordinates
            (why we have plus instead of minus for the y calculation).
            */
            g.drawString(label, (getWidth() - (int) labelBounds.getWidth()) / 2, (int) (getHeight() +
                    labelBounds.getHeight() + labelBounds.getY() / 2) / 2);
        }
    }

    /**
     * The preferred size of the button.
     */
    @Override
    public Dimension getPreferredSize() {
        Font f = getFont();
        if (f != null) {
            FontMetrics fm = getFontMetrics(getFont());
            int max = Math.max(fm.stringWidth(label) + 40, fm.getHeight() + 40);
            return new Dimension(max, max);
        } else {
            return new Dimension(100, 100);
        }
    }

    /**
     * The minimum size of the button.
     */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    /**
     * Adds the specified action listener to receive action events from this
     * button.
     *
     * @param listener the action listener
     */
    public void addActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.add(actionListener, listener);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * Removes the specified action listener so it no longer receives action
     * events from this button.
     *
     * @param listener the action listener
     */
    public void removeActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.remove(actionListener, listener);
    }

    /**
     * Determine if click was inside round button.
     */
    @Override
    public boolean contains(int x, int y) {
        int mx = getSize().width / 2;
        int my = getSize().height / 2;
        return (((mx - x) * (mx - x) + (my - y) * (my - y)) <= mx * mx);
    }

    /**
     * Paints the button and distribute an action event to all listeners.
     */
    @Override
    public void processMouseEvent(MouseEvent e) {
        Graphics g = getGraphics();

        if (!super.isEnabled()) {
            return;
        }

        switch (e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                // render myself inverted....
                pressed = true;

                // Repaint might flicker a bit. To avoid this, you can use
                // double buffering (see the Gauge example).
                repaint();
                break;
            case MouseEvent.MOUSE_RELEASED:
                if (actionListener != null) {
                    actionListener.actionPerformed(new ActionEvent(
                            this, ActionEvent.ACTION_PERFORMED, label));
                }
                // render myself normal again
                if (pressed) {
                    pressed = false;

                    // Repaint might flicker a bit. To avoid this, you can use
                    // double buffering (see the Gauge example).
                    repaint();
                }
                break;
            case MouseEvent.MOUSE_ENTERED:
                Color existingColor = g.getColor();
                g.setColor(Color.green);
                // draw green outline
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g.setColor(existingColor);
                break;
            case MouseEvent.MOUSE_EXITED:
                // undraw green outline
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                if (pressed) {
                    // Cancel! Don't send action event.
                    pressed = false;

                    // Repaint might flicker a bit. To avoid this, you can use
                    // double buffering (see the Gauge example).
                    repaint();
                }
                break;
        }
        super.processMouseEvent(e);
    }
}
