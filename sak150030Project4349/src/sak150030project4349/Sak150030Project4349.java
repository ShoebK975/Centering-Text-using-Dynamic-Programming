/*
Shoeb Kazi
CS 4349
Spring Semester
3/12/2018
This program fully justifies all text input.
 */
package sak150030project4349;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;


public class Sak150030Project4349 {

    static String lines[] = new String[10000];
    static int totalPenalty = 0;
    static int configureLines(int[] aux, int j,ArrayList<String>words,char extra, int[][] extraSpaces){
        int k;
        int i = aux[j];
        if(i <= 1)
        {
            k = 1;
        }
        else{
            k = configureLines(aux,i-1,words,extra,extraSpaces) + 1;
        }
        makeArray(k,i,j,words,extra,extraSpaces);
        return k;
    }
    static void makeArray(int k, int i, int j,ArrayList<String> words,char extra, int[][] extraSpaces){
        lines[k] = "";
        int s = i;
        int numWords = j - i + 1;
        int numSpacesToAdd = extraSpaces[i][j];
        totalPenalty += Math.pow(numSpacesToAdd, 3);
        int a1 = numSpacesToAdd / numWords;
        int a2 = numSpacesToAdd % numWords;
        char modifiedExtra = extra;
        while(a1 > 1)
        {
            modifiedExtra += extra;
            a1--;
        }
          
        while(s <= j)
        {
            if(s <= j){
                lines[k] += words.get(s);
                lines[k] += modifiedExtra;
                s++;
            }
            if(a2 > 0)
            {
                lines[k] += extra;
                a2--;
            }
        }
        
    }
    
    public static void main(String[] args) throws FileNotFoundException {
	Scanner in;
	int M = 80;
	char extra = ' ';
        ArrayList<Integer> wordLength = new ArrayList<>();
        ArrayList<String> words = new ArrayList<>();
        int numWords = 0;
        int extraSpaces[][];
        int lineCost[][];
	int cost[]; 
        int aux[];  // parallel array to cost array used to hold where line was split
        int numParagraphs = 1;
        String finalLine = "";
        Scanner finalScan;
	if(args.length == 0 || args[0].equals("-")) {
	    in = new Scanner(System.in);
	} 
        else {		
	    File inputFile = new File(args[0]);
	    in = new Scanner(inputFile);
	}
	if (args.length > 1) {
	    M = Integer.parseInt(args[1]);
	    if(M < 0) {
		M = -M;
		extra = '+';
	    }
	}
        wordLength.add(0);
        words.add("");
	while(in.hasNextLine()) {
	    String s = in.nextLine();
	    if(s.trim().isEmpty()) {
		// Empty line, marking end of paragraph
                numParagraphs++;
                
                
	    } else {  // Use scanner to break line into words
		Scanner strScanner = new Scanner(s);
		while(strScanner.hasNext()) {
		    String word = strScanner.next();  // Next word of input
                    words.add(word);
                    numWords++;
                    wordLength.add(word.length());
                }
	    }
        }
        
        
        // initialize arrays to the number of total words in the text
        extraSpaces = new int[numWords+1][numWords+1];
        lineCost = new int[numWords+1][numWords+1];
        cost = new int[numWords+1];
        aux = new int[numWords+1];
        
        // fill values in the extraSpaces array
        for(int i =1; i <= numWords; i++)
        {
            // for all values less than current i make their extraSpaces number negative
            for(int k = 0; k < i; k++)
            {
                extraSpaces[i][k] = -1;
            }
            extraSpaces[i][i] = M - wordLength.get(i);
            for(int j = i + 1; j <= numWords; j++)
            {
                extraSpaces[i][j] = extraSpaces[i][j-1] - wordLength.get(j) -1;
            }
        }
        
        // set zero values in extraSpaces array to negative 
        for(int i =0; i <= numWords; i++)
        {
            extraSpaces[0][i] = -1;
        }
        
        // fill values in lineCost array 
        for(int i = 1; i <= numWords; i++)
        {
            for(int j = i; j <= numWords; j++)
            {
                if(extraSpaces[i][j] < 0) // for sequence i-j of words that dont fit in a line
                {
                    lineCost[i][j] = Integer.MAX_VALUE;
                }
                else if(j == (numWords) && extraSpaces[i][j] >= 0) // for sequence of words i-j that is the final line
                {
                    lineCost[i][j] = 0;
                }
                else // for normal lines
                {
                   lineCost[i][j] = (int)Math.pow(extraSpaces[i][j], 3);
                }
            }
        }
    
        // find cost of splitting line at each value of i
        cost[0] = 0;
        aux[0] = 0;
        
        for(int j = 1; j <= numWords; j++)
        {
            cost[j] = Integer.MAX_VALUE/3;
            for(int i = 1; i <= j; i++)
            {
                if(cost[j] > (cost[i-1] + lineCost[i][j]) &&  (cost[i-1] + lineCost[i][j]) >= 0)
                {
                    cost[j] = (cost[i-1] + lineCost[i][j]);
                    aux[j] = i;
                }
            }
        }
        
        int k = configureLines(aux, numWords, words, extra, extraSpaces);
        
        for(int i = 1; i <=k; i++)
        {
            if(i == k)
            {
                finalScan = new Scanner(lines[k]);
                while(finalScan.hasNext()){
                    finalLine += finalScan.next();
                    finalLine += " ";
                }
            }
            else{
                System.out.println(lines[i]);
            }
        }
        System.out.println(finalLine);
        
        System.out.println(totalPenalty);
    }
    
}

