package com.view;

import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MenuBarView extends AbstractView{

	private final  Background background = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
	private final ObjectProperty<Background> backgroundProperty = new SimpleObjectProperty<>(background);

	public MenuBarView(MediaInfo Info) {
		super(Info);
	}

	@Override
	protected Node initView() {
		MenuBar bar = new MenuBar();

		//file tag
		Menu file = new Menu("File");
		MenuItem deletePlaylist = new MenuItem("Delete PlayList");
		file.getItems().addAll(deletePlaylist);

		//customize tag
		Menu customize = new Menu("Window");
		MenuItem backGroundColor = new MenuItem("Change Players color");
		MenuItem backGroundImage = new MenuItem("Change Players Image");
		backGroundColor.setOnAction(e -> colorPicker());
		backGroundImage.setOnAction(e -> imagePicker());
		customize.getItems().addAll(backGroundColor, backGroundImage);

		//about tag
		Menu about = new Menu("About");
		MenuItem aboutMe = new MenuItem("About Me");
		MenuItem aboutThis = new MenuItem("About MP3 Player V3");
		about.getItems().addAll(aboutMe, aboutThis);

		//Edit tag
		Menu edit = new Menu("Edit");
		MenuItem editSongInfo = new MenuItem("Edit Song Information");
		MenuItem editPlaylist = new MenuItem("Edit PlayList");
		edit.getItems().addAll(editSongInfo, editPlaylist);

		bar.getMenus().addAll(file, edit, customize, about);

		return bar;
	}

	/** Create the window */
	private Stage createStage(Parent p, String msg, int x, int y){
		Stage stage = new Stage();
		Scene s = new Scene(p, x, y);
		stage.setTitle(msg);
		stage.setScene(s);

		return stage;
	}

	//Under window tab
	/** Create a new Window for the user to pick and choose a
	 * new color to change the background to. */
	private void colorPicker() {
		final ColorPicker colorPicker = new ColorPicker();

        VBox box = new VBox();
        box.setPadding(new Insets(15, 15, 15, 15));
        box.getChildren().addAll(new Label("Pick a color"), colorPicker);
        box.setAlignment(Pos.CENTER);

		Stage stage = createStage(box, "Color Picker", 100, 100);
		stage.show();
        colorPicker.setOnAction(e -> setBackground(colorPicker.getValue()));
	}

	private void imagePicker(){
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(new Stage());

		if(!(file == null)){
			setBackground(new Image(file.toURI().toString()));
		}
	}

	/** return the new background */
	public ObjectProperty<Background> getBackground(){
		return backgroundProperty;
	}

	/** Set a new Background when the color changes */
	private void setBackground(Color c){
		backgroundProperty.setValue(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	/** set a new Background when a picture has been picked */
	private void setBackground(Image img){
		// new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		// new BackgroundImage(image, repeatX, repeatY, position, size)
		BackgroundImage backgroundImage = new BackgroundImage(img, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
		// new Background(images...)
		backgroundProperty.setValue(new Background(backgroundImage));
	}




	//Under File tab





	//Under Edit tab

	/**
	 * Check out Random/default/ audioPlaylist.java
	 *  */




	//Under About tab
}
