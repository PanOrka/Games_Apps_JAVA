import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.util.*;
import java.awt.geom.GeneralPath;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


// KLAWIATURA //////////////////////////////////////////////////////////////////
class MyKeyAdapter extends KeyAdapter {
MyPanel p;

MyKeyAdapter(MyPanel p) {
        this.p = p;
}

@Override
public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                p.isFocused2 = true;
                p.Resize(true);
        }
}

@Override
public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                p.Resize(false);
                p.isPressed2 = false;
        }
}
}
// KONIEC KLAWIATURA ///////////////////////////////////////////////////////////


// MYSZKA //////////////////////////////////////////////////////////////////////
class MyMouseAdapter extends MouseAdapter implements ActionListener {
public int startX, startY, endX, endY;
MyPanel p;
Timer timer;
boolean isPressed;
MyMouseAdapter(MyPanel p) {
        this.p = p;
        this.timer = new Timer(33, this);
        timer.start();
        this.isPressed = false;
}

public void mousePressed(MouseEvent e) {
        this.startX = e.getX();
        this.startY = e.getY();
        p.startX = this.startX;
        p.startY = this.startY;

        this.isPressed = true;
        p.isPressed = true;
}


public void actionPerformed(ActionEvent ev){ // RYSOWANIE 30 KLATEK/s PODCZAS EDYCJI/RYSOWANIA NOWYCH
        if(this.isPressed && (p.buttonName == "Prostokat" || p.buttonName == "Owal") && ev.getSource() == this.timer) {

                p.getMLocation(this);

                if (this.endX <= this.startX && this.endY <= this.startY) p.action(this.endX, this.endY, this.startX, this.startY, false);
                else if (this.endX < this.startX && this.endY > this.startY) p.action(this.endX, this.startY, this.startX, this.endY, false);
                else if (this.endX > this.startX && this.endY < this.startY) p.action(this.startX, this.endY, this.endX, this.startY, false);
                else if (this.endX > this.startX && this.endY > this.startY) p.action(this.startX, this.startY, this.endX, this.endY, false);

        } else if (this.isPressed && p.buttonName == "Edycja" && ev.getSource() == this.timer) {
                p.getMLocationEdit();
        }
}

public void mouseReleased(MouseEvent e) {
        this.endX = e.getX();
        this.endY = e.getY();

        this.isPressed = false;
        p.whichEdited = -1;

        if (this.endX <= this.startX && this.endY <= this.startY) p.action(this.endX, this.endY, this.startX, this.startY, true);
        else if (this.endX < this.startX && this.endY > this.startY) p.action(this.endX, this.startY, this.startX, this.endY, true);
        else if (this.endX > this.startX && this.endY < this.startY) p.action(this.startX, this.endY, this.endX, this.startY, true);
        else if (this.endX > this.startX && this.endY > this.startY) p.action(this.startX, this.startY, this.endX, this.endY, true);

}
}
// KONIEC MYSZKI ///////////////////////////////////////////////////////////////


// LIST_FIGURES ///////////////////////////////////////////////////////////////
class Figures {
int startX, startY, lenght, height;
String type, Color;
Point mCenter;
Figures(int startX, int startY, int lenght, int height, String type, String Color) {
        this.mCenter = new Point(0, 0);

        this.startX = startX;
        this.startY = startY;
        this.lenght = lenght;
        this.height = height;
        this.type = type;
        this.Color = Color;

        this.mCenter.x = this.startX + (this.lenght/2);
        this.mCenter.y = this.startY + (this.height/2);
}
}
// KONIEC LIST_FIGURES /////////////////////////////////////////////////////////


// LIST_POLYGONS ///////////////////////////////////////////////////////////////
class Polygons {
int polyPoints;
int[] polyX, polyY;
String Color;
Point mCenter;
Polygons(int[] polyX, int[] polyY, int polyPoints, String Color) {
        this.mCenter = new Point(0, 0);

        this.polyX = new int[40];
        this.polyY = new int[40];
        for (int i=0; i<polyPoints; i++) {
                this.polyX[i] = polyX[i];
                this.mCenter.x += this.polyX[i];

                this.polyY[i] = polyY[i];
                this.mCenter.y += this.polyY[i];
        }
        this.polyPoints = polyPoints;
        this.Color = Color;

        this.mCenter.x /= this.polyPoints;
        this.mCenter.y /= this.polyPoints;
}
}
// KONIEC LIST_POLYGONS ////////////////////////////////////////////////////////


