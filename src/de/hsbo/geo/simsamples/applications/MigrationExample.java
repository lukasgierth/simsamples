package de.hsbo.geo.simsamples.applications;

import java.io.File;

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
	private static String fileCSV="output.csv";
	private static String simName="04_barrier/";

	public static void main(String[] args) throws Exception 
	{
		/* Create automaton consisting of 100x100 cells:
		 * 
		 * Migration(vX, eX, v0, e0, neighbors)
		 * createBarrier(startX, startY, length, height)
		 * enableImageDump(imagerate, scale)
		 * execute(number of steps)
		 */
		
		new File(filelocation+simName).mkdirs();
		
		MigrationAutomaton a = 
			new MigrationAutomaton(100, 100, new Migration(0.5, 0.5, 0.5, 0.5, 1));
		
		a.initializeRandomly();
		
		//a.enableConsoleDump();
		
		a.createCity(70,70,29,29,"O");
		a.createCity(0,0,30,30,"X");
		a.createBarrier(35,35,20,20);
		
		a.enableImageDump(500,10);
		a.enableWriteCSV();
		a.setFileCSV(fileCSV);
		a.setLocation(filelocation+simName);
		a.execute(25000);		
	}
}
