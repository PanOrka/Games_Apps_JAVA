import java.awt.*;

public class Field extends Label {
public boolean fieldBlocker;

public Field(boolean fieldBlocker) {
        super("");

        this.fieldBlocker = fieldBlocker;
}

public void Green() {
        this.setBackground(Color.GREEN);
}

public void White() {
        this.setBackground(Color.WHITE);
}

public void Black() {
        this.setBackground(Color.BLACK);
}

public void Sleep() {
        this.setBackground(Color.GRAY);
}
}
