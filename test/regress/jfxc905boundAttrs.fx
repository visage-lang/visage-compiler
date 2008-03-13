/**
 * Regression test JFXC-905 : Binding Overhaul: bound attributes
 *
 * @test
 * @run
 */
import java.lang.System; 

var enableBindingOverhaul;

class Foo {
    attribute k = 5;
    attribute z = 3.1415926535;
    attribute s = 'blah';
    attribute b = false;
    attribute q = [5..10];
    attribute bs = bind "k={k} z={%6.3f z} z^2={%e z*z} s={s} {if(b) 'clowns' else '{s}{%6d k}'}";
    attribute bk = bind k;
    attribute bz = bind z;
    attribute bb = bind b;
    attribute bq = bind q;

    function go() {
	System.out.println(bk);
	System.out.println(bs);
	k =2;
	System.out.println(bk);
	System.out.println(bs);
	System.out.println(bz);
	z = 1.1;
	System.out.println(bz);
	System.out.println(bs);
	s = 'fog';
	System.out.println(bs);
	System.out.println(bb);
	b = true;
	System.out.println(bb);
	System.out.println(bs);
	System.out.println(bq);
        insert 100 into q;
	System.out.println(bq);
    }
}

var oo = new Foo;
oo.go()



