/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criticalpath;

import com.sun.javafx.collections.MappingChange.Map;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

class Graph { 
  
    // We use Hashmap to store the edges in the graph 
    private HashMap<CustomCircle, LinkedList<CustomCircle> > map = new HashMap<>(); 
  
    // This function adds a new vertex to the graph 
    public void addVertex(CustomCircle s) 
    { 
        map.put(s, new LinkedList<>()); 
    } 
  
    // This function adds the edge 
    // between source to destination 
    public void addEdge(CustomCircle source, 
                        CustomCircle destination, 
                        boolean bidirectional) 
    { 
  
        if (!map.containsKey(source)) 
            addVertex(source); 
  
        if (!map.containsKey(destination)) 
            addVertex(destination); 
  
        map.get(source).add(destination); 
        if (bidirectional == true) { 
            map.get(destination).add(source); 
        } 
    } 
  
    // This function gives the count of vertices 
    public int getVertexCount() 
    { 
        return map.keySet().size(); 
                           
    } 
  
    // This function gives the count of edges 
    public void getEdgesCount(boolean bidirection) 
    { 
        int count = 0; 
        for (CustomCircle v : map.keySet()) { 
            count += map.get(v).size(); 
        } 
        if (bidirection == true) { 
            count = count / 2; 
        } 
        System.out.println("The graph has "
                           + count 
                           + " edges."); 
    } 
  
    // This function gives whether 
    // a vertex is present or not. 
    public void hasVertex(CustomCircle s) 
    { 
        if (map.containsKey(s)) { 
            System.out.println("The graph contains "
                               + s + " as a vertex."); 
        } 
        else { 
            System.out.println("The graph does not contain "
                               + s + " as a vertex."); 
        } 
    } 
  
    // This function gives whether an edge is present or not. 
    public void hasEdge(CustomCircle s, CustomCircle d) 
    { 
        if (map.get(s).contains(d)) { 
            System.out.println("The graph has an edge between "
                               + s + " and " + d + "."); 
        } 
        else { 
            System.out.println("The graph has no edge between "
                               + s + " and " + d + "."); 
        } 
    } 
  
    // Prints the adjancency list of each vertex. 
    @Override
    public String toString() 
    { 
        StringBuilder builder = new StringBuilder(); 
  
        for (CustomCircle v : map.keySet()) { 
            builder.append(v.getName().getText() + ": "); 
            for (CustomCircle w : map.get(v)) { 
                builder.append(w.getName().getText() + " "); 
            } 
            builder.append("\n"); 
        } 
  
        return (builder.toString()); 
    } 
} 
  