package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import model.Graph;
import model.Station;

public class BroadFristSearchAlgorithm  implements Algorithm {
    // 保存已经访问过的地点
    private List<Station> visitedVertex;
    // 保存最短路径
    private Map<Station, Station> path;


    public void perform(Graph g, Station sourceVertex) {
        if (null == visitedVertex) {
            visitedVertex = new ArrayList<>();
        }
        if (null == path) {
            path = new HashMap<>();
        }

        BFS(g, sourceVertex);
    }


    public Map<Station, Station> getPath() {
        return path;
    }

    private void BFS(Graph g, Station sourceVertex) {
        Queue<Station> queue = new LinkedList<>();
        // 标记起点
        visitedVertex.add(sourceVertex);
        // 起点入列
        queue.add(sourceVertex);

        while (false == queue.isEmpty()) {
            Station ver = queue.poll();

            List<Station> toBeVisitedVertex = g.getVer_edgeList_map().get(ver);
            for (Station v : toBeVisitedVertex) {
                if (false == visitedVertex.contains(v)) {
                    visitedVertex.add(v);
                    path.put(v, ver);
                    queue.add(v);
                }
            }
        }
    }
}

