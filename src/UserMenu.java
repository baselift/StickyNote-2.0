package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserMenu extends JFrame {

    public UserMenu() {}

    public void createMenu() {
        super.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 25));
        super.setSize(300, 200);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setTitle("StickyNote");
        super.setVisible(false);

        Image icon = new ImageIcon("./images/stickynoteicon2.png").getImage();
        icon = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        super.setIconImage(icon);

        JLabel logo = new JLabel(new ImageIcon(icon));
        logo.setSize(new Dimension(200, 200));


        RoundedButton addNoteBttn = new RoundedButton("+", "Arial", Font.PLAIN, 50); // 80
        addNoteBttn.setPreferredSize(new Dimension(100, 100));
        addNoteBttn.setBackground(Color.lightGray);
        addNoteBttn.setFocusable(false);
        addNoteBttn.addActionListener(e -> {
            if (e.getSource() == addNoteBttn) {
                SwingUtilities.invokeLater(() -> {
                    addNoteBttn.setEnabled(false);
                    CreateNote creator = new CreateNote(addNoteBttn);
                    creator.createGUI();
                    creator.setVisible(true);
                });
            }
        });
        super.add(addNoteBttn);

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                addNoteBttn.getGraphics().dispose();
            }
        });
    }
}
