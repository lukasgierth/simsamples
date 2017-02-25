package de.hsbo.geo.simsamples.cellularautomata;

import de.hsbo.geo.simsamples.common.RandomValueGenerator;

import java.awt.Color;

import de.hsbo.geo.simsamples.cellularautomata.ExportTools;


/**
 * Implementation of a universal Cellular Automaton operating on Migration
 * cellular spaces.
 * 
 * @author Lukas Gierth, Matthias Hensen
 */
public class MigrationAutomaton extends CellularAutomaton 
{
	private String LOCATION;
	private int nx, ny;
	private int IMG_RATE;
	private int IMG_SCALE;
	private boolean WRITE_FILE;
	private boolean IMAGEDUMP;

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
			double rows = this.nx;
			double col = this.ny;

			Cell[][] arr = ((RectangularSpace) this.getCellularSpace()).getCellArray();
			for (int i = 0; i < this.nx; i++) {
				for (int j = 0; j < this.ny; j++) {
					if (arr[i][j].getValue(ti) == "X")
						countX++;
					if (arr[i][j].getValue(ti) == "O")
						countO++;
					if (arr[i][j].getValue(ti) == ".")
						countP++;
				}
			}

			/*
			 * Console Dump if enabled 
			 */
			if (this.consoleDump == true) {

				int countXpercent = (int) (countX/(rows*col)*100);
				int countOpercent = (int) (countO/(rows*col)*100);
				int countPpercent = (int) (countP/(rows*col)*100);
				
				System.out.println("Größe der Population X: " + countX + " Zellen| " + countXpercent
						+ " %\n" + "Größe der Population O: " + countO + " Zellen| " + countOpercent
						+ " %\n" + "Anzahl leerer Zellen: " + countP + " Zellen| " + countPpercent
						+ " %\n\n");
			}
			
			/*
			 * Write information to file
			 */
			if (this.WRITE_FILE == true) {
				String line = ti+","+countX+","+countO+","+countP;
				saveFile(line);
			}

			/*
			 * Break when no empty cell left
			 */
			if (countP == 0){
				System.out.println("Break after " + this.ti + " timesteps, no free cells available anymore");
				
				/*
				 * One last image dump
				 */
				if (this.IMAGEDUMP == true && this.ti % this.IMG_RATE != 0){
					saveImage();
				}
				
				break;
			}
			
			this.ti++;
		}
		
		this.afterExecute();
	}

	// Note: The implementations of the following overridden methods have a 
	// better performance than those offered by the base class.

	@Override
	public void step() throws Exception {
		
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
		 * Save image of current state every n-defined steps and last image
		 */
		if (this.IMAGEDUMP == true && (this.ti % this.IMG_RATE == 0 || this.ti == this.numberOfSteps )){
			saveImage();
		}	 
	}

	/*
	 * Initialize the cell field 
	 */
	@Override
	public void initializeRandomly()
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
	
	/*
	 * Create densely populated city in cell field
	 */
	public void createCity(int startX, int startY, int length, int height, String population)
	{		
				try {
					Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
					for (int i = startX; i <= startX + length; i++) {
						for (int j = startY; j <= startY + height; j++) {
							arr[i][j].setInitialValue(population);
						}
					} 
				} catch (Exception e) {
					System.out.println("Exception: Out Of Bounds/ Wrong Population named - "+ e );
				}
		
		this.initialized = true;	
	}
	
	/*
	 * Creates barrier inside the cell field 
	 */
	public void createBarrier(int startX, int startY, int length, int height)
	{	
			try {
				Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
				for (int i = startX; i <= startX + length; i++) {
					for (int j = startY; j <= startY + height; j++) {
						arr[i][j].setInitialValue("B");
					}
				} 
			} catch (Exception e) {
				System.out.println("Exception: Out Of Bounds - "+e);	
				}
		this.initialized = true;
	}

	
	/*
	 * Save Image
	 */
	private void saveImage(){
		
		ExportTools exptools = new ExportTools();
		String filename = String.format("%06d", this.ti);
		
		//Define States in String Array
		String[] states = new String[4];
		states[0] = "X";
		states[1] = "O";
		states[2] = ".";
		states[3] = "B";
		
		//Define Colors in Color Array
		java.awt.Color[] colors = new java.awt.Color[4];
		colors[0] = Color.RED;
		colors[1] = Color.BLUE;
		colors[2] = Color.WHITE;
		colors[3] = Color.BLACK;
		
		exptools.save_image(LOCATION, filename, this.IMG_SCALE, this.ti, this.getCellularSpace(), this.nx, this.ny, states, colors);
	}
	
	/*
	 * Save File
	 */
	private void saveFile(String line){
		
		ExportTools exptools = new ExportTools();
		exptools.writeIntoFile(line, LOCATION);
	}
	
	
	/*
	 * PUBLIC
	 * Set location and enable ImageDump, WriteFile
	*/
	 public void setLocation(String location) {
		 this.LOCATION = location;
	 }
	 public void enableImageDump(int rate, int scale) {
		 this.IMAGEDUMP = true;
		 this.IMG_RATE = rate;
		 this.IMG_SCALE = scale;
	 }
	 public void enableWriteFile(){
		 this.WRITE_FILE = true;
	 }
}