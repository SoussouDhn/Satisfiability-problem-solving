package Depth_First;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;

import Binary_Tree.BinaryTree;
import Binary_Tree.Node;
import Methods.Test;

public class DepthFirst {
	public static int[] depth(BinaryTree bt, int[][] clauses) throws FileNotFoundException {
		int g;
		
		//nombre de variables et de clauses de l'instance
		int nbVar = clauses[0].length;
		int nbCls = clauses.length;
		
		//pointer sur la racine
		Node node = bt.getRoot();
		node.setPere(node);
		node.setNiveau(0);
		
		//la pile qui nous permet de revenir au noeud ou on s'est arreté
		LinkedList<Node> pile = new LinkedList<Node>();
		
		//on ajout la racine à la pile
		pile.add(node);
		
		//sol est le tableau des solutions
		int[] sol = new int[nbVar];
		Arrays.fill(sol, -1);
		
		while(!pile.isEmpty()) {
			if(node.getLeft() == null) {
				while(node.getNiveau() < nbVar) {
					//ajouter le fils gauche, son niveau (actuel + 1) et son pere
					bt.addLeft(node);
					node.getLeft().setNiveau(node.getNiveau()+1);
					node.getLeft().setPere(node);
					
					//récupérer la solution à partir de la racine
					Test.getSolFromRoot(node.getLeft(),sol, node.getNiveau());
					
					//calculer combien de clauses satisfait cette solution
					g = Test.clausesSat(clauses, nbCls, node.getLeft().getNiveau(), sol);
					System.out.println("Le nombre de clauses satisfaites: "+g);
					
					//tester si la bonne solution, si c'est le cas la renvoyer
					if(g == nbCls) {
						return sol;
					}

					node = node.getLeft();
					
					//si nous avons pas encore atteint de dernier niveau de l'arbre, on ajoute la noeud à la pile
					if(node.getNiveau() < nbVar) {
						pile.addLast(node);
					}
				}
			}
			//dépiler le dernier noeud ajouté
			node = pile.removeLast();
			
			//ajouter le fils droit, son niveau (actuel + 1) et son pere
			bt.addRight(node);
			node.getRight().setNiveau(node.getNiveau()+1);
			node.getRight().setPere(node);
			
			//récupérer la solution à partir de la racine
			Test.getSolFromRoot(node.getRight(),sol, node.getNiveau());

			//calculer combien de clauses satisfait cette solution
			g = Test.clausesSat(clauses, nbCls, node.getLeft().getNiveau(), sol);
			System.out.println("Le nombre de clauses satisfaites: "+g);
			
			//tester si la bonne solution, si c'est le cas la renvoyer
			if(g == nbCls) {
				return sol;
			}
			
			//si nous avons pas encore atteint de dernier niveau de l'arbre, on ajoute la noeud à la pile
			if(node.getRight().getNiveau() < nbVar) {
				pile.addLast(node.getRight());
				node = pile.peekLast();
			}
			
		}
		return null;
	}
}