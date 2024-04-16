package com.hlllg.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hlllg.springbootinit.model.entity.Chart;
import com.hlllg.springbootinit.service.ChartService;
import com.hlllg.springbootinit.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-04-10 15:30:56
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




