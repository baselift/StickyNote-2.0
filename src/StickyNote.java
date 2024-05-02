package src;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class StickyNote extends JFrame implements Serializable {
    private Color backgroundColour;
    private Color textColour;
    private final Font defaultFont = new Font("Arial", Font.PLAIN, 18);
    private int stickyNoteX;
    private int stickyNoteY;
    private JPanel textAreaDeck = null;
    private int pageNum = 0;
    private final int ROWS = 18;
    private final int COLUMNS = 28;

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
        createGUI();
    }

    private void setMouseListeners() {
        super.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // without this, the point where the mouse cursor is will become the top left point of the sticky note
                    // so to fix this we subtract the screen coords with cursor x and y so that the cursor still remains there
                    // since the top left point is dragged to coords - cursor coords.

                    int newX = e.getXOnScreen() - stickyNoteX;
                    int newY = e.getYOnScreen() - stickyNoteY;
                    StickyNote.super.setLocation(newX, newY);
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

        JMenuItem nextPage = new JMenuItem("Next page");
        nextPage.addActionListener(e -> {
            nextPage(false);
        });
        menu.addSeparator();
        menu.add(nextPage);

        JMenuItem previousPage = new JMenuItem("Previous page");
        previousPage.addActionListener(e -> {
            previousPage();
        });
        menu.addSeparator();
        menu.add(previousPage);

        JMenuItem closeBttn = new JMenuItem("Close");
        closeBttn.addActionListener(e -> {
            super.dispose();
        });
        menu.addSeparator();
        menu.add(closeBttn);

        return menu;
    }

    private void nextPage(boolean shouldCreate) {
        if (shouldCreate) {
            JTextArea newTextArea = createJTextArea();
            textAreaDeck.add(newTextArea);
        }
        CardLayout cl = (CardLayout) (textAreaDeck.getLayout());
        cl.next(textAreaDeck);
    }

    private void previousPage() {
        CardLayout cl = (CardLayout) (textAreaDeck.getLayout());
        cl.previous(textAreaDeck);
    }

    private void createGUI() {
        final int PADDING = 20;

        JLayeredPane textPane = new JLayeredPane();

        textAreaDeck = new JPanel();
        textAreaDeck.setLayout(new CardLayout());
        textAreaDeck.setOpaque(false); // so the background is shown, since JPanel has background color
        JTextArea textArea = createJTextArea();
        textAreaDeck.add(textArea);
        textAreaDeck.setBounds(new Rectangle(new Point(0, 0), textArea.getSize()));
        textPane.add(textAreaDeck, Integer.valueOf(100));

        FontMetrics fm = textArea.getFontMetrics(defaultFont);
        Rectangle drawBounds = new Rectangle(0, 0, textArea.getWidth(), textArea.getHeight());
        StickyNoteBackground background = new StickyNoteBackground(backgroundColour, ROWS, fm.getHeight(), drawBounds);
        background.setBounds(new Rectangle(new Point(0, 0), textArea.getSize()));
        textPane.add(background, Integer.valueOf(0));

        GridBagConstraints textC = new GridBagConstraints();
        textC.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        textPane.setPreferredSize(textArea.getSize());
        super.add(textPane, textC);
        super.validate();
        super.pack();
    }

    private JTextArea createJTextArea() {
        JTextArea textArea = new JTextArea(ROWS, COLUMNS);
        textArea.setBorder(new LineBorder(Color.black));
        textArea.setFont(defaultFont);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setSize(textArea.getPreferredSize());
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
                if (SwingUtilities.isRightMouseButton(e)) {
                    menu.show(StickyNote.this, e.getX(), e.getY());
                }
            }
        });

        textArea.addCaretListener(e -> {
            FontMetrics fm = textArea.getFontMetrics(defaultFont);
            View mainView = textArea.getUI().getRootView(textArea);
            int lineHeight = fm.getHeight();
            int currentNumberOfLines = (int) mainView.getPreferredSpan(View.Y_AXIS) / lineHeight;
            if (currentNumberOfLines > ROWS) {
                nextPage(true);
                SwingUtilities.invokeLater(() -> {
                    try {
                        //TODO: Fix wrapped line -> too many lines problem
                        textArea.replaceRange("", textArea.getLineStartOffset(ROWS - 1), e.getDot());
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        });

        return textArea;
    }
}
