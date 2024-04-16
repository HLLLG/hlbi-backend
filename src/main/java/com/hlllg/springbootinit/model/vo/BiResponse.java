package com.hlllg.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.hlllg.springbootinit.model.entity.Post;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Bi 返回结果
 *
 */
@Data
public class BiResponse implements Serializable {

    private String genChart;

    private String genResult;

    private Long chartId;
}
