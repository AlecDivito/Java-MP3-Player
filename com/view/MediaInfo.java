package com.view;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * controls for playing music and switching the metadata.
 * @author Alec Quinn Di Vito*/
public class MediaInfo {
	private final Image DEAFUALT_VIEW_IMAGE = new Image(getClass().getResource("noCoverFound.png").toString(), 200, 200, true, true);
	private final Image DEAFUALT_LISTVIEW_IMAGE = new Image(getClass().getResource("noCoverFound.png").toString(), 65, 65, true, true);

	private StringProperty artist = new SimpleStringProperty("files are. The program will then add all the song to the songs tab.");
	private StringProperty album = new SimpleStringProperty("Please click the add songs tab and add the folder where all your mp3");
	private StringProperty title = new SimpleStringProperty("If this is your first time here and there are no songs under the songs tab");
	private StringProperty year = new SimpleStringProperty("And if you want to make a playlist make sure you have songs stored");
	private StringProperty message = new SimpleStringProperty("");

	private ObjectProperty<Image> albumCover = new SimpleObjectProperty<>(new Image(getClass().getResource("PickSong.png").toString()));

	private ReadOnlyObjectWrapper<MediaPlayer> mediaPlayer = new ReadOnlyObjectWrapper<>();

	public StringProperty getArtistProperty() {
		return artist;
	}
	public void setArtist(String value) {
		artist.set(value);;
	}
	public String getArtist(){
		return artist.get();
	}

	public StringProperty getAlbumProperty() {
		return album;
	}
	public void setAlbum(String value) {
		album.set(value);
	}
	public String getAlbum(){
		return album.get();
	}

	public StringProperty getTitleProperty() {
		return title;
	}
	public void setTitle(String value) {
		title.set(value);
	}
	public String getTitle(){
		return title.get();
	}

 	public StringProperty getYearProperty() {
		return year;
	}
	public void setYear(String value) {
		year.set(value);
	}
	public String getYear(){
		return year.get();
	}

	public StringProperty getMessageProperty(){
		return message;
	}
	public void setMessage(String value){
		message.set(value);
	}
	public String getMessage(){
		return message.get();
	}

	public ObjectProperty<Image> getAlbumCoverProperty() {
		return albumCover;
	}
	public void setAlbumCover(Image value) {
		albumCover.set(value);
	}
	public Image getAlbumCover(){
		return albumCover.getValue();
	}

	public ReadOnlyObjectWrapper<MediaPlayer> getMediaPlayerProperty(){
		return mediaPlayer;
	}
	public MediaPlayer getMediaPlayer(){
		return mediaPlayer.get();
	}

	public Image getDEAFUALT_VIEW_IMAGE() {
		return DEAFUALT_VIEW_IMAGE;
	}
	public Image getDEAFUALT_LISTVIEW_IMAGE(){
		return DEAFUALT_LISTVIEW_IMAGE;
	}

	/** Play a new song */
	public void playNewSong(String directory){
		if(!(mediaPlayer.getValue() == null)){
			mediaPlayer.get().stop();
		}
		Media media = new Media(new File(directory).toURI().toString());
		mediaPlayer.set(new MediaPlayer(media));
		mediaPlayer.get().play();
		resetlabels(directory);
		findSongDetails(media);
	}

	/** Set the labels to the default values
	 * @param directory */
	private void resetlabels(String directory){
		albumCover.set(DEAFUALT_VIEW_IMAGE);
		year.setValue("");
		title.setValue(new File(directory).getName());
		artist.setValue("Artist: UNKNOWN");
		album.setValue("Album: UNKNOWN");
	}

	/** try and find the mp3 files metadata */
	private void findSongDetails(Media media) {
		media.getMetadata().addListener(new MapChangeListener<String, Object>(){
			@Override
			public void onChanged(Change<? extends String, ? extends Object> arg0) {
				if(arg0.wasAdded()){
					handleMetadata(arg0.getKey(), arg0.getValueAdded());
				}
			}
		});
	}

	/** If the metadata was found, change the variables value */
	private void handleMetadata(String key, Object value) {
		switch(key.toString()){
		case "album": 	album.setValue(value.toString());	break;
		case "artist": 	artist.setValue(value.toString());	break;
		case "title": 	title.setValue(value.toString());	break;
		case "year": 	year.setValue(value.toString());	break;
		case "image": 	albumCover.setValue((Image)value);	break;
		}
	}


}
