package com.hlllg.springbootinit.mapper;

import com.hlllg.springbootinit.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
* @author Lenovo
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-04-10 15:30:56
* @Entity com.hlllg.springbootinit.model.entity.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {


    List<Map<String, Object>> queryChartData(String querySql);
}




