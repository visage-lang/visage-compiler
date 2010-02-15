/**
 * JFXC-4090 : TreeView indentation breaks as bind no longer evaluated as in 1.2
 *
 * @compilefirst jfxc4090TreeItemBase.fx
 * @test
 * @run
 */

var itemlvl1:jfxc4090TreeItemBase;
var itemlvl2:jfxc4090TreeItemBase;
var itemlvl3:jfxc4090TreeItemBase;
var itemlvl4:jfxc4090TreeItemBase;
var itemlvl5:jfxc4090TreeItemBase;

var simpleTree:jfxc4090TreeItemBase = jfxc4090TreeItemBase {
    children: [
        jfxc4090TreeItemBase { },
        jfxc4090TreeItemBase { },
        itemlvl1 = jfxc4090TreeItemBase { },
        jfxc4090TreeItemBase { },
        jfxc4090TreeItemBase {
            children: [
                jfxc4090TreeItemBase { },
                itemlvl2 = jfxc4090TreeItemBase { },
                jfxc4090TreeItemBase { },
                jfxc4090TreeItemBase {
                    children: [
                        itemlvl3 = jfxc4090TreeItemBase { },
                        jfxc4090TreeItemBase { },
                        jfxc4090TreeItemBase { },
                        jfxc4090TreeItemBase {
                            children: [
                                jfxc4090TreeItemBase { },
                                itemlvl4 = jfxc4090TreeItemBase { },
                                jfxc4090TreeItemBase { },
                                jfxc4090TreeItemBase {
                                    children: [
                                        jfxc4090TreeItemBase { },
                                        itemlvl5 = jfxc4090TreeItemBase { },
                                        jfxc4090TreeItemBase { },
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        }
        jfxc4090TreeItemBase { }
    ]
}

println("level of simpleTree is {simpleTree.level}");
println("level of itemlvl1 is {itemlvl1.level}");
println("level of itemlvl2 is {itemlvl2.level}");
println("level of itemlvl3 is {itemlvl3.level}");
println("level of itemlvl4 is {itemlvl4.level}");
println("level of itemlvl5 is {itemlvl5.level}");
