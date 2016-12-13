package de.hsbo.geo.simsamples.cellularautomata.instances;

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
		// Bekomme die 4 Nachbarn
		List<Cell> neighbors = c.getNeighbors(NeighborhoodIndex.NEIGH_4());
		
		// Wähle zufällig eines der 4 Nachbarfelder
		Random randomizer = new Random();
		Cell c2 = neighbors.get(randomizer.nextInt(neighbors.size()));
		
		// Werte für Zelle und Nachbarzelle
		String valC = (String) c.getValue(ti);
		String valC2 = (String) c2.getValue(ti);
		
		if (valC != "."){
			
			// Wenn gleiche Population
			//TODO: Vermehrungsrate einbauen
			if (valC == valC2) {
				List<Cell> neighbors2 = c2.getNeighbors(NeighborhoodIndex.NEIGH_4());
				Cell c3 = neighbors2.get(randomizer.nextInt(neighbors2.size()));
				c3.setValue(ti + 1, valC);		
				return;
			}
			
			// Wenn leeres Feld --> Migration
			if (valC2 == ".") {
				// Neues Feld bekommt die gleiche Population
				c2.setValue(ti + 1, valC);
				// Altes Feld wird leer
				c.setValue(ti + 1, ".");
				return;
			}
			
			//TODO: Verdrängungsrate einbauen
			else if (valC2 != valC){
				//Verdrängungsrate E
				c2.setValue(ti + 1, valC);
				c.setValue(ti + 1, ".");
				return;
			}
		}
		
		else {
			//do nothing
			
		}
		
	}
}
