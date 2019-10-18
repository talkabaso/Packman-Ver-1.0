package GUI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import File_format.Path2KML;
import File_format.writeCsv;
import GIS.fruit;
import GIS.game;
import GIS.metaDataFruit;
import GIS.metaDataPack;
import GIS.packman;
import GIS.solution;
import Geom.Point3D;
import Map.converts;
import Threads.MyThread;
import algorithm.algo;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * this class represent Gui Window with menuBar , options such as : open Csv , save Csv , save KML , run,
 * showing the progress of the game(packmans in their way to eat the fruits)
 * @author Tal and Aric
 */

public class guiGame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	// package friendly elements
	static JMenuBar wholeMenuBar;
	JMenu fileMenu;
	JMenuItem openItem, saveItem,clearItem,runItem;
	static JMenuItem createKml;
	static solution s;
	static converts c;

	static ArrayList<fruit> fruits;
	static ArrayList<packman> packmans;

	static int x = -1; // for initialize
	static int y = -1; // for initialize
	static long pressedTime; // pressed 
	static long timeClicked; // leave

	static ImageIcon imageIcon;
	static MyJLabel jLabel ;

	// Constructor //
	public guiGame ()
	{
		super("Packman Game");
		fruits=new ArrayList<fruit>();
		packmans=new ArrayList<packman>();
		s=new solution();		
		try { // because there is map element in the converts
			c=new converts();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param x pixel in X-axis
	 * @param y pixel in Y-axis
	 * @param c random color
	 * @param index the index of Packman
	 */
	public static void callToPaintT(int x,int y,Color c,int index) {

		jLabel.Thredpaint(x,y,c,index);
	}

	public void init() {

		wholeMenuBar = new JMenuBar();
		setJMenuBar(wholeMenuBar);
		wholeMenuBar.setVisible(true);

		//set file menu	with open, save
		fileMenu = new JMenu("File");

		//Open csv
		openItem = new JMenuItem("Open");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,  ActionEvent.CTRL_MASK));
		fileMenu.add(openItem);

		//Save to csv
		saveItem = new JMenuItem("save");
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(saveItem);

		//run
		runItem=new JMenuItem("run");
		fileMenu.add(runItem);
		runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));

		//clear
		clearItem=new JMenuItem("clear");
		fileMenu.add(clearItem);
		clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

		//createKml
		createKml=new JMenuItem("save as kml");
		fileMenu.add(createKml);
		createKml.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
		createKml.setEnabled(false); // not allowing to create kml file when the game is empty

		wholeMenuBar.add(fileMenu);

		imageIcon = new ImageIcon("Ariel.png");
		jLabel = new MyJLabel(imageIcon);
		getContentPane().add(jLabel);
	}

	public void createAndShowGUI()
	{
		init();

		// Actions Listeners for all JMenuItems //
		//local classes

		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fruits.clear();
					packmans.clear();
					readFileDialog();
					createKml.setEnabled(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeFileDialog();
			}
		});

		runItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setTotalTime(0);
				game.setScore(0);
				createKml.setEnabled(true);
				System.out.println("the game start");
				try {

					if (packmans.size()==0) {

						throw new RuntimeException("can't start , there is no packman");
					}

					//converts from pixel to coordinates because all the algorithms use coordinates
					ArrayList<fruit>fCoords=converts.pixels2CoordsFruit(fruits,getHeight(),getWidth()); 
					ArrayList<packman>pCoords=converts.pixels2CoordsPack(packmans,getHeight(),getWidth());
					
					s=algo.calcAll(fCoords, pCoords); // Solution store all the paths

					solution solutionPixel=converts.solutionToPixel(s,getHeight(),getWidth()); // convert to pixel for show in GUI
					jLabel.paintEat(solutionPixel);

				JOptionPane.showMessageDialog(null, "total time is: "
				+game.getTotalTime()+"\n"+"total score is: "+game.getScore());

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

		clearItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.out.println("clean pressed");
				fruits.clear();
				packmans.clear();
				createKml.setEnabled(false);
				repaint();
			}
		});

		createKml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				writeFileDialogKml();
			}
		});


		addWindowListener( new WindowAdapter()
		{
			public void windowResized(WindowEvent evt)
			{
				jLabel.repaint();
			}
		});

		setSize(imageIcon.getIconWidth(),imageIcon.getIconHeight()); // default bounds of map picture
		System.out.println(JLabel.HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		jLabel.repaint();
	}

	public void readFileDialog() throws IOException {

		this.setExtendedState(Frame.MAXIMIZED_BOTH); // Full screen when click on Open file

		// try read from the file
		FileDialog fd = new FileDialog(this, "Open text file", FileDialog.LOAD);
		fd.setFile("*.csv");
		fd.setDirectory("C:\\");
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".csv");
			}
		});

		fd.setVisible(true); // select path to open csv File
		String folder = fd.getDirectory();
		String fileName = fd.getFile();       
		this.setTitle(fileName);// give the game the name of the file

		//game
		game g=new game(fruits,packmans);
		game.createGameCollection(folder+"\\"+fileName);
		g.paintGame(getHeight(),getWidth()); // use create

		try {
			FileReader fr = new FileReader(folder + fileName);
			BufferedReader br = new BufferedReader(fr);
			String str = new String();
			System.out.println("csv Data:");
			for (int i = 0; str != null; i = i + 1) {
				str = br.readLine();
				if (str != null) {
					System.out.println(str);
				}
			}
			br.close();
			fr.close();
		} catch (IOException ex) {
			System.out.print("Error reading file " + ex);
			System.exit(2);
		}
	}

	public void writeFileDialog() {
		// try write to the file
		FileDialog fd = new FileDialog(this, "Save the text file", FileDialog.SAVE);
		fd.setFile("*.csv");
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		fd.setVisible(true);
		String folder = fd.getDirectory();
		String fileName = fd.getFile();
		try {
			FileWriter fw = new FileWriter(folder + fileName);
			PrintWriter outs = new PrintWriter(fw);
			// receive arrayList of fruits and packmans 
			//and return string represents all the data in the game like: points,weight etc
			String csvString=writeCsv.Write(fruits, packmans,getHeight(),getWidth());
			outs.println(csvString);
			outs.close();
			fw.close();
		} catch (IOException ex) {
			System.out.print("Error writing file  " + ex);
		}

	}

	public void writeFileDialogKml() {
		// try write to the file
		FileDialog fd = new FileDialog(this, "create and save the kml file", FileDialog.SAVE);
		fd.setFile("*.kml");
		fd.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".kml");
			}
		});
		fd.setVisible(true);
		String folder = fd.getDirectory();
		String fileName = fd.getFile();
		try {
			FileWriter fw = new FileWriter(folder + fileName);
			PrintWriter outs = new PrintWriter(fw);

			//converts from pixel to coordinates for create KML file
			ArrayList<packman> ppCoords=converts.pixels2CoordsPack(packmans,getHeight(),getWidth());
			ArrayList<fruit> ffCoords = converts.pixels2CoordsFruit(fruits,getHeight(),getWidth());

			String save=folder+"\\"+fileName; // set name to the KML file same as csv name

			//create new ArrayLists for use calcAll function then send packmans Path
			ArrayList<fruit>fCoords=converts.pixels2CoordsFruit(fruits,getHeight(),getWidth());
			ArrayList<packman>pCoords=converts.pixels2CoordsPack(packmans,getHeight(),getWidth());
			algo.calcAll(fCoords, pCoords);

			Path2KML.run(ffCoords, ppCoords,s,save,pCoords);
			outs.close();
			fw.close();
		} catch (IOException ex) {
			System.out.print("Error writing file  " + ex);
		}

	}

	//get arrayList of fruits and packmans from game into the gui and repaint with the new arraysList
		public void openFileGUI(ArrayList<fruit>ff,ArrayList<packman>pp)
	{	
		//push the ff and pp into fruits and packmans
		int i=0;
		while(i<pp.size()) {

			packmans.add(pp.get(i));
			i++;
		}
		i=0;
		while(i<ff.size()) {

			fruits.add(ff.get(i));
			i++;
		}
		addWindowListener( new WindowAdapter()
		{
			public void windowResized(WindowEvent evt)
			{
				jLabel.repaint();
			}
		});

		jLabel.repaint();

	}

	// Getters and Setters //

	public static int fruitSize() {

		return fruits.size();
	}

	public static int packSize() {
		return packmans.size();
	}

	public static ArrayList<fruit> getFruitArr() {

		return fruits;
	}
	public static ArrayList<packman> getPackArr() {

		return packmans;
	}

	public static void main(String st[])
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			@Override
			public void run()
			{
				guiGame demo = new guiGame();
				demo.createAndShowGUI();

			}
		});
	}
}


