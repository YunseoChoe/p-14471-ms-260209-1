package org.example.domain.wiseSaying.repository;

import org.example.domain.wiseSaying.entity.WiseSaying;
import org.example.standard.util.Util;

import java.util.ArrayList;

public class WiseSayingRepository {
    int id = 0;
    ArrayList<WiseSaying> list = new ArrayList<>();

    private final String DIR_PATH = "db/WiseSaying";
    private final String LAST_ID_PATH = DIR_PATH + "/lastId.txt";

    // id 재설정
    public void setNewId() {
        if (Util.file.exists(LAST_ID_PATH)) {
            id = Util.file.getAsInt(LAST_ID_PATH, 0);
        }
    }

    public WiseSaying save(WiseSaying wiseSaying) {
        setNewId(); // id 재설정

        // 등록
        if (wiseSaying.getId() == 0) {
            wiseSaying.setId(++id);
            list.add(wiseSaying);
        }
        // 수정
        else {
            for (int i = 0; i < list.size(); i++) {
                if (wiseSaying.getId() == list.get(i).getId()) {
                    WiseSaying ws = list.get(i);

                    ws.setContent(wiseSaying.getContent());
                    ws.setAuthor(wiseSaying.getAuthor());
                }
            }
        }

        // 디렉토리 생성
        Util.file.mkdir(DIR_PATH);

        // json 파일 생성 및 갱신
        String json = Util.wiseSayingToJson.toJson(wiseSaying);
        String jsonPath = DIR_PATH + "/" + wiseSaying.getId() + ".json"; // 전역변수의 id가 아닌, 객체의 id.
        Util.file.set(jsonPath, json);

        // lastId 갱신
        Util.file.set(LAST_ID_PATH, String.valueOf(id));

        return wiseSaying;
    }

    public ArrayList<WiseSaying> allList() {
        // json 파일에서 명언 읽어오기
        list.clear();

        // 명언이 없으면
        if (!Util.file.exists(DIR_PATH)) return null;

        String[] fileNames = Util.file.getFileNames(DIR_PATH);

        for (String fileName : fileNames) {
            if (fileName.equals("lastId.txt")) continue;
            if (fileName.equals("data.json")) continue;

            String jsonPath = DIR_PATH + "/" + fileName;
            String json = Util.file.get(jsonPath);

            WiseSaying wiseSaying =
                    Util.wiseSayingToJson.fromJson(json);

            // list에 추가
            list.add(wiseSaying);
        }

        // return
        return list;
    }

    public boolean remove(int id) {
        for (int i = 0; i < list.size(); i++) {
            WiseSaying ws = list.get(i);

            if (ws.getId() == id) {
                // 리스트에서 삭제
                list.remove(i);

                // json 파일 삭제
                String jsonPath = DIR_PATH + "/" + id + ".json";
                Util.file.delete(jsonPath);

                // lastId 가져오기
                int lastId = Util.file.getAsInt(LAST_ID_PATH, 0);

                // 마지막 id를 삭제한 경우
                if (id == lastId) {
                    // lastId값 -1
                    Util.file.set(LAST_ID_PATH, String.valueOf(lastId - 1));
                }

                return true;
            }
        }

        return false; // id를 못 찾은 경우
    }

    public WiseSaying getWiseSaying(int id) {
        allList();
        System.out.println("수정할 id: " + id + "!");
        for (int i = 0; i < list.size(); i++) {
            WiseSaying ws = list.get(i);
            System.out.println(ws.getId() + ", " + ws.getContent() + ", " + ws.getAuthor());
        }
        for (int i = 0; i < list.size(); i++) {
            WiseSaying ws = list.get(i);
            System.out.println("ws의 각 id: " + ws.getId());
            if (ws.getId() == id) {
                return ws;
            }
        }
        return null;
    }

    public void dataBuild() {
        allList(); // 최신 데이터 로드

        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < list.size(); i++) {
            WiseSaying ws = list.get(i);

            String wsJson = Util.wiseSayingToJson.toJson(ws);

            // 각 줄 앞에 두 칸 들여쓰기 추가
            String indented = wsJson
                    .lines()
                    .map(line -> "  " + line)
                    .reduce((a, b) -> a + "\n" + b)
                    .orElse("");

            json.append(indented);

            if (i < list.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");

        String dataJsonPath = DIR_PATH + "/data.json";
        Util.file.set(dataJsonPath, json.toString());
    }
}
