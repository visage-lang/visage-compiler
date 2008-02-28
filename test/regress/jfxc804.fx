/*
* @test
*/
import javafx.ui.Button;

var button: Button;
button = Button {
    text: "Click Me"
    action: function() {
        button.enabled = false;
    }
};
button.text = ""; 
