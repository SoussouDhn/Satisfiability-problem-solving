package Methods;

import Binary_Tree.Node;

public class Test {
	public static void getSolFromRoot(Node n, int[] sol, int niveau) {
		Node f = n;
		int i = niveau;
		while(i >= 0) {
			sol[i] = f.getValue();
			i--;
			f = f.getPere();
		}
	}
	
	public static int clausesSat(int[][] clauses, int nbCls, int niveau, int[] sol) {
		int g = 0;
		int gi;
		for(int i1=0; i1<nbCls; i1++) {
			gi = 0;
			for(int j1=0; j1<niveau; j1++) {
				if(clauses[i1][j1] == sol[j1] && sol[j1] != -1) {
					gi = 1;
					break;
				}
			}
			g = g + gi;
		}
		return g;
	}
}