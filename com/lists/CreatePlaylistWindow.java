package com.lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Alec Quinn Di Vito
 *  */
public class CreatePlaylistWindow {

	private TextField searchBar = new TextField();;

	private final TextField playListName = new TextField("Playlist Name");
	private final ListView<String> lv = new ListView<>();
	private final Button create = new Button("Create Playlist");

	boolean isSearching = false;

	public CreatePlaylistWindow(){
	}

	/** Creates a new window to create a text file with the
	 * locations of songs on it. The txt file is then used as
	 * a playlist*/
	public void CreatePlaylist(SongList songList){
		VBox pane = new VBox();
		Stage stage = createStage("Create Playlist", pane);

		create.setPrefSize(stage.getScene().getWidth(), 100);
		pane.getChildren().addAll(playListName,searchBar, lv, create);

		for(int i = 0; i < songList.Size(); i++){
			lv.getItems().add(new File(songList.getSongDirectory(i)).getName());
		}

		searchBar.addEventHandler(KeyEvent.KEY_TYPED, new SearchList());

		ArrayList<Integer> selectedIndex = new ArrayList<>();

		lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lv.getSelectionModel().selectedIndexProperty().addListener(o -> {
			lv.setOnMouseClicked(e -> {
				if(!isSearching)
					selectedIndex.add(lv.getSelectionModel().getSelectedIndex());
				else
					selectedIndex.add(songList.getIndex(lv.getSelectionModel().getSelectedItem()));
			});
		});

		create.setOnAction(e -> {
			if(createPlayListFile(selectedIndex, songList))
				stage.close();
		});
	}

	/** Create playList file */
	private final boolean createPlayListFile(ArrayList<Integer> selectedIndex, SongList songList) {
		if(!playListName.getText().equals("Playlist Name") && !playListName.getText().equals("Please change Playlists name")){
			try{
				if(new File("bin\\com\\lists\\files\\" + playListName.getText() + ".txt").createNewFile()){
					File playlistFile = Paths.get(getClass().getResource("files/" + playListName.getText() + ".txt").toURI()).toFile();
					PrintWriter pw = new PrintWriter(new FileWriter(playlistFile, true));
					for(Integer i : selectedIndex){
						pw.println(songList.getSongDirectory(i));
					}
					pw.close();
				} else {
					System.out.println("file could not be created");
				}
		    } catch (FileNotFoundException e1) {
		    	e1.printStackTrace();
		    } catch (IOException e1) {
		    	e1.printStackTrace();
		    } catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			playListName.setText("Please change Playlists name");
			return false;
		}

	}

	/** Create the stage */
	private Stage createStage(String message, Parent pane){
		Stage stage = new Stage();
		Scene s = new Scene(pane, 300, 450);
		stage.setResizable(false);
		stage.setTitle(message);
		stage.setScene(s);
		stage.show();

		return stage;
	}


	/** Edit listView whenever something is typed into the searchBar */
	private class SearchList implements EventHandler<KeyEvent>{
		// A second List for listView with all the .mp3 songs
		private ObservableList<String> secondList;

		// A list to store viable songs inside the list view
		private ArrayList<String> tempList = new ArrayList<>();


		public SearchList(){
			secondList = FXCollections.observableArrayList(lv.getItems());
		}

		@Override
		public void handle(KeyEvent event) {
			isSearching = true;
			tempList.clear();
			int length = searchBar.getText().length();

			if(length > 0){
				for(int i = 0; i < secondList.size(); i++){
					if(secondList.get(i).toLowerCase().contains(searchBar.getText().toLowerCase())){
						tempList.add(secondList.get(i));
					}
				}
			}

			lv.setItems(FXCollections.observableArrayList(tempList));

			if(length == 0){
				isSearching = false;
				lv.setItems(secondList);
			}

		}

	}
}