// PANEL ///////////////////////////////////////////////////////////////////////
class MyPanel extends JPanel {
public int startX, startY, endX, endY, whichEdited, lastEdited, polyPoints, OriginX, OriginY, counter;
int[] polyX, polyY, polyOriginX, polyOriginY;
double deltaMouse, OriginLenghtE, OriginHeightE, OriginXE, OriginYE;
double[] polyOriginXE, polyOriginYE;
Point mPoint, dPoint, aPoint;
GeneralPath gP;
Rectangle r;
Polygon poly;
String buttonName, dataCorner;
java.util.List<Figures> figures;
java.util.List<Polygons> polygons;
boolean isPressed, isFocused, isEnded, isPolygon, isResize, isFocused2, isPressed2;
MyPanel() {
        addMouseListener(new MyMouseAdapter(this));
        addKeyListener(new MyKeyAdapter(this));
        setFocusable(true);
        requestFocusInWindow();
        setPreferredSize(new Dimension(1024, 768));

        this.figures = new ArrayList<Figures>();
        this.polygons = new ArrayList<Polygons>();
        this.buttonName = "None";

        this.polyX = new int[40];
        this.polyY = new int[40];

        this.polyOriginX = new int[40];
        this.polyOriginY = new int[40];

        this.polyOriginXE = new double[40];
        this.polyOriginYE = new double[40];

        this.counter = 0;
        this.lastEdited = -1;

        this.isFocused2 = false;
}

public void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i=0; i<this.figures.size(); i++) { // RYSOWANIE CO KLATKE JUZ NARYSOWANYCH Z LISTY
                if (this.figures.get(i).type == "Owal") {
                        switch (this.figures.get(i).Color) {
                        case "black":
                                g2d.setColor(Color.BLACK);
                                break;
                        case "blue":
                                g2d.setColor(Color.BLUE);
                                break;
                        case "green":
                                g2d.setColor(Color.GREEN);
                                break;
                        case "red":
                                g2d.setColor(Color.RED);
                                break;
                        }
                        g2d.fillOval(this.figures.get(i).startX, this.figures.get(i).startY, this.figures.get(i).lenght, this.figures.get(i).height);
                } else if (this.figures.get(i).type == "Prostokat") {
                        switch (this.figures.get(i).Color) {
                        case "black":
                                g2d.setColor(Color.BLACK);
                                break;
                        case "blue":
                                g2d.setColor(Color.BLUE);
                                break;
                        case "green":
                                g2d.setColor(Color.GREEN);
                                break;
                        case "red":
                                g2d.setColor(Color.RED);
                                break;
                        }
                        g2d.fillRect(this.figures.get(i).startX, this.figures.get(i).startY, this.figures.get(i).lenght, this.figures.get(i).height);
                }
        }

        for (int i=0; i<this.polygons.size(); i++) {
                switch (this.polygons.get(i).Color) {
                case "black":
                        g2d.setColor(Color.BLACK);
                        break;
                case "blue":
                        g2d.setColor(Color.BLUE);
                        break;
                case "green":
                        g2d.setColor(Color.GREEN);
                        break;
                case "red":
                        g2d.setColor(Color.RED);
                        break;
                }
                g2d.fillPolygon(this.polygons.get(i).polyX, this.polygons.get(i).polyY, this.polygons.get(i).polyPoints);
        }

        switch (this.buttonName) {
        case "Owal": // RYSOWANIE OVAL (NOWE)
                g2d.fillOval(this.startX, this.startY, this.endX - this.startX, this.endY - this.startY);
                break;
        case "Prostokat": // RYSOWANIE RECT (NOWE)
                g2d.fillRect(this.startX, this.startY, this.endX - this.startX, this.endY - this.startY);
                break;
        case "Wielokat": // RYSOWANIE WIELOKATU (NOWE)
                if (this.counter == 0) {
                        this.gP = new GeneralPath(GeneralPath.WIND_NON_ZERO, this.polyPoints);
                        this.gP.moveTo(this.startX, this.startY);
                } else if (this.counter < this.polyPoints) {
                        this.gP.lineTo(this.startX, this.startY);
                }

                if (this.counter < this.polyPoints && !this.isEnded) {
                        this.polyX[this.counter] = this.startX;
                        this.polyY[this.counter] = this.startY;
                        this.counter++;

                        g2d.setPaint(Color.RED);
                        g2d.setStroke(new BasicStroke(2.0f));
                        g2d.draw(this.gP);
                }

                if (this.counter >= this.polyPoints) {
                        this.isEnded = true;
                }

                if (this.isEnded) {
                        this.gP.closePath();
                        g2d.draw(this.gP);

                        g2d.setColor(Color.BLACK);
                        g2d.fillPolygon(this.polyX, this.polyY, this.polyPoints);
                        this.polygons.add(new Polygons(this.polyX, this.polyY, this.polyPoints, "black"));
                        this.isEnded = false;
                        this.counter = 0;
                }
                break;
        case "Edycja":
                // MOVEMENT && RESIZE FUNC

                requestFocusInWindow();
                if (this.isPressed) { // SZUKANIE CO KLIKNAL USER
                        for (int i=this.figures.size()-1; i>=0; i--) { // SZUKANIE FIGUR
                                this.r = new Rectangle(this.figures.get(i).startX, this.figures.get(i).startY, this.figures.get(i).lenght, this.figures.get(i).height);
                                if (this.figures.get(i).type == "Prostokat" && this.r.contains(this.startX, this.startY)) {
                                        this.whichEdited = i;
                                        this.lastEdited = this.whichEdited;
                                        this.isPolygon = false;
                                        break;
                                } else if (this.figures.get(i).type == "Owal" && this.rElipsy(this.startX, this.startY, this.figures.get(i))) {
                                        this.whichEdited = i;
                                        this.lastEdited = this.whichEdited;
                                        this.isPolygon = false;
                                        break;
                                }
                        }

                        for (int i=this.polygons.size()-1; i>=0; i--) { // SZUKANIE WIELOKATOW
                                this.poly = new Polygon(this.polygons.get(i).polyX, this.polygons.get(i).polyY, this.polygons.get(i).polyPoints);
                                if (this.poly.contains(this.startX, this.startY)) {
                                        this.whichEdited = i;
                                        this.lastEdited = this.whichEdited;
                                        this.isPolygon = true;
                                        break;
                                }
                        }
                        this.isPressed = false;
                        this.isPressed2 = true;

                        this.isFocused = true;
                        this.isFocused2 = true;
                }

                if (this.whichEdited >= 0 && this.isPressed2 && !this.isResize && !this.isPolygon) { // MOVEMENT TEGO CO NIE JEST POLYGONEM PODCZAS PRZESUWANIA
                        if (this.isFocused) { // ZAPIS STARTOWEGO POLOZENIA
                                this.OriginX = this.figures.get(this.whichEdited).startX;
                                this.OriginY = this.figures.get(this.whichEdited).startY;

                                this.isFocused = false;
                        }
                        // PRZESUWANIE O dWEKTOR

                        this.figures.get(this.whichEdited).startX = this.OriginX + (this.dPoint.x - this.startX);
                        this.figures.get(this.whichEdited).startY = this.OriginY + (this.dPoint.y - this.startY);

                        this.figures.get(this.whichEdited).mCenter.x = this.figures.get(this.whichEdited).startX + (this.figures.get(this.whichEdited).lenght/2);
                        this.figures.get(this.whichEdited).mCenter.y = this.figures.get(this.whichEdited).startY + (this.figures.get(this.whichEdited).height/2);

                } else if (this.whichEdited >=0 && this.isPressed2 && !this.isResize) { // MOVEMENT TEGO CO JEST POLYGONEM PODCZAS PRZESUWANIA
                        if (this.isFocused) { // ZAPIS STARTOWEGO POLOZENIA
                                for (int i=0; i<this.polygons.get(this.whichEdited).polyPoints; i++) {
                                        this.polyOriginX[i] = this.polygons.get(this.whichEdited).polyX[i];
                                        this.polyOriginY[i] = this.polygons.get(this.whichEdited).polyY[i];
                                }

                                this.isFocused = false;
                        }
                        // PRZESUWANIE O dWEKTOR

                        this.polygons.get(this.whichEdited).mCenter.x = 0;
                        this.polygons.get(this.whichEdited).mCenter.y = 0;

                        for (int i=0; i<this.polygons.get(this.whichEdited).polyPoints; i++) {
                                this.polygons.get(this.whichEdited).polyX[i] = this.polyOriginX[i] + (this.dPoint.x - this.startX);
                                this.polygons.get(this.whichEdited).mCenter.x += this.polygons.get(this.whichEdited).polyX[i];

                                this.polygons.get(this.whichEdited).polyY[i] = this.polyOriginY[i] + (this.dPoint.y - this.startY);
                                this.polygons.get(this.whichEdited).mCenter.y += this.polygons.get(this.whichEdited).polyY[i];
                        }

                        this.polygons.get(this.whichEdited).mCenter.x /= this.polygons.get(this.whichEdited).polyPoints;
                        this.polygons.get(this.whichEdited).mCenter.y /= this.polygons.get(this.whichEdited).polyPoints;

                } else if (this.whichEdited >=0 && this.isPressed2 && this.isResize && !this.isPolygon) { // RESIZE TEGO CO NIE JEST POLYGONEM
                        if (this.isFocused2) { // ZAPIS STARTOWEGO POLOZENIA
                                this.OriginXE = this.figures.get(this.whichEdited).startX;
                                this.OriginYE = this.figures.get(this.whichEdited).startY;
                                this.OriginLenghtE = this.figures.get(this.whichEdited).lenght;
                                this.OriginHeightE = this.figures.get(this.whichEdited).height;

                                this.isFocused2 = false;
                        }
                        //PRZESUWANIE O dWEKTOR

                        this.deltaMouse = (this.dPoint.x - this.startX + this.dPoint.y - this.startY);
                        this.deltaMouse /= 500;

                        this.figures.get(this.whichEdited).startX = (int)(this.figures.get(this.whichEdited).mCenter.x - (this.figures.get(this.whichEdited).mCenter.x - this.OriginXE) - (this.figures.get(this.whichEdited).mCenter.x - this.OriginXE)*this.deltaMouse);
                        this.figures.get(this.whichEdited).startY = (int)(this.figures.get(this.whichEdited).mCenter.y - (this.figures.get(this.whichEdited).mCenter.y - this.OriginYE) - (this.figures.get(this.whichEdited).mCenter.y - this.OriginYE)*this.deltaMouse);
                        this.figures.get(this.whichEdited).lenght = (int)(this.OriginLenghtE + this.OriginLenghtE*this.deltaMouse);
                        this.figures.get(this.whichEdited).height = (int)(this.OriginHeightE + this.OriginHeightE*this.deltaMouse);

                } else if (this.whichEdited >=0 && this.isPressed2 && this.isResize) { // RESIZE POLYGONU
                        if (this.isFocused2) { // ZAPIS STARTOWEGO POLOZENIA
                                for (int i=0; i<this.polygons.get(this.whichEdited).polyPoints; i++) {
                                        this.polyOriginXE[i] = this.polygons.get(this.whichEdited).polyX[i];
                                        this.polyOriginYE[i] = this.polygons.get(this.whichEdited).polyY[i];
                                }

                                this.isFocused2 = false;
                        }
                        //PRZESUWANIE O dWEKTOR

                        this.deltaMouse = (this.dPoint.x - this.startX + this.dPoint.y - this.startY);
                        this.deltaMouse /= 500;

                        for (int i=0; i<this.polygons.get(this.whichEdited).polyPoints; i++) {
                                this.polygons.get(this.whichEdited).polyX[i] = (int)(this.polygons.get(this.whichEdited).mCenter.x - (this.polygons.get(this.whichEdited).mCenter.x - this.polyOriginXE[i]) - (this.polygons.get(this.whichEdited).mCenter.x - this.polyOriginXE[i])*this.deltaMouse);
                                this.polygons.get(this.whichEdited).polyY[i] = (int)(this.polygons.get(this.whichEdited).mCenter.y - (this.polygons.get(this.whichEdited).mCenter.y - this.polyOriginYE[i]) - (this.polygons.get(this.whichEdited).mCenter.y - this.polyOriginYE[i])*this.deltaMouse);
                        }
                }
                break;
        }

        if (this.lastEdited >= 0 && !this.isPolygon) { // CURRENT INFO LEWY GORNY ROG
                g2d.setColor(Color.GRAY);
                this.dataCorner = "Edytowany: " + this.figures.get(this.lastEdited).type + " | X: " + this.figures.get(this.lastEdited).mCenter.x + " | Y: " + this.figures.get(this.lastEdited).mCenter.y + " | " + this.figures.get(this.lastEdited).Color;
                g2d.drawString(this.dataCorner, 20, 20);
        } else if (this.lastEdited >= 0 && this.isPolygon) {
                g2d.setColor(Color.GRAY);
                this.dataCorner = "Edytowany: " + "Wielokat" + " | X: " + this.polygons.get(this.lastEdited).mCenter.x + " | Y: " + this.polygons.get(this.lastEdited).mCenter.y + " | " + this.polygons.get(this.lastEdited).Color;
                g2d.drawString(this.dataCorner, 20, 20);
        }
}

