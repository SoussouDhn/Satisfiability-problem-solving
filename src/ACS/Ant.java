package ACS;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Ant {
	int [] solution;
    int fitness;
    boolean[] done;
    double q0=0.5;
    double alpha=0.3;
    double beta=0.8;
    double somme=0;
    
    public Ant () {
    	super();
    }

    public Ant (int nbvar ) {
    	solution=new int [nbvar];
    	this .fitness=0;
    	for (int i=0; i<nbvar; i++) {
    		solution[i] = (int)(Math.random()*2);
	    }    	
	    this.done = new boolean[nbvar];
	    Arrays.fill(done, Boolean.TRUE);
    }
    
    public Ant contructSolution(int [][]tab,int nbClause,int nbVar,Pheromons pheromons,double ro,double to) {
    	somme = 0;
    	Arrays.fill(done, Boolean.FALSE);
    	for (int i=0; i<nbVar; i++) {
    		solution[i] = -1;
	  	}
    	
    	//initialiser quelle variable on va commencer avec
    	int pos=(int) (Math.random()*nbVar);
    	solution[pos]=(int) (Math.random()*2);

    	done[pos] = true;
    	onlineStepByStepPheromonUpdate(pos,solution[pos],pheromons,ro,to);
	 	    
	    int[] argmax;
	    
	    for (int i = 0; i < nbVar-1; i++) {
	    	// condition  d'arret fitness == nbVar
	    	if (this.calculefitnessSolution(tab, nbClause, nbVar) == nbClause) break;
	    	double q = ThreadLocalRandom.current().nextDouble();
	    	
	    	q=0.2;// tester forcer la selection avec selection normal
	    	if (q <= q0) {
	    		argmax = getArgmax(tab,nbClause,nbVar,pheromons);
	    		solution[argmax[0]]=argmax[1];
	    		done[argmax[0]] = true;
	    	} else {
        	   argmax = selectionRoulette(tab,nbClause,nbVar,pheromons);
        	   solution[argmax[0]]=argmax[1];
        	   done[argmax[0]] = true;
           	}
	    	onlineStepByStepPheromonUpdate(argmax[0], argmax[1], pheromons, ro,to);
	    }
	    return this;
    }

    //selection roulette
    public int[] selectionRoulette(int [][]tab, int nbClause, int nbVar, Pheromons pheromons) {
    	Ant tmp=new Ant(nbVar);

    	for (int i=0; i<nbVar; i++) {
    		tmp.solution[i]=this.solution[i];
    	}
    	for (int i=0;i<nbVar;i++) {
    		tmp.done[i]=this.done[i];
    	}
    	
		double totale=tmp.getProba(tab,pheromons, nbClause, nbVar);	
		double q = ThreadLocalRandom.current().nextDouble();
		int[] argmax=tmp.getArgmax(tab,nbClause,nbVar,pheromons);
		double partialSum=tmp.getPherHeur(argmax[0], argmax[1],pheromons,tab,nbClause,nbVar)/totale; 
		int compteReste=0,compte=0;

		//compter le nombre de variable qu'on doit visité 
		for(int c=0; c<pheromons.pheromonValues.length; c++) {
			if(done[c] == Boolean.FALSE) {
	    	   compteReste++;
			}
		} 
		
		while (q > partialSum) {
			if(compte!=compteReste-1) {
				argmax =tmp.getArgmax(tab,nbClause,nbVar,pheromons);
				partialSum +=(tmp.getPherHeur(argmax[0], argmax[1],pheromons,tab,nbClause,nbVar)/totale);
				compte++;
			} else return argmax;
		} 
		return argmax;
    }
    
    int[] getArgmax(int [][]tab,int nbClause,int nbVar,Pheromons pheromons) {
    	int[] argMax = {0, 0};
    	int k=0;

    	while ((done[k]==Boolean.TRUE)&& (k< pheromons.pheromonValues.length)) {
    		k++;
    		if (k==pheromons.pheromonValues.length-1) break;
    	}
    	if (done[k]==Boolean.TRUE) return argMax;
    	
    	argMax[0] = k;
    	argMax[1] = 0;
    	
    	double max=getPherHeur(k, 0,pheromons,tab,nbClause,nbVar);
    	double cpt ;
    	
    	for(int i=0; i<pheromons.pheromonValues.length; i++) {
    		if(done[i] == Boolean.FALSE) {
    			for (int j=0; j<2 ; j++) {
    				cpt=getPherHeur(i, j,pheromons,tab,nbClause,nbVar);
    				if (cpt > max) {
    					argMax[0] = i;
    					argMax[1] = j;
    					max=cpt;
    				}
    			}
    		}
    	}
    	done[argMax[0]] = true;
    	
    	return argMax;
    }

    double getPherHeur(int variable, int literal,Pheromons pheromons,int [][]tab,int nbClause , int nbVar) {
    	Ant tmp=new Ant(nbVar);
    	
    	for (int i=0;i<nbVar;i++) {
    		tmp.solution[i]=this.solution[i];
    	}
    	
    	for (int i=0;i<nbVar;i++) {
    		tmp.done[i]=this.done[i];
    	}
    	
    	tmp.solution[variable]=literal;
    	tmp.done[variable]=true;
        int mu = tmp.calculefitnessSolution(tab, nbClause, nbVar);
        double muBeta = Math.pow(mu, beta);
        double ti = (double)  pheromons.pheromonValues[variable][literal];
        double tiAlpha = Math.pow(ti, alpha);
        return tiAlpha * muBeta;
    }
    
    public int calculefitnessSolution(int [][]tab,int nbClause,int nbVar) {
    	int b=0;
  		int fitness=0;
  		
  		for(int j=0; j<nbClause; j++) {
  			b=0;
  			for(int i=0; i<nbVar; i++) {
  				if(done[i]==true) {
  					if(((tab[j][i]==1)&&(this.solution[i]==1)) || ((tab[j][i]==0)&&(this.solution[i]==0)) ) {
  						fitness++; b=1;break;
  					}
  					if (b==1) break;
  				}
  			}
  		} 
  		return fitness;
  	}
    
    double getProba(int [][]tab ,Pheromons pheromons,int nbClause,int nbVar) {
    	double sum = 0;
    	for(int i=0;i<nbVar;i++) {
    		if(done[i]==Boolean.FALSE) {
    			for (int j = 0; j < 2; j++) {
    				sum += getPherHeur(i, j,pheromons,tab,nbClause,nbVar);
    			}
    		}
    	}
    	return (double) sum;
    }
    
    public int calculefitness(int [][]tab,int nbClause,int nbVar) {
    	int b=0;
		int fitness=0;
		for(int j=0; j<nbClause; j++) {
			b=0;
			for(int i=0; i<nbVar; i++) {
				if(((tab[j][i]==1)&&(this.solution[i]==1)) || ((tab[j][i]==0)&&(this.solution[i]==0))) {
					fitness++; b=1;break;
				}
				if (b==1) break;
			}
		}
		this.fitness=fitness;
		return fitness;
	}
	
	Double onlineStepByStepPheromonUpdate(int variable, int literal,Pheromons pheromons,double ro,double to) {
		 double Ti = pheromons.pheromonValues[variable][literal]; 
	     pheromons.pheromonValues[variable][literal] = (1 - ro) * Ti + ro * to;
	     this.somme=pheromons.pheromonValues[variable][literal] -Ti;
	     return pheromons.pheromonValues[variable][literal] ;
	}
	
	public int[] getSolution() {
		return solution;
	}
	
	public void setSolution(int[] solution,int i,int val) {
		this.solution[i] = val;
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}	
}