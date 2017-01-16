package de.hsbo.geo.simsamples.cellularautomata;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	private boolean IMAGEDUMP;
	private String LOCATION;
	private int IMG_RATE;
	private int IMG_SCALE;
	private boolean WriteCSV;
	private String fileCSV;

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

			if (this.consoleDump == true) {

				int countXpercent = (int) (countX/(rows*col)*100);
				int countOpercent = (int) (countO/(rows*col)*100);
				int countPpercent = (int) (countP/(rows*col)*100);
				
				System.out.println("Größe der Population X: " + countX + " Zellen| " + countXpercent
						+ " %\n" + "Größe der Population O: " + countO + " Zellen| " + countOpercent
						+ " %\n" + "Anzahl leerer Zellen: " + countP + " Zellen| " + countPpercent
						+ " %\n\n");

			}
			
			if (this.WriteCSV == true) {
				writeCSV(ti, countX, countO, countP);
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
					String filename = String.format("%06d", this.ti);
					save_image(LOCATION + filename);
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
		 * Make screenshot every n steps and last image
		 */
		if (this.IMAGEDUMP == true && (this.ti % this.IMG_RATE == 0 || this.ti == this.numberOfSteps )){
			String filename = String.format("%06d", this.ti);
			save_image(LOCATION + filename);
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
	 * Create densely populated city in cell field
	 */
	public void createCity(int startX, int startY, int length, int height, String population) throws Exception
	{
		if (population == "X" || population == "O"){
			if ((startX+length) < this.nx && (startY+height) < this.ny){
				Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
				for (int i = startX; i <= startX+length; i++) {
					for (int j = startY; j <= startY+height; j++) {
						arr[i][j].setInitialValue(population);
					}
				}
			}
			
			else{
				System.out.println("City out of bounds\nSet default city 5x5 bottomright");
				
				Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
				for (int i = (this.nx-10); i <=(this.nx-5) ; i++) {
					for (int j = (this.ny-10); j <= (this.ny-5); j++) {
						arr[i][j].setInitialValue(population);
					}
				}
			}
		}
		
		else {
			System.out.println("POPULATION "+population + " UNKNOW");
		}
		
		this.initialized = true;	
	}
	
	/*
	 * Creates barrier inside the cell field 
	 */
	public void createBarrier(int startX, int startY, int length, int height) throws Exception
	{	
		if ((startX+length) < this.nx && (startY+height) < this.ny){
			Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
			for (int i = startX; i <= startX+length; i++) {
				for (int j = startY; j <= startY+height; j++) {
					arr[i][j].setInitialValue("B");
				}
			}
		}
		
		else{
			System.out.println("Barrier out of bounds\nSet default barrier 10x10 centered");
			
			Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
			for (int i = (this.nx/2-5); i <= (this.nx/2+5); i++) {
				for (int j = (this.ny/2-5); j <= (this.ny/2+5); j++) {
					arr[i][j].setInitialValue("B");
				}
			}
		}
		
		this.initialized = true;

	}
	
	
	 /*
	  *  Addition
	  *  Later integrate into CellularAutomaton???
	  *  
	  *  Only called internally
	  *  
	  */
	
	/*
	 * CSV File Writer
	 */
	
	private void writeCSV(int ti, int countX, int countO, int countP){
		
		FileWriter fileWriter = null;
		//final String FILE_HEADER = "timestep,countX,countO,countP";
		// Delimiter used in CSV file
		final String COMMA_DELIMITER = ",";
		final String NEW_LINE_SEPARATOR = "\n";

		try {
			fileWriter = new FileWriter(LOCATION+fileCSV, true);
			// Write the CSV file header
			// fileWriter.append(FILE_HEADER.toString());
			// Add a new line separator after the header
			// fileWriter.append(NEW_LINE_SEPARATOR);

			fileWriter.append(String.valueOf(ti));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(countX));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(countO));
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(countP));
			fileWriter.append(NEW_LINE_SEPARATOR);

		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * Image Writer
	 */
	private void write_buffer_image (Graphics2D plot) throws Exception  {
		Cell[][] arr =((RectangularSpace) this.getCellularSpace()).getCellArray();
		for (int i = 0; i < this.nx; i++) {
			for (int j = 0; j < this.ny; j++) {

	     if (arr[i][j].getValue(ti) == "X") plot.setColor(new java.awt.Color( 255, 0, 0)); else
	     if (arr[i][j].getValue(ti) == "O") plot.setColor(new java.awt.Color(0, 0, 255)); else
	     if (arr[i][j].getValue(ti) == ".") plot.setColor(new java.awt.Color(220, 220, 220)); else
		 if (arr[i][j].getValue(ti) == "B") plot.setColor(new java.awt.Color(0, 0, 0)); else
	     plot.setColor(new java.awt.Color(0, 0, 0));
	     plot.fillRect(j, i, 1, 1);
	     
			}
		}
	 }

	
	private void save_buffer_image (String filename) throws Exception {
		 BufferedImage img = new BufferedImage(this.nx,this.ny,BufferedImage.TYPE_INT_ARGB);
		 write_buffer_image(img.createGraphics());
	  
		 //int scale=this.IMG_SCALE;
		 BufferedImage bi = new BufferedImage(this.IMG_SCALE * img.getWidth(null),
                                             this.IMG_SCALE * img.getHeight(null),
                                             BufferedImage.TYPE_INT_ARGB);

		 Graphics2D grph = (Graphics2D) bi.getGraphics();
		 grph.scale(this.IMG_SCALE, this.IMG_SCALE);

        // everything drawn with grph from now on will get scaled.

		 grph.drawImage(img, 0, 0, null);
		 grph.dispose();

		 	try {
			   ImageIO.write(bi, "png", new File(filename +".png"));
			   } catch (Exception ex) {
			     throw new Exception("Save image failed!");
			   }
	   
	 		}

	 private void save_image (String filename) {
	   try {
	     save_buffer_image(filename);
	   } catch (Exception ex) {
	     System.exit(1);
	   }
	 }
	 
	/*
	 * PUBLIC
	 * Set location and enable ImageDump
	*/
	 public void setLocation(String loc) {
		 this.LOCATION = loc;
	 }
	 public void enableImageDump(int rate, int scale) {
		 this.IMAGEDUMP = true;
		 this.IMG_RATE = rate;
		 this.IMG_SCALE = scale;
	 }
	 public void enableWriteCSV(){
		 this.WriteCSV = true;
	 }
	 public void setFileCSV(String fileCSV){
		 this.fileCSV = fileCSV;
	 }
}