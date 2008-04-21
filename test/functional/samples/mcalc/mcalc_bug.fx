// Bug in javafx.ui : ui does not update, seems to be a binding issue in javafx.ui that is not in javafx.gui.
//
// UI updates everyother increment of Price or Down amount.
// See sample below which is javafx.gui version and functions properly.
// 
// I don't think javafx.ui will be fixed, but if they do plan on updating it, this can be entered as a bug.
//

/*
 * @test
 */
import javafx.ui.*;
import java.lang.System;

function NUM (s:String):Number{ return java.lang.Double.parseDouble(s); }

var PurchasePrice = "15000";
var DownPercent = "10" ;
var DownAmount:Number  = bind NUM(PurchasePrice) * NUM(DownPercent)/100.0;

var Page1Row1  = FlowPanel {content: [
						Label{text:"Price $"},
						TextField{background: Color.WHITE columns:8 text: bind PurchasePrice with inverse}]};

var Page1Row2  = FlowPanel {content: [
						Label{text:"Down %"},
						TextField{background: Color.WHITE columns:3 text: bind DownPercent with inverse}]};

var Page1Row3  = FlowPanel {content: [
						Label{text:"Down Amt $"},
						TextField{background: Color.WHITESMOKE columns:8 editable: false text: bind DownAmount.toString()}]};

var Page1 = GridPanel {
	background: Color.LIGHTGREEN
	rows: 3
	columns: 1 
 	cells: [Page1Row1,Page1Row2,Page1Row3]
};

Frame {
    title: "???"
    content: Page1
    visible: true
    height: 150
    width: 300
}

/*
// Reprise javafx.gui version of same little application, this works
import javafx.gui.*;
import java.lang.System;

function NUM (s:String):Number{ return java.lang.Double.parseDouble(s); }

var PurchasePrice = "15000";
var DownPercent = "10" ;
var DownAmount:Number  = bind NUM(PurchasePrice) * NUM(DownPercent)/100.0;

var Page1Row1  = FlowPanel {content: [
						Label{text:"Price $"},
						TextField{background: Color.WHITE columns:8 text: bind PurchasePrice with inverse}]};

var Page1Row2  = FlowPanel {content: [
						Label{text:"Down %"},
						TextField{background: Color.WHITE columns:3 text: bind DownPercent with inverse}]};

var Page1Row3  = FlowPanel {content: [
						Label{text:"Down Amt $"},
						TextField{background: Color.WHITESMOKE columns:8 editable: false text: bind DownAmount.toString()}]};

var Page1 = GridPanel {
	background: Color.LIGHTGREEN
	rows: 3
	columns: 1
 	content: [Page1Row1,Page1Row2,Page1Row3]
};

Frame {
    title: "???"
    content: Page1
    visible: true
    height: 150
    width: 300
}
*/
