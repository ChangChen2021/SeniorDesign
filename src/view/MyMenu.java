package view;

import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

/** For showing the menu */
public class MyMenu{
	MenuBar menuBar = new MenuBar();
	Menu file = new Menu();
	Menu start = new Menu();
	Menu insert = new Menu();
	public Label label_file = new Label("File ");
	public Label label_start = new Label("Edit ");
	public Label label_insert = new Label("Help ");
}
