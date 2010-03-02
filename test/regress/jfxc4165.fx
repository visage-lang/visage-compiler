/*
 * JFXC-4165 : Sometimes order of code executed in init block is not linear
 *
 * @test
 * @run
 */

class StringHolder {
    public var content:String[];
}


class TheBug {
    public var w:Integer = 240;
    public var s:String;

    init {
        s = "Hi There";
        println("s={s}");
        var holder = StringHolder {
            content: s
        }
        println("holder.content={holder.content}");
        var foo = bind w;
    }
}

TheBug { }
