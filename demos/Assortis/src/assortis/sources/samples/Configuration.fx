package assortis.sources.samples;

import assortis.core.Module;
import assortis.core.Sample;

Module {
    name: "Samples"
    samples: [
    Sample{
        name: "Cube3D"
        className: "assortis.sources.samples.mathematics.geometry.HyperCube"
    },
    Sample{
        name: "Electro Simulator"
        className: "assortis.sources.samples.physics.electrosim.ElectroSimulator"
    },
    Sample{
        name: "Planetary System"
        className: "assortis.sources.samples.physics.planets.Main"
    }
    ]
}
