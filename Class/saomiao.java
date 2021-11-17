//package map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class saomiao {
	static String ip="";
	public static void exeCmd(String str) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(str);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
	 public static String readFile(String pathname) {
	        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
	        //不关闭文件会导致资源的泄露，读写文件都同理
	        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
	        try (FileReader reader = new FileReader(pathname);
	             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
	        ) {
	            String line;
	    
	            while ((line = br.readLine()) != null) {
	                // 一次读入一行数据

	         	   if(line.indexOf("IPv4 地址")>-1) {
	         	    ip+=line.substring(line.indexOf("IPv4 地址")+33,line.length()-5);
	         	    break;
	         	   }
	         	  }
	         	  if(!"".equals(ip)) {
	         	   System.out.println(ip);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return ip;
	    }
	 
	 public static void saomiao(String pathname){
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 try{

			 DocumentBuilder db = dbf.newDocumentBuilder();
			 Document document = db.parse(new File(pathname));
			 NodeList booklist = document.getElementsByTagName("port");
			 NodeList booklist1 = document.getElementsByTagName("state");
			 NodeList booklist2 = document.getElementsByTagName("service");
			 for(int i = 0; i <booklist.getLength(); i++){
				 Element ele = (Element) booklist.item(i);
				 Element ele1 = (Element) booklist1.item(i);
				 Element ele2 = (Element) booklist2.item(i);
				 System.out.print("protocol:"+ele.getAttribute("protocol")+"\t"+"portid:"+ele.getAttribute("portid")+"\t");
				 System.out.print("state:"+ele1.getAttribute("state")+"\t");
				 System.out.println("service:"+ele2.getAttribute("name"));
			 }
		 }catch (ParserConfigurationException e){
			 e.printStackTrace();
		 }catch (IOException e){
			 e.printStackTrace();
		 }catch (SAXException e){
			 e.printStackTrace();
		 }
	 }

	 public static void main(String[] args) throws InterruptedException, IOException{
		 exeCmd("cmd.exe /c ipconfig");
		 System.out.print("请输入目标地址（格式为X:/XXX/XXX/）");
		 Scanner input = new Scanner(System.in);
		 String location = input.next();
		 exeCmd("cmd.exe /c ipconfig/all>"+location+"ip.txt");
		 readFile(location+"ip.txt");
		 exeCmd("cmd.exe /c start nmap "+ ip + " -oX "+location+"result.xml");
		 saomiao(location+"result.xml");
	 }
}


