package ACS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ACS {
	public static void offlinePheromonUpdate(Ant bestAnt,Pheromons pheromons,double ro,double to) {
		 int j; int nbVarVisité=0;
		 // la somme de toute la phéromone déposé donc c'est ro*to*nbr de variable visiter
		 for(int i=0; i<bestAnt.solution.length; i++) {
			 if(bestAnt.done[i]==true)  nbVarVisité++;
		 }
		 for (int i = 0; i < pheromons.pheromonValues.length; i++)
		 {
			 if (bestAnt.solution[i] == 0) j = 0;
			 else j = 1;
			 double Ti = pheromons.pheromonValues[i][j];
			 pheromons.pheromonValues[i][j] = (1 - ro) * Ti + ro*to*nbVarVisité;
		 }
	}
	 
	public static void main(String [] args) throws FileNotFoundException {  
		InputStream inputStream = new FileInputStream("C:\\\\Users\\\\Thinkpad\\\\Desktop\\\\Downloads\\\\uuf75-325\\\\uuf75-0100.cnf");
		
		//définir la matrice de clauses et le nombre de variables et clauses
		int[][] clauses = File_Transformation.DimacsToMatrix.parse(inputStream);
		int nbVar = clauses[0].length;
		int nbClause = clauses.length;	
		int nbrFourmi = 3;
		int nbrgeneration = 1000;
		int sizeproblem = nbVar;
		Iteration Iteration = new Iteration(nbrFourmi,sizeproblem);
		int best2;
		Ant sbest = new Ant();
	    double to = 0.1;//0.63
		Pheromons pheromons=new Pheromons(sizeproblem, to);
		double ro = 0.20; // 0.22 evaporation rate
		double debut = System.currentTimeMillis();
		String s = "";
		
		//initialisation des fourmis avec A*
		int [] S1= {1, 1, 0, 1, 0, 1 ,0, 1, 0, 0, 0, 0, 0, 0, 0, 0 ,1, 0, 0, 0 ,0 ,0 ,0 ,0 ,0 ,1 ,1 ,0 ,1, 1, 1 ,1, 0, 1,0 ,1, 0, 0, 0, 1, 0, 0, 1 ,0 ,0 ,0 ,1 ,0 ,0 ,0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0 ,1, 0, 1 ,0, 1, 1, 1, 0};
		int [] S2= {1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1,1, 0 ,0, 0, 0,1, 0, 1, 0 ,1 ,1, 1, 0};
		int [] S3= {1 ,1 ,0 ,1, 0,1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 ,0 ,1 ,1 ,0 ,1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,1, 0, 1,1 ,0, 0, 0, 0, 1, 0, 1, 0, 1 ,1, 1, 0};
		//initialisation des fourmis avec A*		
		Iteration.individu[0].solution=S1;
		Iteration.individu[1].solution=S2;	
		Iteration.individu[2].solution=S3;
		
		// faire la mise a jour en ligne de la phéromone
		for (int i=0;i<nbrFourmi;i++)
		{
			int k;
			for(int j=0;j<nbVar;j++)
			{
				if (Iteration.individu[i].solution[j]==0) k=0;
				else k=1;
				pheromons.pheromonValues[j][k] =Iteration.individu[i].onlineStepByStepPheromonUpdate(j, k, pheromons,ro, to);	
			}		
		}
		
		// faire la mise a jour offline de la phéromone
		
		/////////////2 evaluate Iteration /////////////:
		Iteration.evaluatPopulation(clauses,nbClause,nbVar);
		int best = Iteration.getSGBest(Iteration);
		Ant gbest = Iteration.individu[best];
		gbest.calculefitness(clauses,nbClause,nbVar);
		
		System.out.println("the best candidate is "+best+" sa fitness est "+Iteration.individu[best].fitness);
				
		for (int i=0; i<nbVar; i++) {
			s = s + String.valueOf(gbest.solution[i]);
		}
		System.out.println(s+"--"+gbest.fitness);

		offlinePheromonUpdate(gbest, pheromons, ro,to);
		
		for (int generationIndex = 0; generationIndex < nbrgeneration; generationIndex++) {
			//la contruction des nouvelles fourmis
			for (int i=0; i<nbrFourmi; i++) {  
				Iteration.individu[i]=Iteration.individu[i].contructSolution(clauses, nbClause, nbVar, pheromons, ro, to) ;
			}
			// faire la mise a jour offline de la phéromone 
			///2 evaluate Iteration /////////////:
			Iteration.evaluatPopulation(clauses,nbClause,nbVar);
			best2=Iteration.getSGBest(Iteration);
			sbest=Iteration.individu[best2];
			sbest.calculefitness(clauses,nbClause,nbVar);
			offlinePheromonUpdate(sbest, pheromons, ro,to);
			s = "";
			
			for (int i=0; i<nbVar; i++) {
				s = s + String.valueOf(sbest.solution[i]);
			}
			System.out.println("sbest"+s+"--"+sbest.fitness);

			if (gbest.fitness<sbest.fitness) {
				for (int i=0;i<nbVar;i++) {
					gbest.solution[i]=sbest.solution[i];
				}
				gbest.fitness=sbest.fitness;
			}

			//gbest affichage	
			s = "";
			for (int i=0;i<nbVar;i++) {
				s= s + String.valueOf(gbest.solution[i]);
			}
			System.out.println("gbest"+s+"--"+gbest.fitness);	  
		}
		
		//afficher the best fitness à la fin de l'algorithme 		
		s = "";
		for (int i=0;i<nbVar;i++) {
			s = s + String.valueOf(gbest.solution[i]);
		}
		System.out.println("final best"+s+"--"+gbest.fitness);
		
		double temps = (System.currentTimeMillis() - debut) * 0.001;
		System.out.println("temps"+ temps );
	}
}