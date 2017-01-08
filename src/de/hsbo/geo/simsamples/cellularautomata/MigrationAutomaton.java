package de.hsbo.geo.simsamples.cellularautomata;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import de.hsbo.geo.simsamples.common.RandomValueGenerator;

/**
 * Implementation of a universal Cellular Automaton operating on Migration
 * cellular spaces.
 * 
 * @author Lukas Gierth, Matthias Hensen
 */
public class MigrationAutomaton extends CellularAutomaton 
{
	private int nx, ny;
	private boolean IMAGES;
	private String location;
	private int img_counter = 0;
	

	/**
	 * Constructor
	 * 
	 * @param nx Number of rows of the cellular grid
	 * @param ny Number of columns of the cellular grid
	 * @param states Set of cell state objects
	 * @param delta Transition function
	 * 
	 * @see CellularAutomaton
	 */
	public MigrationAutomaton(
		int nx, 
		int ny, 
		StateSet states, 
		TransitionFunction delta) 
	{
		this(nx, ny, delta);
		this.setStateSet(states);  
	}

	/**
	 * Constructor. Note that the set of cell states will be got from the
	 * given transition function <tt>delta</tt>.
	 * 
	 * @param nx Number of rows of the cellular grid
	 * @param ny Number of columns of the cellular grid
	 * @param delta Transition function
	 * 
	 * @see CellularAutomaton
	 */
	public MigrationAutomaton(
		int nx, 
		int ny, 
		TransitionFunction delta) 
	{
		this.cells = new RectangularSpace(nx, ny);
		this.nx = nx;
		this.ny = ny;
		this.setDelta(delta); 
	}

	@Override
	public void execute(int numberOfSteps) throws Exception 
	{
		// TODO: Check if main parts of the code could be moved to the base class.

		this.numberOfSteps = numberOfSteps;
		this.beforeExecute();
		
		if (! this.initialized) {
			this.initialize();
		}

		// Then step through time:
		for (int ti = 0; ti <= numberOfSteps; ti++) {
			
			this.step();

			if (this.consoleDump) {
				System.out.println("ti = " + ti + ":"); 
				((RectangularSpace) this.getCellularSpace()).dump(this.ti);
			}
			
			int countX = 0;
			int countO = 0;
			int countP = 0;
			 
			Cell[][] arr =((RectangularSpace) this.getCellularSpace()).getCellArray();
			for (int i = 0; i < this.nx; i++) {
				for (int j = 0; j < this.ny; j++) {
					if (arr[i][j].getValue(ti) == "X")
						countX ++;
					if (arr[i][j].getValue(ti) == "O")
						countO ++;
					if (arr[i][j].getValue(ti) == ".")
						countP ++;
				}
			}
			
			if (this.consoleDump == true){
				System.out.println("Größe der Population X: " + countX + " Zellen| " + (countX/(nx*ny)*100) + " %\n"+
						"Größe der Population O: " + countO + " Zellen| " + (countO/(nx*ny)*100)+" %\n"+
						"Anzahl leerer Zellen: "+ countP + " Zellen| " +(countP/(nx*ny)*100)+" %\n\n");
			
			}
			
			this.ti++;
		}
		
		this.afterExecute();
	}

	// Note: The implementations of the following overridden methods have a 
	// better performance than those offered by the base class.

	@Override
	public void step() throws Exception 
	{
		
		this.delta.beforeStep(this.ti);

		Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
		for (int i = 0; i < this.nx; i++) {
			for (int j = 0; j < this.ny; j++) {
				this.delta.step(arr[i][j], this.ti);
				// Step for a cell, could be implemented as empty function!
			}
		}
		this.delta.step(this.ti);
		
		/*
		 * Make screenshot every 10 steps and last image
		 */
		if (this.IMAGES == true && (this.ti % 10 == 0 || this.ti == this.numberOfSteps )){
			String filename = String.format("%06d", img_counter);
			save_image(location + filename);
			img_counter ++;
		}
	
		// Step for automaton, could be implemented as empty function!
		 
	}

