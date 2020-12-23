package Depth_First;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import Binary_Tree.BinaryTree;
import File_Transformation.DimacsToMatrix;

public class Main_DFS {
	public static void main(String [] args) throws FileNotFoundException {
		long startTime = System.currentTimeMillis();
		InputStream inputStream = new FileInputStream("C:\\Users\\aaa88\\Desktop\\Projet Bio\\UF75.325.100\\uf75-01.cnf.");
		
		//définir la matrice de clauses et le nombre de variables et clauses
		int[][] clauses = DimacsToMatrix.parse(inputStream);
		
		BinaryTree bt = new BinaryTree(-1);
		int[] depth = DepthFirst.depth(bt, clauses);
		
		long endTime = System.currentTimeMillis();
	    System.out.println("Temps d'execution: "+ (endTime-startTime));
	}
}
