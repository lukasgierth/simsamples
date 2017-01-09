package de.hsbo.geo.simsamples.applications;

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
	
	private static String filelocation ="/home/lukas/migration/";
	private static int timesteps = 5000;

	public static void main(String[] args) throws Exception 
	{
		/* Create automaton consisting of 200x200 cells:
		 * 
		 * vX, eX, v0, e0
		 */
		
		MigrationAutomaton a = 
			new MigrationAutomaton(100, 100, new Migration(1, 0, 1, 0));
		
		//a.enableConsoleDump();
		a.initializeRandomly();
		a.enableImages();
		a.setLocation(filelocation);
		a.execute(timesteps);		
	}
}
