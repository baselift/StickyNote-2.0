package src;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class PasteOverflowAction extends TextAction {
    private Action wrappingAction;
    private JTextComponent textComponent;
    private int charLimit;

    public PasteOverflowAction(Action wrappingAction, JTextComponent focus, int charLimit) {
        super("PasteOverflowAction");
        this.wrappingAction = wrappingAction;
        this.textComponent = focus;
        this.charLimit = charLimit;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // check if the createnote text box is the current component being referenced
        if (super.getFocusedComponent() == textComponent) {
            int length = 0;
            int clipLength = 0;

            try {
                length = CommonUtils.collapseNewLine(textComponent.getText()).length();
                String clipText = clip.getData(DataFlavor.stringFlavor).toString();
                clipLength = CommonUtils.collapseNewLine(clipText).length();
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
            }
            if (length + clipLength <= charLimit) {
                // do not paste the clipboard's contents in
                wrappingAction.actionPerformed(e);
            }
        }

    }
}
