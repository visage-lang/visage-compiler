import javax.swing.*;

/**
 * ExtendsJava$Impl
 *
 * @author Brian Goetz
 */

interface But$Intf {
    public String getText();
}

interface SubBut$Intf {
    public String getText();
}

class But extends JButton implements But$Intf {
    // Usual initialization code

    public String getText() {
        return getText(this);
    }

    public void foo() { }

    public static String getText(But$Intf receiver) {
        return ((But) receiver).super$getText();
    }

    private String super$getText() {
        return super.getText();
    }
}

class SubBut extends JButton implements But$Intf {
    // Usual initialization code

    public String getText() {
        return getText(this);
    }

    public void foo() { }

    public static String getText(But$Intf receiver) {
        return ((SubBut) receiver).super$getText();
    }

    private String super$getText() {
        return super.getText();
    }
}
