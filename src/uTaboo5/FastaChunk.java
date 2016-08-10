package uTaboo5;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class FastaChunk {

	private String head;
	private String body;
	
	public FastaChunk(String s) throws IOException {
	
		String[] temp;
		//temp = guillotine(s);
		temp = guillotine1(s);

		this.head = temp[0]; 
		this.body = temp[1];
		
	}
	
	/**
	 * CHoops of the first Line of a string, the head, and returns it as
	 * the first element of the returned String array. 
	 * The second element is the rest of the input string. 
	 * @param s
	 * @return
	 */
	public static String[] guillotine(String s) {
			
		String[] temp = s.split("\n"); 
		
		String head = temp[0];
		String body = "";
		
		for (int i=1; i<temp.length;i++) {
			body = body+temp[i];
			System.out.println("fastachunk loop: " +i);
		}
		
		String[] whole = new String[2];
		whole[0] = head;
		whole[1] = body;
		
		return whole;
	
	}
	
	/**
	 * Alternatv version of the above method, that uses InputStreams to accelerate the process.
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static String[] guillotine1(String s) throws IOException {
		String[] whole = new String[2];
		
        InputStream is = new ByteArrayInputStream(s.getBytes(Charset.forName("UTF-8")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
		StringBuilder body = new StringBuilder(s.length());
		
		String head = bf.readLine();
		String tempString ="";
		
		while( (tempString = bf.readLine()) != null) {
			
			body.append(tempString);
		}
	
		whole[0] = head;
		whole[1] = body.toString();
		return whole;
	}	
	
	
	/**
	 * A static FastaChunk factory.
	 * @param string
	 * @return
	 * @throws IOException
	 */
	public static FastaChunk fastaChunckCreator(String string) throws IOException {
		return new FastaChunk(string);
	}
	
	/**
	 * Returns the Head of the chunk as a string IN A SINGLE LINE WITHOUT A LINE BREAK.
	 * @return
	 */
	public String getHead()  {
		if (head != null) {
			return head;	
		} else {
			throw new RuntimeException();
		}
		
	}
	
	/**
	 * Returns the body of the chunk as a complete and single line.
	 * @return
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Returns a string where the first line contains the head of the 
	 * chunk and the next line is the body of the sequence as a single line.
	 */
	public String toString() {
	
			return this.getHead()+"\n"+this.getBody();
	}

	/**
	 * Custom classException
	 * @author RockefellerSuperstar
	 *
	 */
	public static class FastaChunkException extends Exception {
		
		public FastaChunkException(String msg) {
			super(msg);
		}
		
		
	}
	
}
