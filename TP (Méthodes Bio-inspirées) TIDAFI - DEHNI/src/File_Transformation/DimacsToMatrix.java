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
	     
	     //d�clarer un scanner pour parcourir le contenu du fichier
	     @SuppressWarnings("resource")
	     Scanner scanner = new Scanner(inputstream);
	     
	     //d�passer les lignes qui commencent par le caract�re 'c'
	     String token = scanner.next();                                                                      
	     while (token.equals("c")) {
	    	 scanner.nextLine();
	         token = scanner.next();                                                                           
	     }
	     
	     //Lire la t�te du fichier et r�cuperer le nombre de variables et le nombre de clauses
	     scanner.next();
	     nbVar = scanner.nextInt();                                                                   
	     nbCls = scanner.nextInt();
	     
	     //cr�er et initialiser la matrice r�sultante de clauses � -1
	     int[][] clauses = new int[nbCls][nbVar];
	     for (int[] row: clauses) Arrays.fill(row, -1);

	     //lire les clauses ligne par ligne et les codifier
	     //-1 si la variable n'existe pas
	     //1 si la variable est positive
	     //0 si la variable est n�gative
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