package jfx.assortie.samples;

import jfx.assortie.system.Module;
import jfx.assortie.system.Sample;

Module {
    name: "Samples"
    samples: [
    Sample{
        name: "Hello World"
        className: "jfx.assortie.samples.various.HelloWorld"
    },
    Sample{
        name: "Planetary System"
        className: "jfx.assortie.samples.physics.planets.Main"
    }
    ]
}
