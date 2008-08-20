import javafx.ui.*;
		
class HelloWorldModel {
    var saying: String;
}

var model = HelloWorldModel {
    saying: "Hello World"
};

Frame {
    title: bind "{model.saying} JavaFX"
    width: 200
    content: TextField {
        value: bind model.saying
    }
    visible: true
};
