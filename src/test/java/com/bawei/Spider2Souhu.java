package com.bawei;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bawei.util.FileUtilIO;

//爬虫类
public class Spider2Souhu {

	public static void main(String[] args) throws Exception {
		//利用jsoup用java代码来模拟浏览器发送一个get请求或者是post请求
		Document document = Jsoup.connect("http://news.sohu.com/").get();
		//打印doc对象,就是咱们熟悉的html标签
//		System.out.println(document);
		//根据jsoup的选择器语法,来进行选择咱们需要的标签
		Elements divs = document.select(".list16");
		for (Element div : divs) {
			//打印class为list16的div
//			System.out.println(div);
			//获取class为list16的div下面的所有的a标签
			Elements as = div.select("a");
			for (Element a : as) {
				String content =null;
				
				//得到a标签
				String title = a.attr("title");
				title.replace("|", "").replace("|", "").replace("\\", "").replace(":", "").replace("*", "").replace("\"", "").replace("?", "").replace("<", "").replace(">", "");
				if(!title.contains("疫情风暴席")) {
					
					//打印属性为title的值
//				System.out.println(title);
					//获取a标签中的属性为href的值,这个就是文字内容的连接
					String url = a.attr("href");
					if(!url.startsWith("http")) {
						url="http:"+url;
					}
				
					//这个判断是解决反爬问题
					System.out.println(url);
					if(!url.contains("subject")&&!url.contains("319586")) {
						//拿到链接之后,就还可以再次模拟浏览器发送请求
						Document document2 = Jsoup.connect(url).get();
						//得到文章内容的相关页面,获取article标签
						Elements articleM = document2.select("article");
						for (Element element : articleM) {
//					System.out.println(element);
							//如何获取标签内所有的内容呢?
							//利用选择器获取文章内容
							content = element.text();
//						System.out.println(content);
						}
						
					}
				}
				FileUtilIO.writeFile("E:\\articles\\"+title+".txt", content, "utf8");
				
			}
		}
		
	}
}
