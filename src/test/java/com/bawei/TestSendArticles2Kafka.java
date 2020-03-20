package com.bawei;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.bawei.util.FileUtilIO;
import com.bawei.util.cms_article;
//测试发送文章到kafka
//需求1:读取爬虫爬取出来的文章,并且封装到一个article对象中.发送到kafka(作为一个生产者)
//需求2:在cms系统中,编写一个消费者,然后接收爬虫项目发来的消息.并且成功的保存在cms_article表中

//这个类就是生产者的类
@RunWith(SpringJUnit4ClassRunner.class)
//加载生产者的配置
@ContextConfiguration("classpath:producer.xml")
public class TestSendArticles2Kafka {

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	@Test
	public void testSendArticles2Kafka() throws Exception {
		//1.从D:\杂乱\articles下的所有文件
		File file = new File("E:\\articles");
		File[] listFiles = file.listFiles();
		//2.遍历目录下的所有文件.
		for (File file2 : listFiles) {
//			System.out.println(file2.getName());
			//3.拿到文件的名称,作为Article对象的title
			String fileName = file2.getName();
			String title = fileName.replace(".txt", "");
//			System.out.println(title);
			//4.拿到文件内容作为文章的content来封装
			String content = FileUtilIO.readFile(file2, "utf8");
//			System.out.println(content);
			//创建一个文章对象
			cms_article article = new cms_article();
			//4.1.把标题和内容封装到一个文章对象里
			article.setTitle(title);
			article.setContent(content);
			//5.补全文章对象的其他属性
			article.setCategory_id(2);
			article.setChannel_id(1);
			//其他待补全字段,自己补全..............
			//6.把这个文章对象,转成json串(提高效率)
			//fastJson的方式把对象,转成json串
			String jsonString = JSON.toJSONString(article);//{"title":"zhongguo","content":"xinguan"}
			//7.发送到kafka
			//7.1在爬虫项目中整合kakfa的生产者
			//7.2.导入kafka的依赖
			//7.3.复制producer.xml配置文件,修改ip和主题名称
			//7.4.加载这个配置文件,并注入Kafkatemplate,然后就可以调用发送方法了
			kafkaTemplate.send("cms_articles",jsonString);
		}
	}
}
