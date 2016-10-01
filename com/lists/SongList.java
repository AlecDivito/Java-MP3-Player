package com.lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * A List to store all the directory's of songs.
 * DO NOT USE FOR ANYTING ELSE!
 * @author Alec Quinn Di Vito*/
public class SongList {

	private String[] SongArray;
	private MainDirectoryList path;

	private int size;
	private final int INITIAL_CAPACITY = 100;

	public SongList(){
		SongArray = new String[100];
		path = new MainDirectoryList();
		size = 0;

		returnSongArrayToDefault();
	}

	/** Adds a new folder of songs directory's to the arrayList. Remember to
	 * update the View you are using to display the the info taken from the
	 * array list with the new elements added.
	 * @param File folder, boolean newFolder */
	public void addFolderToList(File[] folder, boolean newFolder){
		if(newFolder)
			path.addDirectory(folder);

		for(int i = 0; i < folder.length; i++){
			if(!path.doesConatian(folder[i].toString())){
				File[] listOfFiles = folder[i].listFiles();
				for(int j = 0; j < folder[i].listFiles().length; j++){
					if(getFileExtension(listOfFiles[j]).equals("mp3")){
						checkArraySpace();
						SongArray[size] = listOfFiles[j].getAbsolutePath();
						size++;

					}
				}
			}
		}
	}

	/** Check the size of the array list.
	 * Grow the ArrayList to store 100 more directory's if the arrayList is filled */
	private void checkArraySpace() {
		if(size >= SongArray.length){
			String[] newSongs = new String[size + 100];
			System.arraycopy(SongArray, 0, newSongs, 0, size);
			SongArray = newSongs;
		}
	}

	/** Cut the array down to the correct size */
	private void trimToSize(){
		String[] newSongs = new String[size];
		System.arraycopy(SongArray, 0, newSongs, 0, size);
		SongArray = newSongs;
	}

	/** Sort the array by the files name */
	private void sort(){
		int i, j;
        String temp;
	    for ( i = 0;  i < SongArray.length - 1;  i++ ) {
	    	for ( j = i + 1;  j < SongArray.length;  j++ ) {
	    		if ( SongArray[i].substring(SongArray[i].lastIndexOf("\\")).compareToIgnoreCase( SongArray[j].substring(SongArray[j].lastIndexOf("\\")) ) > 0 ){
	    			// ascending sort
	    			temp = SongArray [i];
	    			SongArray [i] = SongArray [j];    // swapping
	    			SongArray [j] = temp;
	    		}
	    	}
	    }
	}

	/** get the extension from a file
	 * @param File file
	 * @return String*/
	private String getFileExtension(File file){
		String name = file.getName();
		try{
			return name.substring(name.lastIndexOf(".") + 1);
		} catch(Exception e){
			return "";
		}
	}

	/** Return the String at i
	 * @param int i
	 * @return String from the arraylist */
	public String getSongDirectory(int i){
		return SongArray[i];
	}

	/** return the index with the given location name.
	 * else return -1 if the index was not found */
	public int getIndex(String fileName){
		for(int i = 0; i < size; i++){
			if(new File(SongArray[i]).getName().equals(fileName)){
				return i;
			}
		}

		return -1;
	}

	/** return the name of the directory stored inside SongArray
	 * @param String location
	 * @return The name of the directory -- > ex. ( C:\\User\\Alec\\song.mp3 ) -->
	 * returns song.mp3 else it returns the original String you sent */
	public String getSongName(String location) {
		for(int i = 0; i < size; i++){
			if(SongArray[i].equals(location)){
				return new File(SongArray[i]).getName();
			}
		}
		return location;
	}

	/** add a new Directory at the end of the arraylist
	 * @param String value */
	public void add(String value){
		SongArray[size] = value;
		size++;
	}

	/** Clear's the arrayList */
	public void clear(){
		SongArray = new String[INITIAL_CAPACITY];
		size = 0;
	}

	/** Return The arrayList to its full capacity. Use when
	 * moving from a playlist to the songsList */
	public void returnSongArrayToDefault(){
		clear();
		for(int i = 0; i < path.getSize(); i++){
			if(new File(path.getDirectory(i)).exists()){
				addFolderToList(new File[]{new File(path.getDirectory(i))}, false);
			} else {
				System.out.println(path.getDirectory(i));
			}
		}

		trimToSize();
		sort();
	}

	/** Fill the arrayList with playlist names.
	 * Uses the res folder, any .txt files will be read.
	 * Remember to update the view you are using to display
	 * the ArrayList */
	public void getPlayLists(){
		File file = null;
		try {
			file = Paths.get(getClass().getResource("files").toURI()).toFile();
		} catch(URISyntaxException ex) {
			ex.printStackTrace();
		}
		size = 0;
		add("--> Create A New Playlist <--");
		File[] listOfFiles = file.listFiles();
		for(int i = 0; i < listOfFiles.length; i++){
			if(!listOfFiles[i].getName().equals("DirectroyLocations.txt")){
				checkArraySpace();
				String item = listOfFiles[i].getAbsolutePath();
				SongArray[size] = (item);
				size++;
			}
		}
	}

	/** Read through every line of the text file chosen and
	 * add all the directory's to the list. Remember to update
	 * the view you are using to display the arrayList
	 * @param int index
	 * @throws FileNotFoundException*/
	public void SelectedPlayList(int index){
		String directory = SongArray[index];
		clear();
		try(Scanner in = new Scanner(new File(directory))){
			while(in.hasNextLine()){
				checkArraySpace();
				SongArray[size] = in.nextLine();
				size++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


	}

	/** return The size of the arrayList
	 * @return ArrayList Size*/
	public int Size() {
		return size;
	}



}
