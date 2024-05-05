package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.io.Serializable;

/**
 * Provides a implementation for a sticky note. Note that by default there is no physical scrollbar provided for scrolling
 * through this sticky note's text, rather the user must use a scroll wheel in order to scroll through.
 */
public class StickyNote extends JFrame implements Serializable {
    @Serial
    private static final long serialVersionUID = 8195518566344094545L;
    private Color backgroundColour;
    private Color textColour;
    private final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 18);
    private int stickyNoteX;
    private int stickyNoteY;
    private StickyNoteBackground background = null;
    private final int ROWS = 18;
    private final int COLUMNS = 28;
    private int pageNum = 0;
    private JScrollPane scrollTextPane = null;
    private boolean canDrag = true;


    public StickyNote(Color textColour, Color backgroundColour) {
        super();
        setMouseListeners();
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setUndecorated(true);
        super.setLayout(new GridBagLayout());
        super.getContentPane().setBackground(backgroundColour);

        Image icon = new ImageIcon("./images/stickynoteicon2.png").getImage();
        icon = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        super.setIconImage(icon);

        this.backgroundColour = backgroundColour;
        this.textColour = textColour;
        this.createGUI();
    }

    public StickyNote() {
        this(Color.black, Color.yellow);
    }

    private void setMouseListeners() {
        super.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // without this, the point where the mouse cursor is will become the top left point of the sticky note
                    // so to fix this we subtract the screen coords with cursor x and y so that the cursor still remains there
                    // since the top left point is dragged to coords - cursor coords.

                    if (StickyNote.this.canDrag) {
                        int newX = e.getXOnScreen() - stickyNoteX;
                        int newY = e.getYOnScreen() - stickyNoteY;
                        StickyNote.super.setLocation(newX, newY);
                    }
                }
            }
        });
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    SwingUtilities.invokeLater(() -> {
                        createSettingPopup().show(StickyNote.this,
                                e.getX(), e.getY());
                    });

                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    stickyNoteX = e.getX();
                    stickyNoteY = e.getY();
                }
            }
        });
    }

    private JPopupMenu createSettingPopup() {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem settingsBttn = new JMenuItem("Settings");
        settingsBttn.addActionListener(e -> {
            CreateNote note = new CreateNote(this);
            note.setIcon(new ImageIcon("./images/settings icon.png"));
            note.setVisible(true);
        });
        menu.add(settingsBttn);

        JMenuItem lockBttn = new JMenuItem(canDrag ? "Lock" : "Unlock");
        lockBttn.addActionListener(e -> {
            if (StickyNote.this.canDrag) { // if the note can be dragged
                this.canDrag = false;
                lockBttn.setText("Unlock");
            } else { // if the note cannot be dragged
                this.canDrag = true;
                lockBttn.setText("Lock");
            }
        });
        menu.addSeparator();
        menu.add(lockBttn);

        JMenuItem closeBttn = new JMenuItem("Close");
        closeBttn.addActionListener(e -> {
            super.dispose();
        });
        menu.addSeparator();
        menu.add(closeBttn);

        return menu;
    }

    private void createGUI() {
        final int PADDING = 30;

        JLayeredPane textPane = new JLayeredPane();

        JTextArea textArea = createJTextArea();
        Dimension textAreaSize = textArea.getPreferredSize();
        Rectangle textAreaRect = new Rectangle(new Point(0, 0), textAreaSize);

        scrollTextPane = new JScrollPane(textArea);
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

        GridBagConstraints textC = new GridBagConstraints();
        textC.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        textPane.setPreferredSize(textAreaSize);
        super.add(textPane, textC);
        super.validate();
        super.pack();
    }

    private JTextArea createJTextArea() {
        JTextArea textArea = new JTextArea(ROWS, COLUMNS);
        //textArea.setBorder(new LineBorder(Color.black));
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
                copyBttn.addActionListener(ev -> {
                    textArea.copy();
                });
                menu.add(copyBttn);

                JMenuItem pasteBttn = new JMenuItem("Paste");
                pasteBttn.addActionListener(ev -> {
                    textArea.paste();
                });
                menu.addSeparator();
                menu.add(pasteBttn);

                JMenuItem scrollTop = new JMenuItem("Scroll to top");
                scrollTop.addActionListener(ev -> {
                    JViewport textViewport = scrollTextPane.getViewport();
                    textViewport.setViewPosition(new Point(0, 0));
                });
                menu.addSeparator();
                menu.add(scrollTop);

                JMenuItem scrollBottom = new JMenuItem("Scroll to bottom");
                scrollBottom.addActionListener(ev -> {
                    JViewport textViewport = scrollTextPane.getViewport();
                    Component textArea = textViewport.getView();
                    Dimension textAreaSize = textArea.getPreferredSize();
                    // translate view to its total height - total visible view height
                    textViewport.setViewPosition(new Point(0, textAreaSize.height -
                            textViewport.getExtentSize().height));
                });
                menu.addSeparator();
                menu.add(scrollBottom);
                if (SwingUtilities.isRightMouseButton(e)) {
                    menu.show(StickyNote.this, e.getX(), e.getY());
                }
            }
        });

        return textArea;
    }
    public void setTextColour(Color c) {
        if (scrollTextPane != null) {
            JViewport textViewport = scrollTextPane.getViewport();
            Component textArea = textViewport.getView();
            textArea.setForeground(c);
        } else {
            // this is an instance method, so this code should never be reached, but is for precaution purposes
            throw new NullPointerException("textScrollPane field was not populated");
        }
    }
    public void setBackgroundColour(Color c) {
        if (background != null) {
            super.getContentPane().setBackground(c);
            background.setBackgroundColour(c);
        } else {
            // this is an instance method, so this should never be reached, but is for precaution purposes
            throw new NullPointerException("background field was not populated");
        }
    }

    public void setDragAbility(boolean canDrag) {
        this.canDrag = canDrag;
    }
}
