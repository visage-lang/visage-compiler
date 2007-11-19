package javafxcpad;
import javafx.ui.*;
import java.io.*;
import javafx.ui.canvas.*;
import javafx.ui.FileFilter;

public class SourcePathDialog {
    attribute sourcePath: File*;
    attribute selectedSourcePath:Number?;
    operation show(parent:UIElement);
    operation add(w:Widget);
    operation remove();
    operation up();
    operation down();
    operation edit(w:Widget);
    attribute action: operation(sourcePath: File*);
}

operation SourcePathDialog.remove() {
    var i = selectedSourcePath;
    delete sourcePath[selectedSourcePath];
    if (i == sizeof sourcePath) {
        i--;
    }
    selectedSourcePath = i;
}

operation SourcePathDialog.up() {
    // A
    // => B
    // C
    var i = selectedSourcePath; // 1
    var v = sourcePath[i]; // B
    delete sourcePath[i]; // sp[1] == C
    insert v before sourcePath[i-1];
    selectedSourcePath = i-1;
}

operation SourcePathDialog.down() {
    var i = selectedSourcePath;
    var v = sourcePath[selectedSourcePath];
    delete sourcePath[i];
    insert v after sourcePath[i];
    selectedSourcePath++;
}

operation SourcePathDialog.add(w:Widget) {
    var fc:FileChooser;
    fc = FileChooser {
        title: "Class Path"
        files: true
        directories: true
        fileFilters: 
        [FileFilter {
            filter: operation(f:File) {
                if (f.directory) {
                    return true;
                }
                var name = f.name.toUpperCase();
                return name.endsWith(".ZIP") or name.endsWith(".JAR");
            }
            description: "Archive Files (*.zip, *.jar)"
        },
        FileFilter {
            filter: operation(f: File) {
                return f.directory;
            }
            description: "Source Directories"
        }]
        action: operation(selectedFile:File) {
            if (selectedSourcePath < 0) {
                insert selectedFile into sourcePath;
                selectedSourcePath = 0;
            } else {
                var i = selectedSourcePath;
                insert selectedFile before sourcePath[selectedSourcePath];
                selectedSourcePath = i;
            }
        }
    };
    fc.showOpenDialog(w);

}

operation SourcePathDialog.edit(w:Widget) {
    var fc:FileChooser;
    fc = FileChooser {
        title: "Class Path"
        files: true
        directories: true
        fileFilters: 
        [FileFilter {
            filter: operation(f: File) {
                if (f.directory) {return true;}
                var name = f.name.toUpperCase();
                return name.endsWith(".ZIP") or name.endsWith(".JAR");
            }
            description: "Archive Files (*.zip, *.jar)"
        },
	FileFilter {
            filter: operation(f: File) {return f.directory;}
            description: "Source Directories"
        }]
        action: operation(selectedFile:File) {
            sourcePath[selectedSourcePath] = selectedFile;
        }
        cwd: sourcePath[selectedSourcePath]
    };
    fc.showOpenDialog(w);

}

operation SourcePathDialog.show(win:UIElement) {
    var self = this;
    var oldSourcePath = self.sourcePath;
    Dialog {
        owner: win
        visible: true
        var: dlg     
        title: "Class Path"
        height: 300
        width : 500
        modal: true
        content: BorderPanel {
            top: Label {text: "Class Path"}
            center: ListBox {
                action: operation() {self.edit(dlg.content);}
                selection: bind self.selectedSourcePath
                cells: bind 
                foreach (f in self.sourcePath) ListCell {text: f.canonicalPath}
            }
            bottom: FlowPanel {
                border: EmptyBorder {
                    top: 5
                    left: 2
                    right: 5
                    bottom: 2
                }
                alignment: LEADING
                content: 
                [Button {
                    preferredSize: {width: 80}
                    text: "Add"
                    mnemonic: A
                    action: operation() {self.add(dlg.content);}
                },
                Button {
                    preferredSize: {width: 80}
                    text: "Edit"
                    action: operation() {self.edit(dlg.content);}
                    enabled: bind self.selectedSourcePath >= 0
                },
                Button {
                    preferredSize: {width: 80}
                    text: "Remove"
                    action: operation() {self.remove();}
                    enabled: bind self.selectedSourcePath >= 0
                },
                Button {
                    preferredSize: {width: 80}
                    text: "Up"
                    action: operation() {self.up();}
                    enabled: bind self.selectedSourcePath <> null and self.selectedSourcePath > 0
                },
                Button {
                    preferredSize: {width: 80}
                    text: "Down"
                    action: operation() {self.down();}
                    enabled: bind self.selectedSourcePath <> null and self.selectedSourcePath < sizeof self.sourcePath-1
                }]
            }
        }
        buttons: 
        [Button {
            text: "OK"
            action: operation() {
		(self.action)(self.sourcePath);
                dlg.hide();
            }
        },
        Button {
            text: "Cancel"
            defaultCancelButton: true
            action: operation() {
		self.sourcePath = oldSourcePath;
                dlg.hide();
            }
        }]
    };
}
