package lightsout.math;


public class DampingOscillatorEquation extends AbstractEquation {
    public static final String PROPERTY_AMPLITUDE = "amplitude";
    public static final String PROPERTY_PHASE = "phase";
    public static final String PROPERTY_STIFFNESS = "stiffness";
    public static final String PROPERTY_MASS = "mass";
    public static final String PROPERTY_FRICTION_MULTIPLIER = "friction_multiplier";
    
    // exposed parameters
    private double amplitude;
    private double phase;
    private double stiffness;
    private double mass;
    private double frictionMultiplier;
    
    // internal parameters
    private double pulsation;
    private double friction;
    
    public DampingOscillatorEquation(double amplitude, double frictionMultiplier,
                                double mass, double rigidity, double phase) {
        this.amplitude = amplitude;
        this.frictionMultiplier = frictionMultiplier;
        this.mass = mass;
        this.phase = phase;
        this.stiffness = rigidity;

        computeInternalParameters();
    }

    private void computeInternalParameters() {
        // never call computeFriction() without
        // updating the pulsation
        computePulsation();
        computeFriction();
    }

    private void computePulsation() {
        this.pulsation = Math.sqrt(stiffness / mass);
    }
    
    private void computeFriction() {
        this.friction = frictionMultiplier * pulsation;
    }

    public double compute(double x) {
        double y = amplitude * Math.exp(-friction * x) *
                   Math.cos(pulsation * x + phase);
        return y;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        double oldValue = this.amplitude;
        this.amplitude = amplitude;
        firePropertyChange(PROPERTY_AMPLITUDE, oldValue, amplitude);
    }

    public double getFrictionMultiplier() {
        return frictionMultiplier;
    }

    public void setFrictionMultiplier(double frictionMultiplier) {
        double oldValue = this.frictionMultiplier;
        this.frictionMultiplier = frictionMultiplier;
        computeInternalParameters();
        firePropertyChange(PROPERTY_FRICTION_MULTIPLIER, oldValue, frictionMultiplier);
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        double oldValue = this.mass;
        this.mass = mass;
        computeInternalParameters();
        firePropertyChange(PROPERTY_MASS, oldValue, mass);
    }

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        double oldValue = this.phase;
        this.phase = phase;
        firePropertyChange(PROPERTY_PHASE, oldValue, phase);
    }

    public double getStiffness() {
        return stiffness;
    }

    public void setStiffness(double rigidity) {
        double oldValue = this.stiffness;
        this.stiffness = rigidity;
        computeInternalParameters();
        firePropertyChange(PROPERTY_STIFFNESS, oldValue, rigidity);
    }
}
