package format;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Format  {
    //By using a @FXML annotation on a member,
    // you are declaring that the FXML loader can access the
    // member even if it is private. A public member used by the FXML
    // loader does not need to be annotated with @FXML.
    @FXML
    private Circle circleValidator;
    @FXML
    private TextField inputTextField;
    @FXML
    private ComboBox<String> formatBox;
    @FXML
    public void initialize() {
        formatBox.setItems(FXCollections.observableArrayList("Natural", "Integer", "Floating point",
                "Date", "Time", "E-mail"));
        formatBox.setValue("Natural");
    }
    @FXML
    public void formatTyped(KeyEvent e) {
        validate();
    }
    @FXML
    public void formatChanged(ActionEvent e) {
        validate();
    }
    @FXML
    private void validate() {
        if(inputTextField.getText().isEmpty()) {
            circleValidator.setFill(Color.YELLOW);
        }else {
            if (FormatUtils.isCorrectFormat(inputTextField.getText(), formatBox.getValue())) {
                circleValidator.setFill(Color.GREEN);
            } else {
                circleValidator.setFill(Color.RED);
            }
        }
    }
}
