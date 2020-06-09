import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPanel extends JPanel implements ActionListener {
    private int _birdX, _counter, _hitCounter, _oldX, _oldY, _points;
    private Timer _t;
    private BirdMove _birdMove;
    private Obstacles[] _obstacles;
    private boolean _obstacleHit;

    public int timeX, birdY;
    public boolean isStarted, GameOver;

    MyPanel() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(600, 500));
        this.setBackground(Color.CYAN);
        this.addKeyListener(new PlayerController(this));

        this._birdX = 200;
        this.birdY = 235;
        this._t = new Timer(25, this);
        this._t.start();
        this._birdMove = new BirdMove(this);
        this._obstacles = new Obstacles[3];

        for (int i=0; i<3; i++) {
            this._obstacles[i] = new Obstacles();
        }

        this._hitCounter = 0;
        this._counter = 0;

        this._obstacles[0].x = 600;
        this._obstacles[1].x = 800;
        this._obstacles[2].x = 1000;

        this._oldX = -60; // OUTSIDE MAP IN CASE OF NULL POINTER
        this._oldY = 0;

        for (int i=0; i<3; i++) {
            this._obstacles[i].setGap();
        }

        this._obstacleHit = false;

        this._points = 0;

        this.isStarted = false;
        this.GameOver = false;
        this.timeX = 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Obstacles
        g2d.setColor(Color.GREEN);
        for (int i=0; i<3; i++) {
            g2d.fillRect(this._obstacles[i].x, this._obstacles[i].y, 40, 500 - this._obstacles[i].y);
            g2d.fillRect(this._obstacles[i].x, 0, 40, this._obstacles[i].y - 150);
        }

        g2d.fillRect(this._oldX, this._oldY, 40, 500 - this._oldY);
        g2d.fillRect(this._oldX, 0, 40, this._oldY - 150);

        if (this._obstacles[this._counter].x == 0) { // MAP ROLL
            this._oldX = this._obstacles[this._counter].x;
            this._oldY = this._obstacles[this._counter].y;

            this._obstacles[this._counter].x = 600;
            this._obstacles[this._counter].setGap();

            this._counter++;
            this._counter %= 3;
        }

        // Bird
        if (!this._obstacleHit) {
            g2d.setColor(Color.BLACK);
        } else g2d.setColor(Color.RED);
        g2d.fillOval(this._birdX, this.birdY, 30, 30);

        // Grass
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 480, 600, 20);

        // Ground
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(0, 470, 600, 10);

        // Points
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        g2d.drawString(Integer.toString(this._points), 295, 35);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this._t && this.isStarted && !this.GameOver) {
            this.timeX++;
            this._birdMove.doMove();

            if (this._obstacles[_hitCounter].isHit(this._birdX, this.birdY)) {
                this.GameOver = true;
                this._obstacleHit = true;
            }

            for (int i=0; i<3; i++) {
                this._obstacles[i].x -= 5;
            }

            this._oldX -= 5;

            if (this._birdX >= this._obstacles[this._hitCounter].x + 40) {
                this._points++;

                this._hitCounter++;
                this._hitCounter %= 3;
            }

            this.repaint();
        } else if (this._obstacleHit) {
            this.birdY += 15;

            if (this.birdY >= 440){
                this.birdY = 440;
                this._obstacleHit = false;
            }

            this.repaint();
        }
    }
}
