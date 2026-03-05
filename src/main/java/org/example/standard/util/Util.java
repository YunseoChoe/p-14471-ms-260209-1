package org.example.standard.util;

import org.example.domain.wiseSaying.entity.WiseSaying;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class Util {

    /* ===================== 파일 유틸 ===================== */
    public static class file {

        private static Path getPath(String filePath) {
            return Paths.get(filePath);
        }

        /* 파일 생성 (빈 파일) */
        public static void touch(String filePath) {
            set(filePath, "");
        }

        /* 파일 내용 덮어쓰기 */
        public static void set(String filePath, String content) {
            Path path = getPath(filePath);
            try {
                writeFile(path, content);
            } catch (IOException e) {
                handleFileWriteError(path, content, e);
            }
        }

        private static void writeFile(Path path, String content) throws IOException {
            Files.writeString(
                    path,
                    content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        }

        private static void handleFileWriteError(Path path, String content, IOException e) {
            Path parentDir = path.getParent();

            if (parentDir != null && Files.notExists(parentDir)) {
                try {
                    Files.createDirectories(parentDir);
                    writeFile(path, content);
                } catch (IOException ex) {
                    throw new RuntimeException("파일 쓰기 실패: " + path, ex);
                }
            } else {
                throw new RuntimeException("파일 접근 실패: " + path, e);
            }
        }

        /* 파일 존재 여부 */
        public static boolean exists(String filePath) {
            return Files.exists(getPath(filePath));
        }

        /* 파일 읽기 (기본값 없음) */
        public static String get(String filePath) {
            return get(filePath, "");
        }

        /* 파일 읽기 (기본값 지정) */
        public static String get(String filePath, String defaultValue) {
            try {
                return Files.readString(getPath(filePath));
            } catch (IOException e) {
                return defaultValue;
            }
        }

        /* int 값으로 읽기 */
        public static int getAsInt(String filePath, int defaultValue) {
            String value = get(filePath, "");
            if (value.isEmpty()) return defaultValue;

            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }

        /* 디렉토리 생성 */
        public static void mkdir(String dirPath) {
            try {
                Files.createDirectories(getPath(dirPath));
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + dirPath, e);
            }
        }

        /* 파일 삭제 */
        public static boolean delete(String filePath) {
            try {
                return Files.deleteIfExists(getPath(filePath));
            } catch (IOException e) {
                return false;
            }
        }

        /* 디렉토리 내 파일 이름 목록 */
        public static String[] getFileNames(String dirPath) {
            try (Stream<Path> paths = Files.list(getPath(dirPath))) {
                return paths
                        .map(path -> path.getFileName().toString())
                        .toArray(String[]::new);
            } catch (IOException e) {
                return new String[0];
            }
        }
    }

    /* ===================== JSON 유틸 ===================== */
    public static class wiseSayingToJson {

        /* WiseSaying → JSON */
        public static String toJson(WiseSaying ws) {
            return """
                    {
                      "id": %d,
                      "content": "%s",
                      "author": "%s"
                    }
                    """.formatted(
                    ws.getId(),
                    escape(ws.getContent()),
                    escape(ws.getAuthor())
            );
        }

        /* JSON → WiseSaying */
        public static WiseSaying fromJson(String json) {
            int id = Integer.parseInt(extract(json, "id"));
            String content = extract(json, "content");
            String author = extract(json, "author");

            return new WiseSaying(id, content, author);
        }

        /* ===== 내부 파싱 ===== */

        private static String extract(String json, String key) {
            String token = "\"" + key + "\"";
            int start = json.indexOf(token);
            start = json.indexOf(":", start) + 1;

            if (json.charAt(start) == ' ') start++;

            if (json.charAt(start) == '"') {
                start++;
                int end = json.indexOf("\"", start);
                return json.substring(start, end);
            } else {
                int end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);
                return json.substring(start, end).trim();
            }
        }

        private static String escape(String value) {
            return value.replace("\"", "\\\"");
        }
    }
}