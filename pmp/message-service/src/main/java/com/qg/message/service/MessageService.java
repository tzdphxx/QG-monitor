package com.qg.message.service;



import com.qg.common.domain.po.Result;
import com.qg.message.domain.po.Message;

import java.util.List;

/**
 * @Description: // 类说明
 * @ClassName: MessageService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/13 19:02   // 时间
 * @Version: 1.0     // 版本
 */
public interface MessageService {
    Result sendMessage(List<Message> messages);

    Result getAllMessages(Long userId);
}
