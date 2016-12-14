package de.hsbo.geo.simsamples.cellularautomata;

import java.util.Random;

import de.hsbo.geo.simsamples.common.RandomValueGenerator;

/**
 * Implementation of a universal Cellular Automaton operating on Migration
 * cellular spaces.
 * 
 * @author Lukas Gierth
 */
public class MigrationAutomaton extends SingleCellularAutomaton 
{
	private int nx, ny;
	

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
		SingleTransitionFunction delta) 
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
		SingleTransitionFunction delta) 
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
			
			this.stepSingle();

			if (this.consoleDump) {
				System.out.println("ti = " + ti + ":"); 
				((RectangularSpace) this.getCellularSpace()).dump(this.ti); 
			}
			
			this.ti++;
		}
		
		this.afterExecute();
	}

	// Note: The implementations of the following overridden methods have a 
	// better performance than those offered by the base class.

	
	public void stepSingle() throws Exception {
		this.delta.beforeStep(this.ti);

		Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
		Random random = new Random();
		
		this.delta.step(arr[random.nextInt(this.nx)][random.nextInt(this.ny)], this.ti);
	}
	@Override
	public void step() throws Exception 
	{
		/*
		this.delta.beforeStep(this.ti);

		Cell[][] arr = ((RectangularSpace) this.cells).getCellArray();
		

		Random random = new Random();
		
		this.delta.step(arr[random.nextInt(this.nx)][random.nextInt(this.ny)], this.ti);
		//this.delta.step(this.ti);
		 */
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
}