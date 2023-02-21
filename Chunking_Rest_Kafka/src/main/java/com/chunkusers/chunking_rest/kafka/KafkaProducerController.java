package com.chunkusers.chunking_rest.kafka;

import com.chunkusers.chunking_rest.chunking.Chunker;
import com.chunkusers.chunking_rest.chunking.Unchunker;
import com.chunkusers.chunking_rest.users.Users;
import com.chunkusers.chunking_rest.users.UsersData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import  com.chunkusers.chunking_rest.users.UsersData;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaProducerController
{
    private final KafKaProducerService producerService;

    @Autowired
    public KafkaProducerController(KafKaProducerService producerService)
    {
        this.producerService = producerService;
    }

    @Autowired
    private UsersData usersdata;

    @PostMapping(value = "/users/{id}")
    public void sendMessageToKafkaTopic(@PathVariable int id) throws UnsupportedEncodingException {
        if (id == -1)
            return;

        usersdata.findOne(id);
    }
}