@Override
public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
}

public void action(int startX, int startY, int endX, int endY, boolean ended) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        if (ended && (this.buttonName == "Owal" || this.buttonName == "Prostokat") && this.startX != this.endX && this.startY != this.endY) {
                this.figures.add(new Figures(this.startX, this.startY, this.endX - this.startX, this.endY - this.startY, this.buttonName, "black"));
        }

        this.repaint();
}

public void getMLocation(MyMouseAdapter m) {
        this.mPoint = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(this.mPoint, this);
        m.endX = this.mPoint.x;
        m.endY = this.mPoint.y;
}

public void getMLocationEdit() {
        this.dPoint = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(this.dPoint, this);

        this.repaint();
}

public void getMLocationResize() {
        this.aPoint = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(this.aPoint, this);

        this.startX = this.aPoint.x;
        this.startY = this.aPoint.y;
}

public boolean rElipsy(int x, int y, Figures owal) {
        double upA = (x - (owal.startX + (0.5*owal.lenght)));
        double upB = (y - (owal.startY + (0.5*owal.height)));

        double A = (upA*upA)/((0.5*owal.lenght)*(0.5*owal.lenght));
        double B = (upB*upB)/((0.5*owal.height)*(0.5*owal.height));

        if ((A + B) <= 1) {
                return true;
        } else return false;
}

