package org.example.domain.wiseSaying.service;

import org.example.domain.wiseSaying.entity.WiseSaying;
import org.example.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.ArrayList;

public class WiseSayingService {
    WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();

    public WiseSaying write(int id, String content, String author) {
        WiseSaying wiseSaying = new WiseSaying(id, content, author); // 등록일 때 id는 항상 0.
        WiseSaying savedWiseSaying = wiseSayingRepository.save(wiseSaying);
        return savedWiseSaying;
    }

    public ArrayList<WiseSaying> get() {
        return wiseSayingRepository.allList();
    }

    public boolean delete(int id) {
        return wiseSayingRepository.remove(id);
    }

    public WiseSaying getOldWiseSaying(int id) {
        return wiseSayingRepository.getWiseSaying(id);
    }

    public void build() {
        wiseSayingRepository.dataBuild();
    }
}
