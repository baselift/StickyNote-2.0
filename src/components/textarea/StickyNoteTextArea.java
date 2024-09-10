package src.components.textarea;

import java.awt.*;

public class StickyNoteTextArea extends TransparentTextArea {
    public static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 18);
    public static final int TEXT_BOX_ROWS = 18;
    public static final int TEXT_BOX_COLUMNS = 28;

    public StickyNoteTextArea(Color textColour) {
        super(TEXT_BOX_ROWS, TEXT_BOX_COLUMNS, DEFAULT_FONT, textColour);
        super.enableLineWrap();
    }

    public int getRowHeight() {
        return super.getRowHeight(DEFAULT_FONT);
    }
}
