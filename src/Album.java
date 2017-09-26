import javafx.scene.effect.Light;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.View;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;


public class Album {

    static LightTable lightTable;
    static JPanel photoPanel;
    static JPanel splitPhotoPanel;
    static JPanel splitThumbnail;
    static JPanel browserPanel;
    static JPanel tagPanel;
    static JLabel familyLabel;
    static JLabel schoolLabel;
    static JLabel workLabel;
    static JLabel vacationLabel;
    static JCheckBox family;
    static JCheckBox work;
    static JCheckBox school;
    static JCheckBox vacation;
    static JButton familyMagnet;
    static JButton schoolMagnet;
    static JButton vacationMagnet;
    static JButton workMagnet;
    static JRadioButtonMenuItem photo;
    static JRadioButtonMenuItem grid;
    static JRadioButtonMenuItem split;
    static JMenu file;
    static JMenu view;
    static JPanel toolMenu;
    static JLayeredPane layeredPane;
    static HashMap<Tags, ArrayList<ThumbnailComponent>> magnetComponents = new HashMap<>();

    public final static int TENTH_OF_A_SECOND = 100;
    public static int numIterations = 0;

    public static Timer timer;

    private static void createAndShowGUI() {
        /* Creating basic JFrame for Album */
        JFrame jframe = new JFrame("Album");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setMinimumSize(new Dimension(200, 500)); //requirement for minimum size so as not to skew the tool bar
        jframe.setName("JFrame");

        /* Menu Bar */
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(200, 200, 200));
        menuBar.setPreferredSize(new Dimension(800, 20));
        menuBar.setName("Menu Bar");

        /* Content Pane */
        lightTable = new LightTable();
        lightTable.setOpaque(true);

        lightTable.setPreferredSize(new Dimension(775, 800));
        lightTable.setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(775, 800));
        layeredPane.setOpaque(true);
        layeredPane.setBackground(new Color(200, 200, 200));

