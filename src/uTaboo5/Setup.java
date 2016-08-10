package uTaboo5;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import uTaboo5.Setup.SetupException;

public class Setup {

	/**
	 * might be useful
	 */
	private static String OS = null;

	/**
	 * might be useful
	 */
	private static String FileSeparator = null;

	/**
	 * String array with all accepted file extensions
	 */
	private static String[] acceptedFileTypes = {"txt", "fna", "fasta"};

	
	
	/**
	 * Checks if the String representing the number of cores can be parsed to an integer.
	 * If so, checks if the integer is larger than 0.
	 * @param coresString The number of cores to be used
	 * @return number of cores, must be largen than 0
	 * @throws SetupException
	 */
	public static int checkNumberOfCores(String coresString) throws SetupException {
		String errorMsg = "Parameter cores: expected positive integer. (Was \"";
		int cores = 0;
		
		try {
			cores = Integer.parseInt(coresString);
			if (cores < 0) {
				throw new SetupException(errorMsg + cores + "\")");
			}
		}
		catch (Exception e) {
			throw new SetupException(errorMsg + coresString + "\")");
		}
		
		return cores;
	}
	
	
	
	public static String typeOfFilter(int i) {
		if(i == 0) {
			return "Single thread filtering";
		}
		else if (i == 1) {
			return "Standard Multi thread filtering";
		}
		else if (i == 2) {
			return "Multit thread RAM filtering";
		}
		return "";
	}
	
	
	
	
	
	/**
	 * might be useful
	 * @return
	 */
	public static String getOsName() {
		if (OS == null) {
			OS = System.getProperty("os.name");
		}
		return OS;
	}

	/**
	 * might be useful
	 * @return
	 */
	public static String getFileSeparator() {
		if(FileSeparator == null) {
			FileSeparator = File.separator;
		}
		return FileSeparator;
	}

