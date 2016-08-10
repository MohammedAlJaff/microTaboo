package uTaboo5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import uTaboo5.Encoder.EncoderException;

public class FilterEngine {



	public static class partialThreadRAMSKIP implements Callable {

		PartialOrganism a;
		Dictree bdt;
		ArrayList<Integer> survivorIndexList;
		int begin;
		int endEx; 
		boolean exclude;
		int thresh;
		int seqWordLength;
		ArrayList<HashMap<Integer, ArrayList<int[]>> > hash;
		Encoder e;
		int currentMM = 0;
		int didLastDie = 1;

		public partialThreadRAMSKIP(PartialOrganism a, Dictree bdt, Encoder e,  int seqWordLength, int thresh, ArrayList<Integer> survivorIndexList, int begin, int endEx, 
				ArrayList<HashMap<Integer, ArrayList<int[]>>> h, boolean exclude) {
			this.a = a;
			this.bdt = bdt;
			this.survivorIndexList = survivorIndexList;
			this.begin = begin;
			this.endEx = endEx;
			this.exclude = exclude;
			this.hash = h;	
			this.thresh = thresh;
			this.seqWordLength=seqWordLength;
			this.e = e;


		}

		public ArrayList<Integer> call() throws EncoderException{
			ArrayList<Integer> partialSurvivorIndexList = new ArrayList<Integer>(this.endEx-this.begin);
			String tempSeq;
			int tempPos;
			int[] tempVals = {0, 9999, 0, 0, 0};	//return, mm, lastDied, posBefore, posCurrent


			long a1000 = System.currentTimeMillis(); 
			for(int listIndex= this.begin; listIndex<this.endEx; listIndex++ ) {
				tempPos = survivorIndexList.get(listIndex);

				if(listIndex%100000==0) {
					long b1000 = System.currentTimeMillis();
					System.out.println("\t\tSeq nr. " + listIndex + " out of " + survivorIndexList.size() + " from " + Thread.currentThread().getId());
					System.out.println("\t\t\tElapsed timed : " + (b1000-a1000)/1000 + "sek");
					a1000 = System.currentTimeMillis();
				}

				tempSeq = a.getSeq().substring(tempPos, tempPos+seqWordLength);


				tempVals[4] = tempPos;
				tempVals = bdt.containsWithin3(e.encode(tempSeq) , this.thresh, this.e, this.hash, tempVals);				
				tempVals[3] = tempPos;

				if(tempVals[0] == 0)  {
					partialSurvivorIndexList.add(tempPos);
					//System.out.println(tempPos);
				}				

			}
			System.out.println("Thread " + Thread.currentThread().getId() + " returned indexList of size " + partialSurvivorIndexList.size());
			return partialSurvivorIndexList;
		}


	}



	public static ArrayList<ArrayList<Integer>> getInitialList(Organism aorg, int seqWordLength){

		ArrayList<ArrayList<Integer>> initialSurvivorIndexList = 
				new ArrayList<ArrayList<Integer>>(aorg.getNumbPartials());

		for(int z=0; z<aorg.getNumbPartials(); z++) {

			ArrayList<Integer> temp = new ArrayList<Integer>(aorg.getPartials().get(z).getSeqLength()+1-seqWordLength);

			for(int i=0; i<aorg.getPartials().get(z).getSeqLength()+(1-seqWordLength) ; i++) {
				temp.add(i);
				//System.out.println(temp.get(i));
			}

			initialSurvivorIndexList.add(temp);
		}

		return initialSurvivorIndexList;

	}

