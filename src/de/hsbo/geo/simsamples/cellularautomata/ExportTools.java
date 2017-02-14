package de.hsbo.geo.simsamples.cellularautomata;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ExportTools {
	
	public ExportTools(){}
	
	/*
	 * 
	 * PUBLIC
	 * 
	 * 
	 */
	
	/*
	 * write a content line into a file
	 */
	public void writeIntoFile(String linecontent, String location){
			
			FileWriter fileWriter = null;
			final String NEW_LINE_SEPARATOR = "\n";
	
			try {
				fileWriter = new FileWriter(location, true);
				fileWriter.append(linecontent);
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
	 * Save current state as image
	 */
	 public void save_image (String location, String filename, int scale, int ti, CellularSpace sp, int nx, int ny) {
		   try {
		     save_buffer_image(location + filename, scale, ti, sp, nx, ny);
		   } catch (Exception ex) {
		     System.exit(1);
		   }
		 }

	/*
	 * 
	 * PRIVATE
	 * 
	 * 
	 */

	private void write_buffer_image (Graphics2D plot, int ti, CellularSpace sp, int nx, int ny) throws Exception  {
		Cell[][] arr = ((RectangularSpace) sp).getCellArray();
		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < ny; j++) {

	     if (arr[i][j].getValue(ti) == "X") plot.setColor(new java.awt.Color( 255, 0, 0)); else
	     if (arr[i][j].getValue(ti) == "O") plot.setColor(new java.awt.Color(0, 0, 255)); else
	     if (arr[i][j].getValue(ti) == ".") plot.setColor(new java.awt.Color(220, 220, 220)); else
		 if (arr[i][j].getValue(ti) == "B") plot.setColor(new java.awt.Color(0, 0, 0)); else
	     plot.setColor(new java.awt.Color(0, 0, 0));
	     plot.fillRect(j, i, 1, 1);
			}
		}
	 }

	
	private void save_buffer_image (String filename, int scale, int ti, CellularSpace sp, int nx, int ny) throws Exception {
		 BufferedImage img = new BufferedImage(nx, ny, BufferedImage.TYPE_INT_ARGB);
		 write_buffer_image(img.createGraphics(), ti, sp, nx, ny);
	  
		 BufferedImage bi = new BufferedImage(scale * img.getWidth(null),
                                             scale * img.getHeight(null),
                                             BufferedImage.TYPE_INT_ARGB);

		 Graphics2D grph = (Graphics2D) bi.getGraphics();
		 grph.scale(scale, scale);
		 grph.drawImage(img, 0, 0, null);
		 grph.dispose();

		 	try {
			   ImageIO.write(bi, "png", new File(filename +".png"));
			   } catch (Exception ex) {
			     throw new Exception("Save image failed!");
			   }
	 }	
}
