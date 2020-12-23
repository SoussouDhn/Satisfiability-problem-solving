package A_Star;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import File_Transformation.DimacsToMatrix;

public class Main_Astar {
	public static void main(String [] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		InputStream inputStream = new FileInputStream("C:\\Users\\aaa88\\Desktop\\Projet Bio\\UF75.325.100\\uf75-01.cnf.");
		
		//définir la matrice de clauses et le nombre de variables et clauses
		int[][] clauses = DimacsToMatrix.parse(inputStream);
		int[] astar = AStar.Astar(clauses);
		
		long endTime = System.currentTimeMillis();
	    System.out.println("Temps d'execution: "+ (endTime-startTime));
	}
}