	// Multithread RAM
	public static ArrayList<ArrayList<Integer>> filterMultiThreadRAM(Organism aorg, Organism borg, int seqWordLength, int thresh, Encoder e, int NcodeN,
			ArrayList<ArrayList<Integer>> initialSurvivorIndexList, int numbThreads) throws EncoderException, InterruptedException, ExecutionException {

		// Create a organismSurvivorIndexList 
		ArrayList<ArrayList<Integer>> organismSurvivorIndexList = new ArrayList<ArrayList<Integer>>(aorg.getNumbPartials());

		//		for(int z=0; z<aorg.getNumbPartials(); z++) {
		//			ArrayList<Integer> partialSurvivorList = new  ArrayList<Integer>(aorg.getPartials().get(z).getSeqLength()+1-seqWordLength);
		//			organismSurvivorIndexList.add(partialSurvivorList);
		//		}

		// Generate and populate Dictree for uuuTaboo555 organism b.
		Dictree bdt = Dictree.createDictreeRAM(borg, e, seqWordLength, NcodeN);
		System.out.println("Dictree complete.");

		// Generate number of LuckUp hashmaps.
		ArrayList<HashMap<Integer, ArrayList<int[]>>> h;

		if(thresh > NcodeN) {
			h = new ArrayList<HashMap<Integer, ArrayList<int[]>>>(NcodeN+1);
			for(int i=0; i < NcodeN+1; i++) {
				h.add(Encoder.getListIndicator(e, i));
			}
		}
		else {
			h = new ArrayList<HashMap<Integer, ArrayList<int[]>>>(thresh+1);
			for(int i=0; i < thresh+1; i++) {
				h.add(Encoder.getListIndicator(e, i));
			}
		}

		// This list will catchALL subPartialSurvivorIndexList generated from a thread.

		for( int j=0; j<aorg.getNumbPartials(); j++) {
			System.out.println("\tPartial nr:" + (j+1) + " out of " + initialSurvivorIndexList.size());

			PartialOrganism a = aorg.getPartials().get(j);
			//ArrayList<Integer> sIndxList = survivorIndexList.get(j);
			int size = initialSurvivorIndexList.get(j).size();

			// make numbThreads amounts of threads.
			ThreadPoolExecutor ex = (ThreadPoolExecutor) Executors.newFixedThreadPool(numbThreads);
			ArrayList<partialThreadRAMSKIP> threads = new ArrayList<partialThreadRAMSKIP>(numbThreads);
			ArrayList<Future<ArrayList<Integer>>> res = new ArrayList<Future<ArrayList<Integer>>>(numbThreads);

			// Populate ArrayList containing the threads that read to be executed next.
			/*for(int i=0; i<numbThreads; i++) { 
				threads.add(new partialThreadRAM(a, bdt, e, seqWordLength, thresh, initialSurvivorIndexList.get(j),
						Math.floorDiv(size, numbThreads)*i, Math.floorDiv(size, numbThreads)*(i+1), h, exclude));
			}*/


			//========================
			int indx = 0;
			while(indx < numbThreads-1) {
				threads.add(new partialThreadRAMSKIP(a, bdt, e, seqWordLength, thresh, initialSurvivorIndexList.get(j), 
						(size/numbThreads)*indx, (size/numbThreads)*(indx+1), h, true));
				indx++;
			}
			threads.add(new partialThreadRAMSKIP(a, bdt, e, seqWordLength, thresh, initialSurvivorIndexList.get(j), 
					(size/numbThreads)*indx, size, h, true));
			//========================

			System.out.println("\n\tFireing threads.");
			for(int i=0; i<numbThreads; i++) {
				res.add(ex.submit(threads.get(i)));
			}

			ex.shutdown();

			// WARNING!!!!!!!!!!
			//ex.awaitTermination(6, TimeUnit.HOURS);


			ArrayList<Integer>  postMulti = new ArrayList<Integer>();
			System.out.println("\t\tInitial size of postMulti: " + postMulti.size());
			for(Future<ArrayList<Integer>> f: res) {
				for(int i=0; i<f.get().size(); i++) {
					postMulti.add(f.get().get(i));
				}
				System.out.println("\t\tpostMult size after addition: " + postMulti.size());
			}


			//			for(Integer i: postMulti) {
			//				System.out.println(i);
			//			}

			Collections.sort(postMulti);
			System.out.println("postMulti size of partial after Sorting: "+ postMulti.size());;
			//survivorIndexList.set(j, postMulti);
			organismSurvivorIndexList.add(postMulti);

		}

		System.out.println("survivorIndexList size of : "+ organismSurvivorIndexList.size());
		for(ArrayList<Integer> i: organismSurvivorIndexList) {
			System.out.println("\t>>>" + i.size());
		}


		return organismSurvivorIndexList;






	}


	
	/**
	 * This method returns the complement of the disjoint result set after a full filtering. 
	 * @param aorg
	 * @param disjoint
	 * @param seqWordLength
	 * @return
	 */
	public static ArrayList<ArrayList<Integer>> disjointToIntersection(Organism aorg, ArrayList<ArrayList<Integer>> disjoint, int seqWordLength) {
		ArrayList<ArrayList<Integer>> intersect = new ArrayList<ArrayList<Integer>>(disjoint.size());

		for(int i = 0; i < disjoint.size(); i++) {
			HashSet<Integer> h = new HashSet<Integer>(disjoint.get(i).size());
			for(int j = 0; j < disjoint.get(i).size(); j++) {
				h.add(disjoint.get(i).get(j));
			}

			intersect.add(new ArrayList<Integer>());

			for(int j = 0; j < aorg.getPartials().get(i).getSeqLength()-seqWordLength+1; j++) {
				if(!h.contains(j)) {
					intersect.get(i).add(j);
				}
			}

		}

		return intersect;
	}

	/**
	 * tHIS METHOD PRINTS THE FINAL RSULT OF A FILTERING OF AN a AGOINST ALL TABOO ORGANISMS TO A FILE.
	 * @param o
	 * @param survivorIndexList
	 * @param seqWordLength
	 * @param outputDir
	 * @param exclude
	 * @param thresh
	 * @throws IOException
	 */
	public static void resultsToFile(Organism o, ArrayList<ArrayList<Integer>> survivorIndexList, int seqWordLength, String outputDir, String exclude, int thresh) throws IOException {

		String[] extension = o.getGi().split("\\.");
		String n = "";
		for(int i = 0; i < extension.length-1; i++) {
			n = n + extension[i];
		}

		File f = new File(outputDir + "/" + n + "W" + seqWordLength + "T" + thresh + "_" + exclude + ".txt");
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);

		if( (survivorIndexList.size() != o.getNumbPartials()) ) {
			throw new RuntimeException("Survvivor does not match");
		};


		for(int i = 0; i<survivorIndexList.size(); i++) {

			bw.write(o.getPartials().get(i).getGi()+"\n");

			for(int j=0; j<survivorIndexList.get(i).size(); j++) {

				int pos = survivorIndexList.get(i).get(j);
				String outString = o.getPartials().get(i).getSeq().substring(pos, pos+seqWordLength);
				bw.write(pos + "," + outString+"\n");
				//bw.write(outString+"\n");
			}
		}

		bw.close();



	}

	public static void printTime(Organism o, String outputDir, long t, int seqWordLength, int thresh) throws IOException {
		String[] extension = o.getGi().split("\\.");
		String n = "";
		for(int i = 0; i < extension.length-1; i++) {
			n = n + extension[i];
		}

		File f = new File(outputDir + "/" + n + "W" + seqWordLength + "T" + thresh + "_Time.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		bw.write("Runtime: " + t + " ms");
		bw.close();
	}



}
