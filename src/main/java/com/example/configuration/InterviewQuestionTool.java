package com.example.configuration;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.entity.Books;
import com.example.service.BooksService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InterviewQuestionTool {
    @Autowired
    private BooksService booksService;
    @Autowired
    private ImageModelFactory imageModelFactory;

    @Tool("""
            【重要】此工具仅用于查询书籍信息，禁止提及购买、预订、预留、购买链接、价格、会员优惠等任何销售相关行为。
                
            功能：根据书名查询书籍的作者、简介和剩余库存。
            行为规范：
            - 只能回答“有几本”、“作者是谁”、“讲什么”这类问题
            - 绝对不能建议用户“尽快预订”、“我可以帮您预留”、“联系管理员购买”等
            - 如果用户想买或预留，请回答：“本系统仅提供查询服务，不支持购买或预留。”
                
            匹配规则：
            - 支持模糊匹配（如“jojo”可匹配《JOJO的奇妙冒险》）
            - 支持简称、系列名、部分名称
            - 即使未找到，也应返回“未找到相关书籍”
                
            示例问题：
            - 《不灭钻石》还有多少本？
            - 鸽子神的救赎讲的是啥？
            - jojo的书有多少卷？
            - 星尘远征军是谁写的？
            - 有《黄金之风》吗？
            如果没有书的时候的返回格式:
            -近期将会进货尽情期待 
            返回格式：
            - 书名：xxx
            - 作者：xxx
            - 简介：xxx
            - 剩余库存：x 本
            -如有喜欢可以在次平台购买订阅
            """)
    public String askTool(@P("书名") String title) {
        System.out.println("✅ askTool 被调用了！title = " + title); // 加日志
        Books book = booksService.getOne(new UpdateWrapper<Books>().like("title", title));
        if (book == null) {
            return "未找到与 \"" + title + "\" 相关的书籍。"
                    + "请确认书名是否准确，或尝试更具体的名称。";
        }
        return "书名：" + book.getTitle() + "作者：" + book.getAuthor() + "这本书描述的是:" + book.getDescription() + "剩余库存：" + book.getAvailableQuantity();
    }

    @Tool("此工具仅用于查询书籍信息，禁止提及购买、预订、预留、购买链接、价格、会员优惠等任何销售相关行为。")
    public List<Books> listBooks() {
        System.out.println("✅ listBooks 被调用了！");
        return booksService.list();
    }

    @Tool("仅生成用户描述对应的图片，不提供任何文字说明、解释、引导语或额外回复。")
    public String generateImage(String prompt) {
        String taskId = imageModelFactory.createAsyncTask(prompt);
        String s = imageModelFactory.waitAsyncTask(taskId);
        return s;
    }
}
