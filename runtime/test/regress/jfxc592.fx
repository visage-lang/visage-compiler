/*
 * Verify that we can access top-level symbols.
 * @test
 */
import javafx.ui.*;

var rows:Row[] = [
    Row {},
    Row {alignment: Alignment.BASELINE}
    ];
var columns:Column[] = [
    Column {alignment: Alignment.TRAILING},
    Column {alignment: Alignment.LEADING}
    ]; 

public class PanelsPanel {
    attribute title = "Panels";
    attribute description = "Panels notes here";

    attribute groupPanel: GroupPanel = GroupPanel {
        border: TitledBorder {title: "Group Panel"}
        
        rows: rows
        columns: columns
    }
}
