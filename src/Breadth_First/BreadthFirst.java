package Breadth_First;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;

import Binary_Tree.BinaryTree;
import Binary_Tree.Node;
import Methods.Test;

public class BreadthFirst {
	//arbre binaire pour créer le chainage
	public static int[] breadth(BinaryTree bt, int[][] clauses) throws FileNotFoundException {
		int g;
		
		//nombre de variables et de clauses
		int nbVar = clauses[0].length;
		int nbCls = clauses.length;
		
		int niveau = 0;
		int extaille = 1;
		int taille = 0;
		
		//pointer sur la racine de l'arbre
		Node node = bt.getRoot();
		
		//la pile qui nous permet de revenir au noeud ou on s'est arreté
		LinkedList<Node> file = new LinkedList<Node>();
		
		//on ajoute la racine à la pile
		file.add(node);
		
		//sol est le tableau des solutions
		int[] sol = new int[nbVar];
		Arrays.fill(sol, -1);
		
		while(!file.isEmpty()) {
			//pointer sur le premier noeud
			node = file.removeFirst();
			
			//travailler le fils gauche et droit du noeud et les insérer dans la file ********
			
			//ajouter le fils gauche et son pere
			bt.addLeft(node);
			node.getLeft().setPere(node);
			
			taille++;
			
			//ajouter le noeud à la file
			file.addLast(node.getLeft());
			
			//récupérer la solution à partir de la racine
			Test.getSolFromRoot(node.getLeft(),sol, niveau);
			
			//calculer combien de clauses satisfait cette solution
			g = Test.clausesSat(clauses, nbCls, niveau+1, sol);
			System.out.println("Le nombre de clauses satisfaites: "+g);
			
			//tester si la bonne solution, si c'est le cas la renvoyer
			if(g == nbCls) {
				return sol;
			}
			
			//--------------------------------------------------------
			
			//ajouter le fils droit et son pere
			bt.addRight(node);
			node.getRight().setPere(node);
			
			taille++;
			
			//ajouter le noeud à la file
			file.addLast(node.getRight());
			
			//récupérer la solution à partir de la racine
			Test.getSolFromRoot(node.getRight(),sol, niveau);
			
			//calculer combien de clauses satisfait cette solution
			g = Test.clausesSat(clauses, nbCls, niveau+1, sol);
			System.out.println("Le nombre de clauses satisfaites: "+g);
			
			//tester si la bonne solution, si c'est le cas la renvoyer
			if(g == nbCls) {
				return sol;
			}
			
			if(taille == extaille*2) {
				niveau++;
				extaille = taille;
				taille = 0;
			}
		}
		
		//s'il ne y a pas de solution on ne retourne rien
		return null;
	}
}