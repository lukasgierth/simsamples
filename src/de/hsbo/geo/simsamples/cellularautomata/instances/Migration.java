package de.hsbo.geo.simsamples.cellularautomata.instances;

import java.util.Collections;
import java.util.List;

import de.hsbo.geo.simsamples.cellularautomata.Cell;
import de.hsbo.geo.simsamples.cellularautomata.DiscreteStateSet;
import de.hsbo.geo.simsamples.cellularautomata.NeighborhoodIndex;
import de.hsbo.geo.simsamples.cellularautomata.RectangularSpace;
import de.hsbo.geo.simsamples.cellularautomata.TransitionFunction;
import de.hsbo.geo.simsamples.common.RandomValueGenerator;

/**
 * Transition functions for "MigrationExample"
 * 
 * @author Lukas Gierth, Matthias Hensen
 */

public class Migration extends TransitionFunction {
	
	double vX;
	double vO;
	double eX;
	double eO;
	
	public Migration(double vX, double eX, double vO, double eO){
		this.vX = vX;
		this.vO = vO;
		this.eX = eX;
		this.eO = eO;
	}
	
	@Override
	public void defineStates() {
		
		// Define states: blank, population X, population Y
		this.states = new DiscreteStateSet(".", "X", "O");
	}

	@Override
	public void step(int ti) throws Exception{
		
		RectangularSpace sp = (RectangularSpace) this.getAutomaton().getCellularSpace();
		int row = RandomValueGenerator.number(0, sp.numberOfRows() - 1);
		int column = RandomValueGenerator.number(0, sp.numberOfColumns() - 1);	
	    Cell c = sp.getCell(row, column);
		String valC = (String) c.getValue(ti);
		
		while (valC == "."){
			row = RandomValueGenerator.number(0, sp.numberOfRows() - 1);
			column = RandomValueGenerator.number(0, sp.numberOfColumns() - 1);
			c = sp.getCell(row, column);
			valC = (String) c.getValue(ti);
		}
		

		//if (valC != "."){
			
			// Get 4 neighbors and shuffle them
			List<Cell> neighbors = c.getNeighbors(NeighborhoodIndex.NEIGH_4());
			Collections.shuffle(neighbors);

			for (int j = 0; j<neighbors.size(); j++){
				
				Cell c2 = neighbors.get(j);
				String valC2 = (String) c2.getValue(ti);
			
				double d = Math.random();
				
				// Migration
				if (valC2 == "."){
					
					c2.setValue(ti + 1, valC);
					c.setValue(ti + 1, valC2);
					return;
				}
				
				// Vermehrung
				else if (valC == valC2){
					
					double v;
					if (valC == "X"){
						v = vX;
					}
					else{
						v = vO;
					}
					
					if (d < v){
						
						List<Cell> neighbors2 = c2.getNeighbors(NeighborhoodIndex.NEIGH_4());
						neighbors2.addAll(neighbors);
						Collections.shuffle(neighbors2);
	
						for (int i = 0; i < neighbors2.size(); i++){
							
							if ((String) neighbors2.get(i).getValue(ti) == "."){
								neighbors2.get(i).setValue(ti + 1, valC2);
								//System.out.println("BREAK: "+ i + "   SIZE: "+ neighbors2.size());
								return;
							}
						}
					}
				}
				
				// Verdraengung
				else if (valC2 != valC){
					
					double e;
					if (valC == "X"){
						e = eX;
					}
					else {
						e = eO;
					}
					
					if (d < e){
						c2.setValue(ti + 1, valC);
						c.setValue(ti + 1, ".");
						return;
					}
				}
			}
			return;
			
		//}
		//c.setValue(ti + 1, "."); 			
	}
}
