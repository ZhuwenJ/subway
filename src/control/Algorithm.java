package control;

import java.util.Map;

import model.Graph;
import model.Station;

public interface Algorithm {
    /**
     * 执行算法
     */
    void perform(Graph g, Station firstVertax);

    /**
     * 得到路径
     */
    Map<Station, Station> getPath();
}

