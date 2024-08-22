package src.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serial;

public class SaveAction extends AbstractAction {
    @Serial
    private static final long serialVersionUID = 8531809851756602706L;

    public SaveAction(Component parent, Icon saveIcon) {
        super(null, saveIcon);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
