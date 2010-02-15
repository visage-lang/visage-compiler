/**
 * JFXC-4090 : TreeView indentation breaks as bind no longer evaluated as in 1.2
 *
 * @subtest
 */

public class jfxc4090TreeItemBase {

    public-read var parent:jfxc4090TreeItemBase;

    public var children:jfxc4090TreeItemBase[] on replace {
        for (child in children) {
            child.parent = this;
        }
    }

    public-read var level:Integer = bind parent.level + 1;
}
