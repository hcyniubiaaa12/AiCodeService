package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName books
 */
@TableName(value ="books")
@Data
public class Books {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String author;

    /**
     * 
     */
    private String isbn;

    /**
     * 
     */
    private String category;

    /**
     * 
     */
    private Integer totalQuantity;

    /**
     * 
     */
    private Integer availableQuantity;

    /**
     * 
     */
    private String coverUrl;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Integer isDeleted;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;
}