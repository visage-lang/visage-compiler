package assortis.library.mathematics.multidim;

/**
 * @author Alexandr Scherbatiy
 */

public function rotate(angle:Number, axisN:Integer, axisM:Integer,dim:Integer):MDRotate{
    MDRotate{
        dim: dim
        axisN: axisN
        axisM: axisM
        angle: angle
    }
}

public function composite(transforms: IMDSquareTransform[],dim: Integer):MDCustomSquareTransform{
    MDCustomSquareTransform{
        dim: dim
        transforms: transforms
    }
}