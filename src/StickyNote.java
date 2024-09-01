package src;

import src.actions.SaveAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Provides a implementation for a sticky note. Note that by default there is no physical scrollbar provided for scrolling
 * through this sticky note's text, rather the user must use a scroll wheel in order to scroll through. The default font
 * of all StickyNotes is Arial Size 18 that cannot be changed (for now). The sticky note has a default size that also
 * cannot be changed. The sticky note is by default draggable.
 */
public class StickyNote implements SettingsPopupMenu {
    protected JFrame mainFrame;
    protected StickyNoteBackground background;
    protected JTextArea textArea;
    private Color backgroundColour;
    private Color textColour;
    private int lastLocationX;
    private int lastLocationY;
    private boolean canDrag;
    public static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 18);
    public static final int ROWS = 18;
    public static final int COLUMNS = 28;

    public StickyNote() {
        this(Color.BLACK, Color.YELLOW);
    }

    public StickyNote(Color textColour, Color backgroundColour) {
        this(textColour, backgroundColour, 0, 0, true);
    }

    public StickyNote(Color textColour, Color backgroundColour, int lastLocationX, int lastLocationY, boolean canDrag) {
        mainFrame = new JFrame();
        mainFrame.setLocation(lastLocationX, lastLocationY);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setUndecorated(true);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.getContentPane().setBackground(backgroundColour);
        Image icon = CommonUtils.getScaledIcon("./images/stickynoteicon1.png",
                30, 30, Image.SCALE_SMOOTH);
        mainFrame.setIconImage(icon);

        this.backgroundColour = backgroundColour;
        this.textColour = textColour;
        this.lastLocationX = lastLocationX;
        this.lastLocationY = lastLocationY;
        this.canDrag = canDrag;
        this.createGUI();
    }

    @Override
    public void onSettingsButtonClicked(ActionEvent e, JMenuItem settingsButton) {
        mainFrame.setEnabled(false);
        CreateNote note = new CreateNote(this);
        note.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                mainFrame.setEnabled(true);
            }
        });
        note.setIcon(new ImageIcon("./images/settings icon.png"));
        note.setVisible(true);
    }

    @Override
    public void onLockButtonClicked(ActionEvent e, JMenuItem lockBttn) {
        if (StickyNote.this.canDrag) { // if the note can be dragged
            this.canDrag = false;
            lockBttn.setText("Unlock");
        } else { // if the note cannot be dragged
            this.canDrag = true;
            lockBttn.setText("Lock");
        }
    }

    @Override
    public void onCloseButtonClicked(ActionEvent e, JMenuItem closeButton) {
        mainFrame.dispose();
    }

    private JTextArea createNoteTextArea(JViewport textViewport) {
        JTextArea textArea = new JTextArea(ROWS, COLUMNS);
        textArea.setFont(DEFAULT_FONT);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setForeground(textColour);
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPopupMenu menu = new JPopupMenu();

                JMenuItem copyBttn = new JMenuItem("Copy");
                copyBttn.addActionListener(ev -> textArea.copy());
                menu.add(copyBttn);

                JMenuItem pasteBttn = new JMenuItem("Paste");
                pasteBttn.addActionListener(ev -> textArea.paste());
                menu.addSeparator();
                menu.add(pasteBttn);

                JMenuItem scrollTop = new JMenuItem("Scroll to top");
                scrollTop.addActionListener(ev -> textViewport.setViewPosition(new Point(0, 0)));
                menu.addSeparator();
                menu.add(scrollTop);

                JMenuItem scrollBottom = new JMenuItem("Scroll to bottom");
                scrollBottom.addActionListener(ev -> {
                    Dimension textAreaSize = textArea.getPreferredSize();
                    // translate view to its total height - total visible view height
                    textViewport.setViewPosition(new Point(0, textAreaSize.height -
                            textViewport.getExtentSize().height));
                });
                menu.addSeparator();
                menu.add(scrollBottom);
                if (SwingUtilities.isRightMouseButton(e)) {
                    menu.show(textArea, e.getX(), e.getY());
                }
            }
        });

        return textArea;
    }

    private JToolBar createNoteToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // without this, the point where the mouse cursor is will become the top left point of the sticky note
                    // so to fix this we subtract the screen coords with cursor x and y so that the cursor still remains there
                    // since the top left point is dragged to coords - cursor coords.

                    if (StickyNote.this.canDrag) {
                        int newX = e.getXOnScreen() - lastLocationX;
                        int newY = e.getYOnScreen() - lastLocationY;
                        mainFrame.setLocation(newX, newY);
                    }
                }
            }
        });
        toolBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    SwingUtilities.invokeLater(() -> {
                        createSettingsPopup(canDrag).show(mainFrame, e.getX(), e.getY());
                    });

                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    lastLocationX = e.getX();
                    lastLocationY = e.getY();
                }
            }
        });
        toolBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                lastLocationX = mainFrame.getLocation().x;
                lastLocationY = mainFrame.getLocation().y;
            }
        });

        toolBar.setBackground(CommonUtils.getScaledColour(backgroundColour, 0.9));
        toolBar.setBorder(null);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        Image saveIcon = CommonUtils.getScaledIcon("./images/saveIcon.png",
                30, 30, Image.SCALE_SMOOTH);
        CommonUtils.createToolbarButton(toolBar, new SaveAction(mainFrame, new ImageIcon(saveIcon)));

        return toolBar;
        //TODO: Add the other icons, then see if icon image is displayed on toolbar
    }


    private void createGUI() {
        //final int PADDING = 30;

        JLayeredPane textPane = new JLayeredPane();
        JScrollPane scrollTextPane = new JScrollPane();
        textArea = createNoteTextArea(scrollTextPane.getViewport());

        scrollTextPane.setViewportView(textArea);
        Dimension textAreaSize = textArea.getPreferredSize();
        Rectangle textAreaRect = new Rectangle(new Point(0, 0), textAreaSize);

        // if we have borders, then scroll bars pop up by default
        scrollTextPane.setBorder(null);
        scrollTextPane.setOpaque(false);
        scrollTextPane.getViewport().setOpaque(false);
        // hide the scroll bar, but still maintain wheel scrolling
        scrollTextPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollTextPane.setBounds(textAreaRect);
        textPane.add(scrollTextPane, Integer.valueOf(100));

        FontMetrics fm = textArea.getFontMetrics(DEFAULT_FONT);
        background = new StickyNoteBackground(backgroundColour, ROWS, fm.getHeight(), textAreaRect);
        background.setBounds(textAreaRect);
        textPane.add(background, Integer.valueOf(0));

        JToolBar noteToolbar = createNoteToolbar();
        GridBagConstraints toolbarC = new GridBagConstraints();
        toolbarC.gridx = 1;
        toolbarC.gridy = 1;
        toolbarC.fill = GridBagConstraints.HORIZONTAL;
        mainFrame.add(noteToolbar, toolbarC);

        GridBagConstraints textC = new GridBagConstraints();
        textC.gridx = 1;
        textC.gridy = 2;
        textPane.setPreferredSize(textAreaSize);
        mainFrame.add(textPane, textC);

        mainFrame.validate();
        mainFrame.pack();
    }


    /**
     * Sets the text colour of this sticky note
     * @param c the new text colour
     */
    public void setTextColour(Color c) {
        textArea.setForeground(c);
    }

    /**
     * Sets the background colour of this sticky note.
     * @param c the new background colour
     */
    public void setBackgroundColour(Color c) {
        mainFrame.getContentPane().setBackground(c);
        background.setBackgroundColour(c);
    }

    public void setVisible(boolean b) {
        mainFrame.setVisible(b);
    }
}
