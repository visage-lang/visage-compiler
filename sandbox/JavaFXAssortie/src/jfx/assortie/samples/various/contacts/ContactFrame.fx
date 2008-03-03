
package jfx.assortie.samples.various.contacts;

import javafx.ui.*;
import java.lang.System;

Frame{
    title: "JavaFX Contact Library"
    width:  400
    height: 250
    content: ContactEditor {
        contacts: [
        Contact {
            firstName: "Mike"
            lastName: "Wazowski"
            eMailAddress: "Mike.Wazowski@monster.com"
        },
        Contact {
            firstName: "Sulley"
            lastName: "Monster"
            eMailAddress: "Sulley.Monster@monster.com"
        },
        ]
    }
    visible: true
}