public void pressedButton(String name) {
        if (!isColor(name)) {
                this.buttonName = name;
        } else if (this.lastEdited >= 0 && !this.isPolygon && isColor(name)) {
                this.figures.get(this.lastEdited).Color = name;
                this.repaint();
        } else if (this.lastEdited >= 0 && this.isPolygon && isColor(name)) {
                this.polygons.get(this.lastEdited).Color = name;
                this.repaint();
        }
}

public boolean isColor(String name) {
        if (name == "black" || name == "blue" || name == "green" || name == "red") return true;
        else return false;
}

public void polyPoints(int polyPoints) {
        this.polyPoints = polyPoints;
        this.counter = 0;
        this.isEnded = false;
        if (this.polyPoints > 40) this.polyPoints = 40;
}

public void Resize(boolean isResize) {
        this.isResize = isResize;

        this.getMLocationResize();
}
}
// KONIEC PANEL ////////////////////////////////////////////////////////////////


// PRZYCISK ////////////////////////////////////////////////////////////////////
class MyButtonAdapter implements ActionListener {
MyFrame f;
String name;
MyButtonAdapter(String name, MyFrame f) {
        this.f = f;
        this.name = name;
}

public void actionPerformed(ActionEvent e) {
        f.buttonAction(this.name);
}
}

