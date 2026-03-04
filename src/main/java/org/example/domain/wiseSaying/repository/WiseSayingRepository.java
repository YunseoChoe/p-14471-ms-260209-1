package org.example.domain.wiseSaying.repository;

import org.example.domain.wiseSaying.entity.WiseSaying;

import java.util.ArrayList;

public class WiseSayingRepository {
    int id = 0;
    ArrayList<WiseSaying> list = new ArrayList<>();

    public WiseSaying save(WiseSaying wiseSaying) {
        // 등록
        if (wiseSaying.getId() == 0) {
            System.out.println("등록");
            wiseSaying.setId(++id);
            list.add(wiseSaying);
        }
        // 수정
        else {
            System.out.println("수정");
            for (int i = 0; i < list.size(); i++) {
                if (wiseSaying.getId() == list.get(i).getId()) {
                    WiseSaying ws = list.get(i);
                    ws.setContent(wiseSaying.getContent());
                    ws.setAuthor(wiseSaying.getAuthor());
                }
            }
        }

        return wiseSaying;
    }

    public ArrayList<WiseSaying> allList() {
        return list;
    }

    public boolean remove(int id) {
        for (int i = 0; i < list.size(); i++) {
            WiseSaying ws = list.get(i);
            if (ws.getId() == id) {
                list.remove(i);
                return true;
            }
        }
        return false; // id를 못찾은 경우
    }

    public WiseSaying getWiseSaying(int id) {
        for (int i = 0; i < list.size(); i++) {
            WiseSaying ws = list.get(i);
            if (ws.getId() == id) {
                return ws;
            }
        }
        return null;
    }
}
