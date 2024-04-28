package src;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateNote extends JDialog {
    private Component source;
    private Color selectedColour = Color.black;
    private Color selectedBackgroundColour = Color.YELLOW;

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
    }

    protected void createGUI() {
        final int VERTICAL_PADDING = 30;

        JPanel textSettingsPanel = new JPanel();
        textSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 20, 0));
        GridBagConstraints textSettingsC = new GridBagConstraints();
        textSettingsC.gridy = 0;
        textSettingsC.gridx = 0;
        textSettingsC.insets = new Insets(VERTICAL_PADDING, 0, 0, 0);

        // "Text:" label
        JLabel textLbl = new JLabel("Text:");
        textLbl.setFont(new Font("Arial", Font.BOLD, 16));

        // text area and remaining char label panel
        JPanel textBoxSubPanel = new JPanel();
        textBoxSubPanel.setLayout(new BoxLayout(textBoxSubPanel, BoxLayout.Y_AXIS));

        // text area
        JTextArea textArea;
        if (this.source instanceof StickyNote) {
            textArea = new JTextArea(((StickyNote) this.source).getFullText(), 6, 30);
        } else {
            textArea = new JTextArea("This is what goes in your sticky note!", 6, 30);
        }
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setMinimumSize(new Dimension(330, 96));

        // remaining characters label
        final int CHAR_LIMIT = 1000;
        JLabel charCountLbl = new JLabel(CHAR_LIMIT - (textArea.getText().length()) + " characters remaining",
                SwingConstants.RIGHT);
        textArea.addKeyListener(new KeyAdapter() {
            int length;
            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(
                        () -> {
                            // length is how much characters currently in text box
                            length = CommonUtils.collapseNewLine(textArea.getText()).length();
                            charCountLbl.setText(CHAR_LIMIT - length + " characters remaining");
                        });
                // ignore any events when the character limit has been reached
                if(length >= CHAR_LIMIT) {
                    e.consume();
                }
            }
        });
        // attach action that will stop any clipboard pasting above the char limit
        Action action = textArea.getActionMap().get("paste-from-clipboard");
        textArea.getActionMap().put("paste-from-clipboard", new PasteOverflowAction(action, textArea, CHAR_LIMIT));

        JPanel colourSettingsPanel = new JPanel();
        colourSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 36, 0));
        GridBagConstraints colourSettingsC = new GridBagConstraints();
        colourSettingsC.gridx = 0;
        colourSettingsC.gridy = 1;
        colourSettingsC.insets = new Insets(VERTICAL_PADDING, 0, VERTICAL_PADDING, 50);
        // colour sub-heading
        JLabel colourLbl = new JLabel("<html>Text<br>Colour:</html>");
        colourLbl.setFont(new Font("Arial", Font.BOLD, 16));
        // the line that will display user-chosen colour
        ColouredLine line = new ColouredLine(Color.black, 0, 0, 100, 0, 3F);
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
        backgroundSettingsC.insets = new Insets(0, 0, 0, 50);
        // try changing weights if it seems to take up more space
        backgroundSettingsC.weighty = 0.01;
        backgroundSettingsC.anchor = GridBagConstraints.PAGE_START;

        // "Background Image" sub-heading
        JLabel backgroundImgLbl = new JLabel("<html>Background<br>Colour:</html> ");
        backgroundImgLbl.setFont(new Font("Arial", Font.BOLD, 16));

        // choose background button
        ColouredLine bgLine = new ColouredLine(Color.YELLOW, 0, 0, 100, 0, 3F);
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
        confirmBttn.addActionListener(e -> {
            if (e.getSource() == confirmBttn) {
                if (this.source instanceof StickyNote) {
                    ((StickyNote) this.source).dispose();
                }
                SwingUtilities.invokeLater(() -> {
                    StickyNote note = new StickyNote(textArea.getText(), selectedColour, selectedBackgroundColour);
                    note.setVisible(true);
                    note.createGUI();
                    close();
                });
            }
        });
        JButton cancelBttn = new JButton("Cancel");
        cancelBttn.addActionListener(e -> {
            if (e.getSource() == cancelBttn) {
                close();
            }
        });

        textSettingsPanel.add(textLbl);
        textBoxSubPanel.add(scrollPane);
        textBoxSubPanel.add(charCountLbl);
        textSettingsPanel.add(textBoxSubPanel);
        super.add(textSettingsPanel, textSettingsC);

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