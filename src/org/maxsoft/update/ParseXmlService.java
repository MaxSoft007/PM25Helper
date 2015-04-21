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
		//ʵ�����ĵ�����������
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//ͨ���ĵ����������������ĵ�������
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		//ͨ���ĵ������������ĵ�ʵ��
		Document document = documentBuilder.parse(inputStream);
		//ͨ���ĵ���������ȡ�ĵ����ڵ�
		Element element = document.getDocumentElement();
		//ͨ���ĵ����ڵ��ȡ�ĵ����нڵ�
		NodeList childNodes = element.getChildNodes();
		int length = childNodes.getLength();
		for(int i=0; i<length; i++){
			//��ʼ�����ڵ�
			Node node = childNodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element childElement = (Element)node;
				//�汾
				if("version".equals(childElement.getNodeName())){
					hashMap.put("version", childElement.getFirstChild().getNodeValue());
				}
				//������� 
				else if("name".equals(childElement.getNodeName())){
					hashMap.put("name", childElement.getFirstChild().getNodeValue());
				}
				//���ص�ַ
				else if("url".equals(childElement.getNodeName())){
					hashMap.put("url", childElement.getFirstChild().getNodeValue());
				}
			}
		}
		return hashMap;
	}
	
	//��ȡ���ذ��İ汾����
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
