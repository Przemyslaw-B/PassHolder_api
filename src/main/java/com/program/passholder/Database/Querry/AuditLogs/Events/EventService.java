package com.program.passholder.Database.Querry.AuditLogs.Events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public Optional<String> getNameById(int id) {
        Optional<EventEntity> entity = eventRepository.findById(id);
        return entity.map(EventEntity::getName);
    }
}
