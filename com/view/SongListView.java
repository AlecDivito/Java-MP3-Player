package com.view;

import java.io.File;
import java.util.ArrayList;

import com.lists.CreatePlaylistWindow;
import com.lists.SongList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author Alec Quinn Di Vito*/
public class SongListView extends AbstractView{

	/**The variable is used to know when the forward and backward button is pressed
	 * and for when an item is selected on the list view. when CurrentSongIndex hears
	 * the value change it switches the current song to a new song*/
	protected static IntegerProperty CurrentSongIndex = new SimpleIntegerProperty(0);


	private SongList songList;
	private CreatePlaylistWindow cpw;

	private ListView<String> lv;

	private ArrayList<Integer> playedSongs;
	//keeps track of the arrayList index
	private int playedSongsIndex;

	private EditListCell editCell;
	private SearchList search;

	private  ToggleButton defaultbtk;
	private  ToggleButton playlists;
	private  ToggleButton addFile;
	private  ToggleButton shuffle;

	private TextField searchBar;

	private boolean isPlaylistSelected = false;
	private boolean isShuffle = false;
	private boolean isSearching = false;

	public SongListView(MediaInfo info) {
		super(info);
		cpw = new CreatePlaylistWindow();
		playedSongs = new ArrayList<>();
		playedSongsIndex = 0;
	}

	/** Create the view for the songs, buttons, and search bar */
	@Override
	protected Node initView() {
		HBox box = new HBox();
		defaultbtk = CreateButton("Songs", 1);
		playlists = CreateButton("Playlists", 2);
		addFile = CreateButton("Add Songs..", 3);
		shuffle = CreateButton("Shuffle", 4);

		box.getChildren().addAll(defaultbtk, playlists, addFile, shuffle);
		box.setSpacing(5);

		songList = new SongList();
		editCell = new EditListCell();

		lv = new ListView<>();
		lv.setCellFactory(editCell);

		FillListWithDefault();

		lv.getSelectionModel().selectedIndexProperty().addListener(ov -> {
			lv.setOnMouseClicked(e -> {
				if(!isPlaylistSelected){
					CurrentSongIndex.set(lv.getSelectionModel().getSelectedIndex());
				} else {
					isPlaylistSelected = false;
					changeListToPlayList(lv.getSelectionModel().getSelectedIndex());
				}
			});
		});
		 ChangeSong cs = new ChangeSong();

		CurrentSongIndex.addListener(cs);

		//Craete the search bar
		searchBar = new TextField();
		search = new SearchList();
		searchBar.addEventHandler(KeyEvent.KEY_TYPED, search);

		VBox pane = new VBox();
		pane.getChildren().addAll(box, searchBar, lv);

		lv.setPrefHeight(260);

		return pane;
	}

	/** Pop up a "make new playlist" window and listview and the songlist
	 * back to default values with all the songs in them or switch the listview
	 * to the songs saved with the playlist*/
	private void changeListToPlayList(int value) {
		if(songList.getSongDirectory(value).equals("--> Create A New Playlist <--")){
			FillListWithDefault();
			cpw.CreatePlaylist(songList);
		} else {
			songList.SelectedPlayList(value);
			lv.getItems().clear();
			FillListView();
		}

	}

	/** Crate the buttons inside this view */
	private ToggleButton CreateButton(String name, int i){
		ToggleButton b = new ToggleButton(name);
		double width = (630 - 40)/4;
		b.setPrefWidth(width - 4);
		b.setStyle("-fx-background-color: lightgray");
		switch(i){
		case 1: b.setOnAction(e -> FillListWithDefault()); break;
		case 2: b.setOnAction(e -> FillListWithPlaylists());break;
		case 3: b.setOnAction(e -> addAFile());break;
		case 4: b.setOnAction(e -> toggleShuffle());break;
		default: break;
		}
		b.addEventHandler(MouseEvent.MOUSE_ENTERED, new ButtonEffects(b));
		b.addEventHandler(MouseEvent.MOUSE_EXITED, new RemoveButtonEffects(b));
		return b;
	}

	/** Listen for when the shuffle button is pressed.
	 * if is, start listing listened to songs and shuffling is true
	 * else, clear everything and set everything back to default */
	private void toggleShuffle(){
		if(isShuffle){
			isShuffle = false;
			Info.setMessage("");
			shuffle.setStyle("-fx-background-color: lightgray");
			playedSongs.clear();
			playedSongsIndex = 0;
		} else {
			playedSongs.clear();
			isShuffle = true;
			Info.setMessage("Shuffling songs");
			shuffle.setStyle("-fx-background-color: orange");
		}
	}

	/** Return Everything back to there default values
	 * that the program first loaded up with */
	private void FillListWithDefault(){
		lv.getItems().clear();
		isPlaylistSelected = false;
		songList.returnSongArrayToDefault();
		defaultbtk.setStyle("-fx-background-color: orange");
		playlists.setStyle("-fx-background-color: lightgray");
		FillListView();
	}

	/** fill the listView with songList */
	private void FillListView(){
		for(int i = 0; i < songList.Size(); i++){
			lv.getItems().add(songList.getSongDirectory(i));
		}
		Info.setMessage(songList.Size() + " songs ");
	}

	/** fill the listView with with the new SongList
	 * created by the saved info on a txt file */
	private void FillListWithPlaylists(){
		lv.getItems().clear();
		songList.clear();

		songList.getPlayLists();

		isPlaylistSelected = true;
		playlists.setStyle("-fx-background-color: orange");
		defaultbtk.setStyle("-fx-background-color: lightgray");

		FillListView();
	}

