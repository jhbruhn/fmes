package modell;


import graph.Graph;

public class Battery {
    ZielFeld battery;
    Graph enforcedGraph;
    int longestEnergyPath;

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

    public void setLongestEnergyPath(int longestEnergyPath) {
        this.longestEnergyPath = longestEnergyPath;
    }

    public int getLongestEnergyPath() {
        return this.longestEnergyPath;
    }
}
