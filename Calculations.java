import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

class Year {

	private String year;
	private ArrayList<Double> prcp;
	private int observedDays;

	public Year() {
		year = null;
		prcp = new ArrayList<Double>();
		observedDays = 0;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void addPRCP(double prcp) {
		//this.prcp.add(prcp / 100); // DATA IS READ IN AS 100th OF AN INCH
		//this.prcp.add(prcp); // MILLIMETERS
		//this.prcp.add(prcp / 10); // 10TH MILLIMETER
		this.prcp.add(prcp / Calculations.units);
	}
	
	public void addObservedDays() {
		observedDays++;
	}
	
	public String getYear() {
		return year;
	}
	
	public double getPRCP(int i) {
		return prcp.get(i);
	}
	public int getObserveredDays() {
		return observedDays;
	}
	
}

public class Calculations {
	
	private String station;
	private String stationName;
	public static int units = 1; 	// 1 is for millimeters
													 // 10 is for 10th of a millimeter
													// 100 is for 100th of an inch
													// 1 is also for inches (different initSArray is used)
	public String unitsName;
	private static boolean isUnitsSelected = false;
	final private double[] cnArray = { 0, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48,
			49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
			76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100 };

	private double[] cnDry = new double[ cnArray.length ] ;
	private double[] cnWet = new double[ cnArray.length ] ;
	
	private double[] sArray = new double[cnArray.length];
	
	// inch
	void initSArrayInch1() {
		for (int i = 0; i < sArray.length; i++) {
			sArray[i] = (1000 / cnArray[i]) - 10;
		}
		unitsName = "in";
	}
	
	// 100th of an inch
	void initSArrayInch100() {
		for (int i = 0; i < sArray.length; i++) {
			sArray[i] = (100000 / cnArray[i]) - 1000;
		}
		unitsName = "100th of an in";
	}
	
	// millimeters
	void initSArrayMillimeter1() {
		for (int i = 0; i < sArray.length; i++) {
			sArray[i] = (2540 / cnArray[i]) - 25.4;
		}
		unitsName = "mm";
	}
	
	// 10th of a millimeter
	void initSArrayMillimeter10() {
		for (int i = 0; i < sArray.length; i++) {
			sArray[i] = (25400 / cnArray[i]) - 254;
		}
		unitsName = "10th of a mm";
	}
	
