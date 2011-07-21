/*
 * Regression: JFXC-4161 - Compiled bind optimization: new style functions need to be usable from Java
 *
 * @subtest
 *
 */

import com.sun.visage.functions.*;

public class jfxc4161J {
    
    static public  Function2<Integer, Integer, Integer> getF() {
        return new Function2<Integer, Integer, Integer>() {
            public Integer  invoke(Integer x, Integer y) {
                return x + y;
            }
        };
    }
}
