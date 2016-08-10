package uTaboo5;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import uTaboo5.FastaChunk.FastaChunkException;

public class FastaChunkTest {

	@Test (expected = FastaChunkException.class)
	public void FastaChunkExceptionTest1() throws FastaChunkException {
		throw new FastaChunkException("Bla");	
	}
	
	@Test
	public void getterTest1() throws IOException {
		String s1 = ">blablabla\n"+"AAAAATTTTT\nTTTTTTTT";
		FastaChunk f1 = new FastaChunk(s1);
		
		boolean b1 = f1.getHead().equals(">blablabla");
		boolean b2 = f1.getBody().equals("AAAAATTTTTTTTTTTTT");
		boolean b3 = f1.getHead().equals("blablabla\n");
		boolean b4 = f1.getHead().equals("blablabla");
		
		boolean[] actual  = {b1, b2, b3, b4};
		boolean [] expected = {true, true, false, false};
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void guillotineTest1() throws FastaChunkException {
		String s1 = ">bla1\n"+"AAA";
		String s2 = ">bla2\n"+"AAA\n";
		String s3 = s1+"\n"+s2;
		
		String[] a1 = FastaChunk.guillotine(s1);	
		String[] a2 = FastaChunk.guillotine(s2);
		String[] a3 = FastaChunk.guillotine(s3);
		
		boolean b1 = a1[0].equals(">bla1") && a1[1].equals("AAA");
		boolean b2 = a2[0].equals(">bla2") && a2[1].equals("AAA");
		boolean b3 = a3[0].equals(">bla1") && a3[1].equals("AAA>bla2AAA");
		
		boolean[] actual = {b1, b2, b3};
		boolean[] expected = {true, true, true};
		
		assertArrayEquals(expected, actual);
	}

	
	
	@Test
	public void guillotine1Test1() throws FastaChunkException, IOException {
		String s1 = ">bla1\n"+"AAA";
		String s2 = ">bla2\n"+"AAA\n";
		String s3 = s1+"\n"+s2;
		
		String[] a1 = FastaChunk.guillotine1(s1);	
		String[] a2 = FastaChunk.guillotine1(s2);
		String[] a3 = FastaChunk.guillotine1(s3);
		
		boolean b1 = a1[0].equals(">bla1") && a1[1].equals("AAA");
		boolean b2 = a2[0].equals(">bla2") && a2[1].equals("AAA");
		boolean b3 = a3[0].equals(">bla1") && a3[1].equals("AAA>bla2AAA");
		
		boolean[] actual = {b1, b2, b3};
		boolean[] expected = {true, true, true};
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void fastachunkCreatorTest1() throws IOException {
		
		FastaChunk f1 = FastaChunk.fastaChunckCreator(">Bla1\nABA");
		
		boolean b1 = f1.getHead().equals(">Bla1");
		boolean b2 = f1.getBody().equals("ABA");
		
		boolean[] actual = {b1, b2};
		boolean[] expected = {true, true};
		
		assertArrayEquals(actual, expected);
		
	}

}
