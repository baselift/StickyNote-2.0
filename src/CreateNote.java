package src;

import src.components.StickyNote;

import java.awt.event.ActionEvent;

public class CreateNote extends NotePropertySelectDialog {
    @Override
    protected void onConfirmBttnClicked(ActionEvent e) {
        StickyNote note = new StickyNote(selectedColour, selectedBackgroundColour);
        note.setVisible(true);
        close();
    }
}
