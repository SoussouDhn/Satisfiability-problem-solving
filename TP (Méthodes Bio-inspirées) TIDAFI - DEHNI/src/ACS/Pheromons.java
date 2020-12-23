package ACS;


public class Pheromons
{
	public Double initValue;
    public Double[][] pheromonValues;
    public Double to;

    public Pheromons(int numberOfVariables, double initValue) {
    	this.initValue = initValue;
        this.to = initValue;
        this.pheromonValues = new Double[numberOfVariables][2];
        init(initValue);
    }
    
    public void init(Double initValue) {
        for (int i = 0; i < pheromonValues.length; i++) {
            for (int j = 0; j < pheromonValues[i].length; j++) {
                pheromonValues[i][j] = initValue;
            }
        }
    }
    
    public Double[][] getPheromonValues() {
        return pheromonValues;
    }
    
    public String toString() {
        String ret = "";
        for (int i = 0; i < 2; i++) {
            ret += "[";
            for (int j = 0; j < pheromonValues.length; j++) {
                ret += "'" + String.format("%.8f", pheromonValues[j][i]) + "' ,";
            }
            ret += "]\n";
        }
        return ret;
    }
}