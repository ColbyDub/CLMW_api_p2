package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Pin;
import com.revature.teamManager.services.PinService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pins")
public class PinController {
    private final PinService pinService;

    public PinController(PinService pinService) {
        this.pinService = pinService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Pin registerNewPin(@RequestBody Pin pin) {
        return pinService.register(pin);
    }

}
