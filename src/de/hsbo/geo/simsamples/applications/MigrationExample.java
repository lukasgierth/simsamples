package de.hsbo.geo.simsamples.applications;

import de.hsbo.geo.simsamples.cellularautomata.CellularAutomaton;
import de.hsbo.geo.simsamples.cellularautomata.MigrationAutomaton;
import de.hsbo.geo.simsamples.cellularautomata.instances.Migration;

/**
 * "Migration Model" example. The automaton's transition function is implemented 
 * by the class {@link Migration}.
 * 
 * @author Lukas Gierth, Matthias Hensen
 */
public class MigrationExample 
{
	public static void main(String[] args) throws Exception 
	{
		// Create automaton consisting of 200x200 cells:
		CellularAutomaton a = 
			new MigrationAutomaton(20, 20, new Migration(0.7, 0.7, 0.5, 0.5));
		a.initializeRandomly();
		
		// Execute 200 time steps and provide console output:
		a.enableConsoleDump();
		a.execute(10);
	}
}