class MyJLabel extends JLabel implements MouseListener
{

	private static final long serialVersionUID = 1L;
	ImageIcon imageIcon;
	private int oldWidth; // for stretch the screen
	private int oldHeight; // for stretch the screen  

	public MyJLabel(ImageIcon icon)
	{
		super();
		this.imageIcon = icon;
		addMouseListener(this);
		oldHeight=getHeight();
		oldWidth=getWidth();
	}

	/**
	 * this function receive arrayList of fruit and change the position accordingly to resize window
	 */
	public void reSizeFruit(){

		int size=guiGame.fruits.size();
		int i=0;
		while(i<size) {

			double xOld=guiGame.fruits.get(i).getX();
			double yOld=guiGame.fruits.get(i).getY();

			double newWidth = xOld*getWidth()/oldWidth;
			double newHeight = yOld*getHeight()/oldHeight;

			guiGame.fruits.get(i).setX(newWidth);
			guiGame.fruits.get(i).setY(newHeight);

			i++;
		}
	}

	/**
	 * this function receive arrayList of Packmans and change the position accordingly to resize window
	 */
	public void reSizePackman(){

		int size=guiGame.packmans.size();
		int i=0;
		while(i<size) {

			double xOld=guiGame.packmans.get(i).getX();
			double yOld=guiGame.packmans.get(i).getY();

			double newWidth = xOld*getWidth()/oldWidth;
			double newHeight = yOld*getHeight()/oldHeight;

			guiGame.packmans.get(i).setX(newWidth);
			guiGame.packmans.get(i).setY(newHeight);

			i++;
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(imageIcon.getImage(),0,0,getWidth(),getHeight(),this);

		// update the pixels after stretch
        reSizeFruit();
		reSizePackman();
		
		oldHeight=getHeight();
		oldWidth=getWidth();

		paintFruits(g);
		paintPackmans(g);
	}

	/**
	 * this function call for paint function for all the elements (include paths)
	 * @param s ArrayList that contains all the packmans path
	 */
	public void paintEat(solution s) {

		Graphics g=this.getGraphics();
		super.paintComponent(g);
		g.drawImage(imageIcon.getImage(),0,0,getWidth(),getHeight(),this);

		//stretch the window 
		reSizeFruit();
		reSizePackman();
		
		//update the current Height and Width
		oldHeight=getHeight();
		oldWidth=getWidth();

		paintFruits(g);
		paintPackmans(g);
		paintPath(s);
	}

	/**
	 * this function scan all the fruit elements and paint them on the screen as red point
	 */
	public void paintFruits(Graphics g) {

		Color c=new Color(251,0,0); //red apple
		g.setColor(c);

		int sizeFruit=guiGame.fruitSize(); // get the number of fruits we draw
		int i=0; // index of the arrayList
		while(i<sizeFruit) { // there are still fruits to show

			int r = 10;
			guiGame.x = (int)guiGame.getFruitArr().get(i).getX() - (r / 2); // get the X-Axis pixel
			guiGame.y = (int)guiGame.getFruitArr().get(i).getY() - (r / 2); // get the Y-Axis pixel
			Image packman;
			try {
				packman = ImageIO.read(new File("Fruit.png"));
				g.drawImage(packman,guiGame.x,guiGame.y,this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}

	/**
	 * this function scan all the Packmans elements and paint them on the screen as blue point
	 */
	public void paintPackmans(Graphics g) {

		Color c=new Color(0,0,204); //blue Sea
		g.setColor(c);
		int sizePackmans=guiGame.packSize(); // get the number of fruits we draw
		int j=0; // index of the arrayList
		while(j<sizePackmans) { // there are still packmans to show

			int r = 15;
			guiGame.x = (int)guiGame.getPackArr().get(j).getX() - (r / 2); // get the X-Axis pixel
			guiGame.y = (int)guiGame.getPackArr().get(j).getY() - (r / 2); // get the Y-Axis pixel
			Image packman;
			try {
				packman = ImageIO.read(new File("Packman.png"));
				g.drawImage(packman, guiGame.x,guiGame.y,this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j++;
		}
	}

	/**
	 * this function scan all the solution paths and paint them on the screen as line 
	 * between packmans to fruits
	 * @param s Solution contains all packman's path
	 */
	public void paintPath(solution s) {

		Graphics g=this.getGraphics();

		int solutionSize=s.getSize(); // get the number of paths we draw
		int k=0; // index of the arrayList

		// pass all the path in the solution ,create thread for each path and use the start function
		while(solutionSize>k) {

			Color c=randomColor();
			g.setColor(c);

			// create object of MyThread for parallel running 
			MyThread t1 = new MyThread(""+k,c, s.getPathCollection().get(k),k);
			t1.start();
			k++;

		}
	}

	/**
	 * This function calculate the time of sleeping between the painting ovals according to the speed
	 * @param speed the speed of specific Packman
	 * @param x pixel on X-axis
	 * @param y pixel on Y-axis
	 * @param c random color
	 * @param index the index of specific Packman 
	 */
	public void Thredpaint(int x,int y,Color c,int index) {

		Graphics g=this.getGraphics();
		g.setColor(c);

		try {

			double speed=guiGame.packmans.get(index).getSpeed();
			Thread.sleep((long) (15/speed));
			g.fillOval(x, y, 3, 3);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
/**
 * this function choose random color between 1 to 255
 * @return random color
 */
	public Color randomColor() {

		int r=(int) (Math.random()*255+1);
		int g=(int) (Math.random()*255+1);
		int b=(int) (Math.random()*255+1);

		return new Color(r,g,b);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

		guiGame.pressedTime = System.currentTimeMillis();
		guiGame.x = e.getX(); // pixel on X-axis
		guiGame.y = e.getY(); // pixel on Y-axis
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		guiGame.createKml.setEnabled(true);
		
		// by pressing more then half second (0.5) create a packman 
		// by pressing less then half second (0.5) create an fruit
		
		System.out.println("mouse Clicked");
		guiGame.timeClicked = System.currentTimeMillis() - guiGame.pressedTime;
		System.out.println("("+ guiGame.x + "," + guiGame.y +")");

		if (guiGame.timeClicked >= 500) {

			String i="draw pack";
			i+=1;
			metaDataPack data=new metaDataPack(i,1,1);
			Point3D position =new Point3D(guiGame.x,guiGame.y,0); // add packman location in pixels
			packman pack=new packman(data,position);
			guiGame.packmans.add(pack);
			repaint();
			System.out.println("number of packmans: "+guiGame.packmans.size());
			guiGame.createKml.setEnabled(false);
		}
		else {

			String j="draw fruit";
			j+=1;
			metaDataFruit data=new metaDataFruit(j,1);
			Point3D position =new Point3D(guiGame.x,guiGame.y,0); // add fruit location in pixels
			fruit f=new fruit(data,position);
			guiGame.fruits.add(f);
			repaint();
			System.out.println("number of fruits: "+guiGame.fruits.size());
		}
	}
}