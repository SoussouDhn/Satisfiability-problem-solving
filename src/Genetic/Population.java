package Genetic;

public class Population {
	 Chromosome [] individu;
	 int sizePopulation;
	 
	 public Population (int sizePopulation,int sizeProbleme) {
		 this.sizePopulation=sizePopulation;
		 individu=new Chromosome[sizePopulation];
		 
		 for (int i=0; i<sizePopulation; i++) { 
			 individu[i]=new Chromosome(sizeProbleme);
		 }
	 }
	 
	 Chromosome getChromosome(int index) {
		 return individu[index];
	 }
	
	 //fonction get max individu who have max fitness
	 public int getSGBest(Population p) {
		 int max=0;
		 int pos=0;
		 for (int i = 0; i < p.sizePopulation; ++i) {
             if (p.individu[i].fitness > max) {
                 max = p.individu[i].fitness;
                 pos=i;
             }
         }
		 return pos;
	 }
	 
	 //evaluation de la population
	 public void evaluatPopulation(int[][]tab, int nbClause,int nbVar) {
		 for (int i=0;i<sizePopulation;i++)
		 {
			 individu[i].calculefitness(tab, nbClause, nbVar);
		 }
	 }
}