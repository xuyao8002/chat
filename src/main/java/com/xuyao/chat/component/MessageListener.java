package com.xuyao.chat.component;

import com.xuyao.chat.bean.po.Message;
import com.xuyao.chat.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageListener {

    @Autowired
    private IMessageService messageService;

    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    private final List<Message> saveList = new ArrayList<>(1000);
    private final List<Message> readList = new ArrayList<>(1000);
    private final List<Message> deleteList = new ArrayList<>(1000);

    @EventListener
    public void onMessage(Message message){
        queue.offer(message);
    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void saveJob(){
        Message message;
        while ((message = queue.poll()) != null){
            if(Objects.equals(message.getIsRead(), 1)){
                readList.add(message);
            }else if(Objects.equals(message.getIsDelete(), 1)){
                deleteList.add(message);
            }else{
                saveList.add(message);
            }
            if (saveList.size() == 1000){
                messageService.saveBatch(saveList);
                saveList.clear();
            }
        }
        if (!saveList.isEmpty()){
            messageService.saveBatch(saveList);
            saveList.clear();
        }
        if (!readList.isEmpty()) {
            messageService.read(readList);
            readList.clear();
        }
        if (!deleteList.isEmpty()) {
            messageService.delete(deleteList);
            deleteList.clear();
        }
    }

}