//        panel.add(lightTable, BorderLayout.CENTER);

        /* Tag Panel Instantiation */
        tagPanel = new JPanel();
        tagPanel.setPreferredSize(new Dimension(675, 20));
        tagPanel.setBackground(new Color(102, 102, 102));
        tagPanel.setOpaque(true);
        tagPanel.setLayout(new BoxLayout(tagPanel, BoxLayout.LINE_AXIS));

        tagPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        familyLabel = new JLabel("Family");
        familyLabel.setOpaque(true);
        familyLabel.setBackground(new Color(226, 226, 226));
        familyLabel.setPreferredSize(new Dimension(50, 30));

        vacationLabel = new JLabel("Vacation");
        vacationLabel.setOpaque(true);
        vacationLabel.setBackground(new Color(226, 226, 226));
        vacationLabel.setPreferredSize(new Dimension(50, 30));

        workLabel = new JLabel("Work");
        workLabel.setOpaque(true);
        workLabel.setBackground(new Color(226, 226, 226));
        workLabel.setPreferredSize(new Dimension(50, 30));

        schoolLabel = new JLabel("School");
        schoolLabel.setOpaque(true);
        schoolLabel.setBackground(new Color(226, 226, 226));
        schoolLabel.setPreferredSize(new Dimension(50, 3));

        /* Photo View JPanel */
        photoPanel = new JPanel();
        photoPanel.setPreferredSize(new Dimension(675, 800));
        photoPanel.setBackground(new Color(102, 102, 102));
        lightTable.add(photoPanel, BorderLayout.CENTER);
        photoPanel.setOpaque(true);
        photoPanel.setLayout(new BorderLayout());
        photoPanel.add(tagPanel, BorderLayout.NORTH);

        /* Split View JPanel's */
        splitPhotoPanel = new JPanel();
        splitPhotoPanel.setPreferredSize(new Dimension(675, 500));
        splitPhotoPanel.setBackground(new Color(102, 102, 102));
        //lightTable.add(splitPhotoPanel, BorderLayout.CENTER);

        splitThumbnail = new JPanel();
        JScrollPane scrollPane = new JScrollPane(splitThumbnail);
        //lightTable.add(scrollPane, BorderLayout.SOUTH);

        splitThumbnail.setLayout(new BoxLayout(splitThumbnail, BoxLayout.LINE_AXIS));
        splitThumbnail.setPreferredSize(new Dimension(675, 210));
        splitThumbnail.setBackground(new Color(200, 200, 200));

        splitThumbnail.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));




        /* Browser View JPanel */
        browserPanel = new JPanel();
        browserPanel.setPreferredSize(new Dimension(675, 800));
        browserPanel.setBackground(new Color(200, 200, 200));
        browserPanel.setOpaque(true);
        browserPanel.setLayout(new GridLayout(0, 3, 10, 10));
        browserPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        /* Status Bar Along Bottom */
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(new Color(226, 226, 226));
        label.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        label.setPreferredSize(new Dimension(800, 20));

        /* Options on Menu Bar */
        file = new JMenu("File");
        view = new JMenu("View");
        file.setName("File Menu");
        menuBar.add(file);
        menuBar.add(view);

        /* File Menu Items */
        JMenuItem _import = new JMenuItem("Import");
        JMenuItem _delete = new JMenuItem("Delete");
        JMenuItem _exit = new JMenuItem("Exit");

        file.add(_import);
        file.add(_delete);
        file.add(_exit);

        /*
         *   Adding an Action Listeners to each option
         *   under the "View" option to write to status bar. */

        //Photo View Option
        photo = new JRadioButtonMenuItem("Photo View");
        photo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Photo view selected.");
                changeMode(lightTable.getMode(), ViewMode.PHOTO);
            }
        });

        //KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

        /*focusManager.addPropertyChangeListener(
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        String properties = e.getPropertyName();
                        if (("focusOwner".equals(properties)) && (e.getNewValue() != null)) {
                            Component component = (Component)e.getNewValue();
                            String name = component.getName();

                            System.out.println(name + " take focus");
                        }
                    }
                });*/


        //Grid View Option
        grid = new JRadioButtonMenuItem("Grid View");
        grid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Grid view selected.");
                changeMode(lightTable.getMode(), ViewMode.BROWSER);
            }
        });

        //Split View Option
        split = new JRadioButtonMenuItem("Split View");
        split.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Split view selected.");
                changeMode(lightTable.getMode(), ViewMode.SPLIT);
            }
        });

        /* Using ButtonGroup Class to ensure that
         * only 1 View Option is selected at a time. */
        ButtonGroup viewButtons = new ButtonGroup();
        viewButtons.add(photo);
        viewButtons.add(grid);
        viewButtons.add(split);

        /* Adding Photo, Grid, and Split View
         * options to the View Menu in Menu Bar */
        view.add(photo);
        view.add(grid);
        view.add(split);

        /* Side Tool Bar */
        toolMenu = new JPanel();
        toolMenu.setOpaque(true);
        toolMenu.setBackground(new Color(226, 226, 226));
        toolMenu.setPreferredSize(new Dimension(125, 800));

        /* Instantiating Tag Toggle Buttons (Family, School, Vacation, Work) */
        family = new JCheckBox("Family", false);

        /* Action Listeners to write to status bar when selected/deselected */
        family.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText(family.isSelected() ? "Family Tag Selected." : "Family Tag Deselected.");
                if (family.isSelected()) {
                    lightTable.getCurrPhoto().addTag(Tags.FAMILY);
                    tagPanel.add(familyLabel);
                    tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
                } else {
                    lightTable.getCurrPhoto().removeTag(Tags.FAMILY);
                    tagPanel.remove(familyLabel);
                }
                tagPanel.revalidate();
//                tagFamily();
            }
            // If tag is selected, status bar text changed to "... Tag Selected."
            // If tag is deselected, status bar text changed to "... Tag Deselected."
        });

        vacation = new JCheckBox("Vacation", false);
        vacation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText(vacation.isSelected() ? "Vaction Tag Selected." : "Vacation Tag Deselected.");
                if (vacation.isSelected()) {
                    lightTable.getCurrPhoto().addTag(Tags.VACATION);
                    tagPanel.add(vacationLabel);
                    tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
                } else {
                    lightTable.getCurrPhoto().removeTag(Tags.VACATION);
                    tagPanel.remove(vacationLabel);
                }
                tagPanel.revalidate();
//                tagVacation();
            }
        });

        school = new JCheckBox("School", false);
        school.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText(school.isSelected() ? "School Tag Selected." : "School Tag Deselected.");
                if (school.isSelected()) {
                    lightTable.getCurrPhoto().addTag(Tags.SCHOOL);
                    tagPanel.add(schoolLabel);
                    tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
                } else {
                    lightTable.getCurrPhoto().removeTag(Tags.SCHOOL);
                    tagPanel.remove(schoolLabel);
                }
                tagPanel.revalidate();
