package views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class WaitingDialog {
    public static Alert getWaitingDialog() {
        javafx.scene.control.Alert mylert = new Alert(Alert.AlertType.INFORMATION, "Operation in Progress");

        mylert.getButtonTypes().clear();
        mylert.setResizable(true);
        mylert.getDialogPane().setPrefSize(480, 170);
        mylert.setResult(ButtonType.CLOSE);
        return mylert;
    }
}
