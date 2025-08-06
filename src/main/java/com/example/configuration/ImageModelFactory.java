package com.example.configuration;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

@Component
public class ImageModelFactory {
    @Value("${langchain4j.community.dashscope.chat-model.api-key}")
    private String key;

    public String createAsyncTask(String prompt) {
        ImageSynthesisParam param = ImageSynthesisParam.builder()
                .apiKey(key)
                .model(ImageSynthesis.Models.WANX_V1)
                .prompt(prompt)
                .style("<watercolor>")
                .n(1)
                .size("1024*1024")
                .build();
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            result = imageSynthesis.asyncCall(param);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
        String taskId = result.getOutput().getTaskId();
        System.out.println("taskId=" + taskId);
        return taskId;

    }

    public void waitAsyncTask(String taskId) {
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        ImageSynthesisResult result = null;
        try {
            //环境变量配置后，可在这里将apiKey设置为null
            result = imageSynthesis.wait(taskId, key);
        } catch (ApiException | NoApiKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println(JsonUtils.toJson(result));
        System.out.println(JsonUtils.toJson(result.getOutput()));
    }


}
