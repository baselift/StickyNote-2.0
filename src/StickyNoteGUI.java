package src;

import javax.swing.*;
import java.awt.*;

public abstract class StickyNoteGUI {
    protected JFrame mainFrame;
    protected StickyNoteBackground background;
    protected JTextArea textArea;
    private Color backgroundColour;
    private Color textColour;


    public StickyNoteGUI(Color backgroundColour, Color textColour) {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setUndecorated(true);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.getContentPane().setBackground(backgroundColour);
        Image icon = CommonUtils.getScaledIcon("./images/stickynoteicon1.png",
                30, 30, Image.SCALE_SMOOTH);
        mainFrame.setIconImage(icon);

        this.backgroundColour = backgroundColour;
        this.textColour = textColour;
    }

}