	/**
	 * might be useful
	 * @param url
	 * @return
	 */
	public static boolean fileExists(String url) {
		File f = new File(url);
		if(f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the file extension is recognized
	 * @param url file to be checked
	 * @return true if file extension is recognized, SetupException otherwise
	 * @throws IOException
	 * @throws SetupException
	 */
	public static boolean checkFiletype(String url) throws IOException, SetupException {
		String[] parsedURL = url.split("\\.");
		String fileExtension = parsedURL[parsedURL.length-1];

		for(String extension : acceptedFileTypes) {
			if (extension.equals(fileExtension)) {				
				return true;
			}
		}

		throw new SetupException(url + " file type was not recognized");
	}

	/**
	 * Checks whether a certain folder with filepath "url" exists.
	 * @param url file path to folder
	 * @return true if folder exists, SetupException otherwise
	 * @throws SetupException
	 */
	public static boolean checkDirectory(String url) throws SetupException {
		File folder = new File(url);
		if(!folder.isDirectory()) {
			throw new SetupException(url + " is not a valid folder!");
		}
		return true;
	}

	/**
	 * Returns the absolute filepath to the folder corresponding to the input url as well as all subfolders
	 * of that folder.
	 * @param url filepath to folder
	 * @return a list with absoulte paths to input folder and all it's subfolders.
	 */
	public static List<File> getAllFolders(String url) {
		List<File> allFolders = new ArrayList<File>();
		allFolders.add(new File(url).getAbsoluteFile());
		allFolders.addAll(getAllFoldersHelp(url));

		return allFolders;
	}

	/**
	 * Help class to {@link Setup#getAllFolders(String)}
	 * @param url filepath to folder
	 * @return
	 */
	private static List<File> getAllFoldersHelp(String url) {
		List<File> allFolders = new ArrayList<File>();
		File[] listOfFolders = getOnlyFolders(url);

		allFolders.addAll(Arrays.asList(listOfFolders));
		for(File folders : listOfFolders) {
			if(folders.isDirectory()) {
				allFolders.addAll(getAllFoldersHelp(folders.getAbsolutePath()));
			}
		}

		return allFolders;
	}

	/**
	 * Help class to {@link Setup#getAllFoldersHelp(String)}. Removes all files from a File[] array
	 * so that only folders remains in the array.
	 * @param url filepath to folder
	 * @return File[] array containing only filepaths to folders
	 */
	private static File[] getOnlyFolders(String url) {
		File[] all = new File(url).listFiles();
		String[] onlyFolders = new String[all.length];
		int i = 0;

		for(File folders : all) {
			if(folders.isDirectory()) {
				onlyFolders[i] = folders.toString();
				i++;
			}
		}

		File[] returnOnlyFolders = new File[i];
		for(int j = 0; j < i; j++) {
			returnOnlyFolders[j] = new File(onlyFolders[j]);
		}

		all = null;
		onlyFolders = null;

		return returnOnlyFolders;
	}

	/**
	 * Gets all absolute filepaths to files from a List of folders.
	 * @param folders List with folders to retrieve filepaths from
	 * @return List with filepaths
	 * @throws IOException 
	 * @throws SetupException 
	 */
	public static List<File> getFilesFromFolder(List<File> folders) throws IOException, SetupException {
		List<File> files = new ArrayList<File>();

		for(File folder : folders) {
			File[] allFiles = new File(folder.toString()).listFiles();

			for(File file : allFiles) {
				if(file.isFile() && !file.isHidden()) {
					Setup.checkFiletype(file.getAbsolutePath());
					files.add(file.getAbsoluteFile());
				}
			}
		}
		return files;
	}

	/**
	 * Gets all absoulte filepaths to files from a folder
	 * @param url filepath to folder
	 * @return List with filepaths
	 * @throws SetupException 
	 * @throws IOException 
	 */
	public static List<File> getFilesFromFolder(String url) throws IOException, SetupException {
		File[] listOfFiles = new File(url).listFiles();
		List<File> filePaths = new ArrayList<File>();

		for(File file : listOfFiles) {
			if (file.isFile() && !file.isHidden()) {
				Setup.checkFiletype(file.getAbsolutePath());
				filePaths.add(file.getAbsoluteFile());
			}
		}
		return filePaths;
	}

	/**
	 * Checks if the first character of a file is a ">"
	 * @param url Filepath to file
	 * @return True if first character of file is ">", false otherwise
	 * @throws IOException
	 */
	public static boolean checkFastaHeader(String url) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(url)));	
		String s = br.readLine().substring(0, 1);	
		return s.equals(">");
	}
	
	/**
	 * Checks if the input String can be properly parsed to an integer. If so, this method
	 * also checks if the parsed string is divisible by 3,4 or 5 and is not equal to zero.
	 * @param wordLengthString The word length as a String
	 * @return the word length as an acceptable integer
	 * @throws SetupException
	 */
	public static int checkWordLength(String wordLengthString) throws SetupException {
		String errorMsg = "Parameter word length: expected postive integer divisible by 3,4 or 5. (Was \"";
		int wordLength = 0;
		
		try {
			wordLength = Integer.parseInt(wordLengthString);
			
			if(wordLength == 0) {
				throw new SetupException(errorMsg + wordLength + "\")");
			}
			
			if(wordLength%3 != 0) {
				if(wordLength%4 != 0) {
					if(wordLength%5 != 0) {
						throw new SetupException(errorMsg + wordLength + "\")");
					}
				}
			}
			
			
		}
		catch (Exception e){
			throw new SetupException(errorMsg + wordLengthString + "\")");
		}
		return wordLength;
	}
	
	/**
	 * Checks if the input String can be properly parsed to an integer. 
	 * If so, the parsed String is returned.
	 * @param threshString The amount of tolerated mismatches as a String
	 * @return the tolerated mismatches as an integer
	 * @throws SetupException
	 */
	public static int checkMismatchThresh(String threshString) throws SetupException {
		String errorMsg = "Parameter Mismatch threshold: expected positive integer. (Was \"";
		int thresh = 0;
		
		try {
			thresh = Integer.parseInt(threshString);
			if(thresh < 0) {
				throw new SetupException(errorMsg + thresh + "\")");
			}
		}
		catch (Exception e) {
			throw new SetupException(errorMsg + threshString + "\")");
		}
		
		return thresh;
		}
	
	/**
	 * Checks which NcodeN to use in descending order based on the word length. 
	 * An NcodeN of 5 is preferred over an NcodeN of 3.
	 * @param wordLength the word length to be used. This parameter is expected to be a valid word length.
	 * @return the NcodeN based on the word length
	 */
	public static int getNcodeN(int wordLength) {
		if(wordLength%5 == 0) {
			return 5;
		}
		else if(wordLength%4 == 0) {
			return 4;
		}
		return 3;
	}
	
	/**
	 * Checks if the directory exists 
	 * @param url path to directory
	 * @return true if directory exists, false otherwise
	 * @throws SetupException
	 */
	public static String directoryExists(String url) throws SetupException {
		File f = new File(url);
		if (f.exists()) return url;
		
		throw new SetupException("Directory (" + url + ") does not exist!");
	}
	
	public static int checkExclude(String excludeString) throws SetupException {
		excludeString = excludeString.toLowerCase();
		
		if(excludeString.equals("d")) return 0;
		
		else if(excludeString.equals("i")) return 1;
		
		else if (excludeString.equals("a")) return 2;
		
		else throw new SetupException("Argument exclude: expected d, i, a. Was: " + excludeString);
	}
	
	/**
	 * Only for testing purposes
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String url) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(url)));
		StringBuilder sb = new StringBuilder();
		String s;

		while((s = br.readLine()) != null) {
			sb.append(s);
		}

		br.close();
		return sb.toString();
	}

	/**
	 * Custom exception
	 * @author erique
	 *
	 */
	public static class SetupException extends Exception {
		public SetupException(String msg) {
			super(msg);
		}
	}

	
