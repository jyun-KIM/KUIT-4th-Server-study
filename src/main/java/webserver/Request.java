package webserver;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {
    private String method;
    private String path;
    private String httpVersion;

    public static Request from(BufferedReader br) throws IOException {
        Request request = new Request();
        String requestLine = br.readLine();  // 첫 번째 요청 라인 읽기

        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 요청 라인입니다.");
        }

        // 요청 라인 처리: 메서드, 경로, HTTP 버전 파싱
        String[] tokens = requestLine.split(" ");
        request.method = tokens[0];  // 예: GET
        request.path = tokens[1];    // 예: /index.html
        request.httpVersion = tokens[2];  // 예: HTTP/1.1

        return request;  // 완성된 HttpRequest 객체 반환
    }

    // Getter 메서드들
    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

}
