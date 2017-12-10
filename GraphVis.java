import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.*;

public class GraphVis {
    org.graphstream.graph.Graph graph;

    public GraphVis(int[][] am) {
        graph = new SingleGraph("g1");

        for (int i = 0; i < am.length; i++) {
            String str = "" + i;
            graph.addNode(str);
            graph.getNode(str).addAttribute("ui.label", str);
        }

        for (int i = 0; i < am.length; i++) {
            for (int j = i; j < am[0].length; j++) {
                if (am[i][j] > 0) {
                    String strI = "" + i;
                    String strJ = "" + j;
                    graph.addEdge(strI + strJ, strI, strJ);
                    graph.getEdge(strI + strJ).addAttribute("ui.label", am[i][j]);
                }
            }
        }
    }

    public void display() {
        graph.display();
    }

    public static void main(String args[]) {
        // DONT DELETE BELOW YET, NEED TO FIGURE OUT LABELING

//        org.graphstream.graph.Graph graph = new SingleGraph("Tutorial 1");
//
//
//        SpriteManager sman = new SpriteManager(graph);
//        Sprite s = sman.addSprite("S1");
//
//        graph.addNode("A");
//        graph.addNode("B");
//        graph.addNode("C");
//        graph.addEdge("AB", "A", "B");
//        graph.addEdge("BC", "B", "C");
//        graph.addEdge("CA", "C", "A");
//
////        s.clearAttributes();
////        s.attachToEdge("AB");
//        s.attachToNode("A");
////        s.setPosition(0.05);
//        s.setPosition(StyleConstants.Units.PX, 0, 0, 0);
//        s.setAttribute("ui.label", "Happy");
//
//        graph.display();

    }

}