	public static void main(String[] args) {

		Calculations calc = new Calculations();
		
		JFrame frame = new JFrame("Runoff");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton millimeters = new JButton("millimeter");
		millimeters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.units = 1;
				calc.initSArrayMillimeter1();
				isUnitsSelected = true;
			}
		});
		JButton inches = new JButton("inch");
		inches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.units = 1;
				calc.initSArrayInch1();
				isUnitsSelected = true;
			}
		});
		JButton inches100th = new JButton("100th inch");
		inches100th.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.units = 100;
				calc.initSArrayInch100();
				isUnitsSelected = true;
			}
		});
		JButton millimeters10th = new JButton("10th millimeter");
		millimeters10th.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc.units = 10;
				calc.initSArrayMillimeter10();
				isUnitsSelected = true;
			}
		});
		JLabel directions = new JLabel("Please select the input data's metric system");
		JLabel emptyLabel = new JLabel();
		frame.setLayout(new GridLayout(3, 2));
		frame.add(directions);
		frame.add(emptyLabel);
		frame.add(millimeters);
		frame.add(inches);
		frame.add(inches100th);
		frame.add(millimeters10th);
		frame.pack();
		frame.setVisible(true);
		
		while (isUnitsSelected == false)
			System.out.println("select units");
		
		millimeters.setVisible(false);
		inches.setVisible(false);
		inches100th.setVisible(false);
		millimeters10th.setVisible(false);
		directions.setText("Please wait.. results in \"Results\" folder");
		//this.prcp.add(prcp / 100); // DATA IS READ IN AS 100th OF AN INCH
		//this.prcp.add(prcp); // MILLIMETERS
		//this.prcp.add(prcp / 10); // 10TH MILLIMETER
		
		
		long startTime = System.currentTimeMillis();
		//Calculations calc = new Calculations();
		//calc.initSArray();
		//calc.initSArrayMM();
		//calc.initializeCNdry();
		//calc.initializeCNwet();
		ArrayList<Year> years = new ArrayList<Year>();

		File folder = new File(".");
		//File folder = new File("new_input/");
		
		File[] listOfFiles = folder.listFiles();
		String[] csvFilePaths = new String[listOfFiles.length];
		String[] csvFileNames = new String[listOfFiles.length];
		int counter = 0;

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".csv")) {
				years = calc.readData(file.getAbsolutePath()); // read in the entire csv file first and store all of the data
				csvFilePaths[counter] = file.getAbsolutePath();
				csvFileNames[counter] = file.toString();
				//System.out.println(csvFilePaths[i]);
				//System.out.println(file.getName());
				//calc.generateOutput(csvFileNames[i], csvFilePaths[i], years); // perform all of the computations and then print it out to the output file // only for eclipse
				calc.generateOutput(file.getName(), null, years); // for jar file
				counter++;
				/*for (int j = 0; j < years.size(); j++) {
					System.out.println(years.get(j).getYear());
				}*/
			} 
		}
		
		
		long estimatedTime = System.currentTimeMillis() - startTime;

		System.out.println("Running time : " + estimatedTime + " seconds");

		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		
	}

	ArrayList<Year> readData(String filename) {
	
		ArrayList<Year> years = new ArrayList<Year>();
		BufferedReader br = null;
		String line = "";
		
		try {
	
			br = new BufferedReader(new FileReader(filename));
	
			line = br.readLine();
			String[] columnHeaders = line.split(",");
	
			// constant values
			line = br.readLine();
			String[] dataConstants = line.split(",");
			int dateIndex = 0;
			int prcpIndex = 0;
			Year year = new Year();
	
			// grabbing constant values first
			for (int i = 0; i < dataConstants.length; i++) {
				if (columnHeaders[i].toLowerCase().equals("station")) {
					station = dataConstants[i];
				} else if (columnHeaders[i].toLowerCase().equals("station_name")) {
					stationName = dataConstants[i];
				} else if (columnHeaders[i].toLowerCase().equals("date")) { // determined which column the year is on
					dateIndex = i;
					year.setYear(dataConstants[i].substring(0, 4)); 
				} else if (columnHeaders[i].toLowerCase().equals("prcp")) { // determined which column the prcp is on
					prcpIndex = i;
					year.addPRCP(Double.parseDouble(dataConstants[i]));
					year.addObservedDays();
				}
			}
	
			// reading in the data line by line
			while ((line = br.readLine()) != null) {
	
				String[] tokens = line.split(",");
				if (!tokens[dateIndex].substring(0, 4).equals(year.getYear())) { // different year
					years.add(year);
					year = new Year();
					year.setYear(tokens[dateIndex].substring(0, 4));
				}
				year.addPRCP(Double.parseDouble(tokens[prcpIndex]));
				year.addObservedDays();
			}
			
			years.add(year); // reaches end of file so just add the last computed year onto the arraylist of years
	
			br.close();
	
		} catch (Exception e) {
	
			e.printStackTrace();
	
		}
		
		return years;
		
	}

	void generateOutput(String filename, String filePath, ArrayList<Year> years) {
		/* Object to write on a file */
		FileWriter write = null ;
		FileWriter write2 = null;

		/*for (int i = 0; i < years.size(); i++) {
			System.out.println(years.get(i).getYear());
		}*/
		/* Output file is opened here */
		try {
			
			/* Check if the OUTPUT folder exists or not. Create it if it doesn't exist. */
			//System.out.println(filePath.substring(0, filePath.indexOf(filename)));
			//System.out.println("filepath: " + filePath.toString()); // eclipse path
			System.out.println("filename: " + filename);
			//CreateDir(filePath.substring(0, filePath.indexOf(filename)) + "OUTPUT") ; // eclipse path
			//write = new FileWriter(filePath.substring(0, filePath.indexOf(filename)) + "OUTPUT" + filename.substring(filename.indexOf("\\"), filename.indexOf("."))+ "q.txt"); // eclipse path
			CreateDir("Results");
			CreateDir("Results/Data");
			write = new FileWriter("Results\\" + filename.substring(0, filename.indexOf(".")) + "q.txt");
			write2 = new FileWriter("Results/Data\\" + filename.substring(0, filename.indexOf(".")) + "data.txt");
		} catch ( FileNotFoundException e ) {
			e.printStackTrace() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
			PrintWriter outf = new PrintWriter( write ) ;
			PrintWriter outf2 = new PrintWriter( write2 );
			//String line = null ;
			
			/* Printing the data found so far */
			outf.printf( "%s\t", filename.substring(filename.indexOf("\\") + 1, filename.indexOf("."))) ;
			//outf.printf( "%s\t", station) ;
			outf.printf( "Station: %s\t", stationName) ;
			outf.printf( "%s\t", "Version 1.0");
			outf.printf( "ObservedYears:%d", years.size()) ;
			outf.printf( "\tDataUnits:%s", unitsName ) ;
			outf.println();
			outf.printf( "CNvalue" ) ;
			outf.print( "\t\t" ) ;
			
			for(int i = 0 ; i < cnArray.length ; i++ ) {
				
				if( cnArray[ i ] < 10 ) {
					outf.printf( "CN00%.0f", cnArray[ i ] ) ;
					outf.print( "\t" ) ;
				}// if ends here
				
				if( cnArray[ i ] >= 10 && cnArray[ i ] < 100 ) {
					outf.printf( "CN0%.0f", cnArray[ i ] ) ;
					outf.print( "\t" ) ;
				}//if ends here
				
				if( cnArray[ i ] >= 100 && cnArray[ i ] < 1000 ) {
					outf.printf( "CN%.0f", cnArray[ i ] ) ;
					outf.print( "\t" ) ;
				}//if ends here
				
			}//for loop ends here
			outf.println() ;
			
			
			////////////////////////////////////////////////////////////////////////////////////////////

			//double[] outputQs = new double[cnArray.length];
			/*
			for (int i = 0; i < years.size(); i++) { // loop through every year and print out the row
				outf.printf("Year: %.0f", Double.parseDouble(years.get(i).getYear()));
				for (int j = 0; j < cnArray.length; j++) { // loop through all columns for a year (all cn values)
					double[] qValues = new double[365];//[years.get(i).getObserveredDays()]; // store all of computed q values per day
					for (int k = 0; k < 365; k++) { // calculates all of the q values in a year that were observed
						//System.out.println(years.get(i).getObserveredDays());
						if (k < years.get(i).getObserveredDays()) {
						if (years.get(i).getPRCP(k) < (0.2 * sArray[j])) {
							//qValues[k] = Math.pow((0 - (0.2 * sArray[j])), 2) / (0 + (0.8 * sArray[j]));
							qValues[k] = 0;
						} else {
							qValues[k] = Math.pow((years.get(i).getPRCP(k) - (0.2 * sArray[j])), 2) / (years.get(i).getPRCP(k) + (0.8 * sArray[j]));
						}} else{
							qValues[k] = Math.pow((0 - (0.2 * sArray[j])), 2) / (0 + (0.8 * sArray[j]));
						}
					}
					// calculate the Q value of the year
					
					double sum = 0;
					for (int k = 0; k < qValues.length; k++) {
						sum += qValues[k];
					}
					outf.printf("%8.2f", sum / qValues.length);
				}
				outf.println();
			}
		 */
			/*for (int i = 0; i < sArray.length; i++) {
				System.out.println(sArray[i] + " " + cnArray[i]);
			}
			for (int i = 0; i < years.size(); i++) {
				System.out.print(years.get(i) + " ");
			}*/
			//System.out.println(years.size());
			
			outf2.printf( "%s", filename.substring(filename.indexOf("\\") + 1, filename.indexOf("."))) ;
			outf2.println();
			outf2.printf( "Observed Days are the number of days recorded per year in the spreadsheet");
			outf2.println();
			//outf2.printf( "Observed Days are the number of days that produced enough precipitation to produce runoff");
			//outf2.println();
			outf2.println();
			
			for (int i = 0; i < years.size(); i++) { // loop through every year and print out the row

				outf2.printf("Year: %s\tObserved Days: %d", years.get(i).getYear(), years.get(i).getObserveredDays());

				outf.printf("Year: %.0f", Double.parseDouble(years.get(i).getYear()));
				int actualObservedDays = years.get(i).getObserveredDays();
				//System.out.println(actualObservedDays + " " + years.get(i).getObserveredDays());
				for (int j = 0; j < cnArray.length; j++) { // loop through all columns for a year (all cn values)
					//double[] qValues = new double[years.get(i).getObserveredDays()]; // store all of computed q values per day
					ArrayList<Double> qValues = new ArrayList<Double>();
					for (int k = 0; k < years.get(i).getObserveredDays(); k++) { // calculates all of the q values in a year that were observed
						if (years.get(i).getPRCP(k) < (0.2 * sArray[j])) { // completely skip it
							//qValues[k] = Math.pow((0 - (0.2 * sArray[j])), 2) / (0 + (0.8 * sArray[j]));
							//qValues[k] = 0;
							//if (k == 0)
								//actualObservedDays--;
							continue;
						} else {
							//qValues[k] = Math.pow((years.get(i).getPRCP(k) - (0.2 * sArray[j])), 2) / (years.get(i).getPRCP(k) + (0.8 * sArray[j]));
							qValues.add(Math.pow((years.get(i).getPRCP(k) - (0.2 * sArray[j])), 2) / (years.get(i).getPRCP(k) + (0.8 * sArray[j])));
						}
					}
					
					// calculate the Q value of the year
					
					double sum = 0;
					/*for (int k = 0; k < qValues.length; k++) {
						sum += qValues[k];
					}*/
					for (int k = 0; k < qValues.size(); k++) {
						sum += qValues.get(k);
					}
					//double average = sum / qValues.size();
					if (Double.isNaN(sum)) {
						outf.printf("%8.2f", 0.00);
					} else {
						outf.printf("%8.2f", sum);
					}
				}

				//outf2.printf("Observed Days:%d", actualObservedDays);
				outf2.println();
				
				outf.println();
			}

			// Q = (P - Ia) ^ 2 / ((P - Ia) + S)
			
			// Ia = 0.2 * S
			
			//Q=(P-.0.2 * S)^2 / (P+0.8 * S)
			//Q =   Math.pow(   ( precp - ( 0.2 * sArray[ i ] )  ) , 2  )    /      (   precp + ( 0.8 * sArray[ i ] )   )   ;
			// S=1000/CN - 10
			//Double.valueOf(df.format(((25400 / cnWet[i]) - 254)));
			
			
			
			////////////////////////////////////////////////////////////////////////////////////////////
			
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			// calculate all of the data and print it out to the file
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			
			
			try {
				write.close();
				write2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			outf.close();
			outf2.close();

	}

	/* Create a folder if it doesn't exist */
	void CreateDir( String dirAddress ) {
		
		File theDir = new File( dirAddress ) ;
	
		  // if the directory does not exist, create it
		  if (!theDir.exists()) {
		    System.out.println("creating directory: " + dirAddress ) ; 
		    boolean result = false;
	
		    try{
		        theDir.mkdir();
		        result = true;
		     } catch(SecurityException se){
		        //handle it
		     }        
		     if(result) {    
		       System.out.println("DIR created");  
		     }
		  }
	}


	/* Initialize CNdry array */
	void initializeCNdry() {
	
		int i = 0 ;
		for(i = 0 ; i < cnArray.length ; i++ ) {
			cnDry[ i ] = 0.39 * cnArray[ i ] * Math.exp( 0.009 * cnArray[ i ] ) ;
			//System.out.println( cnDry[ i ] ) ;
		}
		
	}
	
	void initializeCNwet() {
	
		int i = 0 ;
		for(i = 0 ; i < cnArray.length ; i++ ) {
			cnWet[ i ] = 1.95 * cnArray[ i ] * Math.exp( -0.00663 * cnArray[ i ] ) ;
		//System.out.println( cnWet[ i ] ) ;
		}
	}
	
}