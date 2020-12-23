package A_Star;

import java.util.Arrays;
import java.util.LinkedList;

import Methods.Test;

public class AStar {
	//méthode qui vérifie si un noeud existe dans une liste de listes
	public static boolean checkIfExists(LinkedList<List> closed, int nb, int val) {
		List p;
		LinkedList<List> l = (LinkedList<List>) closed.clone();
		while(!l.isEmpty()) {
			p = l.remove();
			if(p.val == val && p.nb == nb) {
				return true;
			}
		}
		return false;
	}
	
	//méthode qui ajoute un noeud dans une liste en gardant la liste triée en ordre décroissant
	public static LinkedList<List> addSort(LinkedList<List> l, List n) {
		if(l.isEmpty()) {
			l.addFirst(n);
		}
		else if(n.h > l.peekFirst().h) {
			l.addFirst(n);
		}
		else if(n.h <= l.peekLast().h) {
			l.addLast(n);
		} else {
			List f = l.peekFirst();
			while(n.h <= l.peekFirst().h) {
				l.addLast(l.removeFirst());
			}
			l.addLast(n);
			while(l.peekFirst() != f) {
				l.addLast(l.removeFirst());
			}
		}
		return l;
	}
	
	//méthode qui nous retourne la solution d'un noeud depuis la racine
	public static void getSolFromRoot(List n, int[] sol, LinkedList<List> closed) {
		List f = n;
		closed.add(f);
		closed.removeAll(closed);
		sol[f.nb - 1] = f.val;
		while(f.pere != null) {
			sol[f.pere.nb - 1] = f.pere.val;
			f = f.pere;
			closed.add(f);
		}
	}
	
	//méthode qui calcule la fitness
	public static int fitness(int[][] clauses, int[] sol, int i) {
		int g = 0;
		int gi;
		int h = 0;
		int nbCls = clauses.length;
		int nbVar = clauses[0].length;
		
		for(int l=0; l<nbCls; l++) {
			gi = 0;
			h = 0;
			for(int k=0; k<nbVar; k++) {
				if(clauses[l][k] == sol[k] && sol[k] != -1) {
					gi = 1;
					break;
				}
			}
			
			for(int k=0; k<nbVar; k++) {
				if(sol[k] != -1) {
					if(clauses[l][k] != -1) {
						h = 1;
					} else {
						h = 0;
						break;
					}
				}
			}
			g = g + gi + h;
		}
		return g;
	}
	
	//la méthode A*
	public static int[] Astar(int[][] clauses) {
		int g;
		
		int nbVar = clauses[0].length;
		int nbCls = clauses.length;
		
		//initialisation du tableau de solution
		int[] sol = new int[nbVar];
		Arrays.fill(sol,-1);
		
		//les listes open et closed
		LinkedList<List> open = new LinkedList<List>();
		LinkedList<List> closed = new LinkedList<List>();
		
		//liste de listes pour le chainage
		List l = new List(-1,0);
		
		//ajouter la racine à la liste open
		open.add(l);
		
		//un pointeur sur le noeud courant
		List current;

		//tant qu'il y a toujours des noeuds à parcourir dans la liste open on rentre dans la boucle
		while(!open.isEmpty()) {

			//pointer sur la tête de la liste open, ie. le noeud avec la plus grande fitness
			current = open.removeFirst();
			
			if(current.nb != 0) {
				if(sol[current.nb -1] != -1) {
					Arrays.fill(sol,-1);
					getSolFromRoot(current,sol,closed);
				}
			}
			
			//ajouter le noeud qu'on va parcourir dans la liste des noeuds visités
			closed.addLast(current);
			
			//creer une liste qui pointe sur la liste de chainage
			LinkedList<List> q = new LinkedList<List>();

			//pout chaque variable, tester pour les cas 0 et 1
			for(int i=0; i<nbVar; i++) {
				//si le noeud existe déja dans la liste closed, ie. il a déjà été parcouru on ne l'ajoute par dans open
				//si c'est la même variable on ne l'ajoute pas à open car elle a déjà une valeur
				if((!checkIfExists(closed,i+1,0) && !checkIfExists(closed,i+1,1))&& current.nb != i+1) {
					//créer le chainnage
					q.addLast(new List(0,i+1));
					
					//insérer la solution de la variable pour le test
					sol[i] = 0;
					
					//calculer la valeur de fitness correspondante au noeud
					q.peekLast().h = fitness(clauses, sol, i);
					
					//définir le père du noeud
					if(current.val == -1) q.peekLast().pere = null;
					else q.peekLast().pere = current;
					
					//ajouter le noeud dans open en gardant la liste triée
					open = addSort(open,q.peekLast());
					
					//apres le calcul de fitness on remet la valeur de cette variable à -1 (on ne sait pas encore quelle valeur prendre)
					sol[i] = -1;
				}
				
				//-----------------------------------------------------------
				
				
				if((!checkIfExists(closed,i+1,1) && !checkIfExists(closed,i+1,0))&& current.nb != i+1) {
					q.addLast(new List(1,i+1));
					
					sol[i] = 1;
					
					q.peekLast().h = fitness(clauses, sol, i);

					if(current.val == -1) q.peekLast().pere = null;
					else q.peekLast().pere = current;
					
					open = addSort(open,q.peekLast());
					
					sol[i] = -1;
				}
			}
			
			//construction de la solution
			if(!open.isEmpty()) {
				Arrays.fill(sol,-1);
				getSolFromRoot(open.peekFirst(),sol,closed);
			}
			for(int ll=0; ll<sol.length; ll++) {
				System.out.print(sol[ll]+" ");
			}
			System.out.println();
			
			//calculer combien de clause cette solution satisfait
			g = Test.clausesSat(clauses,nbCls,nbVar,sol);
			System.out.println("Le nombre de clauses satisfaites: "+g);
			
			//vérifier si c'est la bonne solution et la renvoyer si c'est le cas
			if(g == nbCls) {
				System.out.println("TOUTES LES CLAUSES SONT SAISFAITES: "+g);
				return sol;
			}
			
			q = null;
		}
		
		
		//s'il ne y a pas de solution on ne retourne rien
		return null;
	}
}