package de.hsbo.geo.simsamples.cellularautomata.instances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.hsbo.geo.simsamples.cellularautomata.Cell;
import de.hsbo.geo.simsamples.cellularautomata.DiscreteStateSet;
import de.hsbo.geo.simsamples.cellularautomata.NeighborhoodIndex;
import de.hsbo.geo.simsamples.cellularautomata.TransitionFunction;

public class Migration extends TransitionFunction 
{
	@Override
	public void defineStates() {
		
		// Define states: blank, population X, population Y
		this.states = new DiscreteStateSet(".", "X", "Y");
	}

	@Override
	public void step(Cell c, int ti) throws Exception
	{	
		// Get 4 neighbors and choose one randomly
		List<Cell> neighbors = c.getNeighbors(NeighborhoodIndex.NEIGH_4());
		Random randomizer = new Random();
		Cell c2 = neighbors.get(randomizer.nextInt(neighbors.size()));
		
		double vermehrungsrate = 0.7;
		double verdraengungsrate = 0.6;
		double d = Math.random();

		// Get values for cell and neighbor cell
		String valC = (String) c.getValue(ti);
		String valC2 = (String) c2.getValue(ti);
		
		if (valC != "."){
			
			// Vermehrung
			if (valC == valC2){
				if (d < vermehrungsrate){
					
					List<Cell> neighbors2 = c2.getNeighbors(NeighborhoodIndex.NEIGH_4());
					Collections.shuffle(neighbors2);
					int size = neighbors.size();

					if ((String) neighbors2.get(0).getValue(ti) == "." && size > 0){
						neighbors2.get(0).setValue(ti + 1, valC);	
					}
					else if ((String) neighbors2.get(1).getValue(ti) == "." && size > 1){
						neighbors2.get(1).setValue(ti + 1, valC);	
					}
					else if ((String) neighbors2.get(2).getValue(ti) == "." && size > 2){
						neighbors2.get(2).setValue(ti + 1, valC);	
					}
					else {}
					
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
