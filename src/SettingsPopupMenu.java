package src;

import javax.swing.*;
import java.awt.event.ActionEvent;

public interface SettingsPopupMenu {

    void onSettingsButtonClicked(ActionEvent e, JMenuItem settingsButton);
    void onLockButtonClicked(ActionEvent e, JMenuItem lockButton);
    void onCloseButtonClicked(ActionEvent e, JMenuItem closeButton);

    default JPopupMenu createSettingsPopup(boolean initialLockStatus) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem settingsBttn = new JMenuItem("Settings");
        settingsBttn.addActionListener(e -> onSettingsButtonClicked(e, settingsBttn));
        menu.add(settingsBttn);

        JMenuItem lockBttn = new JMenuItem(initialLockStatus ? "Lock" : "Unlock");
        lockBttn.addActionListener(e -> onLockButtonClicked(e, lockBttn));
        menu.addSeparator();
        menu.add(lockBttn);

        JMenuItem closeBttn = new JMenuItem("Close");
        closeBttn.addActionListener(e -> onCloseButtonClicked(e, closeBttn));
        menu.addSeparator();
        menu.add(closeBttn);

        return menu;
    }
}
