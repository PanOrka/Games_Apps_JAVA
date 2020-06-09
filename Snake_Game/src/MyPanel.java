import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class MyPanel extends JPanel implements ActionListener {
    private Timer timer;
    private Movement snakeMovement;
    private Fruit fruit;
    private boolean newBorn;
    private int newBornX, newBornY;

    ArrayList<Snake> snakeList;
    SnakeHead snakeHead;
    char moveDir;
    boolean GameOver;
    boolean switchDir;

    MyPanel() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setPreferredSize(new Dimension(500, 500));
        this.setBackground(Color.BLACK);
        this.addKeyListener(new PlayerController(this));

        this.GameOver = false;
        this.snakeList = new ArrayList<>();
        this.snakeHead = new SnakeHead(250, 250);
        this.snakeList.add(new Snake(240, 250));
        this.timer = new Timer(66, this);
        this.timer.start();
        this.snakeMovement = new Movement(this);

        this.moveDir = 'd';
        this.fruit = new Fruit(this);
        this.newBorn = false;
        this.switchDir = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.doDrawing(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.timer) {
            if (this.snakeList.get(this.snakeList.size() - 1).ateFruit) {
                this.newBornX = this.snakeList.get(this.snakeList.size() - 1).xPos;
                this.newBornY = this.snakeList.get(this.snakeList.size() - 1).yPos;
                this.newBorn = true;
            }

            this.snakeMovement.doMove();

            if (this.snakeHead.xPos == this.fruit.xPos && this.snakeHead.yPos == this.fruit.yPos) {
                this.fruit.isEaten = true;
                this.snakeHead.ateFruit = true;
            }

            if (this.newBorn) {
                this.snakeList.add(new Snake(this.newBornX, this.newBornY));
                this.newBorn = false;
            }

            this.repaint();
        }
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (!this.GameOver) {
            // SNAKE
            g2d.setColor(Color.WHITE);
            g2d.fillRect(this.snakeHead.xPos, this.snakeHead.yPos, 10, 10);

            for (Snake snake : snakeList) {
                g2d.fillRect(snake.xPos, snake.yPos, 10, 10);
            }

            // Fruit
            if (!fruit.isEaten) {
                g2d.setColor(Color.RED);
                g2d.fillRect(this.fruit.xPos, this.fruit.yPos, 10, 10);
            }
        } else {
            // GAMEOVER
            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("TimesRoman", Font.BOLD, 50));
            g2d.drawString("GAME OVER", 100, 100);
        }
    }
}