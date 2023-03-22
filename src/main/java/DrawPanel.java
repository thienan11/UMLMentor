import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * CSC 309 UML Tutor
 * Handles all the drawing for the program
 *
 * @author Alex Arrieta
 * @author Chloe Anbarcioglu
 * @version 1.1
 */
public class DrawPanel extends JPanel implements Observer {
    private PanelClickHandler clickListener;
    private final int defaultHeight = 50;
    private int newMethodsHeight;
    private int newGlobalsHeight;

    /**
     * DrawPanel constructor
     */
    public DrawPanel(){
        setBorder(BorderFactory.createLineBorder(Color.black));
        clickListener = new PanelClickHandler();
        addMouseListener(clickListener);
        addMouseMotionListener(clickListener);
        setBackground(Color.WHITE); //Let user decide, make this a parameter
    }

    /**
     * Paints the main screen with all the classes and their information
     * @param g : Graphics
     */
    public void paintComponent(Graphics g){
        HashMap<String, UserClass> mapClasses = clickListener.canEdit() ? Blackboard.getInstance().getClasses() : Blackboard.getInstance().getProblemSolution();
        Collection<UserClass> classes = mapClasses.values();
        Color backgroundColor = new Color(203, 195, 227);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, 10000, 10000);
        for(UserClass c : classes){
            g.setColor(Color.BLACK);
            drawConnections(g, c);
            drawClass(g, c, c.getHeight());
            g.setColor(Color.BLACK);
            drawSubBoxes(g, c);
        }
    }

    /**
     * Takes in a class and draws it according to its information
     * @param g The graphics component it needs to paint on
     * @param c The class to draw
     * @param height The height of the class
     */
    public void drawClass(Graphics g, UserClass c, int height){
        Color classColor = new Color(253, 253, 150);
        g.setColor(classColor);
        g.fillRect((int) c.getPoint().getX(),(int) c.getPoint().getY(), c.getWidth(), height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Visible", Font.PLAIN, 20));
        if (c.getName().length() == 1 || c.getName().length() == 0) {
            g.drawString(c.getName(),(int) c.getPoint().getX() + 45, (int) c.getPoint().getY() + 20);
        } else {
            g.drawString(c.getName(),(int) c.getPoint().getX() + (c.getWidth()/c.getName().length()), (int) c.getPoint().getY() + 20);
        }
    }

    /**
     * Takes in a class and draws its connections
     * @param g The graphic to paint on
     * @param c The class whose connections need to be drawn
     */
    public void drawConnections(Graphics g, UserClass c){
        HashMap<UserClass, ArrayList<Point>> map = c.getConnections();
        int offset = 0;
        for (UserClass connected : map.keySet()) {
            for (Point p : map.get(connected)) {
                int x1 = searchClosest(c.getPoint().getX(), c.getWidth(), connected.getPoint().getX());
                int y1 = searchClosest(c.getPoint().getY(), c.getHeight(), connected.getPoint().getY());
                int x2 = searchClosest(connected.getPoint().getX(), connected.getWidth(), x1);
                int y2 = searchClosest(connected.getPoint().getY(), connected.getHeight(), y1);
                if (p.getX() == 0) {
                    g.drawLine(x1 + offset, y1, x2 + offset, y2);
                } else {
                    Graphics2D g2d = (Graphics2D) g.create();
                    Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                            0, new float[]{9}, 0);
                    g2d.setStroke(dashed);
                    g2d.drawLine(x1 + offset, y1, x2 + offset, y2);
                    g2d.dispose();
                }
                drawArrowHead(g, x1 + offset, y1, x2 + offset, y2, 20, p.getY());
                offset += 15;
            }
            offset = 0;
        }
    }

    /**
     * Finds closest points between connected class boxes to draw connection line
     * @param c1 The point of the first class
     * @param d1 The height/width of the first class
     * @param c2 The point of the second class
     * @return The closest point between the two as an offset of the first point (along a one dimensional line)
     */
    private int searchClosest(double c1, double d1, double c2){
        double min = c1;
        double minDist = Math.abs(c1 - c2);
        for (double i = c1 + 1; i < c1 + d1; i++){
            if ((Math.abs(i - c2)) < minDist){
                min = i;
                minDist = (Math.abs(i - c2));
            }
            else{
                break;
            }
        }
        return (int) min;
    }

    /**
     * Draws the arrowhead according to the type of connections
     * @param g The graphics of the panel
     * @param x1 X of the first class
     * @param y1 Y of the first class
     * @param x2 X of the first class
     * @param y2 Y of the first class
     * @param size How large to draw the arrow head
     * @param type type of connection
     */
    public static void drawArrowHead(Graphics g, int x1, int y1, int x2, int y2, int size, double type) {
        double dir = Math.atan2(x1 - x2, y1 - y2);
        Polygon p = new Polygon();
        if (type == 0 || type == 3) {
            g.drawLine(x2, y2, x2 + x(size, dir + .5), y2 + y(size, dir + .5));
            g.drawLine(x2, y2, x2 + x(size, dir - .5), y2 + y(size, dir - .5));
        } else if (type == 1 || type == 5) {
            p.addPoint(x2, y2);
            p.addPoint(x2 + x(size, dir + .5), y2 + y(size, dir + .5));
            p.addPoint(x2 + x(size, dir - .5), y2 + y(size, dir - .5));
            p.addPoint(x2, y2);
            g.drawPolygon(p);
        } else if (type == 2 || type == 4) {
            dir = Math.atan2(x2 - x1, y2 - y1);
            p.addPoint(x1, y1);
            p.addPoint(x1 + x(size, dir + .5), y1 + y(size, dir + .5));
            p.addPoint(x1 + (int) (1.8 * x(size, dir)), y1 + (int) (1.8 * y(size, dir)));
            p.addPoint(x1 + x(size, dir - .5), y1 + y(size, dir - .5));
            p.addPoint(x1, y1);
            if(type == 2) {
                g.fillPolygon(p);
            }else{
                g.drawPolygon(p);
            }
        }
    }

    /**
     * Returns what needs to be added to the x coordinate to draw the arrowhead
     * @param len
     * @param dir
     * @return Offset
     */
    public static int x(int len, double dir) {
        return (int) (len * Math.sin(dir));
    }

    /**
     * Returns what needs to be added to the y coordinate to draw the arrowhead
     * @param len
     * @param dir
     * @return Offset
     */
    public static int y(int len, double dir) {
        return (int) (len * Math.cos(dir));
    }

    /**
     * Takes in a class and draws it's subboxes
     * Draws the class global variables and methods
     * @param g Graphic of this panel
     * @param c UserClass to draw
     */
    public void drawSubBoxes(Graphics g, UserClass c){
        int globalsX = (int) (c.getPoint().getX() + 5);
        int globalsY = (int) (c.getPoint().getY() + 30);
        int globalsWidth = c.getWidth() - 10;
        int globalsHeight = defaultHeight - 80;

        int methodsX = (int) (c.getPoint().getX() + 5);
        int methodsY = (int) (c.getPoint().getY() + 70);
        int methodsWidth = c.getWidth() - 10;
        int methodsHeight = defaultHeight - 80;

        newGlobalsHeight = 30 * c.getGlobals().size();
        newMethodsHeight = 30 * c.getMethods().size();

        drawClass(g, c, defaultHeight + newGlobalsHeight + newMethodsHeight);
        c.setHeight(defaultHeight + newGlobalsHeight + newMethodsHeight);

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(globalsX, globalsY, globalsWidth, newGlobalsHeight);

        for (VariableInformation v: c.getGlobals()){
            g.setColor(Color.BLACK);
            g.setFont(new Font("Visible", Font.PLAIN, 16));
            g.drawString(v.getConnectedTo().getName() + " " + v.getName(), globalsX + 10, globalsY + 60 + (int)(globalsHeight - 20/2.0));
            globalsHeight += 30;
            methodsY += 30;
        }

        if (newMethodsHeight > 0) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(methodsX, methodsY - 30, methodsWidth, newMethodsHeight);

            for (String m: c.getMethods()){
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Visible", Font.PLAIN, 16));
                    g.drawString(m + "  ("+ "" +")", methodsX + 5, methodsY + 30 + (int)(methodsHeight - 20/2.0));
                    methodsHeight += 30;
            }
        }
    }

    /**
     * Sets whether the panel can be edited or not
     * @param problem The boolean value or whether the panel can be edited or not
     */
    public void setEditable(boolean b){
        this.clickListener.setCanEdit(b);
    }

    /**
     * Displays the problem information
     * @param problem The problem information as a string
     */
    public void loadProblem(String problem){

    }

    /**
     * Redraws the panel with the new information
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}