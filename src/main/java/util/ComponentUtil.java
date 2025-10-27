package util;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class ComponentUtil {
	public static Button createButton(String text, double fontSize) {
        Button button = new Button();
        button.setText(text);
        button.setFont(Font.font(fontSize));
        return button;
    }
}
