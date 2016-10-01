
import com.view.MediaInfo;
import com.view.MenuBarView;
import com.view.MetaDataView;
import com.view.PlayerControlsView;
import com.view.SongListView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Alec Quinn Di Vito*/
public class MAIN extends Application{

	public static void main(String[] args) {
		Application.launch(args);
	}

	private double startTime;

	private PlayerControlsView playerControlsView;
	private SongListView songView;
	private MetaDataView metaView;
	private MenuBarView menuBar;

	private MediaInfo Info;

	//binds for background color and images

	public MAIN(){
		startTime = System.currentTimeMillis();
		Info = new MediaInfo();
	}
	/** Create Window to show music app */
	@Override
	public void start(Stage stage) throws Exception {
		playerControlsView = new PlayerControlsView(Info);
		songView = new SongListView(Info);
		metaView = new MetaDataView(Info);
		menuBar = new MenuBarView(Info);

		//Set all the view's
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(metaView.getView());
		borderPane.setBottom(playerControlsView.getView());
		borderPane.setCenter(songView.getView());

		BorderPane.setMargin(borderPane.getCenter(), new Insets(0, 20, 0, 20));
		BorderPane.setMargin(borderPane.getBottom(), new Insets(5, 20, 20, 20));
		BorderPane.setMargin(borderPane.getTop(), new Insets(20));

		VBox pane = new VBox();
		pane.getChildren().addAll(menuBar.getView(),borderPane);

		borderPane.backgroundProperty().bind(menuBar.getBackground());

		//Create the window
		Scene s = new Scene(pane, 630, 650);
		pane.setStyle("-fx-background-color: white");
		stage.setTitle("MP3 Player V3");
		stage.setScene(s);
		stage.show();


		Info.setMessage(Info.getMessage() + "Loaded in " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds");

		pane.widthProperty().addListener(ov -> {
			playerControlsView.setSliderWidth(s.getWidth());
			songView.setNodeWidth(s.getWidth());
		});
		pane.heightProperty().addListener(ov -> songView.setNodeHeight(s.getHeight()));
	}

}
