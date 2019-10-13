
一、Github代码链接
	[https://github.com/1060183116/subway](https://github.com/1060183116/subway "repo")

二、程序基本功能描述
　　

　　1.获得北京地铁单条线路所有站点信息

	java SubwayStart  -a 线路名称 -map 地铁线路文件.txt-o 输出文件.txt
	或者
	java SubwayStart -a 线路名称(直接显示)-map 地铁线路文件.txt 

　　2.获得两个站点间最短线路

	java SubwayStart  -b 起始站点 目的站点 -map 地铁线路文件.txt -o 输出文件.txt
 	或者
	java SubwayStart  -b 起始站点 目的站点 -map 地铁线路文件.txt (直接显示)
	

　　3.获得图的基本信息
	
		java SubwayStart -map 地铁线路文件.txt

　　4.帮助，命令行详解
	
		java SubwayStart -help


三、北京地铁线路存储格式
　　

	线路以文本文件形式存放，具体格式如下：

	{
		'Line' : '线路名称',
		'Station' : ['站点1', '站点2', '站点3, ......]
	},......

　　其中环形线路，头站点在尾部再加上首站点即可。
	
对于读取通过json格式读取，添加了一个jar包，在项目的lib下：名字为fastjson-1.2.53.jar

　　
四、代码设计：

   1.类的设计

　　总共有6各类:

　　　　Station站点类，Graph图类(图的相关操作),Algorithm算法类（是个接口，表示算法类应该实现的一些方法),

　　　　BroadFristSearchAlgorithm算法类（BFS具体实现），Main类（json读取，数据处理等操作），subwayStart类（程序入口）


　　Station类结构主要成员


public class Station {
	private final static int infinite_dis = Integer.MAX_VALUE;
	
	private String name;  //地铁站名字
	private boolean known; //此节点之前是否已知
	private int adjuDist; //此节点距离
	private Station parent; //当前从初始节点到此节点的最短路径下的父节点。
	public String Line;//地铁站归属线路
	
	
}

 

 

　　Graph类结构(因为站点间距离都相同，就可以少写一个Edge类)

	public class Graph{
		
		private List<Station> StationList;   //图的顶点集
		private Map<Station, List<Station>> ver_edgeList_map= new HashMap<>();  //图的每个顶点对应的有向边
		private Station firstVertax;//起始点
		private Algorithm algorithm;//对应的算法
		
		
	}
　　
 

核心算法：BFS算法

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
	
换乘判断

　　返回一个最短路径上的所有站点的栈，只需判断弹出来的Station的Line与弹出来后当前栈顶Station的Line是否一样，不一样就表示下一个站换线了。
　　具体代码：

	while (!result.isEmpty()) {
	        	Station sta=result.pop();
	        	if(result.size()>=2&&!result.peek().Line.equals(sta.Line)) {
	        		resultstring+=(sta.getName()+"\n"+result.peek().Line+"\n");
	        	}
	        	else
	        		resultstring+=sta.getName()+"\n";
	        }
 
五、异常处理:
　　

　　1.地铁线路图数据txt不存在，处理方式：输出cant find xxx.txt并结束程序

　　2.起点站或终点站不存在，处理方式：程序输出“起点站或终点站不存在”并结束程序

　　3.起点终点相同，处理方式：程序输出“起点终点不能相同”并结束程序

　　4.输入参数格式错误，处理方式：程序输出“命令格式有误”并结束程序



六、个人总结
　　本次个人作业，花费时间大约三天，中间出了各种稀奇古怪的错误，其实归根到底还是对于自己写的代码，太过杂乱,自己都里不清楚，模块化有所欠缺，类缺少内聚，类的独立性还不够好，希望在小组作业中能够多多改善，避免诸如此类的错误。
