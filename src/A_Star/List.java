package A_Star;

import java.util.LinkedList;

public class List {
	int val;
	int nb;
	int h;
	List pere;
	LinkedList<List> list;
	
	List(int val, int nb) {
		this.val = val;
		this.nb = nb;
	}
}