import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.event.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
//import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.regex.*;

/**
 * Created by elizabethdudley on 10/6/16.
 */
public class PhotoComponent extends JComponent implements MouseListener{

    private BufferedImage image;
    private boolean flipped;
    private String mode;
//    private Graphics2D g2d;
//    private Image img;
    private ArrayList<TextRegion> textboxes = new ArrayList<>();
    private int lastX;
    private int lastY;
    private int a;
    private int picX;
    private int picY;
    private String drawMode;
    private LightTable lt;
    private ArrayList<Tags> tags = new ArrayList<>();
    private ArrayList<Point> gestureCircle = new ArrayList<>(6);
    private int radius = 0;
    private ArrayList<Point> gesturePoints = new ArrayList<>();
    private ArrayList<Point> currLine;
    private ArrayList<ArrayList<Point>> selectedLines = new ArrayList<>();
    private ArrayList<TextRegion> selectedTextBoxes = new ArrayList<>();
    private boolean selection = false;
    private TextRegion curr;

//    private int width;
//    private int height;
    private ThumbnailComponent tc;
    private String dirPattern = "";
    private ArrayList<ArrayList<Point>> lines = new ArrayList<>();
    private final static String NEXT = "^.{0,2}+[ECS]+[WDS]+.{0,2}+$";
    private final static String BACK = "^.{0,2}+[WDS]+[ECS]+.{0,2}+$";
    private final static String DELETE = "^.{0,2}+[DSC]+[AWD]+[ANB]+[BEC]+[CSD]+.{0,2}+$";
    private final static String VACATION = "^.{0,2}+[SCE]+[NBE]+.{0,2}+$";
    private final static String WORK = "^.{0,2}+[SCE]+[NBE]+[SCE]+[NBE]+.{0,2}+$";
    private final static String SCHOOL = "^.{0,2}+[AWD]+[DSC]+[ECS]+[DSC]+[AWD]+.{0,2}+$";
    private final static String FAMILY = "^.{0,2}+[NBE]+[ECS]+.{0,2}+$";
    private final static String SELECTION = "^.{0,2}+[AWD]+[WDS]+[DSC]+[SCE]+[CEB]+[EBN]+[BNA]+[NAW]+[AWD]+.{0,2}+$";

