package com.hlllg.springbootinit.bizmq;

import com.hlllg.springbootinit.common.ErrorCode;
import com.hlllg.springbootinit.constant.CommonConstant;
import com.hlllg.springbootinit.exception.BusinessException;
import com.hlllg.springbootinit.manager.AiManager;
import com.hlllg.springbootinit.model.entity.Chart;
import com.hlllg.springbootinit.service.ChartService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: HLLLG
 * @Date: 2024/04/20/10:40
 */
@Component
@Slf4j
public class BiMessageConsumer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    // 指定程序监听的消息队列和确认方式
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliverTag) {
        log.info("receiveMessage message = {}", message);
        if (StringUtils.isAnyBlank(message)) {
            // 如果失败，消息拒绝
            channel.basicNack(deliverTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliverTag, false, false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "图标为空");
        }
        // 先修改图表状态为“执行中”， 等执行成功后修改为“已完成”并保存执行结果，如果执行失败则将状态改为“失败”
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("running");
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
        }

        // 调用AI
        String chat = aiManager.doChat(CommonConstant.BI_MODEL_ID, buildUserInput(chart));
        String[] splits = chat.split("【【【【【");
        if (splits.length < 3) {
            channel.basicNack(deliverTag, false, false);
            handleChartUpdateError(chart.getId(), "AI 生成错误");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        // todo 建议定义状态枚举值
        updateChartResult.setStatus("succeed");
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            channel.basicNack(deliverTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
        }
        // 消息确认
        channel.basicAck(deliverTag, false);
    }

    /**
     * 构建用户输入
     *
     * @param chart
     * @return
     */
    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(userGoal)) {
            userGoal += ", 请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

    private void handleChartUpdateError(Long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("图标状态更改失败" + chartId + ", " + execMessage);
        }
    }

}
