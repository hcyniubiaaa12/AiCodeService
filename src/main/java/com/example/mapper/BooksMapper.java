package com.example.mapper;

import com.example.entity.Books;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 陈增
* @description 针对表【books】的数据库操作Mapper
* @createDate 2025-08-05 15:50:43
* @Entity com.example.entity.Books
*/
@Mapper
public interface BooksMapper extends BaseMapper<Books> {

}




