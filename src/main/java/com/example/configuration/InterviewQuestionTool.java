package com.example.configuration;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.entity.Books;
import com.example.service.BooksService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterviewQuestionTool {
    @Autowired
    private BooksService booksService;
    @Tool("""
    根据用户提供的书名查询书籍的详细信息，包括作者和剩余库存。
    
    适用场景：
    - 用户询问某本书的库存数量（如“有多少本”、“还有几本”、“剩多少”）
    - 用户询问作者信息（如“是谁写的”、“作者是谁”）
    - 用户确认是否存在（如“有吗”、“有没有”、“在不在”）
    - 用户使用简称、部分名称或系列名（如“jojo”、“不灭钻石”、“黄金之风”）
    
    匹配规则：
    - 支持模糊匹配：只要书名中包含用户提供的关键词，就应调用此工具
    - 支持系列简称：如“jojo”可匹配《JOJO的奇妙冒险》全系列
    - 即使数据库中没有完全匹配的书籍，也应调用此工具，由后端返回“未找到”
    
    示例问题：
    - 《不灭钻石》还有多少本？
    - jojo的书有多少卷？
    - 星尘远征军是谁写的？
    - 有《黄金之风》吗？
    - 我想找一本叫“石之海”的书
     仅根据数库中已有的书籍信息，查询指定书名的作者和剩余库存。据
      只返回与用户查询书名直接匹配的结果，不要推测、不要联想其他作品。
    """)
    public String askTool( @P("书名") String title){
        System.out.println("✅ askTool 被调用了！title = " + title); // 加日志
        Books book= booksService.getOne(new UpdateWrapper<Books>().like("title", title));
        if (book == null) {
             return "未找到与 \"" + title + "\" 相关的书籍。"
                    + "请确认书名是否准确，或尝试更具体的名称。";
        }
        return "书名："+book.getTitle()+"作者："+book.getAuthor()+"剩余库存："+book.getAvailableQuantity();



    }
}
