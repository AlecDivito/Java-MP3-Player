package com.view;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * @author Alec Quinn Di Vito*/
public class MetaDataView extends AbstractView{

	public MetaDataView(MediaInfo Info) {
		super(Info);
	}

	@Override
	protected Node initView() {
		final Label artist = new Label();
		final Label album = new Label();
		final Label title = new Label();
		final Label year  = new Label();
		final Label message = new Label();
		final ImageView albumCover = new ImageView();

		albumCover.setFitHeight(200);
		albumCover.setFitWidth(200);

		artist.textProperty().bind(Info.getArtistProperty());
		album.textProperty().bind(Info.getAlbumProperty());
		title.textProperty().bind(Info.getTitleProperty());
		year.textProperty().bind(Info.getYearProperty());
		message.textProperty().bind(Info.getMessageProperty());
		albumCover.imageProperty().bind(Info.getAlbumCoverProperty());

		GridPane pane = new GridPane();
		VBox vBox = new VBox();

		vBox.setSpacing(8);
		vBox.getChildren().addAll(title, album, artist, year, new Label(""),new Label(""),new Label(""),message);

		pane.setVgap(20);
		pane.setHgap(10);
		pane.add(albumCover, 0, 0);
		pane.add(vBox, 1, 0);

		pane.setAlignment(Pos.TOP_LEFT);

		return pane;
	}



}
