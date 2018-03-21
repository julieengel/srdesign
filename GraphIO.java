import java.io.*;
import java.util.*;

public class GraphIO {


    /*
     * Output file format:
     *
     * 1.5,13   // this is the c-value,additive-value that was used to generate all the spanners
     *
     * 4        // this is the number of u,v pairs in the coming maintenence set (they will be listed right below)
     * 1,3
     * 3,5
     * 2,5
     * 4,6
     *
     * 2        // once you have read through one whole set, there will be \n then a new count for the next set
     * 3,2
     * 4,1
     *
     *
     */
    public static void outputMaintenanceSets(Set<Set<MultiEdge>> sets) {
        if (sets == null) { return; }

        try {
            BufferedWriter outputStream = new BufferedWriter(new FileWriter("allMaintenanceSets.txt"));

            outputStream.write("" + 1.5 + "," + 13);
            outputStream.newLine();
            outputStream.newLine();

            for (Set<MultiEdge> set : sets) {

                outputStream.write("" + set.size());
                outputStream.newLine();

                for (MultiEdge e : set) {
                    outputStream.write("(" + e.getU() + "," + e.getV() + ")");
                    outputStream.newLine();
                }

                outputStream.newLine();
            }

            outputStream.close();

        } catch (IOException e) {
            System.out.println("THERE WAS AN IO EXCEPTION!");
        }

    }

    public static void outputIDToNames(MultiNode[] nodes) {
        if (nodes == null) { return; }

        try {
            BufferedWriter outputStream = new BufferedWriter(new FileWriter("nodeIDsToName.txt"));

            for (int i = 0; i < nodes.length; i++) {
                outputStream.write("" + i + "," + "name"); // TODO: replace with name when combined with Julies code
                outputStream.newLine();
            }

            outputStream.close();

        } catch (IOException e) {
            System.out.println("THERE WAS AN IO EXCEPTION!");
        }

    }

    public static void main(String[] args)  {
        //outputMaintenanceSets(null);
    }

}
