import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CSVParser {
	public static void main(String[] args) {
		
		//String edgeFilename = "TrainTimesCSV.csv";
		String edgeFilename = "WalkingEdgesCSV.csv";
		parseEdges(edgeFilename, null);
		
		String stationNamesFilename = "IDsCSV.csv";
		parseStationNames(stationNamesFilename);
		
    }
	
	public static void parseEdges(String filename, int[][] am) {
		String currentRelativePath = Paths.get("").toAbsolutePath().toString() + "/src/";
        String csvFile = currentRelativePath + filename;
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        try {
        		br = new BufferedReader(new FileReader(csvFile));
        		int s = 0;
        		while ((line = br.readLine()) != null) {
        			if (s == 0) {
        				s++;
        				continue;
        			}
        			String[] arr = line.split(csvSplitBy);
        			for (int d = 1; d < arr.length; d++) {
        				if (!arr[d].equals("")) {
        					// Replace with real parsing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        					if (am != null) {
        						am[s][d] = Integer.parseInt(arr[d]);
        					}
        					System.out.println("From id=" + s + " to id=" + d + " is " + arr[d] + " mins.");
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
	}
	
	public static Map<Integer, String> parseStationNames(String filename) {
		String currentRelativePath = Paths.get("").toAbsolutePath().toString() + "/src/";
        String csvFile = currentRelativePath + filename;
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        Map<Integer, String> stationNames = new HashMap<Integer, String>();
        boolean first = true;
        try {
        		br = new BufferedReader(new FileReader(csvFile));
        		while ((line = br.readLine()) != null) {
        			if (first) {
        				first = !first;
        				continue;
        			}
        			String[] arr = line.split(csvSplitBy);
        			stationNames.put(Integer.parseInt(arr[0]), arr[1]);
        			System.out.println(stationNames.get(Integer.parseInt(arr[0])));
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
}
