package uTaboo5;

import static org.junit.Assert.*;

import org.junit.Test;

import uTaboo5.Encoder.EncoderException;

import java.util.*;

public class EncoderTest {

	@Test
	public void kmmStringMistachTest1()	{
		String s1 = "AATT";
		String s2 = "AATA";

		boolean b1 = Encoder.withinKMismatches(s1, s2, 0);
		boolean b2 = Encoder.withinKMismatches(s1,s2,1);
		boolean b3 = Encoder.withinKMismatches(s1,s2,2);
		
		boolean[] actual =  {b1, b2, b3};
		boolean[] expected = {false, true, true};
		
		assertArrayEquals(expected, actual);
	}

	@Test
	public void numbMismatchStringNumberOfMistachesTest1()	{
		String s1 = "AATT";
		String s2 = "AATA";
		String s3 = "ATTT";
		String s4 = "TTTT";
 
		int b0 = Encoder.numbMismatch(s1, s1);
		int b1 = Encoder.numbMismatch(s1, s2);
		int b2 = Encoder.numbMismatch(s1, s3);
		int b3 = Encoder.numbMismatch(s2, s3);
		int b4 = Encoder.numbMismatch(s1, s4);
		int b5 = Encoder.numbMismatch(s3, s4);
		
		int[] actual =  {b0, b1, b2, b3, b4, b5};
		int[] expected = {0, 1, 1, 2, 2, 1};
		
		assertArrayEquals(expected, actual);
	}

	@Test 
	public void encodeStringToArrayTest1() throws uTaboo5.Encoder.EncoderException {
		Encoder e = new Encoder(5);
		
		String s1 = "AAAAATTTTT";
		String s2 = "AAAACAAAAG";
		String s3 = "TTTTTTTTTG";
		
		int[][] expRes = {
				{0, 1023}, 
				{1, 2},
				{1023, 1022}
		};
		
		boolean b1 = java.util.Arrays.equals(expRes[0], e.encode(s1) ) ;
		boolean b2 = java.util.Arrays.equals(expRes[1], e.encode(s2) ) ;
		boolean b3 = java.util.Arrays.equals(expRes[2], e.encode(s3) ) ;
		
		boolean[] actual = {b1,b2,b3};
		boolean[] expected = {true, true, true};
		
		assertArrayEquals(expected, actual);
	}

	@Test
	public void MismatchesWithNTest1() throws EncoderException {
		String[] s = {"AANN", "AANA", "AATN", "AAAA"};
		Encoder e = new Encoder(2);
		int[] encoded1 = e.encode(s[0]);
		int[] encoded2 = e.encode(s[1]);
		int[] encoded3 = e.encode(s[2]);
		int[] encoded4 = e.encode(s[3]);
		
		int[] exp1 = {0, 24};
		int[] exp2 = {0, 20};
		int[] exp3 = {0, 19};
		int[] exp4 = {1, 1};
		
		boolean b1 = Arrays.equals(encoded1, exp1);
		boolean b2 = Arrays.equals(encoded2, exp2);
		boolean b3 = Arrays.equals(encoded3, exp3);
		boolean b4 = Arrays.equals(encoded4, exp4);
		
		boolean[] actual = {b1, b2, b3, b4};
		boolean[] expected = {true, true, true, false};
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void withinKMismatchesNcoded() throws EncoderException {
		String s1 = "AATT";
		String s2 = "AATA";
		Encoder e = new Encoder(4);
		
		int[] c1 = e.encode(s1);
		int[] c2 = e.encode(s2);

		boolean b1 = e.withinKMismatches(c1, c2, 0);
		boolean b2 = e.withinKMismatches(c1, c2, 1);
		boolean b3 = e.withinKMismatches(c1, c2, 2);
		
		boolean[] actual =  {b1, b2, b3};
		boolean[] expected = {false, true, true};
		
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void numbMismatchNCodeNumberOfMistachesTest1() throws EncoderException	{
		String s1 = "AATT";
		String s2 = "AATA";
		String s3 = "ATTT";
		String s4 = "TTTT";
		
		Encoder e = new Encoder(4);
		int[] c1 = e.encode(s1);
		int[] c2 = e.encode(s2);
		int[] c3 = e.encode(s3);
		int[] c4 = e.encode(s4);
 
		int b0 = e.numbMismatch(c1, c1);
		int b1 = e.numbMismatch(c1, c2);
		int b2 = e.numbMismatch(c1, c3);
		int b3 = e.numbMismatch(c2, c3);
		int b4 = e.numbMismatch(c1, c4);
		int b5 = e.numbMismatch(c3, c4);
		
		int[] actual =  {b0, b1, b2, b3, b4, b5};
		int[] expected = {0, 1, 1, 2, 2, 1};
		
		assertArrayEquals(expected, actual);
	}
	
	@Test (expected = EncoderException.class)
	public void exceptionIllegalLengthTest1() throws EncoderException {
		Encoder e = new Encoder(4);
		String s1 = "ATATAT";
		
		e.encode(s1);
	}
}
