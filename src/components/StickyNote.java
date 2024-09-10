package src.components;

import src.CommonUtils;
import src.NotePropertySelectDialog;
import src.actions.SaveAction;
import src.components.textarea.StickyNoteTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StickyNote {
    private JFrame mainFrame;
    private StickyNoteBackground background;
    private StickyNoteTextArea textArea;
    private boolean canDrag = true;
    private Color textColour;
    private Color backgroundColour;
    private int lastNoteX;
    private int lastNoteY;

    public StickyNote(Color textColour, Color backgroundColour) {
        mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setUndecorated(true);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.getContentPane().setBackground(backgroundColour);
        Image icon = CommonUtils.getScaledIcon("./images/stickynoteicon1.png",
                30, 30, Image.SCALE_SMOOTH);
        mainFrame.setIconImage(icon);

        this.textColour = textColour;
        this.backgroundColour = backgroundColour;
        this.createGUI();
    }

    private JToolBar createNoteToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (StickyNote.this.canDrag) {
                    int newX = e.getXOnScreen() - lastNoteX;
                    int newY = e.getYOnScreen() - lastNoteY;
                    mainFrame.setLocation(new Point(newX, newY));
                }
            }
        });
        toolBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu menu = new JPopupMenu();

                    JMenuItem settingsBttn = new JMenuItem("Settings");
                    settingsBttn.addActionListener(ev -> {
                        mainFrame.setEnabled(false);
                        StickyNote.StickyNoteEditor noteEditor = new StickyNoteEditor();
                        noteEditor.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                mainFrame.setEnabled(true);
                            }
                        });
                        noteEditor.setVisible(true);
                    });
                    menu.add(settingsBttn);

                    JMenuItem lockBttn = new JMenuItem(canDrag ? "Lock" : "Unlock");
                    lockBttn.addActionListener(ev -> {
                        canDrag = !canDrag;
                        lockBttn.setText(canDrag ? "Lock" : "Unlock");
                    });
                    menu.addSeparator();
                    menu.add(lockBttn);

                    JMenuItem closeBttn = new JMenuItem("Close");
                    closeBttn.addActionListener(ev -> mainFrame.dispose());
                    menu.addSeparator();
                    menu.add(closeBttn);

                    menu.show(mainFrame, e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    lastNoteX = e.getX();
                    lastNoteY = e.getY();
                }

            }
        });
        toolBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastNoteX = mainFrame.getLocation().x;
                lastNoteY = mainFrame.getLocation().y;
            }
        });

        toolBar.setBackground(CommonUtils.getScaledColour(background.getBackgroundColour(), 0.9));
        toolBar.setBorder(null);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        Image saveIcon = CommonUtils.getScaledIcon("./images/saveIcon.png",
                30, 30, Image.SCALE_SMOOTH);
        CommonUtils.addToolbarAction(toolBar, new SaveAction(mainFrame, new ImageIcon(saveIcon)));

        return toolBar;
        //TODO: Add the other icons, then see if icon image is displayed on toolbar
    }

    private void createGUI() {
        //final int PADDING = 30;

        textArea = new StickyNoteTextArea(textColour);
        JLayeredPane textPane = new JLayeredPane();
        JScrollPane scrollTextPane = new JScrollPane();
        textArea.addToViewport(scrollTextPane.getViewport());

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

        background = new StickyNoteBackground(backgroundColour, StickyNoteTextArea.TEXT_BOX_ROWS,
                textArea.getRowHeight(), textAreaRect);
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

    public void setVisible(boolean visible) {
        mainFrame.setVisible(visible);
    }

    public class StickyNoteEditor extends NotePropertySelectDialog {
        public StickyNoteEditor() {
            super();
            super.setIconImage(new ImageIcon("./images/settings icon.png").getImage());
        }

        @Override
        protected void onConfirmBttnClicked(ActionEvent e) {
            textArea.setTextColor(selectedColour);

            mainFrame.getContentPane().setBackground(selectedBackgroundColour);
            background.setBackgroundColour(selectedBackgroundColour);

            close();
        }
    }

}