//                tagSchool();
            }
        });

        work = new JCheckBox("Work", false);
        work.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText(work.isSelected() ? "Work Tag Selected." : "Work Tag Deselected.");
                if (work.isSelected()) {
                    lightTable.getCurrPhoto().addTag(Tags.WORK);
                    tagPanel.add(workLabel);
                    tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
                } else {
                    lightTable.getCurrPhoto().removeTag(Tags.WORK);
                    tagPanel.remove(workLabel);
                }
                tagPanel.revalidate();
//                tagWork();
            }
        });

        JButton magnet = new JButton("Magnet");
        magnet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lightTable.getMode().equals(ViewMode.BROWSER)) {
                    if (lightTable.isMagnet()) {
                        setMagnetMode(false);
                        jframe.getContentPane().remove(layeredPane);
                        jframe.getContentPane().add(lightTable);
                    } else {
                        setMagnetMode(true);
                        jframe.getContentPane().add(layeredPane);
                        jframe.getContentPane().remove(lightTable);
                    }
                }
                jframe.repaint();
            }
        });

        /* Instantiating Magnet Buttons */
        familyMagnet = new JButton("Family");
        familyMagnet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTable.setMagnetLabel("Family");
            }
        });

        workMagnet = new JButton("Work");
        workMagnet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTable.setMagnetLabel("Work");
            }
        });

        schoolMagnet = new JButton("School");
        schoolMagnet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTable.setMagnetLabel("School");
            }
        });

        vacationMagnet = new JButton("Vacation");
        vacationMagnet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lightTable.setMagnetLabel("Vacation");
            }
        });

        toolMenu.add(magnet);


        /*
         *   Drawing and Text Buttons created and set
         *   to write to status bar if selected.
         */
        JRadioButton drawing = new JRadioButton("Drawing", true);
        drawing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Drawing Tool Selected.");
                if (lightTable.getCurrPhoto() != null) {
                    if (lightTable.getCurrPhoto().isFlipped()) {
                        lightTable.getCurrPhoto().setMode("drawing");
                        lightTable.getCurrPhoto().requestFocusInWindow();
                    }
                }
            }
        });

        JRadioButton text = new JRadioButton("Text", false);
        text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                label.setText("Text Tool Selected.");
                if (lightTable.getCurrPhoto() != null) {
                    if (lightTable.getCurrPhoto().isFlipped()) {
                        lightTable.getCurrPhoto().setMode("text");
                        lightTable.getCurrPhoto().requestFocusInWindow();
                    }
                }
            }
        });
        /*
         *  Use ButtonGroup to ensure that only 1 tool mode
         *  can be selected at one time.
         */
        ButtonGroup toolGroup = new ButtonGroup();
        toolGroup.add(drawing);
        toolGroup.add(text);


        /*
         *  Create Next and Previous Page Buttons
         */
        JButton forward = new JButton("Next ->");
        forward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Next Page");
//                int ind = lightTable.getPcs().indexOf(lightTable.getCurrPhoto());
//                if (ind < (lightTable.getPcs().size()- 1)) {
//                    if (lightTable.getMode() == ViewMode.PHOTO) {
//                        photoPanel.remove(lightTable.getCurrPhoto());
//                        photoPanel.revalidate();
//                        lightTable.setCurrPhoto(lightTable.getPcs().get(ind + 1));
//                        photoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
//                        updateTags();
//                        photoPanel.revalidate();
//                    } else if (lightTable.getMode() == ViewMode.SPLIT) {
//                        splitPhotoPanel.remove(lightTable.getCurrPhoto());
//                        splitPhotoPanel.revalidate();
//                        lightTable.setCurrPhoto(lightTable.getPcs().get(ind + 1));
//                        splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
//                        updateTags();
//                        tagPanel.revalidate();
//                        splitPhotoPanel.revalidate();
//                        splitThumbnail.revalidate();
//                    } else {
//                        lightTable.setCurrPhoto(lightTable.getPcs().get(ind + 1));
//                        browserPanel.revalidate();
//                    }
//                }
//                lightTable.revalidate();
                forward();
            }
        });
        JButton back = new JButton("<- Previous");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Previous Page");
