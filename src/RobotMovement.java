import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.text.Position;

public class RobotMovement {
    Graph graph;

    public RobotMovement(Graph graph) {
        this.graph = graph;
    }

    public boolean doNext() {
        //clearGraph(terretorium.getChild());
        if (nextStep()!=null) {
            runStep();
            return true;
        }
        return false;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void clearGraph(Position position) {

    }

    public Enum nextStep() {
        return null;
    }
    public void runStep(){
        //do nextStep
    }
}
