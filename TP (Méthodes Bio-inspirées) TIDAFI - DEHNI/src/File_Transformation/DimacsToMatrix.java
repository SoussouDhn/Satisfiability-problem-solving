package File_Transformation;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class DimacsToMatrix {
	public static int[][] parse(InputStream inputstream){
		 int nbVar;
	     int nbCls;
	     int i = 0;
	     int str;
	     
	     //déclarer un scanner pour parcourir le contenu du fichier
	     @SuppressWarnings("resource")
	     Scanner scanner = new Scanner(inputstream);
	     
	     //dépasser les lignes qui commencent par le caractère 'c'
	     String token = scanner.next();                                                                      
	     while (token.equals("c")) {
	    	 scanner.nextLine();
	         token = scanner.next();                                                                           
	     }
	     
	     //Lire la tête du fichier et récuperer le nombre de variables et le nombre de clauses
	     scanner.next();
	     nbVar = scanner.nextInt();                                                                   
	     nbCls = scanner.nextInt();
	     
	     //créer et initialiser la matrice résultante de clauses à -1
	     int[][] clauses = new int[nbCls][nbVar];
	     for (int[] row: clauses) Arrays.fill(row, -1);

	     //lire les clauses ligne par ligne et les codifier
	     //-1 si la variable n'existe pas
	     //1 si la variable est positive
	     //0 si la variable est négative
	     while(i<nbCls) {
	    	 while((str = scanner.nextInt()) != 0){
		    	 if(str < 0) {
		    		 clauses[i][-str-1] = 0;
		    	 }
		    	 else {
		    		 clauses[i][str-1] = 1;
		    	 }
	    	 }
	    	 i++;
	    	 scanner.hasNextLine();	 
	     }
	     return clauses;
	}
}