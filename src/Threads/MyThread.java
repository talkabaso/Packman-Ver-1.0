package Threads;

import java.awt.Color;
import GUI.guiGame;
import Map.path;

/**
 * this class represents Thread with name, packman's path, color to paint and index of specific packman 
 * @author Tal and Aric
 */
public class MyThread extends Thread {
	private String _name;
	private path p;
	private Color c;
	private int index;
	
	// Constructor
	public MyThread(String name,Color c,path p,int index ) {
	super(name);
	_name = name;
	this.p=p;
	this.c=c;
	this.index=index;
	}
	
	/**
	 * this function pass all the path and send the coordinates (pixels) for paint with color
	 */
	public void run() {
		 
		 for(int i=0;i<p.getPath().size();i++) {
			 
			int x=(int) p.getPathI(i).x();
			int y= (int) p.getPathI(i).y();
			
			guiGame.callToPaintT(x,y,c,index);
			
		 }
     }
	
	@Override 
	public String toString() {
		return "MyThread [_name=" + _name + ", p=" + p + ", c=" + c + "]";
	}
}