//	String orgAfolder = "/Users/RockefellerSuperstar/Desktop/test1/orgA";
//	String orgBfolder = "/Users/RockefellerSuperstar/Desktop/test1/orgB";
	
	
	public static void main(String[] args) throws SetupException, InterruptedException, IOException {
		Scanner scan = new Scanner(System.in);
		System.out.println(Setup.getNcodeN(Setup.checkWordLength("12")));
		System.out.println(Setup.checkMismatchThresh("0"));
		//String orgAfolder = "C:/Users/erique/Desktop/org/orgAfolder";
		//String orgBfolder = "C:/Users/erique/Desktop/org/orgBfolder";
		System.out.println("Absolute path to organism A folder:");
		String orgAfolder = scan.nextLine();
		System.out.println("Absolute path to organism B folder:");
		String orgBfolder = scan.nextLine();
		System.out.println("===================================\n");
		
		System.out.println("Getting and displaying folders:\n");
		List<File> foldersB = Setup.getAllFolders(orgBfolder);
		for(int i = 0; i < foldersB.size(); i++) {
			System.out.println(foldersB.get(i));
		}
		
		System.out.println("===================================");
		System.out.println("Getting and displaying files from folders (method 1):\n");
		for(int i = 0; i < foldersB.size(); i++) {
			List<File> filesB = Setup.getFilesFromFolder(foldersB.get(i).toString());
			for(int j = 0; j < filesB.size(); j++) {
				System.out.println(filesB.get(j));
			}
		}
		
		System.out.println("===================================");
		System.out.println("Getting and displaying files from folders (method 2):\n");
		List<File> filesB2 = Setup.getFilesFromFolder(foldersB);
		for(int i = 0; i < filesB2.size(); i++) {
			System.out.println(filesB2.get(i));
		}
		
		System.out.println("===================================");
		System.out.println("Checking fasta headers:\n");
		for(File f : filesB2) {
			System.out.println(f.toString() + " HAS FASTA HEADER: " + Setup.checkFastaHeader(f.toString()));
		}
		
	}
}
