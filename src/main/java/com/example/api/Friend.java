package com.example.api;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Friend {
    @SystemMessage("我是路飞,我是船长")
    String chat(@MemoryId String id, @UserMessage String message);

}
