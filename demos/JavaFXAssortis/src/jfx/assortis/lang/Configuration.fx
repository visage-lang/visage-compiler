package jfx.assortis.lang;

import jfx.assortis.system.Module;
import jfx.assortis.system.Sample;

Module {
    name: "JavaFX"
    modules: [
    "jfx.assortis.lang.api.Configuration",
    ]
    samples: [
        Sample{
            name: "New"
            className: "jfx.assortis.samples.empty.Empty"
            visible: true
        }
    ]

}
