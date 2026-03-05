package org.example.domain.wiseSaying.controller;

import org.example.domain.wiseSaying.entity.WiseSaying;
import org.example.domain.wiseSaying.service.WiseSayingService;

import java.util.ArrayList;
import java.util.Scanner;

public class WiseSayingController {
    WiseSayingService wiseSayingService = new WiseSayingService();

    public void actionWrite(Scanner sc) {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();

        WiseSaying wiseSaying = wiseSayingService.write(0, content, author);

        int id = wiseSaying.getId();

        System.out.println("%d번 명언이 등록되었습니다.".formatted(id));
    }

    public void actionList(Scanner sc) {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        ArrayList<WiseSaying> savedList = wiseSayingService.get();

        for (int i = savedList.size() - 1; i >= 0; i--) {
            WiseSaying ws = savedList.get(i);
            System.out.println("%d / %s / %s".formatted(ws.getId(), ws.getAuthor(), ws.getContent()));
        }
    }

    public void actionDelete(int id) {
        if (wiseSayingService.delete(id)) {
            System.out.println("%d번 명언은 삭제되었습니다.".formatted(id));
        }
        else {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
        }
    }

    public void actionModify(Scanner sc, int id) {
        // 기존 명언 가져오기
        WiseSaying ws = wiseSayingService.getOldWiseSaying(id);
        if (ws == null) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        System.out.print("명언(기존) : ");
        System.out.println("%s".formatted(ws.getContent()));

        System.out.print("명언 : ");
        String newContent = sc.nextLine();

        System.out.print("작가(기존) : ");
        System.out.println("%s".formatted(ws.getAuthor()));

        System.out.print("작가 : ");
        String newAuthor = sc.nextLine();

        // 명언 덮어쓰기
        wiseSayingService.write(id, newContent, newAuthor);
    }

    public void actionBuild() {
        wiseSayingService.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
