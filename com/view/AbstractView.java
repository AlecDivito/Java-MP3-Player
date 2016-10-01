package com.view;

import javafx.scene.Node;

/**
 * When extended it Allows child class to have one method
 * Dedicated to GUI and this allows you to build the rest of
 * class around what the GUI functionality is. It also lets
 * you access the current song Info and variables.
 * @author Alec Quinn Di Vito*/
public abstract class AbstractView {
	protected final MediaInfo Info;
	protected final Node viewNode;

	public AbstractView(MediaInfo Info){
		this.Info = Info;
		this.viewNode = initView();
	}

	/** return Node made by class
	 * @return Node */
	public Node getView(){
		return viewNode;
	}

	/** Create the classes GUI inside this method */
	protected abstract Node initView();

}
