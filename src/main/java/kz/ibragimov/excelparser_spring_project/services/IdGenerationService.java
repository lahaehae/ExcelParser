package kz.ibragimov.excelparser_spring_project.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class IdGenerationService {
    private final AtomicLong counter = new AtomicLong(0);

    public Long generateId(){
        return counter.incrementAndGet();
    }

}
