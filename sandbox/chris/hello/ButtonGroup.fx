package hello;

public class ButtonGroup {

    var jbuttongroup: javax.swing.ButtonGroup  =
	new javax.swing.ButtonGroup();

    public var content: AbstractButton[] 
	on replace[i](b) {
           jbuttongroup.remove(b.getButton());
   	   jbuttongroup.add(content[i].getButton());
        }
	on insert[i](b) {
	    jbuttongroup.add(b.getButton());
        }
	on delete[i](b) {
	    jbuttongroup.remove(b.getButton());
        }

    public var selection: Integer on replace {
	content[selection].selected = true;
    }
}
