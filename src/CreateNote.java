package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * CreateNote is the lightweight component that provides a user interface for the creation of StickyNote.
 * CreateNote instances are created for when the StickyNote is first created, or some of its properties want to be
 * potentially edited.
 *
 * @see src.StickyNote
 */
public class CreateNote extends JDialog {
    /**
     * The component that is the reason that this instance of CreateNote exists. This can be null,
     * or really any instance of Component. There is specific behaviour when source is an instance of StickyNote.
     *
     * @see src.StickyNote
     */
    private Component source;
    private Color selectedColour = Color.black;
    private Color selectedBackgroundColour = Color.YELLOW;
    private boolean keepScrollBar = false;

    /**
     * Creates a CreateNote instance with fixed size of 500 x 900. This constructor
     * will automaticclly create the GUI.
     * @param source the component that caused this createNote instance to be created.
     */
    public CreateNote(Component source) {
        // (Dialog) null is to make JDialog appear in taskbar
        super((Dialog) null);
        this.source = source;

        super.setLayout(new GridBagLayout());
        super.setSize(500, 900);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setTitle("Create a new Sticky Note");
        super.setResizable(false);

        Image icon = new ImageIcon("./images/stickynoteiconPlus2.png").getImage();
        icon = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        super.setIconImage(icon);

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                source.setEnabled(true);
            }
        });
        createGUI();
    }

    /**
     * Private method which creates the GUI for this CreateNote instance.
     */
    private void createGUI() {
        final int VERTICAL_PADDING = 30;

        JPanel colourSettingsPanel = new JPanel();
        colourSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 0));
        GridBagConstraints colourSettingsC = new GridBagConstraints();
        colourSettingsC.gridx = 0;
        colourSettingsC.gridy = 1;
        colourSettingsC.anchor = GridBagConstraints.FIRST_LINE_START;
        colourSettingsC.insets = new Insets(VERTICAL_PADDING, 0, 0, 0);
        colourSettingsC.weighty = 0.001;
        // colour sub-heading
        JLabel colourLbl = new JLabel("<html>Text<br>Colour:</html>");
        colourLbl.setFont(new Font("Arial", Font.BOLD, 16));
        // the line that will display user-chosen colour
        ColouredLine line = new ColouredLine(Color.black, 3F);
        // the "choose colour" button
        JButton chooseColour = new JButton("Select Colour");
        chooseColour.setFocusable(false);
        chooseColour.addActionListener(e -> {
            if (e.getSource() == chooseColour) {
                JFrame iconParent = new JFrame();
                iconParent.setIconImage(new ImageIcon("./images/colour circle.png").getImage());

                selectedColour = JColorChooser.showDialog(iconParent, "Select colour", Color.black);
                if (selectedColour != null) {
                    line.setColor(selectedColour);
                }
            }
        });

        JPanel backgroundSettingsPanel = new JPanel();
        backgroundSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 0));
        GridBagConstraints backgroundSettingsC = new GridBagConstraints();
        backgroundSettingsC.gridx = 0;
        backgroundSettingsC.gridy = 2;
        // try changing weights if it seems to take up more space
        backgroundSettingsC.weighty = 0.01;
        backgroundSettingsC.anchor = GridBagConstraints.FIRST_LINE_START;

        // "Background Image" sub-heading
        JLabel backgroundImgLbl = new JLabel("<html>Background<br>Colour:</html> ");
        backgroundImgLbl.setFont(new Font("Arial", Font.BOLD, 16));

        // choose background button
        ColouredLine bgLine = new ColouredLine(Color.YELLOW, 3F);
        JButton chooseBackgroundColour = new JButton("Select Colour");
        chooseBackgroundColour.setFocusable(false);
        chooseBackgroundColour.addActionListener(e -> {
            if (e.getSource() == chooseBackgroundColour) {
                JFrame iconParent = new JFrame();
                iconParent.setIconImage(new ImageIcon("./images/colour circle.png").getImage());

                selectedBackgroundColour = JColorChooser.showDialog(iconParent, "Select colour", Color.YELLOW);
                if (selectedBackgroundColour != null) {
                    bgLine.setColor(selectedBackgroundColour);
                }
            }
        });

        // when user wants to finalize changes
        JPanel userDecisionPanel = new JPanel();
        GridBagConstraints userDecisionC = new GridBagConstraints();
        userDecisionC.gridx = 0;
        userDecisionC.gridy = 4;
        userDecisionC.anchor = GridBagConstraints.FIRST_LINE_END;

        JButton confirmBttn = new JButton("Confirm");
        confirmBttn.setFocusable(false);
        confirmBttn.addActionListener(e -> {
            if (e.getSource() == confirmBttn) {
                if (source instanceof StickyNote) {
                    SwingUtilities.invokeLater(() -> {
                        ((StickyNote) source).setBackgroundColour(selectedBackgroundColour);
                        ((StickyNote) source).setTextColour(selectedColour);
                        source.revalidate();
                        source.repaint();
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

        colourSettingsPanel.add(colourLbl);
        colourSettingsPanel.add(chooseColour);
        colourSettingsPanel.add(line);
        super.add(colourSettingsPanel, colourSettingsC);

        backgroundSettingsPanel.add(backgroundImgLbl);
        backgroundSettingsPanel.add(chooseBackgroundColour);
        backgroundSettingsPanel.add(bgLine);
        super.add(backgroundSettingsPanel, backgroundSettingsC);

        userDecisionPanel.add(confirmBttn);
        userDecisionPanel.add(cancelBttn);
        super.add(userDecisionPanel, userDecisionC);
    }

    protected void close() {
        super.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        super.dispose();
    }

    protected void setIcon(ImageIcon i) {
        super.setIconImage(i.getImage());
    }
}