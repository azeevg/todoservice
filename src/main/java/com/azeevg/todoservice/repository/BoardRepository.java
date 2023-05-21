package com.azeevg.todoservice.repository;

import com.azeevg.todoservice.model.Board;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends ListCrudRepository<Board, UUID> {

    void deleteById(UUID id);
}