    public PhotoComponent(File file) {
        FileInputStream reader = null;
        FileOutputStream writer = null;
        try {
            image = ImageIO.read(file);
            File dir = new File("images/");
            if (!dir.exists()) {
                boolean n = new File("images/").mkdir();
            }
            File out = new File("images/" + file.getName());
            if (!out.exists()) {
                reader = new FileInputStream(file);
                writer = new FileOutputStream(out);
                byte[] bytes = new byte[1024];
                int a;
                while ((a=reader.read(bytes)) > 0) {
                    writer.write(bytes, 0, a);
                }
                reader.close();
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        setDoubleBuffered(false);
        setName("Photo Component");
        tc = new ThumbnailComponent(this);
        flipped = false;
        addMouseListener(this);
//        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
//        width = image.getWidth();
//        height = image.getHeight();
        setPreferredSize(new Dimension(800, 600));
        mode = "drawing";
        drawMode = "pen";
        repaint();
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && isFlipped() && !selection && mode.equals("drawing") && drawMode.equals("pen")) {
//                    g2d.drawLine(lastX - picX, lastY-picY, e.getX()-picX, e.getY()-picY);
////                    lastX = e.getX();
////                    lastY = e.getY();
                    currLine.add(e.getPoint());
                    lastX = e.getX();
                    lastY = e.getY();
//                    repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    gesturePoints.add(e.getPoint());
                    if (lastX < e.getX()) {
                        if (lastY > e.getY()) {
                            dirPattern += "B";
                        } else if (lastY < e.getY()) {
                            dirPattern += "C";
                        } else {
                            dirPattern += "E";
                        }
                    } else if (lastX > e.getX()){
                        if (lastY > e.getY()) {
                            dirPattern += "A";
                        } else if (lastY < e.getY()) {
                            dirPattern += "D";
                        } else {
                            dirPattern += "W";
                        }
                    } else {
                        if (lastY < e.getY()) {
                            dirPattern += "S";
                        } else if (lastY > e.getY()) {
                            dirPattern += "N";
                        }
                    }
//                    System.out.println(dirPattern);
                    lastX = e.getX();
                    lastY = e.getY();
                }
                if (isFlipped() && SwingUtilities.isRightMouseButton(e) && !selection) {
                    if (e.getY() < gestureCircle.get(0).y) {
                        gestureCircle.set(0, e.getPoint());
                    }
                    if (e.getX() < gestureCircle.get(1).x) {
                        gestureCircle.set(1, e.getPoint());
                    }
                    if (e.getY() > gestureCircle.get(2).y) {
                        gestureCircle.set(2, e.getPoint());
                    }
                    if (e.getX() > gestureCircle.get(3).x) {
                        gestureCircle.set(3, e.getPoint());
                    }
                } else if (isFlipped() && SwingUtilities.isLeftMouseButton((e)) && selection) {
                    int distanceX = e.getX() - lastX;
                    int distanceY = e.getY() - lastY;
                    for (ArrayList<Point> line :selectedLines) {
                        Point old;
                        for (int i=0; i<line.size(); i++) {
                            old = line.get(i);
                            line.set(i, new Point(old.x + distanceX, old.y + distanceY));
                        }
                    }

                    for (TextRegion t : selectedTextBoxes) {
                        t.setX(t.getX() + distanceX);
                        t.setY(t.getY() + distanceY);
                    }
                    lastX = e.getX();
                    lastY = e.getY();
                }
                repaint();
            }
        });
    }

    public void paintComponent(Graphics g) {
//        System.out.println("PC being drawn");
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g.setColor(new Color(200, 200, 100));
        picX = (getPreferredSize().width/2) - (image.getWidth()/2);
        picY = (getPreferredSize().height/2) - (image.getHeight()/2);
       // g.fillRect(picX, picY, getWidth(), getHeight());
        g2d.setColor(new Color(226, 226, 226));
        g2d.drawRect(picX- 5, picY -5, image.getWidth() +10 , image.getHeight()+10);
        if (!flipped) {
            g2d.drawImage(image, picX, picY, null);

        } else {
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(picX, picY, image.getWidth(), image.getHeight());
            g2d.setColor(Color.BLACK);
            if (lines.size() > 0) {
                for (ArrayList<Point> line : lines) {
                    Point prev = line.get(0);
                    Point curr;
                    for (int j = 1; j < line.size(); j++) {
                        curr = line.get(j);
                        if (prev.x > picX && prev.x < (picX +image.getWidth()) && curr.x > picX && curr.x < (picX +image.getWidth())) {
                            if (prev.y >picY && prev.y < (picY+image.getHeight()) && curr.y >picY && curr.y < (picY+image.getHeight())) {
                                g2d.drawLine(prev.x, prev.y, curr.x, curr.y);
                            }
                        }
                        prev = curr;
                    }
                }
            }
//            g.drawImage(img, picX, picY, null);
//            System.out.println("Fixing to draw textboxes");
//            TextRegion r;
//            if (textboxes) {
                for (TextRegion r: textboxes) {
                    r.paintComponent(g2d);
                }
//            }
            if (selection) {
                g2d.setColor(Color.MAGENTA);
                for (ArrayList<Point> line1 : selectedLines) {
                    Point prev = line1.get(0);
                    Point curr;
                    for (int j = 1; j < line1.size(); j++) {
                        curr = line1.get(j);
                        if (prev.x > picX && prev.x < (picX +image.getWidth()) && curr.x > picX && curr.x < (picX +image.getWidth())) {
                            if (prev.y >picY && prev.y < (picY+image.getHeight()) && curr.y >picY && curr.y < (picY+image.getHeight())) {
                                g2d.drawLine(prev.x, prev.y, curr.x, curr.y);
                            }
                        }
                        prev = curr;
                    }
                }
                for (TextRegion t : selectedTextBoxes) {
                    t.paintComponent(g);
                }

                g2d.setColor(new Color(226, 226, 226));
            }
        }


        if (gesturePoints.size() > 0) {
            g2d.setColor(Color.ORANGE);
            Point prev = gesturePoints.get(0);
            for (int i = 1; i< gesturePoints.size(); i++) {
                g2d.drawLine(prev.x, prev.y, gesturePoints.get(i).x, gesturePoints.get(i).y);
                prev = gesturePoints.get(i);
            }
            g2d.setColor(new Color(226, 226, 226));
        }

    }

    public boolean isFlipped() {
        return flipped;
    }

    public void setFlipped() {
        if (flipped) {
            flipped = false;
            System.out.println("Image is not flipped");
        } else {
            flipped = true;
            System.out.println("Image is flipped");
        }
    }

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && isFlipped() && mode == "drawing" && !selection) {
            currLine = new ArrayList<>();
            currLine.add(e.getPoint());
            lines.add(currLine);
        } else if (SwingUtilities.isRightMouseButton(e) && isFlipped() && !selection) {
            if (gestureCircle.size() == 0) {
                gestureCircle.add(e.getPoint());
                gestureCircle.add(e.getPoint());
                gestureCircle.add(e.getPoint());
                gestureCircle.add(e.getPoint());
                gestureCircle.add(e.getPoint());
                gestureCircle.add(e.getPoint());
            }
        }
        lastX = e.getX();
        lastY = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && isFlipped() && mode.equals("text") && !selection) {
            if (textboxes.size() == 0 ) {
                setFocusable(true);
                a = -1;
            }
            TextRegion tmp;
            if (a > -1) {
//                TextRegion old = textboxes.get(a);
                removeKeyListener(curr);
            }
            if (lastX > picX && lastX < (picX +image.getWidth()) && e.getX() > picX && e.getX() < (picX +image.getWidth())) {
                if (lastX > picY && lastX < (picY + image.getHeight()) && e.getY() > picY && e.getY() < (picY + image.getHeight())) {
                    if (lastX > e.getX()) {
                        if (lastY > e.getY()) {
                            tmp = new TextRegion(e.getX(), e.getY(), (lastX - e.getX()), lastY - e.getY(), this);

                        } else {
                            tmp = new TextRegion(e.getX(), lastY, (lastX - e.getX()), e.getY() - lastY, this);
                        }
                    } else {
                        if (lastY > e.getY()) {
                            tmp = new TextRegion(lastX, e.getY(), (e.getX() - lastX), lastY - e.getY(), this);
                        } else {
                            tmp = new TextRegion(lastX, lastY, (e.getX() - lastX), e.getY() - lastY, this);
                        }
                    }
                    System.out.println("TextBox being added");
                    tmp.requestFocusInWindow();
                    a++;
                    textboxes.add(tmp);
                    curr =tmp;
                    addKeyListener(tmp);
                }
            }
            repaint();
        } else if (SwingUtilities.isLeftMouseButton(e) && isFlipped() && mode == "drawing" && !selection) {
            currLine = null;
            repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (isFlipped() && !selection) {
                System.out.println(selection);
                if (e.getPoint().distance(gestureCircle.get(5).getX(), gestureCircle.get(5).getY()) <= 5) {
//                    selection = true;
                    System.out.println("recognizes it as selection");
                    int xcenter = (gestureCircle.get(1).x + gestureCircle.get(3).x)/2;
                    int ycenter = (gestureCircle.get(0).y + gestureCircle.get(2).y)/2;
                    gestureCircle.set(4, new Point(xcenter, ycenter));
                    radius = (gestureCircle.get(3).x - xcenter);
                } else {
                    dirPattern = "";
                    gesturePoints.clear();
                    gestureCircle.clear();
                }
            }
            if (!dirPattern.equals("")) {
                System.out.println("Calling gesture");
                gesture(dirPattern);
                dirPattern = "";
                gesturePoints.clear();
                gestureCircle.clear();
            }
            System.out.println(selection);
//            repaint();
        } else if (SwingUtilities.isLeftMouseButton(e) && selection) {
            selection = false;
            for (TextRegion t: selectedTextBoxes) {
                System.out.println("Textbox: (" + t.getX() + ", " + t.getY() +")");
                textboxes.add(t);
                t.setSelected(false);
            }
            selectedTextBoxes.clear();
            for (ArrayList<Point> line: selectedLines) {
                lines.add(line);
            }
            selectedLines.clear();
        }
