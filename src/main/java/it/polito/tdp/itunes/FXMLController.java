/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlaylist"
    private Button btnPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtDTOT"
    private TextField txtDTOT; // Value injected by FXMLLoader

    @FXML // fx:id="txtMax"
    private TextField txtMax; // Value injected by FXMLLoader

    @FXML // fx:id="txtMin"
    private TextField txtMin; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPlaylist(ActionEvent event) {
    	
    	this.txtResult.clear();
    	double tempoTot = Double.parseDouble(txtDTOT.getText());
    	
    	List<Track> playlistIdeale = new ArrayList<>(this.model.laMiaPlaylist(tempoTot));
    	for(Track t : playlistIdeale) {
    		this.txtResult.appendText(t.getName()+"\n");
    	}

		this.txtResult.appendText("La playlist ha " + playlistIdeale.size() +" brani");

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Genre g = this.cmbGenere.getValue();
    	double min = 0.0;
    	double max = 0.0;
    	if(g==null) {
    		this.txtResult.setText("Selezionare un genere");
    	}
    	try {
    	 min =Double.parseDouble(this.txtMin.getText());
    	 max =Double.parseDouble(this.txtMax.getText());if((min==0.0 && max==0.0 )) {
    		
    	}
    	
    	}catch(NumberFormatException e ) {
    		this.txtResult.setText("Inserire dei valori numerici");
    	}
    	
    	if((min==0.0 && max==0.0 )) {
    		this.txtResult.appendText("Si prega di inserire valori maggiori di 0");
    		
    	}
    	
    	this.model.createGraph(g, min, max);
    	this.txtResult.appendText("Grafo creato correttamente\n");
    	this.txtResult.appendText("Vi sono in tutto: "+ this.model.nNodes() +" vertici\n");
    	this.txtResult.appendText("Vi sono in tutto: "+ this.model.nEdge() +" archi\n");
    	
    	for(String s : this.model.compConnessa()) {
    		this.txtResult.appendText(s);
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPlaylist != null : "fx:id=\"btnPlaylist\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDTOT != null : "fx:id=\"txtDTOT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMax != null : "fx:id=\"txtMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMin != null : "fx:id=\"txtMin\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbGenere.getItems().addAll(this.model.loadGenres());
    }

}