	@Override
	public void initializeRandomly() throws Exception 
	{
		Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
		for (int i = 0; i < this.nx; i++) {
			for (int j = 0; j < this.ny; j++) {
				
				if (this.stateSet instanceof DiscreteStateSet && ((i%2==0 && j%2==0)||(i%2!=0 && j%2!=0))) {
					Object val = RandomValueGenerator.chooseRandomly("X","O");
					arr[i][j].setInitialValue(val);
				}
				else if (this.stateSet instanceof DiscreteStateSet){
						Object val = ".";
						arr[i][j].setInitialValue(val);
				}
			}
		}
		this.initialized = true;
	}

	@Override
	public void initializeRandomly(Object... vals) throws Exception 
	{
		if (vals.length == 1) {
			this.initializeWith(vals[0]);
			return;
		}
		Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
		for (int i = 0; i < this.nx; i++) {
			for (int j = 0; j < this.ny; j++) {
				
				if (this.stateSet instanceof DiscreteStateSet && ((i%2==0 && j%2==0)||(i%2!=0 && j%2!=0))) {
					Object val = RandomValueGenerator.chooseRandomly("X","O");
					arr[i][j].setInitialValue(val);
				}
				else if (this.stateSet instanceof DiscreteStateSet){
						Object val = ".";
						arr[i][j].setInitialValue(val);
				}
				// TODO continuousstatese
			}
		}
		this.initialized = true;
	}

	@Override
	public void initializeWith(Object val) throws Exception 
	{
		Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
		for (int i = 0; i < this.nx; i++) {
			for (int j = 0; j < this.ny; j++) {
				arr[i][j].setInitialValue(val); 
			}
		}
		this.initialized = true;
	}
	
	
	 /*
	  *  Addition
	  *  Later integrate into CellularAutomaton???
	  *  
	  *  
	  *  
	  *  
	  *  
	  *  
	  *  
	  */
	
	/*
	 * Image Writer
	 */
	public void write_buffer_image (Graphics2D plot) throws Exception  {
		Cell[][] arr =((RectangularSpace) this.getCellularSpace()).getCellArray();
		for (int i = 0; i < this.nx; i++) {
			for (int j = 0; j < this.ny; j++) {

	     if (arr[i][j].getValue(ti) == "X") plot.setColor(new java.awt.Color( 255, 0, 0)); else
	     if (arr[i][j].getValue(ti) == "O") plot.setColor(new java.awt.Color(0, 0, 255)); else
	     if (arr[i][j].getValue(ti) == ".") plot.setColor(new java.awt.Color(220, 220, 220)); else
	     plot.setColor(new java.awt.Color(0, 0, 0));
	     plot.fillRect(j, i, 1, 1);
	     
			}
		}
	 }

	
	public void save_buffer_image (String filename) throws Exception {
		 BufferedImage img = new BufferedImage(this.nx,this.ny,BufferedImage.TYPE_INT_ARGB);
		 write_buffer_image(img.createGraphics());
	  
		 int SCALE=6;
		 BufferedImage bi = new BufferedImage(SCALE * img.getWidth(null),
                                             SCALE * img.getHeight(null),
                                             BufferedImage.TYPE_INT_ARGB);

		 Graphics2D grph = (Graphics2D) bi.getGraphics();
		 grph.scale(SCALE, SCALE);

        // everything drawn with grph from now on will get scaled.

		 grph.drawImage(img, 0, 0, null);
		 grph.dispose();

		 	try {
			   ImageIO.write(bi, "png", new File(filename +".png"));
			   } catch (Exception ex) {
			     throw new Exception("Save image failed!");
			   }
	   
	 		}

	 public void save_image (String filename) {
	   try {
	     save_buffer_image(filename);
	   } catch (Exception ex) {
	     System.exit(1);
	   }
	 }
	 
	 
	/*
	 * Set location and images==true
	 */
	 
	 public void setLocation(String loc){
		 	this.location = loc;
	 }
	 public void setImage(boolean mode) {
			this.IMAGES = mode;
		}
	 public void enableImages() {
			this.setImage(true);
		}
}