	/** add a file to the directory file & add songs to the array list */
	private void addAFile(){
		DirectoryChooser chooser = new DirectoryChooser();
		File file = chooser.showDialog(new Stage());

		if(!(file == null))
			songList.addFolderToList(new File[]{file}, true);

		FillListWithDefault();
	}

	/** Set the width of the buttons compared to the size of the scene */
	public void setNodeWidth(double width){
		width-=40;
		width/=4;
		defaultbtk.setPrefWidth(width - 4);
		playlists.setPrefWidth(width - 4);
		addFile.setPrefWidth(width - 4);
		shuffle.setPrefWidth(width - 4);
	}

	public void setNodeHeight(double height){
		lv.setPrefHeight((height/4) * 3);
	}

	/** Edit listView whenever something is typed into the searchBar */
	private class SearchList implements EventHandler<KeyEvent>{
		private ObservableList<String> secondList;
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

		public int getIndex(){
			String tempString = tempList.get(lv.getSelectionModel().getSelectedIndex());
			for(int i = 0; i < secondList.size(); i++){
				if(tempString.equals(secondList.get(i))){
					return i;
				}
			}
			return -1;
		}
	}

	/** Controls everything for changing the songs and all the states to check for */
	private class ChangeSong implements ChangeListener<Number>{
		@Override
		public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
			//Shuffle button is not pressed, forward or backward button was pressed
			if(CurrentSongIndex.get() >= 0 && CurrentSongIndex.get() <= songList.Size() && !isPlaylistSelected && !isShuffle && !isSearching){
				lv.scrollTo(CurrentSongIndex.get());
				Info.playNewSong(songList.getSongDirectory(CurrentSongIndex.get()));

			} else if(isSearching){
				int index = search.getIndex();
				if(index != -1)
					Info.playNewSong(songList.getSongDirectory(index));

			} else if(isShuffle){ // Shuffle button is pressed

				if(songList.Size() > playedSongs.size()){ // there are songs to play
					if(playedSongsIndex - 1 > 0 && oldValue.intValue() > newValue.intValue()) { // check if the back button was pressed
						playedSongsIndex--;
						playNewSong(" Shuffleing Songs [" + playedSongsIndex + "/" + songList.Size() + "]", songList.getSongDirectory(playedSongs.get(playedSongsIndex - 1)));

					} else if(playedSongsIndex < playedSongs.size() && oldValue.intValue() < newValue.intValue()){ // forward button pressed, not at list cap
						playedSongsIndex++;
						playNewSong(" Shuffleing Songs [" + playedSongsIndex + "/" + songList.Size() + "]", songList.getSongDirectory(playedSongs.get(playedSongsIndex - 1)));

					} else if(playedSongsIndex < songList.Size() && oldValue.intValue() < newValue.intValue()){ // forward Button pressed, playedSongsIndex doesn't exist in songList, find new song
						int i;
						do{
							i = (int)(Math.random() * songList.Size()) + 0;
						}while(playedSongs.contains(i)); // check to see if i is in the list
						playedSongs.add(i);
						playNewSong(" Shuffleing Songs [" + playedSongs.size() + "/" + songList.Size() + "]", songList.getSongDirectory(i));
						playedSongsIndex++;
					}
					lv.scrollTo(playedSongs.get(playedSongsIndex - 1));
				} else {
					Info.setMessage("Shuffle is done");
				}
			}
		}

		/** Change Message, Began a new song */
		private void playNewSong(String message, String nextSong){
			Info.playNewSong(nextSong);
			Info.setMessage(message);
		}

	}

	/** Edit the ListView Cell to create new custom cells when the list is scrolled through */
	private class EditListCell implements Callback<ListView<String>, ListCell<String>> {
		@Override
		public ListCell<String> call(ListView<String> param) {
			return new ListCell<String>(){
				public void updateItem(String name, boolean empty){
					super.updateItem(name, empty);

					if(name != null){
						/*setText(null);
						setGraphic(null);
					} else {
						*/setText(new File(name).getName());
						setGraphic(new ImageView(Info.getDEAFUALT_LISTVIEW_IMAGE()));
					}
					try{
						if(!isPlaylistSelected && name != null){
							final Media media = new Media(new File(name).toURI().toString());
							media.getMetadata().addListener(new MapChangeListener<String, Object>(){
								@Override
								public void onChanged(Change<? extends String, ? extends Object> arg0) {
									if(arg0.wasAdded()){
										if(arg0.getKey().equals("title")){
											setText(arg0.getValueAdded().toString());
										} if(arg0.getKey().equals("image")){
											ImageView view = new ImageView((Image)arg0.getValueAdded());
											view.setFitHeight(65);
											view.setFitWidth(65);
											setGraphic(view);
										}
									}
								}
							});
						}
					} catch(MediaException ex){
						System.out.println("MediaNotFound: in EditListCell " + ex);
					}
				}
			};
		}
	}

	/** Add shadow when mouse hovers over button */
	private class ButtonEffects implements EventHandler<MouseEvent>{
		ToggleButton b;
		public ButtonEffects(ToggleButton b) {
			this.b = b;
		}

		@Override
		public void handle(MouseEvent event) {
			b.setEffect(new DropShadow());
		}
	}

	/** remove shadow when mouse is not hovering over button */
	private class RemoveButtonEffects implements EventHandler<MouseEvent>{
		ToggleButton b;
		public RemoveButtonEffects(ToggleButton b) {
			this.b = b;
		}

		//DropShadow shadow = new DropShadow();
		@Override
		public void handle(MouseEvent event) {
			b.setEffect(null);
		}
	}

}

