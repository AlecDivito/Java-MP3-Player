package com.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 * displays the player's Controls for songs time, volume, and
 * buttons to control the current song and to go to the next song
 * @author Alec Quinn Di Vito */
public class PlayerControlsView extends AbstractView{
	private Image pauseIMG;
	private Image playIMG;

	private Button back;
	private Button play;
	private Button next;

	private Label currentTime;
	private Label totelTime;

	private Slider volume;
	private Slider time;

	public PlayerControlsView(MediaInfo Info) {
		super(Info);
		Info.getMediaPlayerProperty().addListener(new MediaListener());
	}

	/** create the view to control the current song*/
	@Override
	protected Node initView() {
		pauseIMG = new Image(getClass().getResource("pause.png").toString(),50, 50, false, false);
		playIMG = new Image(getClass().getResource("play.png").toString(),50, 50, false, false);
		final Image nextIMG = new Image(getClass().getResource("next.png").toString(),50, 50, false, false);
		final Image backIMG = new Image(getClass().getResource("back.png").toString(),50, 50, false, false);

		currentTime = new Label("00:00");
		totelTime = new Label("00:00");
		time = new Slider();

		volume = new Slider();
		back = createButton(backIMG);
		play = createButton(pauseIMG);
		next = createButton(nextIMG);

		time.setPrefWidth(630 - 125);
		volume.setValue(50);

		HBox mediaTime = new HBox();
		mediaTime.setSpacing(10);
		mediaTime.getChildren().addAll(currentTime, time, totelTime);

		HBox mediaControls = new HBox();
		mediaControls.setSpacing(5);
		mediaControls.getChildren().addAll( back, play, next);
		mediaControls.setAlignment(Pos.CENTER);

		HBox volumeControl = new HBox();
		volumeControl.setSpacing(24);
		volumeControl.getChildren().addAll(new Label("Volume"), volume);
		volumeControl.setAlignment(Pos.CENTER);

		VBox pane = new VBox();
		pane.setSpacing(5);
		pane.getChildren().addAll(mediaTime, mediaControls, volumeControl);
		return pane;
	}

	/** Creates back, next, play buttons */
	private Button createButton(Image img){
		Button b = new Button("", new ImageView(img));

		b.setStyle("-fx-background-color: white");

		b.addEventHandler(MouseEvent.MOUSE_ENTERED, new ButtonEffects(b));
		b.addEventHandler(MouseEvent.MOUSE_EXITED, new RemoveButtonEffects(b));

		return b;
	}

	/** set's the width of the time slider to scale with the window */
	public void setSliderWidth(double width){
		time.setPrefWidth(width - 125);
	}

	/** adds listeners to time and volume slider and creates
	 * setOnAction functions for buttons*/
	private void addListeners(final MediaPlayer mp){

		time.setValue(0);
		mp.currentTimeProperty().addListener(new TimeListener(mp));

		back.setOnAction(e -> SongListView.CurrentSongIndex.set(SongListView.CurrentSongIndex.get() - 1));
		next.setOnAction(e -> SongListView.CurrentSongIndex.set(SongListView.CurrentSongIndex.get() + 1));
		play.setOnAction(new PlayPauseListener(mp));

		mp.volumeProperty().bind(volume.valueProperty().divide(100));

		time.setOnMouseDragged(e -> {
			mp.seek(Duration.seconds(time.getValue()));
		});


	}

	/** Removes the lintener from volume*/
	private void removeListeners(final MediaPlayer mp){
		mp.volumeProperty().unbind();
	}

	/** return a string with the current time from a duration
	 * (don't use unless time is under 10 minutes) */
	private String ConvertToTimeSting(Duration startTime) {
		int min = 0, sec = 0;
		double TotalTime = startTime.toSeconds();

		while(TotalTime > 1){
			if(TotalTime - 60 >= 1){
				min++;
				TotalTime = TotalTime - 60;
			} else {
				sec++;
				TotalTime = TotalTime - 1;
			}
		}
		if(sec < 10 && min >= 10)
			return min + ":0" + sec;
		else if(min >= 10)
			return min + ":" + sec;
		else if(sec < 10)
			return "0" + min + ":0" + sec;
		else
			return "0" + min + ":" + sec;
	}

	/** Switch all the objects that use the current mediaPlayer to the new
	 * MediaPlayer  */
	private class MediaListener implements ChangeListener<MediaPlayer>{
		@Override
		public void changed(ObservableValue<? extends MediaPlayer> observable, MediaPlayer oldValue,MediaPlayer newValue) {
			if(oldValue != null){
				removeListeners(oldValue);
			}
			addListeners(newValue);
		}

	}

	/** control the movement of the time slider and the duration times */
	private class TimeListener implements ChangeListener<Duration>{

		MediaPlayer mp;
		public TimeListener(MediaPlayer mp){
			this.mp = mp;
		}

		@Override
		public void changed(ObservableValue<? extends Duration> arg0, Duration oldTime, Duration newTime) {
			currentTime.setText(ConvertToTimeSting(newTime));
			time.setValue(newTime.toSeconds());

			totelTime.setText(ConvertToTimeSting(mp.getTotalDuration()));

			if((mp.getTotalDuration().toSeconds() > 10)){
				time.setMax(mp.getTotalDuration().toSeconds());
			}
			if(time.getValue() >= mp.getTotalDuration().toSeconds() - 1){
				int i = SongListView.CurrentSongIndex.get();
				SongListView.CurrentSongIndex.set(i+1);
			}

		}

	}

	/** Change the play button Image */
	private class PlayPauseListener implements EventHandler<ActionEvent>{

		MediaPlayer mp;
		public PlayPauseListener(MediaPlayer mp) {
			this.mp = mp;
		}

		@Override
		public void handle(ActionEvent event) {
			if(mp.getStatus() == Status.PLAYING){
				mp.pause();
				play.setGraphic(new ImageView(playIMG));
			} else {
				mp.play();
				play.setGraphic(new ImageView(pauseIMG));
			}

		}

	}

	/** adds shadow when mouse enters buttons area */
	private class ButtonEffects implements EventHandler<MouseEvent>{
		Button b;
		public ButtonEffects(Button b) {
			this.b = b;
		}

		@Override
		public void handle(MouseEvent event) {
			b.setEffect(new DropShadow());
		}
	}
	/** removes shadow when mouse leaves button area */
	private class RemoveButtonEffects implements EventHandler<MouseEvent>{
		Button b;
		public RemoveButtonEffects(Button b) {
			this.b = b;
		}

		@Override
		public void handle(MouseEvent event) {
			b.setEffect(null);
		}
	}
}
