/* Regression test: JFXC-467:A single element needs to be coerced into a one-element sequence when appropriate
 * @test
 * @run
 */
import javafx.ui.*;
import javafx.ui.canvas.*;
Canvas { 
    content: Circle { radius: 5 }
}

Canvas { 
    content: [Circle { radius: 5 }]
}
