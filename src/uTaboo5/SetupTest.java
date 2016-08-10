package uTaboo5;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uTaboo5.Encoder.EncoderException;
import uTaboo5.Setup.SetupException;

public class SetupTest {

	@Test
	public void GetAllFolders_NumberOfFoldersTest1() {
		String orgBfolder = "C:/Users/erique/Desktop/org/orgBfolder";
		List<File> foldersB = Setup.getAllFolders(orgBfolder);

		assertEquals(4, foldersB.size());
	}

	@Test
	public void GetAllFolders_NameOfFoldersTest1() {
		String orgBfolder = "C:/Users/erique/Desktop/org/orgBfolder";
		List<File> foldersB = Setup.getAllFolders(orgBfolder);
		List<String> fBString = new ArrayList<String>();

		for(File f : foldersB) {
			fBString.add(f.toString());
		}

		List<File> e = new ArrayList<File>();
		e.add(new File("C:/Users/erique/Desktop/org/orgBfolder"));
		e.add(new File("C:/Users/erique/Desktop/org/orgBfolder/S.aureus"));
		e.add(new File("C:/Users/erique/Desktop/org/orgBfolder/S.kns"));
		e.add(new File("C:/Users/erique/Desktop/org/orgBfolder/S.kns/S.epi"));
		List<String> eString = new ArrayList<String>();

		for(File f: e) {
			eString.add(f.toString());
		}

		boolean b = true;
		for(int i = 0; i < eString.size(); i++) {
			b = b && eString.get(i).equals(fBString.get(i));
		}

		assertEquals(true, b);
	}

	@Test
	public void GetOnlyFoldersTest1() throws SetupException {
		boolean b = Setup.checkDirectory("C:/Users/erique/Desktop/org/orgBfolder");
		assertEquals(true, b);
	}

	@Test (expected = SetupException.class)
	public void GetOnlyFoldersTest2() throws SetupException {
		Setup.checkDirectory("dinmamma");
	}

	@Test
	public void CheckFileExtensionTest1() throws IOException, SetupException {
		String[] url = {"hello.txt", "he.llo.txt", "hello..txt"};
		boolean[] actual = new boolean[url.length];
		boolean[] expected = new boolean[url.length];

		for(int i = 0; i < url.length; i++) {
			actual[i] = Setup.checkFiletype(url[i]);
			expected[i] = true;
		}

		assertArrayEquals(expected, actual);
	}
	
	@Test (expected = SetupException.class)
	public void CheckFileExtensionTest2() throws IOException, SetupException {
		String url = "hello.t.xt";
		boolean actual = Setup.checkFiletype(url); 
	}
}

