package com.revature.teamManager.services;

import com.revature.teamManager.data.repos.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }
}
