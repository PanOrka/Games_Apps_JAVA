import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;

public class FirstFrame extends JFrame implements ActionListener {
private TextField _x, _y, _sheeps, _factor;
private ErrorDialog _BadInput;
private Button _accept;
private boolean _isOk;
private int _iX, _iY, _iSheeps, _iFactor;
private MainFrame _mF;

public FirstFrame() {
        setLayout(new GridLayout(5, 2));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new Label("Szerokosc: ", Label.CENTER));
        this._x = new TextField("", 20);
        add(this._x);

        add(new Label("Wysokosc: ", Label.CENTER));
        this._y = new TextField("", 20);
        add(this._y);

        add(new Label("Ilosc Owiec: ", Label.CENTER));
        this._sheeps = new TextField("", 20);
        add(this._sheeps);

        add(new Label("Wspolczynnik: ", Label.CENTER));
        this._factor = new TextField("", 20);
        add(this._factor);

        this._accept = new Button("Akceptuj");
        this._accept.addActionListener(this);
        add(this._accept);

        pack();
}

public void actionPerformed(ActionEvent e) {
        this._isOk = true;

        try {
                this._iX=Integer.parseInt(this._x.getText());
                this._iY=Integer.parseInt(this._y.getText());
                this._iSheeps=Integer.parseInt(this._sheeps.getText());
                this._iFactor=Integer.parseInt(this._factor.getText());
        }
        catch (NumberFormatException ex) {
                this._isOk = false;
        }

        if (this._isOk && this._iX > 0 && this._iY > 0 && this._iSheeps > 0 && this._iSheeps < this._iX * this._iY && this._iFactor > 0) {
                this._iX=Integer.parseInt(this._x.getText());
                this._iY=Integer.parseInt(this._y.getText());
                this._iSheeps=Integer.parseInt(this._sheeps.getText());
                this._iFactor=Integer.parseInt(this._factor.getText());

                this.setVisible(false);

                this._mF = new MainFrame(this._iX, this._iY, this._iSheeps, this._iFactor);
                this._mF.setVisible(true);

        } else {
                this._BadInput = new ErrorDialog(this, "Error", true, new Label("Zostaly wprowadzone niepoprawne dane", Label.CENTER));
                this._BadInput.setVisible(true);
        }
}
}
