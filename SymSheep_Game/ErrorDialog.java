import javax.swing.JDialog;
import java.awt.*;
import java.awt.event.*;

public class ErrorDialog extends JDialog implements ActionListener {
private Button _close;

public ErrorDialog(FirstFrame f, String title, boolean block, Label info) {
        super(f, title, block);
        setLayout(new GridLayout(0, 1));
        setResizable(false);

        add(info);

        add(new Label(""));

        this._close = new Button("Zamknij");
        this._close.addActionListener(this);
        add(this._close);

        pack();
}

public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
}
}
