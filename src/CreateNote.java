package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * CreateNote is the lightweight component that provides a user interface for the creation of StickyNote.
 * CreateNote instances are created for when the StickyNote is first created, or some of its properties want to be
 * changed.
 *
 * @see src.StickyNote
 */
public class CreateNote extends JDialog {
    /**
     * The component that is the created this instance of CreateNote. This can be null,
     * or really any instance of Component. There is specific behaviour when source is an instance of StickyNote.
     *
     * @see src.StickyNote
     */
    private Object source;
    private Color selectedColour = Color.BLACK;
    private Color selectedBackgroundColour = new Color(238,232,170);

    /**
     * Creates a CreateNote instance with fixed size of 500 x 900. This constructor
     * will automaticclly create the GUI.
     * @param source the component that caused this createNote instance to be created.
     */
    CreateNote(Object source) {
        // (Dialog) null is to make JDialog appear in taskbar
        super((Dialog) null);

        super.setLayout(new GridBagLayout());
        super.setSize(500, 900);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setTitle("Create a new Sticky Note");
        super.setResizable(false);

        Image icon = CommonUtils.getScaledIcon("./images/stickynoteiconPlus2.png",
                30, 30, Image.SCALE_SMOOTH);
        super.setIconImage(icon);

        this.source = source;
        this.createGUI();
    }

    /**
     * Private method which creates the GUI for this CreateNote instance.
     */
    private void createGUI() {
        final int VERTICAL_PADDING = 30;
        JPanel colourSettingsPanel = createColourSettingsPanel();
        GridBagConstraints colourSettingsC = new GridBagConstraints();
        colourSettingsC.gridx = 0;
        colourSettingsC.gridy = 1;
        colourSettingsC.anchor = GridBagConstraints.FIRST_LINE_START;
        colourSettingsC.insets = new Insets(VERTICAL_PADDING, 0, 0, 0);
        colourSettingsC.weighty = 0.001;

        JPanel backgroundSettingsPanel = createBackgroundSettingsPanel();
        GridBagConstraints backgroundSettingsC = new GridBagConstraints();
        backgroundSettingsC.gridx = 0;
        backgroundSettingsC.gridy = 2;
        // try changing weights if it seems to take up more space
        backgroundSettingsC.weighty = 0.01;
        backgroundSettingsC.anchor = GridBagConstraints.FIRST_LINE_START;

        // when user wants to finalize changes
        JPanel userDecisionPanel = createUserDecisionPanel();
        GridBagConstraints userDecisionC = new GridBagConstraints();
        userDecisionC.gridx = 0;
        userDecisionC.gridy = 4;
        userDecisionC.anchor = GridBagConstraints.FIRST_LINE_END;

        super.add(colourSettingsPanel, colourSettingsC);
        super.add(backgroundSettingsPanel, backgroundSettingsC);
        super.add(userDecisionPanel, userDecisionC);
    }

    private JPanel createColourSettingsPanel() {
        JPanel colourSettingsPanel = new JPanel();
        colourSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 0));

        JLabel colourLbl = new JLabel("<html>Text<br>Colour:</html>");
        colourLbl.setFont(new Font("Arial", Font.BOLD, 16));

        // the line that will display user-chosen colour
        ColouredLine line = new ColouredLine(selectedColour, 3F);

        JButton chooseColour = new JButton("Select Colour");
        chooseColour.setFocusable(false);
        chooseColour.addActionListener(e -> {
            if (e.getSource() == chooseColour) {
                JFrame iconParent = new JFrame();
                iconParent.setIconImage(new ImageIcon("./images/colour circle.png").getImage());

                selectedColour = JColorChooser.showDialog(iconParent, "Select colour", selectedColour);
                if (selectedColour != null) {
                    line.setColor(selectedColour);
                }
            }
        });

        colourSettingsPanel.add(colourLbl);
        colourSettingsPanel.add(chooseColour);
        colourSettingsPanel.add(line);
        return colourSettingsPanel;
    }

    private JPanel createBackgroundSettingsPanel() {
        JPanel backgroundSettingsPanel = new JPanel();
        backgroundSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 0));

        JLabel backgroundImgLbl = new JLabel("<html>Background<br>Colour:</html> ");
        backgroundImgLbl.setFont(new Font("Arial", Font.BOLD, 16));

        ColouredLine bgLine = new ColouredLine(selectedBackgroundColour, 3F);
        JButton chooseBackgroundColour = new JButton("Select Colour");
        chooseBackgroundColour.setFocusable(false);
        chooseBackgroundColour.addActionListener(e -> {
            if (e.getSource() == chooseBackgroundColour) {
                JFrame iconParent = new JFrame();
                iconParent.setIconImage(new ImageIcon("./images/colour circle.png").getImage());
                selectedBackgroundColour = JColorChooser.showDialog(iconParent, "Select colour",
                        selectedBackgroundColour);
                if (selectedBackgroundColour != null) {
                    bgLine.setColor(selectedBackgroundColour);
                }
            }
        });
        backgroundSettingsPanel.add(backgroundImgLbl);
        backgroundSettingsPanel.add(chooseBackgroundColour);
        backgroundSettingsPanel.add(bgLine);
        return backgroundSettingsPanel;
    }

    private JPanel createUserDecisionPanel() {
        JPanel userDecisionPanel = new JPanel();
        JButton confirmBttn = new JButton("Confirm");
        confirmBttn.setFocusable(false);
        confirmBttn.addActionListener(e -> {
            if (e.getSource() == confirmBttn) {
                if (source instanceof StickyNote note) {
                    SwingUtilities.invokeLater(() -> {
                        note.setBackgroundColour(selectedBackgroundColour);
                        note.setTextColour(selectedColour);
                    });
                } else {
                    StickyNote note = new StickyNote(selectedColour, selectedBackgroundColour);
                    note.setVisible(true);
                }
                close();
            }
        });
        JButton cancelBttn = new JButton("Cancel");
        cancelBttn.setFocusable(false);
        cancelBttn.addActionListener(e -> {
            if (e.getSource() == cancelBttn) {
                close();
            }
        });

        userDecisionPanel.add(confirmBttn);
        userDecisionPanel.add(cancelBttn);
        return userDecisionPanel;
    }

    protected void close() {
        super.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        super.dispose();
    }

    protected void setIcon(ImageIcon i) {
        super.setIconImage(i.getImage());
    }
}