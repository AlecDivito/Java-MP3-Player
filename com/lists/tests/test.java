package com.lists.tests;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;


public class test {

	public static void main(String[] args) throws URISyntaxException {
		new test();
	}

	public test() throws URISyntaxException {
		File f= new File("bin\\com\\lists\\files\\DirectroyLocations.txt");
		if(f.exists()) {
			System.out.println("yes");
		} else
			System.out.println("no");

		System.out.println(f.getAbsolutePath());


		System.out.println("\n\n\n\n\n");


		   //    String f1 = test.class.getResource("files/DirectroyLocations.txt").getFile();
		       File file = Paths.get(getClass().getResource("files/DirectroyLocations.txt").toURI()).toFile();
		     //  File file = new File(f1);
		       if(file.exists()){
		    	   System.out.println("yesss");
		       } else
		    	   System.out.println( "2 nope");

		       System.out.println(file.getAbsolutePath());

	}

}
