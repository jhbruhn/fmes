package modell;


import graph.Graph;
import graph.Vector2;

public class Battery {
    public final Vector2 position;
    ZielFeld battery;
    Graph enforcedGraph;
    int longestEnergyPath;

    public Battery(ZielFeld battery, Graph enforcedGraph, Vector2 vector2) {
        this.battery = battery;
        this.enforcedGraph = enforcedGraph;
        this.position = vector2;

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
