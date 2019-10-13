package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import model.Graph;
import model.Station;
public abstract class Main {
	public static String readJsonData(String pactFile){
		StringBuffer strbuffer = new StringBuffer();
		File myFile = new File(pactFile);
		if (!myFile.exists()) {
			System.err.println("Can't Find " + pactFile);
			return "-1";
		}
		try {
			FileInputStream fis = new FileInputStream(pactFile);
			InputStreamReader inputStreamReader = new InputStreamReader(fis, "GBK");
			BufferedReader in  = new BufferedReader(inputStreamReader);
			
			String str;
			while ((str = in.readLine()) != null) {
				strbuffer.append(str);  //new String(str,"UTF-8")
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		return strbuffer.toString();
	}
	
	public static List<String> getLine(String filepath,String line){
		List<String> result=new  ArrayList<String>();
		List<String> list = new ArrayList<String>();
    	
    	String jsonString = readJsonData(filepath);
    	if(jsonString.equals("-1"))return null;
		JSONArray lineArr = JSON.parseArray(jsonString);
		System.out.print("地铁信息加载成功：");
		for(int i = 0; i < lineArr.size(); i++) {
			JSONObject jsonObj = lineArr.getJSONObject(i);
			
			JSONArray stationArr =  jsonObj.getJSONArray("Station");
			list.add(jsonObj.getString("Line"));
			for(int j = 0; j < stationArr.size(); j++) {
				String tmpSta = stationArr.getString(j); 
				list.add(tmpSta);
			}
		}
		int flage=0;

    	for(int i=0;i<list.size();i++) {
    		if(list.get(i).contains("线")) {
    			if(flage==1)flage=0;
    			if(list.get(i).equals(line))
    				flage=1;
    		}else {
    			if(flage==1)
    				result.add(list.get(i));
    		}
    	}
    	return result;
	}
	
	public static Graph init(String filepath, String start) {
		 Graph g = new Graph(new BroadFristSearchAlgorithm());
			
			List<String> list = new ArrayList<String>();
	    	Set<String> set = new LinkedHashSet<String>();
	    	
	    	String jsonString = readJsonData(filepath);
	    	if(jsonString.equals("-1"))return null;
			JSONArray lineArr = JSON.parseArray(jsonString);
			System.out.print("地铁信息加载成功：");
			for(int i = 0; i < lineArr.size(); i++) {
				JSONObject jsonObj = lineArr.getJSONObject(i);
				
				JSONArray stationArr =  jsonObj.getJSONArray("Station");
				list.add(jsonObj.getString("Line"));
				set.add(jsonObj.getString("Line"));
				for(int j = 0; j < stationArr.size(); j++) {
					String tmpSta = stationArr.getString(j); 
					list.add(tmpSta);
				}
			}
	    	Set<String> set1 = new LinkedHashSet<String>(list);
	    	Set<String> set2 = new LinkedHashSet<String>();
	    	for(int i=0;i<list.size();i++) {
	    		if(list.get(i).contains("线")) {
	    			String s1 = list.get(i);
	    			set1.remove(s1);
	    		}else if( (i+1)==list.size() || list.get(i+1).contains("线")) {
	    			continue;
	    		}else {
	    			String s2 = list.get(i)+","+list.get(i+1);
	    			String s3 = list.get(i+1)+","+list.get(i);
	    			if(set2.contains(s3)) continue;
	    			else set2.add(s2);
	    		}
	    	}
	  
	    	//存储地铁站信息
			List<Station> verList = new LinkedList<Station>();
			String line=null;
	    	for(String s: list){
	    		if(s.contains("线")) {
	    			line=s;
	    		}else {
					Station station=new Station(s);
					station.Line=line;
					g.addVertex(station);
					verList.add(station);
	    		}
			}
			for(String edge:set2) {
	            String[] strArr = edge.split(",");
	            Station station1= Station.getStation(verList, strArr[0]);
	            Station station2= Station.getStation(verList, strArr[1]);
	            g.addEdge(station1, station2);
			}
			
			 g.setStart(Station.getStation(verList,start));
			 g.setStationList(verList);
		     g.done();
		     return g;
	}
	
	public static boolean save(String filePath,String input) {
		try {
			File f=new File(filePath);
			if (!f.exists()) {
				f.createNewFile();
			}
	    	FileOutputStream fos1=new FileOutputStream(f);
	    	OutputStreamWriter dos1=new OutputStreamWriter(fos1);
	    	
			dos1.write(input);
			dos1.write("\n");
			dos1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
    	
    	
	}
	
	public static String find(String path,String start,String end) {
		 Graph g=init(path,start);
		 if(Station.getStation(g.getStationList(),end)==null||Station.getStation(g.getStationList(),start)==null) {
			 return "站点不存在";
		 }
		 String resultstring = "";
	     Stack<Station> result = g.findPathTo(Station.getStation(g.getStationList(),end));
	     resultstring+="BFS: From ["+Station.getStation(g.getStationList(),start).getName()+"] to ["+Station.getStation(g.getStationList(),end).getName()+"]:";
	     resultstring+=Station.getStation(g.getStationList(),start).Line+"\n";
	        while (!result.isEmpty()) {
	        	Station sta=result.pop();
	        	if(result.size()>=2&&!result.peek().Line.equals(sta.Line)) {
	        		resultstring+=(sta.getName()+"\n"+result.peek().Line+"\n");
	        	}
	        	else
	        		resultstring+=sta.getName()+"\n";
	        }

	       return resultstring;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filepath="D://Java//work//UndergroundManager//src//data//";
		String outputpath="D://Java//work//UndergroundManager//src//output//";
		if(args.length==1&&args[0].equals("-help")) {
			System.out.println("用法 java subway [-options]");
            System.out.println("\t\t(地铁路线规划程序)");
            System.out.println("其中选项包括：");
            System.out.println("\t-map <地铁信息文件>\n\t\t\t指定地铁信息文件（数据请发在data目录下,每个都有，跟在-a|-b后面或者独自使用）");
            System.out.println("\t-o <输出的文本文件>\t指定输出文件（若不指定则输出控制台）");
            System.out.println("\t-a <指定地铁线路>\t指定地铁线路输出内容");
            System.out.println("\t-b <起点站> <目的站>\t指定起点站与终点站，计算最短路线");
            System.out.println("\t\t(-a，-b 仅能选择其一，若两者都无，则输出所有线路地铁)");
            System.out.println("\t-help\t\t查看帮助信息(不与其他一起使用)");
            return ;
		}else if(args.length==2&&args[0].equals("-map")) {
			filepath+=args[1];
			Graph g=init(filepath,"");
			int count=0;
			if(g!=null) {
				for(Entry<Station, List<Station>> sta:g.getVer_edgeList_map().entrySet()) {
					count++;
					System.out.println((sta.getKey()).getName()+"与之相连的站点:");
					for(Station stas:sta.getValue()) {
						System.out.print(stas.getName()+" ");
					}
					System.out.println("\n------------");
				}
				System.out.print("一共"+count+"个站点");
			}
		}else if(args.length==4&&args[0].equals("-a")&&args[2].equals("-map")) {
			filepath+=args[3];
			List<String> result=getLine(filepath,args[1]);
			if(result==null) {
				System.err.println("Can't Find " + filepath);
				return;
			}else if(result.size()==0) {
				System.out.println("输入的线路有误");
				return;
			}
			for(String s:result)
				System.out.print(s+" ");
			System.out.println();
		}
		else if(args.length==5&&args[0].equals("-b")&&args[3].equals("-map")) {
			filepath+=args[4];
			if(args[1].equals(args[2])) {
				System.out.print("开始站点和结束站点相同");
				return;
			}
			String resultstring=find(filepath,args[1],args[2]);
			System.out.println(resultstring);
		}else if(args.length==6&&args[0].equals("-a")&&args[2].equals("-map")&&args[4].equals("-o")){
			filepath+=args[3];
			outputpath+=args[5];
			String resultstring="";
			List<String> result=getLine(filepath,args[1]);
			if(result==null) {
				System.err.println("Can't Find " + filepath);
				return;
			}else if(result.size()==0) {
				System.out.println("输入的线路有误");
				return;
			}
			for(String s:result)
				resultstring=resultstring+(s+" ");
			save(outputpath,resultstring);
			System.out.println("写入成功\n");
		}else if(args.length==7&&args[0].equals("-b")&&args[3].equals("-map")&&args[5].equals("-o")){
			filepath+=args[4];
			outputpath+=args[6];
			String resultstring=find(filepath,args[1],args[2]);
			save(outputpath,resultstring);
			System.out.print("存入成功");
		}else {
			System.out.println("参数错误，用法参考--help");
			return;
		}

	}

}
