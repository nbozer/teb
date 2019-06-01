<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>

<%
String dataPointsIstanbul = "";
String dataPointsTokyo = "";
String dataPointsBeijing = "";
String dataPointsLondon = "";
String dataPointsMoskow = "";

Gson gsonObj = new Gson();
Map<Object, Object> map = null;
List<Map<Object, Object>> listIstanbul = new ArrayList<Map<Object, Object>>();
List<Map<Object, Object>> listTokyo = new ArrayList<Map<Object, Object>>();

List<Map<Object, Object>> listBeijing = new ArrayList<Map<Object, Object>>();
List<Map<Object, Object>> listLondon = new ArrayList<Map<Object, Object>>();
List<Map<Object, Object>> listMoskow = new ArrayList<Map<Object, Object>>();

String jspPath = "C:/eclipseprojeler/jspLog.txt";
BufferedReader reader = new BufferedReader(new FileReader(jspPath));
StringBuilder sb = new StringBuilder();
String line;

//line = reader.readLine();
out.println(sb.toString());
//System.out.println("line" +line);

//while(true){
int time = 0;
while ((line = reader.readLine()) != null) {//cityName:Tokyo,logCount:1,systemTime:1559389691438
out.println(sb.toString());
//map = new HashMap<Object,Object>(); map.put("x", 0); map.put("y", 1.293E-03); list.add(map);
//	String dataPoints1 = gsonObj.toJson(list);
String[] logSplitted = line.split(",");
String[] citySplited = logSplitted[0].split(":");
String[] logCountSplited = logSplitted[1].split(":");
String[] systemTimeSplited = logSplitted[2].split(":");

try {

map = new HashMap<Object, Object>();
// int time = Integer.valueOf( systemTimeSplited[1].charAt(9) + systemTimeSplited[1].charAt(10) +systemTimeSplited[1].charAt(11)) / 5;
time++;
//char[] time 
map.put("x", time / 5);
map.put("y", logCountSplited[1]);
//list.add(map);

if (citySplited[1].equals("Istanbul")) {
listIstanbul.add(map);
dataPointsIstanbul = gsonObj.toJson(listIstanbul);
} else if (citySplited[1].equals("Tokyo")) {
listTokyo.add(map);
dataPointsTokyo = gsonObj.toJson(listTokyo);
} else if (citySplited[1].equals("Beijing")) {
listBeijing.add(map);
dataPointsBeijing = gsonObj.toJson(dataPointsBeijing);
} else if (citySplited[1].equals("London")) {
listLondon.add(map);
dataPointsLondon = gsonObj.toJson(listLondon);
} else if (citySplited[1].equals("Moskow")) {
listMoskow.add(map);
dataPointsMoskow = gsonObj.toJson(listMoskow);
}
} catch (Exception e) {
//out.println(e.getMessage());
}
time++;

}
time++;
%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
window.onload = function() {

var chart = new CanvasJS.Chart("chartContainer", {
animationEnabled : true,
zoomEnabled : true,
theme : "light2",
title : {
text : "CityLogs"
},
axisX : {
title : "Time",
valueFormatString : "# sec"
},
axisY : {
logarithmic : true, //change it to false
title : "Log Count",
titleFontColor : "#6D78AD",
lineColor : "#6D78AD",
gridThickness : 0,
lineThickness : 1,
valueFormatString : "#",
includeZero : false
},
axisY2 : {
title : "Log Count",
titleFontColor : "#51CDA0",
logarithmic : false, //change it to true
lineColor : "#51CDA0",
gridThickness : 0,
lineThickness : 1
},
toolTip : {
shared : true
},
legend : {
verticalAlign : "top",
dockInsidePlotArea : true
},
data : [ {
type : "line",
yValueFormatString : "#",
xValueFormatString : "#",
showInLegend : true,
name : "Log Count",
legendText : "{name} (in Log Scale)",
dataPoints :
<%out.print(dataPointsTokyo);%>
} ]
});
chart.render();

}
</script>
</head>
<body>
<div id="chartContainer" style="height: 370px; width: 100%;"></div>
<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
</body>
</html>
