package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Books;
import com.example.service.BooksService;
import com.example.mapper.BooksMapper;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈增
 * @description 针对表【books】的数据库操作Service实现
 * @createDate 2025-08-05 15:50:43
 */
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books>
        implements BooksService {


}




