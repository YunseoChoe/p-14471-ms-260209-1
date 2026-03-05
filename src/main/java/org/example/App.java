package org.example;

import org.example.domain.wiseSaying.controller.SystemController;
import org.example.domain.wiseSaying.controller.WiseSayingController;

import java.util.Scanner;

public class App {
    Scanner sc = new Scanner(System.in);
    WiseSayingController wiseSayingController = new WiseSayingController();
    SystemController systemController = new SystemController();

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine();

            // 종료
            if (cmd.equals("종료")) {
                systemController.exit();
                return;
            }

            // 등록
            else if (cmd.equals("등록")) {
                wiseSayingController.actionWrite(sc);
            }

            // 목록
            else if (cmd.equals("목록")) {
                wiseSayingController.actionList(sc);
            }

            // 삭제
            else if (cmd.startsWith("삭제")) {
                // id 추출
                int deleteId = Integer.parseInt(cmd.substring(6));
                wiseSayingController.actionDelete(deleteId);
            }

            // 수정
            else if (cmd.startsWith("수정")) {
                int modifyId = Integer.parseInt(cmd.substring(6));
                wiseSayingController.actionModify(sc, modifyId);
            }

            // 빌드
            else if (cmd.equals("빌드")) {
                wiseSayingController.actionBuild();
            }
        }
    }
}
