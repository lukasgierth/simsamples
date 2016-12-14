package de.hsbo.geo.simsamples.cellularautomata.instances;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.hsbo.geo.simsamples.cellularautomata.Cell;
import de.hsbo.geo.simsamples.cellularautomata.DiscreteStateSet;
import de.hsbo.geo.simsamples.cellularautomata.NeighborhoodIndex;
import de.hsbo.geo.simsamples.cellularautomata.TransitionFunction;

/**
 * Transition functions for "MigrationExample"
 * 
 * @author Lukas Gierth, Matthias Hensen
 */

public class Migration extends TransitionFunction 
{
	@Override
	public void defineStates() {
		
		// Define states: blank, population X, population Y
		this.states = new DiscreteStateSet(".", "X", "Y");
	}

	@Override
	public void step(Cell c, int ti) throws Exception{
		
		String valC = (String) c.getValue(ti);

		if (valC != "."){
			
			// Get 4 neighbors and choose one randomly
			List<Cell> neighbors = c.getNeighbors(NeighborhoodIndex.NEIGH_4());
			Collections.shuffle(neighbors);
			Cell c2 = neighbors.get(0);
			
			double vermehrungsrate = 0.7;
			double verdraengungsrate = 0.6;
			double d = Math.random();
	
			// Get values for cell and neighbor cell
			String valC2 = (String) c2.getValue(ti);
			
			// Vermehrung
			if (valC == valC2){
				if (d < vermehrungsrate){
					
					List<Cell> neighbors2 = c2.getNeighbors(NeighborhoodIndex.NEIGH_4());
					Collections.shuffle(neighbors2);
					
					int size = neighbors2.size();

					for (int i = 0; i < size; i++){
						
						if ((String) neighbors2.get(i).getValue(ti) == "."){
							neighbors2.get(i).setValue(ti + 1, valC2);
							System.out.println("BREAK: "+ i + "   SIZE: "+size);
							break;
						}
					}
					
					return;
				}
				else {}
			}
			
			// Migration
			if (valC2 == "."){
				
				c2.setValue(ti + 1, valC);
				c.setValue(ti + 1, ".");
				return;
			}
			
			// Verdraengung
			else if (valC2 != valC){
				if (d < verdraengungsrate){
					
					c2.setValue(ti + 1, valC);
					c.setValue(ti + 1, ".");
					return;
				}
				
				else {
					c.setValue(ti + 1, ".");
				}
			}
		}
		
		else {}
		
	}
}
