package Breadth_First;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import Binary_Tree.BinaryTree;
import File_Transformation.DimacsToMatrix;

public class Main_BFS {
	public static void main(String [] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		InputStream inputStream = new FileInputStream("C:\\Users\\aaa88\\Desktop\\Projet Bio\\UF75.325.100\\uf75-01.cnf.");
		
		//définir la matrice de clauses et le nombre de variables et clauses
		int[][] clauses = DimacsToMatrix.parse(inputStream);
		
		BinaryTree bt2 = new BinaryTree(-1);
		int[] breadth = BreadthFirst.breadth(bt2, clauses);

	    long endTime = System.currentTimeMillis();
	    System.out.println("Temps d'execution: "+ (endTime-startTime));
	}
}