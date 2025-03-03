package RobotsEssaim_Projet_Nagati_Ahmed;

public class SimulationVariables {
    private int numRobots;
    private double speed;
    private int numFire;
    private double propagationProbability;
    private int resistanceCount;
    private double extinguishedFires;

    // Constructeur
    public SimulationVariables(int numRobots, double speed, int numFire, double propagationProbability, int resistanceCount, double feuEteint) {
        this.numRobots = numRobots;
        this.speed = speed;
        this.numFire = numFire;
        this.propagationProbability = propagationProbability;
        this.resistanceCount = resistanceCount;
        this.extinguishedFires = feuEteint;
    }

    // Getters et setters pour chaque variable
    public int getNumRobots() {
        return numRobots;
    }
    public void setNumRobots(int numRobots) {
        this.numRobots = numRobots;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getNumFire() {
        return numFire;
    }

    public void setNumFire(int numFire) {
        this.numFire = numFire;
    }

    public double getPropagationProbability() {
        return propagationProbability;
    }

    public void setPropagationProbability(double propagationProbability) {
        this.propagationProbability = propagationProbability;
    }

    public int getResistanceCount() {
        return resistanceCount;
    }

    public void setResistanceCount(int resistanceCount) {
        this.resistanceCount = resistanceCount;
    }

    public double getExtinguishedFires() {
        return extinguishedFires;
    }

    public void setExtinguishedFires(int extinguishedFires) {
        this.extinguishedFires = extinguishedFires;
    }

	public int getAttributeCount() {
		// TODO Auto-generated method stub
		return 5;
	}
	@Override
    public String toString() {
        return 
                "numRobots=" + numRobots +
                ", speed=" + speed +
                ", numFire=" + numFire +
                ", propagationProbability=" + propagationProbability +
                ", resistanceCount=" + resistanceCount 
                
                ;
    }

}
