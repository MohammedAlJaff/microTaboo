package uTaboo5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class microEvolve {

	public static void main(String[] args) throws IOException {
		Organism o = new Organism("C:/Users/erique/Desktop/tabaos/A/Sc.fna");
		File f = new File("C:/Users/erique/Desktop/tabaos/A/ScNew.fna");
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);

		HashMap<Character, Character> h = new HashMap<Character, Character>();
		h.put('A', 'G');
		h.put('G', 'A');
		h.put('T', 'C');
		h.put('C', 'T');
		h.put('N', 'N');
		
		ArrayList<ArrayList<Integer>> hitList = new ArrayList<ArrayList<Integer>>();
		
		for(PartialOrganism p : o.getPartials()) {
			
			// Print header of partial to file;
			bw.write(p.getGi() + "\n");
			
			
			// Generate positions of bases to alter.
			//int[] pos = new int[(int)(Math.random()*10)+1];
			int[] pos = new int[5];
			
			for(int i = 0; i < pos.length; i++) {
				pos[i] = (int)(Math.random()*p.getSeqLength());
			}
			
			StringBuilder aP = new StringBuilder();
			aP.append(p.getSeq());
			
			for(int i: pos){
				aP.replace(i, i+1, h.get(aP.charAt(i)).toString());
			}
			
			
			
			
			
//			for(int i=0 ; i< p.getSeqLength(); i++){
//				if(i%80==0){
//					bw.write("\n" + aP.substring(i, i+1));
//				} else {
//					bw.write(aP.substring(i, i+1));		
//				}
//			}
			int indx = 0;
			while(indx+80 < aP.length()) {
				String s = aP.substring(indx, indx+80);
				bw.write(s + "\n");
				indx = indx+80;
			}
			
			
			
			bw.write(aP.substring(indx)+"\n");
			
			
			System.out.println(p.getGi());
			for(int i = 0; i < pos.length; i++) {
				System.out.print(" " + pos[i]);
				
			}
			System.out.println("");
		
		}
		bw.close();
	}

}
