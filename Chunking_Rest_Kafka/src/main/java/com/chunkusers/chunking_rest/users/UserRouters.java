package com.chunkusers.chunking_rest.users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserRouters {
    @Autowired
    private UsersData usersdata;

    // GET All Users

    @GetMapping(path = "/users")
    public List<Users> findAll(){
        return usersdata.findAll();
    }

    // GET specific User

    @GetMapping(path = "/users/{id}")
    public Users findOne(@PathVariable int id) throws UserNotFoundException{
        Users user = usersdata.findOne(id);
        if (user == null){
            throw new UserNotFoundException("Id Not Found");
        }
        return user;
    }

    // ADD a user
    @PostMapping(path = "/users")
    public Users addUser(@RequestBody Users user){
        Users currUser = usersdata.addUser(user);
        if (currUser == null){
            throw new UserNotFoundException("Id Not Found");
        }
        return currUser;
    }

    @DeleteMapping(path = "/users/{id}")
    public Users deleteUser(@PathVariable int id){
        Users currUser = usersdata.deleteUser(id);
        if (currUser == null){
            throw new UserNotFoundException("Id Not Found");
        }
        return currUser;

    }


    @PutMapping(path = "/users/{id}")
    public Object updateUser(@RequestBody Users user , @PathVariable int id){
        Users currUser = usersdata.updateUser(user, id);
        if (currUser == null){
            throw new UserNotFoundException("Id Not Found");
        }
        return currUser;

    }


}
