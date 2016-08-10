package uTaboo5;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uTaboo5.Encoder.EncoderException;
import uTaboo5.Setup.SetupException;

public class Command2 {

	public static void main(String[] args) throws SetupException, IOException, EncoderException, InterruptedException, ExecutionException {
		
		// SystemSetup
		//Takes care of all the input parameters
		List<File> aorgFolders = Setup.getAllFolders(args[0]); 
		List<File> borgFolders = Setup.getAllFolders(args[1]);
		String outputDir = Setup.directoryExists(args[2]);
		int wordLength = Setup.checkWordLength(args[3]);
		int mismatchThresh = Setup.checkMismatchThresh(args[4]);
		int exclude = Setup.checkExclude(args[5]); //i = intersect, d = disjoint, a = both
		int numbThreads = Setup.checkNumberOfCores(args[6]);
		 
		
		//Loops through the input and taboo folder and saves all fasta files
		List<File> aorgFiles = Setup.getFilesFromFolder(aorgFolders); 
		List<File> borgFiles = Setup.getFilesFromFolder(borgFolders); 
		
		//Creates Organism objects for all the fasta files
		ArrayList<Organism> aorgs = new ArrayList<Organism>(aorgFiles.size()); 
		ArrayList<Organism> borgs = new ArrayList<Organism>(borgFiles.size());
		
		for(File f : aorgFiles) {
			aorgs.add(new Organism(f.toString()));
		}
		
		for(File f : borgFiles) {
			borgs.add(new Organism(f.toString()));
		}
		
		//Initialisez the Encoder object
		int NcodeN = Setup.getNcodeN(wordLength);
		Encoder e = new Encoder(NcodeN);
		
		//Prints run parameters to console
		System.out.println("Run parameters:\n"
				+ "\tWord length: " + wordLength + "\n" 
				+ "\tMisMatch threshold: " + mismatchThresh + "\n" 
				+ "\tFilter type: " + exclude + " (0 = t-disjoint, 1 = t-intersection, 2 = both)\n");
		
		
		//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		//Iterate over all A organisms
		for(Organism a: aorgs) {
			long BEGIN = System.currentTimeMillis();
			/* For each A organism, create the initialSurvivorIndexList that contains all
			 * positions in its genome. 
			 */
			ArrayList<ArrayList<Integer>> finalSurvivorIndexList = FilterEngine.getInitialList(a, wordLength);
	
			// Calculation of number of initial surviving sequences in current a organism: equals all sequences initially.
			int sizy=0;
			for(ArrayList<Integer> v: finalSurvivorIndexList) {
				sizy=sizy+v.size();
			}
			
			System.out.println("-----------------------------------------------");
			System.out.println("Partial nr. \t Initial Size \t PostFilter Size");
			for(int i=0; i<a.getNumbPartials(); i++) {
				System.out.println(a.getPartials().get(i).getGi() + " \t "+  (a.getPartials().get(i).getSeqLength()-wordLength+1)+" \t " + finalSurvivorIndexList.get(i).size());
			}
			
			
			
			// Filtering through the Taboo Organisms.
			for(Organism b: borgs) {
				System.out.println("Filtering " + a.getGi() +" against " + b.getGi());
				
				// Filter organism a through the current TabooOrganism b 
				long t0 = System.currentTimeMillis();
				
				finalSurvivorIndexList = FilterEngine.filterMultiThreadRAM(a, b, wordLength, mismatchThresh, e, NcodeN, finalSurvivorIndexList, numbThreads);	
				long t1 = System.currentTimeMillis();
				
				
				System.out.println("\nFilter session took: " + ((t1-t0)/1000) +" sek.");
				
				// Calculation of number of surviving sequences in current a organism post-filtered through current b organism 
				
				sizy=0;
				for(ArrayList<Integer> v: finalSurvivorIndexList) {
					sizy=sizy+v.size();
				}
				
				System.out.println("-----------------------------------------------");
				System.out.println("Total snumber of survivied sequences: " + sizy);
				System.out.println("Partial nr. \t Initial Size \t PostFilter Size");
				for(int i=0; i<a.getNumbPartials(); i++) {
					System.out.println(a.getPartials().get(i).getGi() + " \t "+  (a.getPartials().get(i).getSeqLength()-wordLength+1)+" \t " + finalSurvivorIndexList.get(i).size());
				}
			
				
				// If no remaining sequences survive, exit and end program.
				if(sizy==0) {
					System.out.println("No results found.");
					break;
					//System.exit(0);
				}
			}
			
			
			// Determining the sise of the finalS ruvivor list: 
			sizy=0;
			for(ArrayList<Integer> v: finalSurvivorIndexList) {
				sizy=sizy+v.size();
			}
			
			System.out.println("-----------------------------------------------");
			System.out.println("Partial nr. \t Initial Size \t PostFilter Size");
			for(int i=0; i<a.getNumbPartials(); i++) {
				System.out.println(a.getPartials().get(i).getGi() + " \t "+  (a.getPartials().get(i).getSeqLength()-wordLength+1)+" \t " + finalSurvivorIndexList.get(i).size());
			}
		
			System.out.println("Total snumber of survivied sequences: " + sizy);
			if(sizy==0) {
				System.out.println("No results found.");
				System.exit(0);
			}
			
			
			//============================
			//Outputs the k-disjoint to file
			if(exclude == 0) {
				FilterEngine.resultsToFile(a, finalSurvivorIndexList, wordLength, outputDir, "D", mismatchThresh);
			}
			//Outputs the k-intersect to file
			else if(exclude == 1) {
				ArrayList<ArrayList<Integer>> intersect = FilterEngine.disjointToIntersection(a, finalSurvivorIndexList, wordLength);
				FilterEngine.resultsToFile(a, intersect, wordLength, outputDir, "I", mismatchThresh);
			}
			//Outputs both the k-disjoint and k-intersect to file
			else if(exclude == 2) {
				FilterEngine.resultsToFile(a, finalSurvivorIndexList, wordLength, outputDir, "D", mismatchThresh);
				ArrayList<ArrayList<Integer>> intersect = FilterEngine.disjointToIntersection(a, finalSurvivorIndexList, wordLength);
				FilterEngine.resultsToFile(a, intersect, wordLength, outputDir, "I", mismatchThresh);
			}
			
			//============================
			long END = System.currentTimeMillis();
			FilterEngine.printTime(a, outputDir, END-BEGIN, wordLength, mismatchThresh);
		}
		
			
	}// End Main
	
}// End CLass
