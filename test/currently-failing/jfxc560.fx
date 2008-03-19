/*
 * @test
 * @run/fail
 *
 */

import java.lang.System;

class GroupPanel {
    attribute row: Integer[];
    attribute content: Integer;
}

class PanelsPanel {
    attribute groupPanel: GroupPanel = GroupPanel {
        row: [1..3]
        content: groupPanel.row[0]
    }
}

var panel = new PanelsPanel;
System.out.println("panel.groupPanel.content={panel.groupPanel.content}");
