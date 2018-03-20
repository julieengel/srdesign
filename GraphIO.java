import java.io.*;
import java.util.*;

public class GraphIO {

    public static void outputMaintenanceSets(Set<Set<MultiEdge>> sets) {
        if (sets == null) { return; }

        try {
            BufferedWriter outputStream = new BufferedWriter(new FileWriter("allMaintenanceSets.txt"));

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
