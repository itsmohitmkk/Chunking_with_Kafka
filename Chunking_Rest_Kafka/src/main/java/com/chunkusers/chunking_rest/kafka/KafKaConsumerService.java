package com.chunkusers.chunking_rest.kafka;

import com.chunkusers.chunking_rest.chunking.Chunk;
import com.chunkusers.chunking_rest.chunking.Chunker;
import com.chunkusers.chunking_rest.chunking.Unchunker;
import com.chunkusers.chunking_rest.users.UsersData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class KafKaConsumerService
{
    private final Logger logger = LoggerFactory.getLogger(KafKaConsumerService.class);

    Unchunker unchunker = new Unchunker();

    LoggingUnchunker logging = new LoggingUnchunker(unchunker);

    @KafkaListener(topics = AppConstants.TOPIC_NAME, groupId = AppConstants.GROUP_ID)
    public void consume(String message)
    {
        if (message.equals("START")){
            unchunker = new Unchunker();
            logging = new LoggingUnchunker(unchunker);
            return;
        }
        if (message.equals("END")){
            System.out.println("Unchunked Message formed -> " + new String(logging.messages.get(0)));
            return;
        }

        ByteBuffer m = ByteBuffer.wrap(message.getBytes());
        unchunker.add(m);
        logger.info(String.format("Chunked Message received -> %s", message));
    }





    private static class LoggingUnchunker {
        public List<byte[]> messages = new LinkedList<>();

        public LoggingUnchunker(Unchunker unchunker) {
            unchunker.onMessage(new Unchunker.MessageListener() {
                @Override
                public void onMessage(ByteBuffer message) {
                    final byte[] data = new byte[message.remaining()];
                    message.get(data);
                    LoggingUnchunker.this.messages.add(data);
                }
            });
        }
    }
}