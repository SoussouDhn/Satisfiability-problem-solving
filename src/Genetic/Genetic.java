package Genetic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import File_Transformation.DimacsToMatrix;;

public class Genetic {
	//trier les individu par rapport a leur valeur de fitness 
	public static void tri(Population p) {
		int longueur = p.sizePopulation;
		Chromosome tampon;
		boolean permut;
	
		do {
			//hypothèse : le tableau est trié
			permut = false;
			for (int i = 0; i < longueur - 1; i++) {
				// Teste si 2 éléments successifs sont dans le bon ordre ou non
				if (p.individu[i].fitness < p.individu[i+1].fitness) {
					// s'ils ne le sont pas, on échange leurs positions
					tampon = p.individu[i];
					p.individu[i] = p.individu[i+1];
					p.individu[i+1]= tampon;
					permut = true;
				}
			}
		} while (permut); //cpt<longueur
	}
	
	//Selection des parents
	public static Chromosome[] SelectionParent(Population population,int nbParentSelectionne) {
		Chromosome [] selection = new Chromosome[nbParentSelectionne];
		
		///trier le tableau des individu par raport a la fitness
		tri(population);
		for (int i = 0; i < nbParentSelectionne; i++) {
			selection[i]=population.individu[i];
		}
		return selection;
	}

	// crossover Single point 
	public static  Chromosome singleCrossover(Chromosome parent1,Chromosome parent2,Chromosome children,int pcrossover1,int nbVar) {
		for (int i = 0; i <pcrossover1; i++) children.gene[i]=parent1.gene[i];
		
		for (int i = pcrossover1; i <nbVar; i++)  children.gene[i]=parent2.gene[i];
		
		return children;
	}
 
	// uniform crossover Single point
	Chromosome uniformCrossover(Chromosome parent1,Chromosome parent2,Chromosome children,int pcrossover,int nbVar) {
		for (int i=0; i<nbVar; i++) {
			if ((Math.random()*2) <= 1) {
				children.gene[i]=parent1.gene[i];
			} else {
				children.gene[i]=parent2.gene[i];
			}
		}
		return children;
	}
	
	
	public static void main(String [] args) throws FileNotFoundException {  
		InputStream inputStream = new FileInputStream("C:\\Users\\aaa88\\Desktop\\Projet Bio\\UUF75.325.100\\uuf75-05.cnf");
		
		//définir la matrice de clauses et le nombre de variables et clauses
		int[][] clauses = DimacsToMatrix.parse(inputStream);
		
		int nbVar = clauses[0].length;
		int nbClause = clauses.length;

		//1 initialize the entire population 	
		int populationSize = 100;
		int nbParentSelectionne = 50; //toujours a pair
		int nbrgeneration = 2000;
		
		//taux de croisement 
		double pcrossover = (double)1;
		
		//point de croisement
		int pointcrossover = 2;
		
		//taux de mutation ne dépasse pas entre 0.01 0.1
		double mutationRate = (double)1; 
		
		int sizeproblem=75;
		Population population = new Population(populationSize, sizeproblem);
		int best2;
		int compte = 0;
		int a = 0;
		Chromosome sbest=new Chromosome();
		
		long startTime = System.currentTimeMillis();
		
		
		//2 evaluer la population
		population.evaluatPopulation(clauses,nbClause,nbVar);
				
		//3 get best solution
		int best = population.getSGBest(population);
		Chromosome gbest=population.individu[best];
		
		String s = "";
		for (int i=0;i<nbVar;i++) {
			s = s + String.valueOf(gbest.gene[i]);
			
		}
		System.out.println(s+"--"+gbest.fitness);

		
		for (int generationIndex = 0; generationIndex < nbrgeneration; generationIndex++) {
			Population tmp=new Population(populationSize+nbParentSelectionne,sizeproblem);
			for (int j=0;j<populationSize;j++) {
				for (int i=0;i<nbVar;i++) {
					tmp.individu[j].gene[i]=population.individu[j].gene[i];
				}
			}
			Chromosome children1 = new Chromosome(nbVar);
			Chromosome children2 = new Chromosome(nbVar);
			Chromosome[] potentialParents = SelectionParent(population,nbParentSelectionne);
			
			a = 0;
			compte = 0;
			Population childrens = new Population(nbParentSelectionne,sizeproblem);

			while(a<nbParentSelectionne) {
				Chromosome parent1 = potentialParents[a];  a=a+1; 
			  	Chromosome parent2 = potentialParents[a];a=a+1;
			  	
				if (new Random().nextDouble() <= pcrossover) {
					children1=singleCrossover( parent1, parent2, children1, pointcrossover,nbVar);
					children2=singleCrossover( parent2, parent1, children2, pointcrossover,nbVar);
				} else {
					children1=parent1;
					children2=parent2;
			    }
			    children1.mutate(mutationRate);
			    children2.mutate(mutationRate);
			    children1.calculefitness(clauses, nbClause, nbVar);
			    children2.calculefitness(clauses, nbClause, nbVar);

			    //ajouter children1 et 2 a childrens
				for (int i=0; i<nbVar; i++) {
					childrens.individu[compte].gene[i]=children1.gene[i];
				} 
			    compte++;
			    
			    //childrens.individu[compte]=children2;
			    for (int i=0;i<nbVar;i++) {
			    	childrens.individu[compte].gene[i]=children2.gene[i];
				}
			    compte++;
			}
			childrens.evaluatPopulation(clauses,nbClause,nbVar);


			//get best solution de childrens
			best2=childrens.getSGBest(childrens);
			sbest=childrens.individu[best2];
			
			//sbest affichage
			s="";
			for (int i=0; i<nbVar; i++) {
				s = s+String.valueOf(sbest.gene[i]);
			}
			System.out.println("sbest"+s+"--"+sbest.fitness);

			
			s = "";
			for (int i=0;i<nbVar;i++) {
				s=s+String.valueOf(gbest.gene[i]);
			}
			System.out.println("gbest "+s+"--"+gbest.fitness);
			
			// recalculer la fitnesss
			if (gbest.fitness<sbest.fitness) {
				for (int i=0;i<nbVar;i++) {
					gbest.gene[i]=sbest.gene[i];
				}
				gbest.fitness=sbest.fitness;
			}


			//afficher the best fitness
			s="";
			for (int i=0;i<nbVar;i++)
			{
				s = s + String.valueOf(gbest.gene[i]);
			}
			System.out.println("gbest aprés la comp"+s+"--"+gbest.fitness);


			for (int i = populationSize; i <populationSize+nbParentSelectionne; i++) tmp.individu[i]=childrens.individu[i-populationSize];
			
			tmp.evaluatPopulation(clauses,nbClause,nbVar);
			tri(tmp);
			
			for (int i=0;i<populationSize;i++) population.individu[i]=tmp.individu[i];
		}
			        
		//afficher sbest
		s = "";
		for (int i=0;i<nbVar;i++) {
			s = s + String.valueOf(gbest.gene[i]);
		}
		System.out.println("final best"+s+"--"+gbest.fitness);
			
		long endTime = System.currentTimeMillis();
		System.out.println("Temps d'execution: "+ (endTime-startTime));
	}
}