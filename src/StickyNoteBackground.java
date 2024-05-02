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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(backgroundColour);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        g.setColor(Color.lightGray);
        for (int i = 1; i < linesPerNote; i++) {
            int originX = (int) bounds.getX();
            int originY = (int) bounds.getY();
            int endX = originX + (int) bounds.getWidth();
            g.drawLine(originX, originY + i*rowHeight, endX, originY + i*rowHeight);
        }
    }
}
