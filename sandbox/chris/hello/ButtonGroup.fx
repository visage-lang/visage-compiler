package hello;

public class ButtonGroup {

    private attribute jbuttongroup: javax.swing.ButtonGroup  =
	new javax.swing.ButtonGroup();

    public attribute content: AbstractButton[] 
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

    public attribute selection: Integer on replace {
	content[selection].selected = true;
    }
}
