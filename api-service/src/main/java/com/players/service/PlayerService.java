package com.players.service;

import com.players.domain.entity.Player;
import com.players.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository repository;

    @Autowired
    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public Page<Player> findAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        return repository.findAll(pageable);
    }

    public Iterable<Player> findAll() {
        return repository.findAll();
    }

    public Iterable<Player> getAll() {
        return repository.findAll();
    }

    public Player save(Player user) {
        return repository.save(user);
    }

    public void save(List<Player> players) {
        repository.saveAll(players);
    }

    public Optional<Player> findById(String id) {
        return repository.findById(id);
    }
}