class MyButton extends Button {
MyButton(String name, MyFrame f) {
        super(name);
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        addActionListener(new MyButtonAdapter(name, f));
}
}
// KONIEC PRZYCISKU ////////////////////////////////////////////////////////////


// TOOLBAR /////////////////////////////////////////////////////////////////////
class ToolBar extends JFrame {
MyButton Oval, Rectangle, Polygon, Edit, Black, Blue, Green, Red;
MyFrame f;
Label infoColor, buttonInfo;
ToolBar(MyFrame f) {
        this.f = f;

        setLayout(new GridLayout(0, 1));
        setLocation(1026, 0);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.Oval = new MyButton("Owal", this.f);
        this.Rectangle = new MyButton("Prostokat", this.f);
        this.Polygon = new MyButton("Wielokat", this.f);
        this.Edit = new MyButton("Edycja", this.f);

        this.Black = new MyButton("black", this.f);
        this.Black.setBackground(Color.BLACK);

        this.Blue = new MyButton("blue", this.f);
        this.Blue.setBackground(Color.BLUE);

        this.Green = new MyButton("green", this.f);
        this.Green.setBackground(Color.GREEN);

        this.Red = new MyButton("red", this.f);
        this.Red.setBackground(Color.RED);

        this.buttonInfo = new Label("");
        this.buttonInfo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        this.infoColor = new Label("Wybierz Kolor");
        this.infoColor.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        add(this.Oval);
        add(this.Rectangle);
        add(this.Polygon);
        add(this.Edit);

        add(new Label("Wybrane:"));
        add(this.buttonInfo);
        add(new Label(""));

        add(this.infoColor);

        add(this.Black);
        add(this.Blue);
        add(this.Green);
        add(this.Red);

        pack();
}
}
// KONIEC TOOLBAR //////////////////////////////////////////////////////////////


