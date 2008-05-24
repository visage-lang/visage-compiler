
/*
 * Regression test: JFXC-1037: report lexical errors
 *
 * @test/fail
 */

import javafx.ui.*; 

Frame { 
     content: Label { 
text: bind 
"<html>{ 
for (k in [1..T*t]) { } 
            </html>" 
} 
visible: true 
}