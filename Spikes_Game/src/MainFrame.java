import javax.swing.JFrame;

public class MainFrame extends JFrame {
    private MainPanel p;

    MainFrame() {
        super("Spikes");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);

        // private
        p = new MainPanel();

        this.add(this.p);
        this.p.setVisible(true);

        this.pack();
    }
}