//        System.out.println(selection);
        repaint();
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            selection = false;
            for (ArrayList<Point> line: selectedLines) {
                lines.add(line);
            }
            selectedLines.clear();
            for (TextRegion t: selectedTextBoxes) {
                textboxes.add(t);
                t.setSelected(false);
            }
            selectedTextBoxes.clear();
            setFlipped();
            requestFocusInWindow();
            repaint();
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() { return mode;}

    public TextRegion getCurrentTextbox() {
        return textboxes.get(a);
    }

    public void setDrawMode(String s) {
        drawMode = s;
    }

//    @Override
//    public int getHeight() {
//        return height;
//    }
//
//
//    @Override
//    public int getWidth() {
//        return width;
//    }

    public Image getImage() {
        return image;
    }

    public ThumbnailComponent getThumbnail() {
        return tc;
    }

    public void setLightTable(LightTable lt) {
        this.lt = lt;
    }

    public LightTable getLightTable() {
        return lt;
    }

    public void addTag(Tags tag) {
        tags.add(tag);
    }

    public void removeTag(Tags tag) {
        tags.remove(tag);
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public void gesture(String pattern) {
//        System.out.println("gesture being called");
        Pattern next = Pattern.compile(NEXT);
        Pattern back = Pattern.compile(BACK);
        Pattern delete = Pattern.compile(DELETE);
        Pattern vacation = Pattern.compile(VACATION);
        Pattern work = Pattern.compile(WORK);
        Pattern family = Pattern.compile(FAMILY);
        Pattern school = Pattern.compile(SCHOOL);
        Pattern selection = Pattern.compile(SELECTION);
        if (next.matcher(pattern).matches()) {
//            System.out.println("matching");
            Album.forward();
        } else if (back.matcher(pattern).matches()) {
//            System.out.println("matching");
            Album.backward();
        } else if (delete.matcher(pattern).matches()) {
            if (this.selection) {
//                System.out.println("Delete called");
                selectedTextBoxes.clear();
                selectedLines.clear();
                this.selection = false;
                repaint();
            } else {
                Album.delete();
            }
        } else if (vacation.matcher(pattern).matches()) {
            Album.tagVacation();
        } else if (work.matcher(pattern).matches()) {
            Album.tagWork();
        } else if (family.matcher(pattern).matches()) {
            Album.tagFamily();
        } else if (school.matcher(pattern).matches()) {
            Album.tagSchool();
        } else if (selection.matcher(pattern).matches()) {
            System.out.println("Matched to selection");
            if (isFlipped()) {
                this.selection = true;
                if (!lines.isEmpty()) {
                    for (ArrayList<Point> line: lines) {
                        boolean selected = true;
                        for (Point p : line) {
                            if ((Math.pow((p.x - gestureCircle.get(4).x),2) + Math.pow((p.getY() - gestureCircle.get(4).getY()), 2)) > Math.pow(radius, 2)) {
                                selected = false;
                            }
                        }
                        if (selected) {
                            selectedLines.add(line);
                        }
                    }
                    for (ArrayList<Point> line : selectedLines) {
                        lines.remove(line);
                    }
                    System.out.println("Checked lines");
                }
                if (textboxes.size() != 0) {
                    for (TextRegion t : textboxes) {
                        boolean selected = true;
                        Point topLeft = new Point(t.getX(), t.getY());
                        Point bottomLeft = new Point(t.getX(), t.getY() + t.getHeight());
                        Point topRight = new Point(t.getX() + t.getWidth(), t.getY());
                        Point bottomRight = new Point(t.getX() + t.getWidth(), t.getY() + t.getWidth());
                        if ((Math.pow((topLeft.x - gestureCircle.get(4).x),2) + Math.pow((topLeft.getY() - gestureCircle.get(4).getY()), 2)) > Math.pow(radius, 2)) {
                            selected = false;
                        }
                        if ((Math.pow((bottomLeft.x - gestureCircle.get(4).x),2) + Math.pow((bottomLeft.getY() - gestureCircle.get(4).getY()), 2)) > Math.pow(radius, 2)) {
                            selected = false;
                        }
                        if ((Math.pow((topRight.x - gestureCircle.get(4).x),2) + Math.pow((topRight.getY() - gestureCircle.get(4).getY()), 2)) > Math.pow(radius, 2)) {
                            selected = false;
                        }
                        if ((Math.pow((bottomRight.x - gestureCircle.get(4).x),2) + Math.pow((bottomRight.getY() - gestureCircle.get(4).getY()), 2)) > Math.pow(radius, 2)) {
                            selected = false;
                        }
                        if (selected) {
                            selectedTextBoxes.add(t);
                            t.setSelected(true);
                        }

                    }
                    for (TextRegion t: selectedTextBoxes) {
                        textboxes.remove(t);
                    }
//                    if(selectedTextBoxes.size() == 1) {
//                        removeKeyListener(curr);
//                        curr = selectedTextBoxes.get(0);
//                        addKeyListener(selectedTextBoxes.get(0));
//
//                    }
                }
                System.out.println(this.selection);
             }
            repaint();
        }
    }
}

