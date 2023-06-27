package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao ;
	private Graph<Track,DefaultEdge> grafo;
	private List<Genre> genres;
	private List<Track> nodes;
	private Map<Track,List<Playlist>> trackPlaylist;
	private Map<Integer,Playlist> playlist;
	
	
	//Per ricorsione
	private List<Track> best;
	private int dimMax;
	
	public Model() {
		dao = new ItunesDAO();
	}
	
	public void createGraph(Genre g, double min ,double max) {
		
		grafo = new SimpleGraph<Track,DefaultEdge>(DefaultEdge.class);
		this.loadNodes(g, min, max);
		this.loadPlaylist();
		
		Graphs.addAllVertices(this.grafo, nodes);
		
		trackPlaylist = new HashMap<>(this.dao.getTrackWithPlaylist(nodes, playlist));
		
		for(Track t1:this.grafo.vertexSet()) {
			for(Track t2 : this.grafo.vertexSet()) {
				if(!t1.equals(t2)) {
					if(this.trackPlaylist.get(t1).size() == this.trackPlaylist.get(t2).size()) {
						this.grafo.addEdge(t1, t2);
					}
				}
			}
		}
		
		
		
	}
	
	public List<Track> laMiaPlaylist(double durataTot){
		
		List<Track> parziale = new ArrayList<>();
		List<Track> allTracks = null;
		ConnectivityInspector<Track,DefaultEdge> inspector = new ConnectivityInspector<>(this.grafo);
		int bigSetCon=0;
		dimMax = 0;
		for( Set<Track> set : inspector.connectedSets()) {
			if(set.size()>bigSetCon) {
				bigSetCon = set.size();
				allTracks= new ArrayList<>(set);}
			}
			
		cercaPlaylistIdeale(parziale,0,durataTot,0,allTracks);
		
		return best;
		
	}
	
	private void cercaPlaylistIdeale(List<Track> parziale, int livello, double durataTot, double durataParziale, List<Track> tuttiTrack ) {
		
		System.out.println(parziale.size());
		System.out.println(dimMax);
		
		if(parziale.size()>dimMax) {
			dimMax= parziale.size();
			best = new ArrayList<>(parziale);
		}
		
		
		for(Track t : tuttiTrack) {
			System.out.println(t.getMilliseconds()*0.001);
			System.out.println(durataParziale);
			if(!parziale.contains(t) && (durataParziale+t.getMilliseconds()*0.001)<durataTot){
				parziale.add(t);
				cercaPlaylistIdeale(parziale,livello++,durataTot,durataParziale+t.getMilliseconds()*0.001,tuttiTrack);
				parziale.remove(parziale.size()-1);
				}
		}
	}
	
	
	
	
	
	
	public List<String> compConnessa() {
		
		ConnectivityInspector<Track,DefaultEdge> inspector = new ConnectivityInspector<>(this.grafo);
		List<String> output = new ArrayList<String>();
		for( Set<Track> set : inspector.connectedSets()) {
			Set<Playlist> playlistSet = new HashSet<>();
			   for(Track t : set) {
				   for(Playlist p : this.trackPlaylist.get(t)) {
					   playlistSet.add(p);}
				   }
			output.add("Ci sono nella componente connessa : "+ set.size()+ " vertici "+ "e "+ playlistSet.size()+ " playlist\n");
		}
		return output;
	}
	
	public int nNodes() {
		return this.grafo.vertexSet().size();
	}
	
	public int nEdge() {
		return this.grafo.edgeSet().size();
	}
	
	public void loadNodes(Genre g, double min , double max){
		double minTrack = min*1000;
		double maxTrack = max*1000;
		nodes= new ArrayList<>(this.dao.getTracksByGenre(g, minTrack, maxTrack));
	}
	
	public List<Genre> loadGenres() {
		genres=new ArrayList<>(this.dao.getAllGenres()); 
		Collections.sort(genres);
		return genres;
	}
	
	public void loadPlaylist() {
		List<Playlist> playlistDao = new ArrayList<>(this.dao.getAllPlaylists());
		playlist = new HashMap<>();
		for(Playlist p : playlistDao) {
			playlist.put(p.getPlaylistId(), p);
		}
		
	}
}
