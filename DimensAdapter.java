import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.security.auth.login.AppConfigurationEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class DimensAdapter {

	private static final String UTF_8 = "UTF-8";
	static float scale = 1.25F;
	static String dir = System.getProperty("user.dir");
	private static StringBuilder xh;
	private static StringBuilder xxh;
	private static StringBuilder mdpi;
	static Map<String, Float> adapterMap;
	private static Map<String, StringBuilder> sbMap;
	
	public static void main(String[] args) throws Throwable {
		
		FileInputStream inputStream = new FileInputStream(dir + "/values/dimens.xml");
		
		make(inputStream);
	}

	private static void make(FileInputStream inputStream)
			throws XmlPullParserException, Throwable, IOException,
			FileNotFoundException {
		XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();

		parser.setInput(inputStream,UTF_8);
		
		int eventType = parser.getEventType();
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			
			switch (eventType) {
			case XmlPullParser.START_TAG:
				
				if(!parser.getName().equals("resources")){
					write(parser);
				}
				break ;
				
			default:
				break;
			}
			
			eventType = parser.next();
		}
		
		for (Entry<String, StringBuilder> entry : sbMap.entrySet()) {
			
			String key = entry.getKey();
			StringBuilder value = entry.getValue();
			value.append("</resources>");
			
			File file = new File(dir + key + "/dimens.xml");
			file.getParentFile().mkdirs();
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(value.toString().getBytes());
			stream.close();
			
			System.out.println(key + "完成");
		}
	}

	static {
		
		adapterMap = new HashMap<String, Float>();
		adapterMap.put("/values-hdpi", (float) 1);
		adapterMap.put("/values-xhdpi", scale);
		adapterMap.put("/values-xxhdpi", scale * scale);
		adapterMap.put("/values-w270dp", 270f/240f);
		adapterMap.put("/values-w300dp", 300f/240f);
		adapterMap.put("/values-w330dp", 330f/240f);
		adapterMap.put("/values-w360dp", 360f/240f);
		adapterMap.put("/values-w390dp", 390f/240f);
		adapterMap.put("/values-w420dp", 420f/240f);
		adapterMap.put("/values-w450dp", 450f/240f);
		adapterMap.put("/values-w480dp", 480f/240f);
		
		sbMap = new HashMap<String, StringBuilder>();
		
		for (String key : adapterMap.keySet()) {
			StringBuilder value = new StringBuilder();
			value.append("<resources>\n");
			sbMap.put(key, value);
		}
	}

	private static void write(XmlPullParser parser) throws Throwable {
		
		String start = parser.getText();
		String text = parser.nextText();
		
		if(start.contains("text_size")){
			
			return;
		}
		
		if(text.endsWith("sp") || text.endsWith("dp") || text.endsWith("px")){
			// 标签结束文本
			String end = parser.getText();
			
			double num = Double.valueOf(text.substring(0, text.length() - 2));
			String type = text.substring(text.length() - 2);
			
			for (Map.Entry<String, Float> entry : adapterMap.entrySet()) {
				
				String key = entry.getKey();
				Float value = entry.getValue();
				
				sbMap.get(key).append(start +  (num * value) + "" + type + end + "\n");
			}
		}
	}
}
