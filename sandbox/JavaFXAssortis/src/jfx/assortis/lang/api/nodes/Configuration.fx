package jfx.assortis.lang.api.nodes;


import jfx.assortis.system.Module;
import jfx.assortis.system.Sample;

Module {
    name: "Nodes"

    samples: [
    Sample{
        name: "Circle"
        className: "jfx.assortis.lang.api.nodes.circle.FXCircle"
        //visible: true
    },
    Sample{
        name: "Rectangle"
        className: "jfx.assortis.lang.api.nodes.rectangle.FXRect"
        //visible: true
    }
    ]
}
