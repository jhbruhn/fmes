package modell;


import graph.Graph;

public class Battery
{
    ZielFeld battery;
    Graph enforcedGraph;

    public Battery(ZielFeld battery, Graph enforcedGraph) {
        this.battery = battery;
        this.enforcedGraph = enforcedGraph;
    }
    public ZielFeld getBattery() {
        return battery;
    }

    public void setBattery(ZielFeld battery) {
        this.battery = battery;
    }

    public Graph getEnforcedGraph() {
        return enforcedGraph;
    }

    public void setEnforcedGraph(Graph enforcedGraph) {
        this.enforcedGraph = enforcedGraph;
    }
}