// Pop-UP-PRZYCISK /////////////////////////////////////////////////////////////
class PopUPButtonAdapter implements ActionListener {
MyDialog d;
PopUPButtonAdapter(MyDialog d) {
        this.d = d;
}

public void actionPerformed(ActionEvent e) {
        d.action();
}
}

class PopUPButton extends Button {
PopUPButton(MyDialog d, String name) {
        super(name);
        addActionListener(new PopUPButtonAdapter(d));
}
}
// KONIEC Pop-UP-PRZYCISK //////////////////////////////////////////////////////


// Pop-UP-POLYPOINTS ///////////////////////////////////////////////////////////
class MyDialog extends Dialog {
int polyPoints;
boolean canTake;
PopUPButton accept;
TextField dane;
MyFrame f;
MyDialog(MyFrame f, String name, boolean block) {
        super(f, name, block);
        this.f = f;

        setLocation(450, 300);
        setLayout(new GridLayout(0, 1));
        this.dane = new TextField("", 20);
        this.accept = new PopUPButton(this, "Akceptuj");

        add(this.dane);
        add(this.accept);

        pack();
}

public void action() {
        this.canTake = true;
        try {int n = Integer.parseInt(this.dane.getText());}
        catch (NumberFormatException ex) {
                this.canTake = false;
        }

        if (this.canTake && Integer.parseInt(this.dane.getText()) > 0) {
                this.polyPoints = Integer.parseInt(this.dane.getText());
        } else this.polyPoints = 0;

        this.setVisible(false);
        f.polyPoints(this.polyPoints);
}
}
// KONIEC Pop-UP-POLYPOINTS ////////////////////////////////////////////////////


