package com.hlllg.springbootinit.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: HLLLG
 * @Date: 2024/04/17/14:28
 */
@SpringBootTest
class ChartMapperTest {

    @Resource
    private ChartMapper chartMapper;

    @Test
    void queryChartData() {
        String querySql = "select * from chart_312321";
        List<Map<String, Object>> map = chartMapper.queryChartData(querySql);

    }
}