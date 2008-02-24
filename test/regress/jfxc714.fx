/*
* @test
*/
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.System;

class UIControls {
	function fillupArrays(nodes: Node[]) :Void {
		for (node in nodes) {
                       var gr = node as Group;
				fillupArrays(gr.content);
		}
	};
}
