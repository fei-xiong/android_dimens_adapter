import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;


public class DimensCreater {
	
	static String dir = System.getProperty("user.dir");
	public static void main(String[] args) throws Throwable {
		
		int width = 480;
		if(args != null && args.length > 0){
			width = Integer.parseInt(args[0]);
			if(width <= 0){
				throw new RuntimeException("Width must be greater than zero");
			}
			System.out.println("make dimens for width: " + width);
		}
		
		for (Map.Entry<String, Float> entry : DimensAdapter.adapterMap.entrySet()) {
			
			StringBuilder buffer = new StringBuilder();
			
			buffer.append(
						"<!--\n" +
						"\t制作该dimens文件是以" + width + "px宽度的手机为模板, \n" +
						"\t所以如果你的设计图就是" + width + "的那么你可以直接使用标注的尺寸, \n" +
						"\t例如： 标注为40px， 那么就可以使用@dimens/px_40 \n" + 
						"-->\n"
						);
			
			float value = entry.getValue();
			
			buffer.append("<resources>\n");
			for (int i = 1; i <= 1280; i++) {
				buffer.append("<dimen name=\"px_" + i + "\">" + ((double) i/2 * value * 480/width) +  "dp</dimen>\n");
			}
			buffer.append("</resources>");
			
			File dimens = new File(dir + entry.getKey() + "/dimens.xml");
			write(buffer, dimens);
		}
		
		System.out.println("over");
	}
	
	private static void write(StringBuilder buffer, File dimens)
			throws FileNotFoundException, IOException {
		dimens.getParentFile().mkdirs();
		FileOutputStream out = new FileOutputStream(dimens);
		
		out.write(buffer.toString().getBytes());
		
		out.close();
	}
}
