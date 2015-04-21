package org.maxsoft.update;

import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
//<update>
//<version>2</version>
//<name>baidu_xinwen_1.1.0</name>
//<url>http://gdown.baidu.com/data/wisegame/f98d235e39e29031/baiduxinwen.apk</url>
//</update>
public class ParseXmlService {
	
	public HashMap<String, String> parseXml(InputStream inputStream)throws Exception{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		//实例化文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//通过文档构建器工厂构建文档构建器
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		//通过文档构建器构建文档实例
		Document document = documentBuilder.parse(inputStream);
		//通过文档构建器获取文档根节点
		Element element = document.getDocumentElement();
		//通过文档根节点获取文档所有节点
		NodeList childNodes = element.getChildNodes();
		int length = childNodes.getLength();
		for(int i=0; i<length; i++){
			//开始遍历节点
			Node node = childNodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element childElement = (Element)node;
				//版本
				if("version".equals(childElement.getNodeName())){
					hashMap.put("version", childElement.getFirstChild().getNodeValue());
				}
				//软件名称 
				else if("name".equals(childElement.getNodeName())){
					hashMap.put("name", childElement.getFirstChild().getNodeValue());
				}
				//下载地址
				else if("url".equals(childElement.getNodeName())){
					hashMap.put("url", childElement.getFirstChild().getNodeValue());
				}
			}
		}
		return hashMap;
	}
	
	//获取本地包的版本代码
	public int getVersionCode(Context context,String packagename){
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(packagename,0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
