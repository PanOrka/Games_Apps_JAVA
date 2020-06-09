import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainFrame extends JFrame {
private int _x, _y, _sheeps, _factor, _newWolfPos, _newSheepPos;
private Random _rand;
private java.util.List<Field> _fields;
private Owolf _wolf;
private Osheep[] _sheep;
private WolfFieldFinder _Wff;
private SheepFieldFinder _Sff;

public boolean noSheeps;
public MainFrame(int x, int y, int sheeps, int factor) {
        super("Symulacja");
        this._x = x;
        this._y = y;
        this._sheeps = sheeps;
        this._factor = factor;

        this._rand = new Random();
        this._fields = new ArrayList<Field>();
        this._sheep = new Osheep[this._sheeps];

        this.noSheeps = false;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLayout(new GridLayout(this._y, this._x));

        if (this._sheeps <= 0.5*this._x*this._y) {
                for (int i=1; i<=this._x*this._y; i++) {
                        this._fields.add(new Field(true));
                        this._fields.get(i-1).Green();
                        add(this._fields.get(i-1));
                }
        } else {
                for (int i=1; i<=this._x*this._y; i++) {
                        this._fields.add(new Field(false));
                        this._fields.get(i-1).White();
                        add(this._fields.get(i-1));
                }
        }

        this.Placement();

        this._Wff = new WolfFieldFinder(this._wolf, this._sheep, this._x, this._y);
        this._Sff = new SheepFieldFinder(this._wolf, this._x, this._y, this._fields);

        for (int i=0; i<this._sheeps; i++) {
                this._sheep[i].start();
        }
        this._wolf.start();
}

private void Placement() {
        int i=0, position;

        position = this._rand.nextInt(this._x*this._y-1);
        this._fields.get(position).fieldBlocker = false;
        this._fields.get(position).Black();
        this._wolf = new Owolf(this, position);

        if (this._sheeps <= 0.5*this._x*this._y) {
                while(i<this._sheeps) {
                        position = this._rand.nextInt(this._x*this._y-1);

                        if (this._fields.get(position).fieldBlocker) {
                                this._fields.get(position).fieldBlocker = false;
                                this._fields.get(position).White();
                                this._sheep[i] = new Osheep(this, position, i);

                                i++;
                        }
                }

        } else {
                while(i<(this._x*this._y)-this._sheeps-1) {

                        position = this._rand.nextInt(this._x*this._y-1);

                        if (!this._fields.get(position).fieldBlocker && position != this._wolf.position) {
                                this._fields.get(position).fieldBlocker = true;
                                this._fields.get(position).Green();

                                i++;
                        }
                }

                position = 0; // TUTAJ TO TYLKO MIEJSCE W TABLICY int overload

                for (int l=0; l<this._x*this._y; l++) {
                        if (!this._fields.get(l).fieldBlocker && l != this._wolf.position) {
                                this._sheep[position] = new Osheep(this, l, position);
                                position++;
                        }
                }
        }
}

public void WQueue() {
        if (!this._wolf.isFull) {
                try {
                        Thread.sleep((this._rand.nextInt(10)+5)*this._factor);
                } catch(InterruptedException e) {}
        } else {
                for (int i=0; i<5; i++) {
                        try {
                                Thread.sleep((this._rand.nextInt(10)+5)*this._factor);
                        } catch(InterruptedException e) {}
                }
                this._wolf.isFull = false;
        }
        this.WolfMovement();
}

public void SQueue(int sheepNumber) {
        try {
                Thread.sleep((this._rand.nextInt(10)+5)*this._factor);
        } catch(InterruptedException e) {}

        this.SheepMovement(sheepNumber);
}

public synchronized void WolfMovement() {

        this._newWolfPos = this._Wff.Finder();

        if (this._fields.get(this._newWolfPos).fieldBlocker) {
                this._fields.get(this._wolf.position).Green();
                this._fields.get(this._wolf.position).fieldBlocker = true;

                this._fields.get(this._newWolfPos).fieldBlocker = false;
                this._fields.get(this._newWolfPos).Black();
                this._wolf.position = this._newWolfPos;

        } else if (!this._fields.get(this._newWolfPos).fieldBlocker) {
                this._fields.get(this._wolf.position).Green();
                this._fields.get(this._wolf.position).fieldBlocker = true;

                this._fields.get(this._newWolfPos).fieldBlocker = false;
                this._fields.get(this._newWolfPos).Black();
                this._wolf.position = this._newWolfPos;

                this._sheep[this._wolf.sheepFocused].isEaten = true;
                this._wolf.sheepsEaten++;

                this._fields.get(this._wolf.position).Sleep();
                this._wolf.isFull = true;
        }

        if (this._wolf.sheepsEaten == this._sheeps) {
                this.noSheeps = true;
                System.out.println("WOLF WON!!!");
        }
}

public synchronized void SheepMovement(int sheepNumber) {

        if (!this._sheep[sheepNumber].isEaten) {
                this._newSheepPos = this._Sff.Finder(this._sheep[sheepNumber].position);

                this._fields.get(this._sheep[sheepNumber].position).Green();
                this._fields.get(this._sheep[sheepNumber].position).fieldBlocker = true;

                this._fields.get(this._newSheepPos).fieldBlocker = false;
                this._fields.get(this._newSheepPos).White();
                this._sheep[sheepNumber].position = this._newSheepPos;
        }
}
}
