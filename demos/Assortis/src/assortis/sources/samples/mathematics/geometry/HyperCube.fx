package assortis.sources.samples.mathematics.geometry;

import assortis.library.mathematics.multidim.*;

import javafx.gui.*;

Frame{
    width:  300
    height: 300
    
    title: "Cube 3D"
    
    content: Canvas{
        content: MDUniverse{
            transform: Transform.translate(150,140);
            dimension: 3
            projection: MDMatrix{
                dimN: 2
                dimM: 3
                elems: [ 
                        [1, 0, 0],
                        [0, 1, 0]
                    ]
            }
            transforms: [ 
                MDRotate{
                    dim: 3
                    axisN: 0
                    axisM: 1
                    angle: (3.14 / 16)
                },
                MDRotate{
                    dim: 3
                    axisN: 0
                    axisM: 2
                    angle: ( 3.14 / 10)
                },            
                MDRotate{
                    dim: 3
                    axisN: 1
                    axisM: 2
                    angle: ( 3.14 / 16)
                },            
            ]
            
            shapes: [ MDCube{ dim: 3  side: 50} ]
        }
    }
}