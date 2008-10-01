/*
 * jfxc-1413 - import **
 * @test/compile-error
 */

// import ** on a Class
import java.lang.Math.**;

// import ** on non-existent package
import com.foobar.**;