// SAVE ////////////////////////////////////////////////////////////////////////
class Save extends Dialog implements ActionListener {
TextField dane;
Button save, close;
MyFrame f;
Save(MyFrame f, String name, boolean block) {
        super(f, name, block);
        this.f = f;

        setLocation(450, 300);
        setLayout(new GridLayout(0, 1));
        this.dane = new TextField("", 20);
        this.save = new Button("Zapisz"); this.save.addActionListener(this);
        this.close = new Button("Zamknij"); this.close.addActionListener(this);

        add(this.dane);
        add(this.save);
        add(this.close);

        pack();
}

public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Zapisz")) {
                this.setVisible(false);
                try {
                        f.save(this.dane.getText());
                }
                catch(Exception ex) {
                        System.out.println(ex);
                }
        } else if (e.getActionCommand().equals("Zamknij")) {
                this.setVisible(false);
        }
}
}
// KONIEC SAVE /////////////////////////////////////////////////////////////////


// LOAD ////////////////////////////////////////////////////////////////////////
class Load extends Dialog implements ActionListener {
TextField dane;
Button load, close;
MyFrame f;
Load(MyFrame f, String name, boolean block) {
        super(f, name, block);
        this.f = f;

        setLocation(450, 300);
        setLayout(new GridLayout(0, 1));
        this.dane = new TextField("", 20);
        this.load = new Button("Wczytaj"); this.load.addActionListener(this);
        this.close = new Button("Zamknij"); this.close.addActionListener(this);

        add(this.dane);
        add(this.load);
        add(this.close);

        pack();
}

public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Wczytaj")) {
                this.setVisible(false);
                try {
                        f.load(this.dane.getText());
                }
                catch(Exception ex) {
                        System.out.println(ex);
                }
        } else if (e.getActionCommand().equals("Zamknij")) {
                this.setVisible(false);
        }
}
}
// KONIEC LOAD /////////////////////////////////////////////////////////////////


// INFO ////////////////////////////////////////////////////////////////////////
class Info extends Dialog implements ActionListener {
Button close;
Info(MyFrame f, String name, boolean block) {
        super(f, name, block);

        setLocation(450, 300);
        setLayout(new GridLayout(0, 1));

        this.close = new Button("Zamknij"); this.close.addActionListener(this);

        add(new Label("PAINT", Label.CENTER));
        add(new Label("AUTOR ERYK KUBANSKI", Label.CENTER));
        add(new Label("PWr WPPT", Label.CENTER));
        add(new Label(""));
        add(new Label("Resize: Edycja + LPM + ctrl + mouseMove"));
        add(new Label("ColorChange: Wybor figury w edycji + Wybierz Kolor"));

        add(this.close);
        pack();
}

public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
}
}
// KONIEC INFO /////////////////////////////////////////////////////////////////


// FRAME ///////////////////////////////////////////////////////////////////////
class MyFrame extends JFrame implements ActionListener {
MyPanel p;
ToolBar tb;
MyDialog d;
MenuBar myMenu;
Menu plik;
MenuItem save, load, info;
Save s;
Load l;
Info i;
MyFrame() {
        super("Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //save Dialog
        this.s = new Save(this, "SAVE", true);

        //load Dialog
        this.l = new Load(this, "LOAD", true);

        //info Dialog
        this.i = new Info(this, "INFO", true);

        //ToolBar
        this.tb = new ToolBar(this);
        tb.setVisible(true);

        //PopUP
        this.d = new MyDialog(this, "polyPoints", true);

        // ELEMENTY
        this.p = new MyPanel();
        this.myMenu = new MenuBar();
        this.plik = new Menu("Plik");
        this.save = new MenuItem("Zapisz"); this.save.addActionListener(this);
        this.load = new MenuItem("Wczytaj"); this.load.addActionListener(this);
        this.info = new MenuItem("Info"); this.info.addActionListener(this);

        this.myMenu.add(this.plik);

        this.plik.add(this.save);
        this.plik.add(this.load);
        this.plik.add(this.info);

        add(this.p);

        setMenuBar(this.myMenu);
        pack();
}

public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Zapisz")) {
                this.s.setVisible(true);
        } else if (e.getActionCommand().equals("Wczytaj")) {
                this.l.setVisible(true);
        } else if (e.getActionCommand().equals("Info")) {
                this.i.setVisible(true);
        }
}

