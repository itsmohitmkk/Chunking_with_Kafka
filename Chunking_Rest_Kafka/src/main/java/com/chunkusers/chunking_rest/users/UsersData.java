package com.chunkusers.chunking_rest.users;
import com.chunkusers.chunking_rest.chunking.Chunker;
import com.chunkusers.chunking_rest.kafka.KafKaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsersData {
    private static byte MORE = 0;
    private static byte END = 1;

    public static List<Users> users = new ArrayList<>();

    static {
        users.add(new Users(1, "Mohit", LocalDate.now().minusYears(21)));
        users.add(new Users(2, "Ram", LocalDate.now().minusYears(23)));
        users.add(new Users(3, "Shyam", LocalDate.now().minusYears(25)));
    }

    @Autowired
    KafKaProducerService producerService;


    public List<Users> findAll() {
        sendtoKafka(users.toString());
        return users;
    }

    public Users addUser(Users user) {
        users.add(user);
        return user;
    }

    public Users deleteUser(int id) {
        int ind = findUserOnID(id);
        if (ind == -1) {
            return null;
        }
        return users.remove(ind);
    }

    public Users updateUser(Users user, int id) {
        int ind = findUserOnID(id);
        if (ind == -1) {
            return null;
        }

        users.get(ind).setName(user.getName());
        users.get(ind).setId(user.getId());
        users.get(ind).setBirthDate(user.getBirthDate());
        sendtoKafka(users.get(ind).toString());
        return users.get(ind);

    }

    private int findUserOnID(int id) {
        int ind = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                ind = i;
                break;
            }
        }

        return ind;
    }


    public Users findOne(int id)  {
        int ind = findUserOnID(id);
        if (ind == -1)
            return null;

        Users curr = users.get(ind);
        sendtoKafka(curr.toString());
        return curr;
    }

    void sendtoKafka(String users){
        ByteBuffer message = ByteBuffer.wrap(users.getBytes(StandardCharsets.UTF_8));

        Chunker chunker = new Chunker(12, message, 20);
        producerService.sendMessage(ByteBuffer.wrap("START".getBytes(StandardCharsets.UTF_8)));

        while (chunker.hasNext()) {
            ByteBuffer chunk = chunker.next();
            producerService.sendMessage(chunk);
        }

        producerService.sendMessage(ByteBuffer.wrap("END".getBytes(StandardCharsets.UTF_8)));
    }

}