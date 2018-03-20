import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CSVParser {
	int N;
	String trainEdgesFilename;
	String walkingEdgesFilename;
	String stationNamesFilename;
	String edgeColorsFilename;
	int[][] trainEdges;
	int[][] walkingEdges;
	Line[][] edgeColors;
	Map<Integer, String> stationNames;
			
	public CSVParser(String trainEdgesFilename, String walkingEdgesFilename, 
			String edgeColorsFilename, String stationNamesFilename) {
		this.trainEdgesFilename = trainEdgesFilename;
		this.walkingEdgesFilename = walkingEdgesFilename;
		this.stationNamesFilename = stationNamesFilename;
		this.edgeColorsFilename = edgeColorsFilename;
	}
	
	public void parse() {
		String currentRelativePath = Paths.get("").toAbsolutePath().toString() + "/src/";
		System.out.println("Parsing Names...........");
		stationNames = parseStationNames(currentRelativePath + stationNamesFilename);
		System.out.println("Parsing Train Edges...........");
		trainEdges = parseEdges(currentRelativePath + trainEdgesFilename);
		System.out.println("Parsing Walking Edges...........");
		walkingEdges = parseEdges(currentRelativePath + walkingEdgesFilename);
		System.out.println("Parsing Edge Colors...........");
		edgeColors = parseEdgeColors(currentRelativePath + edgeColorsFilename);
	}
	
	public int[][] parseEdges(String filename) {
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int[][] edges = new int[N][N];
        try {
        		br = new BufferedReader(new FileReader(filename));
        		int s = -1;
        		while ((line = br.readLine()) != null) {
        			if (s == -1) {
        				s++;
        				continue;
        			}
        			String[] arr = line.split(csvSplitBy);
        			for (int d = 1; d < arr.length; d++) {
        				if (!arr[d].equals("") && s < d-1) {
        					edges[s][d-1] = Integer.parseInt(arr[d]);
        					//System.out.println("From id=" + s + " to id=" + (d-1) + " is " + arr[d] + " mins.");
        				}
        			}
        			s++;
        		}
        	} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return edges;
	}
	
	public Map<Integer, String> parseStationNames(String filename) {
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        Map<Integer, String> stationNames = new HashMap<Integer, String>();
        int count = 0;
        try {
        		br = new BufferedReader(new FileReader(filename));
        		while ((line = br.readLine()) != null) {
        			if (count == 0) {
        				count++;
        				continue;
        			}
        			String[] arr = line.split(csvSplitBy);
        			if (count == 1) {
        				count++;
//        				System.out.println(arr[0]);
//        				System.out.println(arr[1]);
        				N = Integer.parseInt(arr[1]);
        				continue;
        			}
        			stationNames.put(Integer.parseInt(arr[0]), arr[1]);
        			//System.out.println(stationNames.get(Integer.parseInt(arr[0])));
        		}
        	} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stationNames;
	}
	
	public Line[][] parseEdgeColors(String filename) {
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        Line[][] edgeColors = new Line[N][N];
        try {
	    		br = new BufferedReader(new FileReader(filename));
	    		int s = -1;
	    		while ((line = br.readLine()) != null) {
	    			if (s == -1) {
	    				s++;
	    				continue;
	    			}
	    			String[] arr = line.split(csvSplitBy);
	    			switch (arr[2]) {
					case "Red": edgeColors[Integer.parseInt(arr[0])][Integer.parseInt(arr[1])] = Line.RED;
								break;
					case "Green": edgeColors[Integer.parseInt(arr[0])][Integer.parseInt(arr[1])] = Line.GREEN;
								break;
					case "Blue": edgeColors[Integer.parseInt(arr[0])][Integer.parseInt(arr[1])] = Line.BLUE;
								break;
					case "Orange": edgeColors[Integer.parseInt(arr[0])][Integer.parseInt(arr[1])] = Line.ORANGE;
								break;
					case "Brown": edgeColors[Integer.parseInt(arr[0])][Integer.parseInt(arr[1])] = Line.BROWN;
								break;
					case "Yellow": edgeColors[Integer.parseInt(arr[0])][Integer.parseInt(arr[1])] = Line.YELLOW;
								break;
					case "": break;
					default: System.out.println("FUCKED: " + arr[2]); break;
				}
//	    			for (int d = 1; d < arr.length; d++) {
//	    				//System.out.println(arr[d].equals("Green"));
//	    				
////	    				if (!arr[d].equals("")) {
////	    					edgeColors[s][d] = Integer.parseInt(arr[d]);
////	    					System.out.println("From id=" + s + " to id=" + d + " is " + arr[d] + " mins.");
////	    				}
//	    			}
	    			s++;
	    		}
	    	} catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (br != null) {
	            try {
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
        return edgeColors;
	}
	
	public int[][] getTrainEdges() {
		return trainEdges;
	}
	
	public int[][] getWalkingEdges() {
		return walkingEdges;
	}
	
	public Line[][] getEdgeColors() {
		return edgeColors;
	}
	
	public Map<Integer, String> getStationNames() {
		return stationNames;
	}
	
	public int getN() {
		return N;
	}
	
	public static void main(String[] args) {
		String trainEdgesFilename = "TrainEdgesCSV.csv";
		String walkingEdgesFilename = "WalkingEdgesCSV.csv";
		String edgeColorsFilename = "EdgeColorsCSV.csv";
		String stationNamesFilename = "StationNamesCSV.csv";
		CSVParser parser = new CSVParser(trainEdgesFilename, walkingEdgesFilename, edgeColorsFilename,
				stationNamesFilename);
		parser.parse();
		Map<Integer, String> stationNames = parser.getStationNames();
		int[][] trainEdges = parser.getTrainEdges();
		int[][] walkingEdges = parser.getWalkingEdges();
		Line[][] edgeColors = parser.getEdgeColors();
		
		
		for (int i = 0; i < stationNames.size(); i++) {
			for (int j = 0; j < stationNames.size(); j++) {
				if (trainEdges[i][j] != 0) {
					System.out.println("Train from " + stationNames.get(i) + 
							" to " + stationNames.get(j) + " is " +
							trainEdges[i][j] + " mins on the " + edgeColors[i][j].toString()
							+ " line.");
				}
				if (walkingEdges[i][j] != 0) {
					System.out.println("Walking from " + stationNames.get(i) + 
							" to " + stationNames.get(j) + " is " +
							walkingEdges[i][j] + " mins.");
				}
			}
		}
		
		for (int i = 0; i < stationNames.size(); i++) {
			for (int j = 0; j < stationNames.size(); j++) {
				if (trainEdges[i][j] != 0) {
					System.out.println(i + "," + j + ",WALK");
				}
			}
		}
		
    }
}
