package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StickyNote extends JFrame implements Serializable {
    private String text;
    private Color backgroundColour;
    private Color textColour;
    private JPopupMenu menu;
    private int stickyNoteX;
    private int stickyNoteY;
    private final Font defaultFont = new Font("Arial", Font.PLAIN, 18);
    private ArrayList<JTextArea> pageArray = new HashMap<>();
    private int pageNum = 0;

    public StickyNote(String text, Color textColour, Color backgroundColour) {
        super();
        setMouseListeners();
        super.setSize(new Dimension(300, 300));
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setUndecorated(true);

        Image icon = new ImageIcon("./images/stickynoteicon2.png").getImage();
        icon = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        super.setIconImage(icon);

        this.text = text;
        this.backgroundColour = backgroundColour;
        this.textColour = textColour;
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
                        menu.show(StickyNote.this,
                                e.getX(), e.getY());
                    });

                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    stickyNoteX = e.getX();
                    stickyNoteY = e.getY();
                }
            }
        });
    }

    protected void createGUI() {
        menu = new JPopupMenu();
        StickyNoteBackground temp = new StickyNoteBackground(text, textColour, backgroundColour, defaultFont);

        JMenuItem settingsBttn = new JMenuItem("Settings");
        settingsBttn.addActionListener(e -> {
            CreateNote note = new CreateNote(this);
            note.setIcon(new ImageIcon("./images/settings icon.png"));
            note.createGUI();
            note.setVisible(true);
        });
        menu.add(settingsBttn);

        JMenuItem nextPage = new JMenuItem("Next page");
        nextPage.addActionListener(e -> {
            /*
            Get the generated textPerLineList after rendering the temporary background since
            we require a Graphics object in order to reliably distribute the text
             */
            ArrayList<String> textList = temp.getTextPerLineList();
            int maxLines = StickyNoteBackground.linesPerNote;
            boolean noteHasTemp;

            // check if the temporary background is still there
            if (pageMap.get(0) == temp) {
                updatePageMap(maxLines, textList, textColour, backgroundColour, defaultFont);
                noteHasTemp = true;
            } else {
                noteHasTemp = false;
            }

            if (pageNum + 1 >= pageMap.size()) {
                return;
            }

            SwingUtilities.invokeLater(() -> {
                super.remove(noteHasTemp ? temp : getPage(pageNum));
                super.validate();
                super.repaint();
            });

            SwingUtilities.invokeLater(() -> {
                super.add(getPage(pageNum + 1));
                pageNum += 1;
                super.validate();
                super.repaint();
            });
        });
        menu.addSeparator();
        menu.add(nextPage);

        JMenuItem previousPage = new JMenuItem("Previous page");
        previousPage.addActionListener(e -> {
            if (pageNum != 0) {
                SwingUtilities.invokeLater(() -> {
                    super.remove(getPage(pageNum));
                    super.validate();
                    super.repaint();
                });

                SwingUtilities.invokeLater(() -> {
                    super.add(getPage(pageNum - 1));
                    pageNum -= 1;
                    super.validate();
                    super.repaint();
                });
            }
        });
        menu.addSeparator();
        menu.add(previousPage);

        JMenuItem closeBttn = new JMenuItem("Close");
        closeBttn.addActionListener(e -> {
            super.dispose();
        });
        menu.addSeparator();
        menu.add(closeBttn);

        pageMap.put(0, temp);
        super.add(temp);
        super.validate(); // because adding component invalidates
    }

    private void updatePageMap(int linesPerPage, ArrayList<String> lines, Color textColour, Color backgroundColour,
                               Font font) {
        int maxFullPages = lines.size() / linesPerPage;
        for (int i = 0; i < maxFullPages; i++) {
            // separate the text into text for each page
            ArrayList<String> textList = new ArrayList<>(lines.subList(i * linesPerPage, ((i + 1) * linesPerPage)));
            pageMap.put(i, new StickyNoteBackground(textList, textColour, backgroundColour, font));
        }

        // deal with remaining lines
        if (lines.size() % linesPerPage > 0) {
            ArrayList<String> textList = new ArrayList<>(lines.subList((maxFullPages * linesPerPage), lines.size()));
            // since map is zero-indexed, the max full pages is 1 more than the actual amount of pages
            pageMap.put(maxFullPages, new StickyNoteBackground(textList, textColour, backgroundColour, font));
        }
    }

    public StickyNoteBackground getPage(int page) {
        return pageMap.get(page);
    }

    public String getFullText() {
        return text;
    }
}
