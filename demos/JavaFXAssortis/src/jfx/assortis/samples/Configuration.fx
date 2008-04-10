package jfx.assortis.samples;

import jfx.assortis.system.Module;
import jfx.assortis.system.Sample;

Module {
    name: "Samples"
    samples: [
    Sample{
        name: "Hello World"
        className: "jfx.assortis.samples.various.hello.HelloWorld"
    },
//    Sample{
//        name: "Contact Editor"
//        className: "jfx.assortis.samples.various.contacts.ContactFrame"
//    },
    Sample{
        name: "Function Graph"
        className: "jfx.assortis.samples.various.graph.FunctionGraph"
    },
    Sample{
        name: "Electro Simulator"
        className: "jfx.assortis.samples.various.electrosym.ElectroSimulator"
    },
    Sample{
        name: "Planetary System"
        className: "jfx.assortis.samples.physics.planets.Main"
    }
    ]
}
