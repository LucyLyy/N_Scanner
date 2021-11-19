package map;
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
	        //��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw;
	        //���ر��ļ��ᵼ����Դ��й¶����д�ļ���ͬ��
	        //Java7��try-with-resources�������Źر��ļ����쳣ʱ�Զ��ر��ļ�����ϸ���https://stackoverflow.com/a/12665271
	        try (FileReader reader = new FileReader(pathname);
	             BufferedReader br = new BufferedReader(reader) // ����һ�����������ļ�����ת�ɼ�����ܶ���������
	        ) {
	            String line;
	            //�����Ƽ����Ӽ���д��
	            while ((line = br.readLine()) != null) {
	                // һ�ζ���һ������

	         	   if(line.indexOf("IPv4 ��ַ")>-1) {
	         	    ip+=line.substring(line.indexOf("IPv4 ��ַ")+33,line.length()-5);
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
		 System.out.print("������Ŀ���ַ����ʽΪX:/XXX/XXX/��");
		 Scanner input = new Scanner(System.in);
		 String location = input.next();
		 exeCmd("cmd.exe /c ipconfig/all>"+location+"ip.txt");
		 readFile(location+"ip.txt");
		 exeCmd("cmd.exe /c start nmap "+ ip + " -oX "+location+"result.xml");
		 saomiao(location+"result.xml");
	 }
}


