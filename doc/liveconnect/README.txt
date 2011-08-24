LiveConnect Support in Visage
------------------------------------

New functionality in the Java Plug-In in Java SE 6 Update 10, as well
as in the Visage Runtime, allows the JavaScript on a web page to
interact with an applet written in Visage. JavaScript can get
and set public variables, call public functions (including
script-level functions), and get and set sequence elements.

Full documentation of the functionality needs to be written, and part
of the forthcoming new LiveConnect specification which will be linked
from http://jdk6.dev.java.net/plugin2/ will discuss this topic. The
following is an overview:

A Visage Stage can be embedded in an applet using the
org.visage.runtime.adapter.Applet class. The "MainVisageClass"
applet parameter indicates the Visage class to be run and which will
provide the Stage instance that is embedded in the applet. For
example,

  Test.visage:

    public var color = Color.YELLOW;
    public function setColor(red: Number,
                             green: Number,
                             blue: Number) : Void {
        color = Color { red: red, green: green, blue: blue };
    }

    Stage {
        scene: Scene {
            fill: Color.DARKGRAY
            content: [
                Circle {
                    centerX : 125
                    centerY : 125
                    radius: 100
                    fill: bind color
                }
            ]
        }
    }

  Test.html:

    <applet id="app" archive="..." code="org.visage.runtime.adapter.Applet" ...>
        <param name="MainVisageClass" value="Test">
    </applet>

This applet is referred to via the name "app" in the example above and
can be referenced from JavaScript by this name or by calling
document.getElementById("app").

When using the new Java Plug-In in Java SE 6 update 10, the JavaScript
on the web page can call in to Visage. Some examples of the
supported functionality:

  - Accessing public script-level variables and calling public
    script-level functions via a synthetic "script" field which is
    attached to the Visage applet object.

      app.script.color = app.Packages.visage.scene.paint.Color.RED;
      app.script.setColor(1.0, 0.0, 0.0);

  - Accessing public variables and calling public functions of Visage
    objects.

  - Fetching elements of Visage sequences. Visage sequences returned
    to JavaScript look like JavaScript arrays, and support the
    "length" field and fetching using array index ("[0]") syntax.
    (Setting elements of Visage sequences is not currently supported.)

  - Descending in to the Visage scene graph from JavaScript, from the
    applet to the stage, scene and further.

      app.stage.scene.content[0].radius = 50;

  - Automatic data type conversions such as converting JavaScript
    arrays to Visage sequences. One example of the
    functionality this enables is passing animation data from the web
    page into the Visage program.

Examples of this functionality will be included in the Visage 1.0 SDK.

Implementation note: the mechanisms in the new Java Plug-In enabling
this JavaScript / Visage bridge are fully general, and allow
any implementor of a language hosted on the JVM to use JavaScript on a
web page to interact with applets written in that language. Full
documentation of this inter-language LiveConnect bridge is forthcoming.
