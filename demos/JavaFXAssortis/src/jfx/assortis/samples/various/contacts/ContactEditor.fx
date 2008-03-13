
package jfx.assortis.samples.various.contacts;

import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;

import java.lang.System;


public class ContactEditor extends CompositeWidget {
    attribute contacts: Contact[];
    
    function composeWidget():Widget {
        
        var selectedRow = 0;
        
        return BorderPanel{
            top: FlowPanel{ content: Label { text: "Contact List"} }
            
            center: Table{
                columns: [
                TableColumn {
                    text: "First Name"
                },
                TableColumn {
                    text: "Last Name"
                },
                TableColumn {
                    text: "EMailAddress"
                    width: 100
                    
                }
                ]
                
                //cells:  bind for(p in contacts)[
                cells:  for(p in contacts)[
                TableCell {
                    text: bind p.firstName
                    selected: true
                },
                TableCell {
                    text: bind p.lastName
                    selected: true
                },
                TableCell {
                    text: bind p.eMailAddress
                    selected: true
                },
                ]
                
                selection: bind selectedRow with inverse
                
            }
            
            bottom: BorderPanel{
                top: FlowPanel{
                    content: [
                    Box{
                        content:[ 
                        Label{ text: "First Name:"},
                        TextField {
                            columns: 8
                            value: bind contacts[selectedRow].firstName with inverse
                        }]
                    },
                    Box{
                        content:[ 
                        Label{ text: "Last Name:"},
                        TextField {
                            columns: 8
                            value: bind contacts[selectedRow].lastName with inverse
                        }]
                    },
                    Box{
                        content:[
                        Label{ text: "EMail Address:"},
                         TextField {
                            columns: 12
                            value: bind contacts[selectedRow].eMailAddress with inverse
                        }]
                    },
                    ]
                }
                center: FlowPanel {
                    content:[
                    Button{
                        text: "   Add   "
                        action: function(){
                            insert Contact{ } into contacts;
                            selectedRow = sizeof contacts - 1;
                        }
                    },
                    Button{
                        text: "Remove"
                        action: function(){
                            delete contacts[selectedRow];
                            selectedRow--;
                        }
                    },
                    Button{
                        text: "Update"
                        
                    },
                    ]
                }
            }
            
            
            
        };
    }
}

var contacts =
[
Contact { firstName: "Mike"   lastName: "Wazowski" eMailAddress: "Mike.Wazowski@monster.com"},
Contact { firstName: "Sulley" lastName: "Monster" eMailAddress: "Sulley.Monster@monster.com"}
];


