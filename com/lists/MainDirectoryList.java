package com.lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A list to store all the folders to be searched by songList
 * to find mp3 files.
 * @author Alec Quinn Di Vito*/
public class MainDirectoryList {
	private File path;
	private int size;
	private String[] pathWays;

	public MainDirectoryList(){
		try {
			path = Paths.get(getClass().getResource("files/DirectroyLocations.txt").toURI()).toFile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		size = 0;
		pathWays = new String[4];

		if(!path.exists())
			createFile();
		else
			setPathWays();
	}

	/** Create the Directory locations file if it does not exist */
	private void createFile(){
		try (PrintWriter writer = new PrintWriter(path, "utf-8")) {
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/** Read the Directory locations file and obtain all the
	 *  directory file locations Store Strings in an array.
	 * @throws FileNotFoundException*/
	private void setPathWays(){
		try(Scanner in = new Scanner(path)){
			while(in.hasNextLine()){
				String nextLine = in.nextLine();
				if(doesConatian(nextLine)){
					pathWays[size] = nextLine;
					size++;
					if(pathWays.length <= size){
						growArrayList(size);
					}
				}
			}
		} catch(FileNotFoundException e){
			System.out.println("path, path(): the file path was not found");
			createFile();
		}
	}

	/** Check to see if the folder has already been searched
	 * and is stored in the arraylist
	 * @param String str
	 * @return if the arrayList does not contain the folder
	 * return true, else return false*/
	public boolean doesConatian(String str){
		for(int i = 0; i < size; i++){
			if(pathWays[i].equals(str)){

				/*
				 * should delete the string that was found as a dup
				 */
				return false;
			}
		}
		return true;
	}

	/** Extend the array list with 4 more spots in it */
	private void growArrayList(int newSize){
		String[] newPathWays = new String[newSize + 4];
		System.arraycopy(pathWays, 0, newPathWays, 0, newSize);
		pathWays = newPathWays;
	}

	/** add a directory to the list so it can be found
	 * when refilling the default songs And if the folder is
	 * not in the list, append it to directoryLocations.txt*/
	public void addDirectory(File[] folders){
		if(pathWays.length + folders.length <= size){
			growArrayList(pathWays.length + folders.length);
		}

		ArrayList<String> newDirectorys = new ArrayList<String>();

		for(int i = 0; i < folders.length; i++){
			if(doesConatian(folders[i].toString())){
				pathWays[size] = folders[i].toString();
				newDirectorys.add(folders[i].toString());
				size++;
			}
		}

		if(newDirectorys.size() > 0){
			try(PrintWriter pw = new PrintWriter(new FileWriter(path, true))){
				for(int i = 0; i < newDirectorys.size(); i++){
					if(!newDirectorys.get(i).equals(""))
						pw.println(newDirectorys.get(i).toString());
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/** Return the size of the arrayList
	 * @return Size of arrayList*/
	public int getSize(){
		return size;
	}

	/** @return The directory at the given location (index) */
	public String getDirectory(int i){
		if(new File(pathWays[i]).exists())
			return pathWays[i];
		else
			return "";
	}

}
