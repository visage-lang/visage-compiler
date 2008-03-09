/* JFXC-267:  Wrong variable type generated for old value variable in replace trigger
 *
 * @test
 * @run
 */
class Foo { 
} 

class Bar { 
    attribute foo: Foo on replace old {} 
}  
