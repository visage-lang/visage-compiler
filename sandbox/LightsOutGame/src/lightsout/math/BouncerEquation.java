package lightsout.math;

public class BouncerEquation extends DampingOscillatorEquation {
    
    public BouncerEquation(double amplitude, double frictionMultiplier,
                           double mass, double rigidity, double phase) {
        super(amplitude, frictionMultiplier, mass, rigidity, phase);
    }

    @Override
    public double compute(double x) {
        return Math.abs(super.compute(x));
    }
}
