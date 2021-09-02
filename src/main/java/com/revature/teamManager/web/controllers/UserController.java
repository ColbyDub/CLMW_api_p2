package com.revature.teamManager.web.controllers;

import com.revature.teamManager.services.PlayerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PlayerService playerService;

    public UserController(PlayerService playerService) {
        this.playerService = playerService;
    }

//    @GetMapping(produces = "application/json")
//    public List<AppUserDTO> getAllUsers() {
//        return userService.findAll();
//    }
//
//    @GetMapping(value = "{id}", produces = "application/json")
//    public AppUserDTO getUserById(@PathVariable String id) {
//        return userService.findUserById(id);
//    }
//
//    @GetMapping("/availability")
//    public AvailabilityStatus checkAvailability(@RequestParam String field, @RequestParam String value) {
//        return userService.determineAvailability(field, value);
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Principal registerNewUser(@RequestBody AppUser newUser) {
//        return new Principal(userService.register(newUser));
//    }

}
