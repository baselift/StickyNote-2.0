package src;

import javax.swing.*;
import java.awt.*;

public class StickyNoteBackground extends JPanel {
    private Color backgroundColour;
    private int linesPerNote;
    private int rowHeight;
    private Rectangle bounds;

    public StickyNoteBackground(Color backgroundColour, int linesPerNote, int rowHeight, Rectangle bounds) {
        this.backgroundColour = backgroundColour;
        this.linesPerNote = linesPerNote;
        this.rowHeight = rowHeight;
        this.bounds = bounds;
    }

    public void setBackgroundColour(Color c) {
        this.backgroundColour = c;
        super.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(backgroundColour);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        for (int i = 1; i < linesPerNote + 1; i++) {
            g.setColor(Color.lightGray);
            int originX = (int) bounds.getX();
            int originY = (int) bounds.getY();
            int endX = originX + (int) bounds.getWidth();
            if (i == linesPerNote) {
                // cannot draw at component border, so can draw at offset of 1
                g.drawLine(originX, originY + getHeight() - 1, endX, originY + getHeight() - 1);
            } else {
                g.drawLine(originX, originY + i*rowHeight, endX, originY + i*rowHeight);
            }
        }
    }
}
