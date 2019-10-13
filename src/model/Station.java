package model;

import java.util.List;

public class Station {
	private final static int infinite_dis = Integer.MAX_VALUE;
	
	private String name;  //地铁站名字
	private boolean known; //此节点之前是否已知
	private int adjuDist; //此节点距离
	private Station parent; //当前从初始节点到此节点的最短路径下的父节点。
	public String Line;
	
	public Station()
	{
		this.known = false;
		this.adjuDist = infinite_dis;
		this.parent = null;
	}
	
	public Station(String name)
	{
		this.known = false;
		this.adjuDist = infinite_dis;
		this.parent = null;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isKnown() {
		return known;
	}
	public void setKnown(boolean known) {
		this.known = known;
	}
	public int getAdjuDist() {
		return adjuDist;
	}
	public void setAdjuDist(int adjuDist) {
		this.adjuDist = adjuDist;
	}
	
	public Station getParent() {
		return parent;
	}

	public void setParent(Station parent) {
		this.parent = parent;
	}
	
	public static Station getStation(List<Station> stationList,String stationname) {
		for(Station sta:stationList) {
			if(sta.name.equals(stationname))
				return sta;
		}
		return null;
	}
	
}
