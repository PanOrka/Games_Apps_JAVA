import javax.swing.JFrame;

class MyFrame extends JFrame {
    MyFrame() {
        super("Snake");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocation(500, 300);

        MyPanel p = new MyPanel();
        this.add(p);
        this.pack();
    }
}