public void buttonAction(String name) {
        if (name == "Wielokat") d.setVisible(true);

        p.pressedButton(name);
        if (name!="black" && name!="blue" && name!="green" && name!="red") {
                tb.buttonInfo.setText(name);
        }
}

public void polyPoints(int polyPoints) {
        p.polyPoints(polyPoints);
}

public void save(String fileName) throws Exception {
        if (fileName != null) {
                PrintWriter imgfile = new PrintWriter(fileName + ".txt");
                String figureData;

                // Zapisywanie do pliku
                imgfile.println(p.figures.size());
                for (int i=0; i < p.figures.size(); i++) {
                        figureData = "";         // int startX, int startY, int lenght, int height, String type, String Color
                        figureData += p.figures.get(i).startX + " " + p.figures.get(i).startY + " " + p.figures.get(i).lenght + " ";
                        figureData += p.figures.get(i).height + " " + p.figures.get(i).type + " " + p.figures.get(i).Color;

                        imgfile.println(figureData);
                }

                imgfile.println(p.polygons.size());
                for (int i = 0; i < p.polygons.size(); i++ ) {
                        figureData = "";         // int[] polyX, int[] polyY, int polyPoints, String Color
                        figureData += p.polygons.get(i).polyPoints + " ";
                        for (int k=0; k<p.polygons.get(i).polyPoints; k++) {
                                figureData += p.polygons.get(i).polyX[k] + " ";
                                figureData += p.polygons.get(i).polyY[k] + " ";
                        }

                        figureData += p.polygons.get(i).Color;

                        imgfile.println(figureData);
                }

                imgfile.close();
        } else {
                throw new Exception("Nie podano nazwy.");
        }
}

public void load(String fileName) throws Exception {
        int j, firstC, secondC, data0, data1, data2, data3;
        String line;
        String[] data;
        char[] type;
        int[] polyX, polyY;

        if (fileName != null) {
                // FIGURES
                BufferedReader reader = new BufferedReader(new FileReader(fileName + ".txt"));
                line = reader.readLine();
                data = line.split("\\s+");
                firstC = Integer.parseInt(data[0]);

                for (int i=0; i<firstC; i++) {
                        line = reader.readLine();
                        data = line.split("\\s+");         // int startX, int startY, int lenght, int height, String type, String Color

                        data0 = Integer.parseInt(data[0]);
                        data1 = Integer.parseInt(data[1]);
                        data2 = Integer.parseInt(data[2]);
                        data3 = Integer.parseInt(data[3]);
                        type = data[4].toCharArray();

                        if (type[0] == 'P') {
                                p.figures.add(new Figures(data0, data1, data2, data3, "Prostokat", data[5]));
                        } else p.figures.add(new Figures(data0, data1, data2, data3, "Owal", data[5]));
                }

                // POLYGONS
                line = reader.readLine();
                data = line.split("\\s+");
                secondC = Integer.parseInt(data[0]);

                for (int i=0; i<secondC; i++) {
                        polyX = new int[40];
                        polyY = new int[40];

                        line = reader.readLine();
                        data = line.split("\\s+");   // int[] polyX, int[] polyY, int polyPoints, String Color but first polyPoints

                        data0 = Integer.parseInt(data[0]);

                        j = 0;
                        for (int k=0; k<data0; k++) {
                                polyX[k] = Integer.parseInt(data[j+1]);
                                polyY[k] = Integer.parseInt(data[j+2]);

                                j+=2;
                        }

                        p.polygons.add(new Polygons(polyX, polyY, data0, data[2*data0 + 1]));
                }

                p.repaint();

        } else {
                throw new Exception("Nie podano nazwy.");
        }
}
}
// KONIEC FRAME ////////////////////////////////////////////////////////////////


// MAIN ////////////////////////////////////////////////////////////////////////
public class Paint {
public static void main(String[] args) {
        MyFrame f = new MyFrame();
        f.setVisible(true);
}
}
// KONIEC MAIN /////////////////////////////////////////////////////////////////
