package dateFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateFormat  {
    @FXML
    private TextArea text;
    @FXML
    private ListView<String> datesList;
    @FXML
    public void initialize() {
        text.appendText("I was born on 19.10.1999. My sister was born on 21.04.2006");
    }
    @FXML
    public void showDates(ActionEvent e) {
        datesList.getItems().clear();
        Pattern p = Pattern.compile("(((0[1-9]|[12][0-9]|30)\\.(04|06|09|11))|" +
                "((0[1-9]|[12][0-9]|30|31)\\.(01|03|05|07|08|10|12))|" +
                "((0[1-9]|[12][0-9]|28|29)\\.02))\\.[1-9]\\d{3}");
        Matcher m = p.matcher(text.getText());
        while(m.find()) {

            datesList.getItems().add(text.getText().substring(m.start(), m.end()));
        }
    }

}
