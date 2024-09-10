package src.components.textarea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TransparentTextArea {
    protected JTextArea textArea;

    public TransparentTextArea(int textAreaRows, int textAreaColumns, Font font, Color textColour) {
        textArea = new JTextArea(textAreaRows, textAreaColumns);
        textArea.setFont(font);
        textArea.setOpaque(false);
        textArea.setForeground(textColour);
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem copyBttn = new JMenuItem("Copy");
                    copyBttn.addActionListener(ev -> textArea.copy());
                    menu.add(copyBttn);

                    JMenuItem pasteBttn = new JMenuItem("Paste");
                    pasteBttn.addActionListener(ev -> textArea.paste());
                    menu.addSeparator();
                    menu.add(pasteBttn);

                    menu.show(textArea, e.getX(), e.getY());
                }
            }
        });
    }

    public void enableLineWrap() {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public Color getTextColor() {
        return textArea.getForeground();
    }

    public void setTextColor(Color c) {
        textArea.setForeground(c);
    }

    public Dimension getPreferredSize() {
        return textArea.getPreferredSize();
    }

    public int getRowHeight(Font f) {
        return textArea.getFontMetrics(f).getHeight();
    }

    public void addToViewport(JViewport viewport) {
        viewport.setView(textArea);
    }
}
