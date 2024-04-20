package com.hlllg.springbootinit.manager;

import com.hlllg.springbootinit.common.ErrorCode;
import com.hlllg.springbootinit.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: HLLLG
 * @Date: 2024/04/16/11:41
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient client;

    /**
     * AI对话
     * @param message
     * @return
     */
    public String doChat(long modelId, String message) {
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }
}
