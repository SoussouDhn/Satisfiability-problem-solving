package Genetic;

import java.util.Random;

public class Chromosome {
	int [] gene;
    int fitness;
     
    public Chromosome () {
    	 super();
    }
    
    public Chromosome (int nbvar) {
    	gene=new int [nbvar];
	    this .fitness=0;
		for (int i=0;i<nbvar;i++) {
			gene[i]=(int) (Math.random()*2) ;
		}
	}
	
	public int[] getGens() {
		return gene;
	}
	
	public void setGens(int[] gens,int i,int val) {
		this.gene[i] = val;
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	//il faut que je fait la fonction mutation
	void mutate(double mutationRate) {
		for(int i = 0; i < gene.length; i++)
			if ( new Random().nextDouble()<= mutationRate) {
				if (gene[i]==1) gene[i] = 0;
				else {
					if (gene[i]==0) gene[i] = 1;
				}
			}
	}
	
	//et calcule de la fitness
	public int calculefitness(int [][]tab,int nbClause,int nbVar) {
		int b = 0;
		int fitness = 0;
		for(int j=0;j<nbClause;j++) {
			b=0;
			for(int i=0;i<nbVar;i++) {
				if(((tab[j][i]==1)&&(this.gene[i]==1)) || ((tab[j][i]==0)&&(this.gene[i]==0)) ) {
					fitness++; b=1;
					break;
				}
				if (b==1) break;
			}
		}
		this.fitness=fitness;
		return fitness;
	}
}