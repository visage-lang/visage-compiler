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
        name: "Planetary System"
        className: "jfx.assortis.samples.physics.planets.Main"
    }
    ]
}
