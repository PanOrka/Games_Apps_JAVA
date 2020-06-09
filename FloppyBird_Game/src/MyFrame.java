import javax.swing.JFrame;

public class MyFrame extends JFrame {
    private MyPanel p;

    MyFrame() {
        super("FLAPPY BIRD");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        this.p = new MyPanel();
        this.add(this.p);
        this.pack();
    }
}
