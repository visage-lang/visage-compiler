package jfx.assortie.samples;

import jfx.assortie.system.Module;
import jfx.assortie.system.Sample;

Module {
    name: "Samples"
    samples: [
    Sample{
        name: "Hello World"
        className: "jfx.assortie.samples.various.hello.HelloWorld"
    },
    Sample{
        name: "Contact Editor"
        className: "jfx.assortie.samples.various.contacts.ContactFrame"
    },
    Sample{
        name: "Planetary System"
        className: "jfx.assortie.samples.physics.planets.Main"
    }
    ]
}