//                int ind = lightTable.getPcs().indexOf(lightTable.getCurrPhoto());
//                if (ind > 0) {
//                    if (lightTable.getMode() == ViewMode.PHOTO) {
//                        photoPanel.remove(lightTable.getCurrPhoto());
//                        photoPanel.revalidate();
//                        lightTable.setCurrPhoto(lightTable.getPcs().get(ind - 1));
//                        updateTags();
//                        photoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
//                        photoPanel.revalidate();
//                    } else if (lightTable.getMode() == ViewMode.SPLIT) {
//                        splitPhotoPanel.remove(lightTable.getCurrPhoto());
//                        splitPhotoPanel.revalidate();
//                        lightTable.setCurrPhoto(lightTable.getPcs().get(ind - 1));
//                        updateTags();
//                        splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
//                        splitPhotoPanel.revalidate();
//                        splitThumbnail.revalidate();
//                    } else {
//                        lightTable.setCurrPhoto(lightTable.getPcs().get(ind - 1));
//                        browserPanel.revalidate();
//                    }
//                }
//                lightTable.revalidate();
                backward();
            }
        });

        splitThumbnail.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 1) {
                    for (PhotoComponent pc : lightTable.getPcs()) {
                        if (pc.getThumbnail().getBounds().contains(e.getPoint())) {
                            splitPhotoPanel.remove(lightTable.getCurrPhoto());
                            lightTable.setCurrPhoto(pc);
                            splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                            updateTags();
                        }
                    }
                }
                if (e.getClickCount() >= 2) {
                    for (PhotoComponent pc : lightTable.getPcs()) {
                        if (pc.getThumbnail().getBounds().contains(e.getPoint())) {
                            splitPhotoPanel.remove(tagPanel);
                            lightTable.setCurrPhoto(pc);
                            changeMode(ViewMode.SPLIT, ViewMode.PHOTO);
                            updateTags();
                        }
                    }
                }
            }
        });


        browserPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 1) {
                    for (PhotoComponent pc : lightTable.getPcs()) {
                        if (pc.getThumbnail().getBounds().contains(e.getPoint())) {
                            lightTable.setCurrPhoto(pc);
                            updateTags();
                            lightTable.revalidate();
                        }
                    }
                }
                if (e.getClickCount() >= 2) {
                    for (PhotoComponent pc : lightTable.getPcs()) {
                        if (pc.getThumbnail().getBounds().contains(e.getPoint())) {
                            lightTable.setCurrPhoto(pc);
                            lightTable.revalidate();
                            changeMode(ViewMode.BROWSER, ViewMode.PHOTO);
                            updateTags();
//                            lightTable.revalidate();
                        }
                    }
                }
            }
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * *  *
        *   Adding an Action Listener to Import button to open    *
        *   a File Chooser upon click. Also, adds text to the     *
        *   status bar along the bottom.                          *
        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        _import.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser importNew = new JFileChooser();
                importNew.setName("File Chooser");
                importNew.showOpenDialog(null);
                label.setText("File Imported.");
                if (lightTable.getPcs().size() == 1 && lightTable.getMode() == ViewMode.PHOTO) {
                    //lightTable.getCurrPhoto().getThumbnail().setSelected();
                    changeMode(ViewMode.PHOTO, ViewMode.SPLIT);
                    splitPhotoPanel.remove(lightTable.getCurrPhoto());
                    splitPhotoPanel.revalidate();
                }
                lightTable.addPhoto(importNew.getSelectedFile());
                tagPanel.removeAll();
                family.setSelected(false);
                work.setSelected(false);
                vacation.setSelected(false);
                school.setSelected(false);
                tagPanel.revalidate();

                /* Photo View */
                if (lightTable.getMode() == ViewMode.PHOTO) {
                    photoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                    //photoPanel.addMouseListener(lightTable.getCurrPhoto());
                    photoPanel.revalidate();
                    //photoPanel.repaint();
                } else if (lightTable.getMode() == ViewMode.SPLIT){
                        /* Split View */
                    splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                    splitPhotoPanel.revalidate();
                    //lightTable.getCurrPhoto().getThumbnail().setSelected();
                    splitThumbnail.add(lightTable.getCurrPhoto().getThumbnail());
                    splitThumbnail.add(Box.createRigidArea(new Dimension(10, 200)));
                    splitThumbnail.revalidate();
                    scrollPane.setPreferredSize(splitThumbnail.getSize());
                    scrollPane.revalidate();
                } else {
                    browserPanel.add(lightTable.getCurrPhoto().getThumbnail());
                    browserPanel.revalidate();
                }
                lightTable.revalidate();
                System.out.println("Break");
                lightTable.repaint();
            }
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        *   Adding an Action Listener to delete button to  add   *
        *   text to the status bar along the bottom.             *
        * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        _delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("File Deleted.");
