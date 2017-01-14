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
	private static String fileCSV=filelocation+"output.csv";

	public static void main(String[] args) throws Exception 
	{
		/* Create automaton consisting of 100x100 cells:
		 * 
		 * Migration(vX, eX, v0, e0, neighbors)
		 * createBarrier(startX, startY, length, height)
		 * enableImageDump(imagerate, scale)
		 * execute(number of steps)
		 */
		
		MigrationAutomaton a = 
			new MigrationAutomaton(100, 100, new Migration(0.2, 1, 0.4, 0.7, 2));
		
		//a.enableConsoleDump();
		a.initializeRandomly();
		a.createCity(70,70,10,15,"O");
		a.createCity(10,10,25,15,"X");
		a.createBarrier(20,30,20,300);
		a.enableImageDump(500,10);
		a.enableWriteCSV();
		a.setFileCSV(fileCSV);
		a.setLocation(filelocation);
		a.execute(25000);		
	}
}