//                if (lightTable.getCurrPhoto() != null) {
//                    PhotoComponent old = lightTable.getCurrPhoto();
//                    if (lightTable.getMode() == ViewMode.PHOTO) {
//                        photoPanel.remove(old);
//                        photoPanel.revalidate();
//                    } else if (lightTable.getMode() == ViewMode.SPLIT) {
//                        splitPhotoPanel.remove(old);
//                        splitThumbnail.remove(old.getThumbnail());
//                        splitThumbnail.revalidate();
//                        splitThumbnail.repaint();
//                        splitPhotoPanel.revalidate();
//                    } else {
//                        browserPanel.remove(old.getThumbnail());
//                        browserPanel.revalidate();
//                    }
//                    lightTable.getPcs().remove(old);
//                    lightTable.setCurrPhoto(lightTable.getPcs().get(0));
//                    updateTags();
//                    if (lightTable.getMode() == ViewMode.PHOTO) {
//                        photoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
//                        photoPanel.revalidate();
//                    } else if (lightTable.getMode() == ViewMode.SPLIT) {
//                        splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
//                        splitPhotoPanel.revalidate();
//                    }
//                    lightTable.revalidate();
//                }
                delete();

            }
        });

        /* * * * * * * * * * * * * * * * * * * * * * * * * * *
        *   Adding an Action Listener to exit button to give *
        *   users another way to exit program.               *
        * * * * * * * * * * * * * * * * * * * * * * * * * *  */
        _exit.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });



        /* Adding All Buttons to the Side Tool Bar */
        toolMenu.add(drawing);
        toolMenu.add(text);
        toolMenu.add(forward);
        toolMenu.add(back);
        toolMenu.add(new JLabel("    Tags    "));
        toolMenu.add(new JLabel("------------"));
        toolMenu.add(family);
        toolMenu.add(vacation);
        toolMenu.add(school);
        toolMenu.add(work);

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (lightTable.isMagnet()) {
                    if (!lightTable.getMagnetLabel().equals("")) {
                        System.out.println("JLabel being created");
                        JLabel tmp = new JLabel(lightTable.getMagnetLabel());
                        tmp.setSize(new Dimension(50, 20));
                        tmp.setLocation(e.getX(), e.getY());
                        tmp.setOpaque(true);
                        tmp.setBackground(Color.PINK);
                        lightTable.addMagnet(tmp);
                        layeredPane.add(tmp, (Integer) 1);
                        layeredPane.revalidate();
                        if (lightTable.getMagnetLabel().equals("Family")) {
                            magnetize(Tags.FAMILY);
                        } else if (lightTable.getMagnetLabel().equals("Work")) {
                            magnetize(Tags.WORK);
                        } else if (lightTable.getMagnetLabel().equals("School")) {
                            magnetize(Tags.SCHOOL);
                        } else if (lightTable.getMagnetLabel().equals("Vacation")) {
                            magnetize(Tags.VACATION);
                        }
                        lightTable.setMagnetLabel("");
                    }
                    lightTable.revalidate();
                }
            }
        });

        jframe.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (lightTable != null) {
                    if (lightTable.getCurrPhoto() != null) {
                        if (lightTable.getCurrPhoto().getCurrentTextbox() != null) {
                            lightTable.getCurrPhoto().getCurrentTextbox().requestFocusInWindow();
                        }
                    }
                }

            }
        });

        File dir = new File ("images/");
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File f: files) {
                lightTable.addPhoto(f);
                //splitThumbnail.add(lightTable.getCurrPhoto().getThumbnail());
            }
            lightTable.setMode(ViewMode.SPLIT);
            //splitPhotoPanel.add(lightTable.getCurrPhoto());
            changeMode(ViewMode.PHOTO, ViewMode.SPLIT);
        }

        timer = new Timer(TENTH_OF_A_SECOND, new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (numIterations >= 20) {
                    timer.stop();
                    numIterations = 0;
                } else {
                    if (lightTable.isMagnet()) {
                        timer.restart();
                        numIterations++;
                        for (Tags tag: magnetComponents.keySet()) {
                            magnetize(tag);
                        }
                    }
                }
            }
        });
        timer.start();


        /* Adding MenuBar, Side Tool Bar, JPanel, and Status Bar to JFrame */
        jframe.setJMenuBar(menuBar);
        jframe.getContentPane().add(label, BorderLayout.SOUTH);
        jframe.getContentPane().add(toolMenu, BorderLayout.WEST);
        jframe.getContentPane().add(lightTable, BorderLayout.CENTER);

        jframe.pack();
        jframe.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    static void changeMode(ViewMode prev, ViewMode next) {
        lightTable.setMode(next);
        if (prev == ViewMode.PHOTO) {
            lightTable.remove(photoPanel);
            photoPanel.remove(lightTable.getCurrPhoto());
            photoPanel.remove(tagPanel);
            tagPanel.removeAll();
            photoPanel.revalidate();
            lightTable.revalidate();
            lightTable.repaint();
            photo.setSelected(false);
            view.revalidate();
        } else if (prev == ViewMode.SPLIT) {
            splitPhotoPanel.remove(lightTable.getCurrPhoto());
            splitPhotoPanel.remove(tagPanel);
            tagPanel.removeAll();
            for (PhotoComponent pc: lightTable.getPcs()) {
                splitThumbnail.remove(pc.getThumbnail());
            }
            lightTable.remove(splitThumbnail);
            lightTable.remove(splitPhotoPanel);
            lightTable.revalidate();
            lightTable.repaint();
            split.setSelected(false);
            view.revalidate();
        } else {
            setMagnetMode(false);
            for (PhotoComponent pc: lightTable.getPcs()) {
                browserPanel.remove(pc.getThumbnail());
            }
            lightTable.remove(browserPanel);
            lightTable.revalidate();
            lightTable.repaint();
            grid.setSelected(false);
            view.revalidate();
        }
        if (next == ViewMode.PHOTO) {
            lightTable.add(photoPanel, BorderLayout.CENTER);
            photoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
            photoPanel.add(tagPanel, BorderLayout.NORTH);
            photoPanel.revalidate();
            photoPanel.repaint();
            lightTable.revalidate();
            lightTable.repaint();
            photo.setSelected(true);
            view.revalidate();
        } else if (next == ViewMode.SPLIT) {
            lightTable.add(splitPhotoPanel, BorderLayout.CENTER);
            lightTable.add(splitThumbnail, BorderLayout.SOUTH);
            splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
            splitPhotoPanel.add(tagPanel, BorderLayout.NORTH);
            for (PhotoComponent pc : lightTable.getPcs()) {
                splitThumbnail.add(pc.getThumbnail());
            }
            lightTable.revalidate();
            lightTable.repaint();
            split.setSelected(true);
            view.revalidate();
        } else {
            lightTable.add(browserPanel);
            for (PhotoComponent pc: lightTable.getPcs()) {
                browserPanel.add(pc.getThumbnail());
            }
            lightTable.revalidate();
            lightTable.repaint();
            grid.setSelected(true);
            view.revalidate();
        }
    }

    static void updateTags() {
        tagPanel.removeAll();
        if (lightTable.getCurrPhoto().getTags().contains(Tags.FAMILY)) {
            tagPanel.add(familyLabel);
            tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            family.setSelected(true);

        } else {
            if (family.isSelected()) {
                family.setSelected(false);
            }
        }
        if (lightTable.getCurrPhoto().getTags().contains(Tags.SCHOOL)) {
            tagPanel.add(schoolLabel);
            tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            school.setSelected(true);

        } else {
            if (school.isSelected()) {
                school.setSelected(false);
            }
        }
        if (lightTable.getCurrPhoto().getTags().contains(Tags.WORK)) {
            tagPanel.add(workLabel);
            tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            work.setSelected(true);

        } else {
            if (work.isSelected()) {
                work.setSelected(false);
            }
        }
        if (lightTable.getCurrPhoto().getTags().contains(Tags.VACATION)) {
            tagPanel.add(vacationLabel);
            tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            vacation.setSelected(true);

        } else {
            if (vacation.isSelected()) {
                vacation.setSelected(false);
            }
        }
        tagPanel.revalidate();
    }

    static void delete() {
        if (lightTable.getCurrPhoto() != null) {
            PhotoComponent old = lightTable.getCurrPhoto();
            if (lightTable.getMode() == ViewMode.PHOTO) {
                photoPanel.remove(old);
                photoPanel.revalidate();
            } else if (lightTable.getMode() == ViewMode.SPLIT) {
                splitPhotoPanel.remove(old);
                splitThumbnail.remove(old.getThumbnail());
                splitThumbnail.revalidate();
                splitThumbnail.repaint();
                splitPhotoPanel.revalidate();
            } else {
                browserPanel.remove(old.getThumbnail());
                browserPanel.revalidate();
            }
            lightTable.getPcs().remove(old);
            lightTable.setCurrPhoto(lightTable.getPcs().get(0));
            updateTags();
            if (lightTable.getMode() == ViewMode.PHOTO) {
                photoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                photoPanel.revalidate();
            } else if (lightTable.getMode() == ViewMode.SPLIT) {
                splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                splitPhotoPanel.revalidate();
            }
            lightTable.revalidate();
        }
    }

    static void forward() {
        //label.setText("Next Page");
        int ind = lightTable.getPcs().indexOf(lightTable.getCurrPhoto());
        if (ind < (lightTable.getPcs().size()- 1)) {
            if (lightTable.getMode() == ViewMode.PHOTO) {
                photoPanel.remove(lightTable.getCurrPhoto());
                photoPanel.revalidate();
                lightTable.setCurrPhoto(lightTable.getPcs().get(ind + 1));
                photoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                updateTags();
                photoPanel.revalidate();
            } else if (lightTable.getMode() == ViewMode.SPLIT) {
                splitPhotoPanel.remove(lightTable.getCurrPhoto());
                splitPhotoPanel.revalidate();
                lightTable.setCurrPhoto(lightTable.getPcs().get(ind + 1));
                splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                updateTags();
                tagPanel.revalidate();
                splitPhotoPanel.revalidate();
                splitThumbnail.revalidate();
            } else {
                lightTable.setCurrPhoto(lightTable.getPcs().get(ind + 1));
                browserPanel.revalidate();
            }
        }
        lightTable.revalidate();
    }

    static void backward() {
        int ind = lightTable.getPcs().indexOf(lightTable.getCurrPhoto());
        if (ind > 0) {
            if (lightTable.getMode() == ViewMode.PHOTO) {
                photoPanel.remove(lightTable.getCurrPhoto());
                photoPanel.revalidate();
                lightTable.setCurrPhoto(lightTable.getPcs().get(ind - 1));
                updateTags();
                photoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                photoPanel.revalidate();
            } else if (lightTable.getMode() == ViewMode.SPLIT) {
                splitPhotoPanel.remove(lightTable.getCurrPhoto());
                splitPhotoPanel.revalidate();
                lightTable.setCurrPhoto(lightTable.getPcs().get(ind - 1));
                updateTags();
                splitPhotoPanel.add(lightTable.getCurrPhoto(), BorderLayout.CENTER);
                splitPhotoPanel.revalidate();
                splitThumbnail.revalidate();
            } else {
                lightTable.setCurrPhoto(lightTable.getPcs().get(ind - 1));
                browserPanel.revalidate();
            }
        }
        lightTable.revalidate();
    }

    static void tagSchool() {
        if (!school.isSelected()) {
            lightTable.getCurrPhoto().addTag(Tags.SCHOOL);
            tagPanel.add(schoolLabel);
            tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            school.setSelected(true);
        } else {
            lightTable.getCurrPhoto().removeTag(Tags.SCHOOL);
            tagPanel.remove(schoolLabel);
            school.setSelected(false);
        }
        tagPanel.revalidate();
        lightTable.revalidate();
    }

    static void tagWork() {
        if (!work.isSelected()) {
            lightTable.getCurrPhoto().addTag(Tags.WORK);
            tagPanel.add(workLabel);
            tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            work.setSelected(true);
        } else {
            lightTable.getCurrPhoto().removeTag(Tags.WORK);
            tagPanel.remove(workLabel);
            work.setSelected(false);
        }
        tagPanel.revalidate();
        lightTable.revalidate();
    }

    static void tagFamily() {
        if (!family.isSelected()) {
            lightTable.getCurrPhoto().addTag(Tags.FAMILY);
            tagPanel.add(familyLabel);
            tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            family.setSelected(true);
        } else {
            lightTable.getCurrPhoto().removeTag(Tags.FAMILY);
            tagPanel.remove(familyLabel);
            family.setSelected(false);
        }
        tagPanel.revalidate();
        lightTable.revalidate();
    }

    static void tagVacation() {
        System.out.println("calling tagVacation");
        if (!vacation.isSelected()) {
            lightTable.getCurrPhoto().addTag(Tags.VACATION);
            tagPanel.add(vacationLabel);
            tagPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            vacation.setSelected(true);
        } else {
            lightTable.getCurrPhoto().removeTag(Tags.VACATION);
            tagPanel.remove(vacationLabel);
            vacation.setSelected(false);
        }
        tagPanel.revalidate();
        lightTable.revalidate();
    }

    static void setMagnetMode(boolean magnetMode) {
        System.out.println("Magnet Mode: " + magnetMode);
        lightTable.setMagnet(magnetMode);
        if (lightTable.isMagnet()) {
            lightTable.setLayout(null);
            lightTable.requestFocus();
            lightTable.remove(browserPanel);
            for (PhotoComponent pc: lightTable.getPcs()) {
                layeredPane.add(pc.getThumbnail(), (Integer) 0 );
            }
            layeredPane.revalidate();
            lightTable.revalidate();
            lightTable.repaint();
            toolMenu.remove(family);
//            toolMenu.remove(familyLabel);
            toolMenu.remove(work);
//            toolMenu.remove(workLabel);
            toolMenu.remove(vacation);
//            toolMenu.remove(vacationLabel);
            toolMenu.remove(school);
//            toolMenu.remove(schoolLabel);

            toolMenu.add(familyMagnet);
            toolMenu.add(workMagnet);
            toolMenu.add(schoolMagnet);
            toolMenu.add(vacationMagnet);
        } else {
            lightTable.setLayout(new BorderLayout());
            lightTable.removeAllMagnets();
            layeredPane.removeAll();
            for (PhotoComponent pc: lightTable.getPcs()) {
                browserPanel.add(pc.getThumbnail());
            }
            lightTable.remove(layeredPane);
            lightTable.add(browserPanel);
            toolMenu.add(family);
//            toolMenu.add(familyLabel);
            toolMenu.add(work);
//            toolMenu.add(workLabel);
            toolMenu.add(vacation);
//            toolMenu.add(vacationLabel);
            toolMenu.add(school);
//            toolMenu.add(schoolLabel);

            toolMenu.remove(familyMagnet);
            toolMenu.remove(workMagnet);
            toolMenu.remove(schoolMagnet);
            toolMenu.remove(vacationMagnet);
        }
        layeredPane.revalidate();
        toolMenu.revalidate();
        lightTable.revalidate();
    }

    public static void magnetize(Tags tag) {
        System.out.println("Magnetize being called: " + tag);
        ArrayList<ThumbnailComponent> thumbnails = lightTable.taggedThumbnails(tag);
        JLabel magnet = lightTable.getMagnet(tag);
        if (magnet != null) {
            for (ThumbnailComponent tc : thumbnails) {
                tc.setX(tc.getLocation().x);
                tc.setY(tc.getLocation().y);
                if (tc.getPc().getTags().size() == 1) {
                    tc.setDestination(magnet.getLocation());
                } else if (tc.getPc().getTags().size() > 1) {
                    int xadd = 0;
                    int yadd = 0;
                    int tagCount = 0;
                    for (Tags t: tc.getPc().getTags()) {
                        if (magnetComponents.keySet().contains(t)) {
                            xadd += lightTable.getMagnet(t).getLocation().x;
                            yadd += lightTable.getMagnet(t).getLocation().y;
                            tagCount++;
                        }
                    }
                    if (tagCount > 0) {
                        tc.setDestination(new Point(xadd/tagCount, yadd/tagCount));
                    }
                }
            }
            magnetComponents.put(tag, thumbnails);
            System.out.println("Timer");
            for (Tags key: magnetComponents.keySet()) {
                ArrayList<ThumbnailComponent> comp = magnetComponents.get(key);
                for (ThumbnailComponent tc: comp) {
                    System.out.println("x: " + tc.getX() +", y: "+ tc.getY() + ", dest: " + tc.getDestination().x +", "+tc.getDestination().y);
                    tc.setLocation(tc.getLocation().x + (tc.getDestination().x - tc.getX())/5, tc.getLocation().y + (tc.getDestination().y - tc.getY())/5);
                    if (numIterations >= 5) {
                        tc.setX(tc.getLocation().x);
                        tc.setY(tc.getLocation().y);
                    }
                }

            }
        }

        lightTable.revalidate();
    }
}