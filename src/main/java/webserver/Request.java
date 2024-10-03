//package webserver;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.net.URLDecoder;
//import java.util.HashMap;
//import java.util.Map;
//
//public class Request {
//    private String method;
//    private String path;
//    private String httpVersion;
//    private Map<String, String> cookies = new HashMap<>();
//    private Map<String, String> params = new HashMap<>();
//
//    // Request 객체 생성 (GET 요청 파싱)
//    public static Request from(BufferedReader br) throws IOException {
//        Request request = new Request();
//        String requestLine = br.readLine();  // 첫 번째 요청 라인 읽기
//
//        if (requestLine == null || requestLine.isEmpty()) {
//            throw new IllegalArgumentException("유효하지 않은 요청 라인입니다.");
//        }
//
//        // 요청 라인 처리: 메서드, 경로, HTTP 버전 파싱
//        String[] tokens = requestLine.split(" ");
//        request.method = tokens[0];  // 예: GET
//        request.path = tokens[1];    // 예: /index.html
//        request.httpVersion = tokens[2];  // 예: HTTP/1.1
//
//
//        // 헤더 파싱
//        Map<String, String> headers = new HashMap<>();
//        String line;
//        // 헤더는 빈 줄이 나오기 전까지 계속 읽는다
//        while (!(line = br.readLine()).isEmpty()) {
//            // ": "로 헤더의 key와 value를 분리
//            String[] headerTokens = line.split(": ", 2);
//            if (headerTokens.length == 2) {
//                headers.put(headerTokens[0], headerTokens[1]);  // key-value로 저장
//            }
//
//            }
////
////        // GET 요청일 때 쿼리 파라미터 파싱
////        if (request.method.equalsIgnoreCase("GET")) {
////            request.params = request.parseQuery();
////        }
////        else if (request.method.equalsIgnoreCase("POST")) {
////            request.params = request.getBodyParams(br);
////        }
//
////        // 헤더 파싱
////        String line;
////        while ((line = br.readLine()) != null && !line.isEmpty()) {
////            // 쿠키 헤더 처리
////            if (line.startsWith("Cookie:")) {
////                request.cookies = parseCookies(line);
////            }
////        }
//
//        return request;  // 완성된 Request 객체 반환
//    }
//
//    // 쿠키 파싱
//    private static Map<String, String> parseCookies(String line) {
//        Map<String, String> cookies = new HashMap<>();
//        // "Cookie: " 부분을 제거하고 실제 쿠키 정보만 가져옴
//        String cookieString = line.substring(8).trim();
//        String[] pairs = cookieString.split(";");
//
//        for (String pair : pairs) {
//            String[] keyValue = pair.split("=");
//            if (keyValue.length > 1) {
//                String key = keyValue[0].trim();
//                String value = keyValue[1].trim();
//                cookies.put(key, value);
//            }
//        }
//        return cookies;
//    }
//
//    // 본문 읽어오기
//    public Map<String, String> getBodyParams(BufferedReader br) throws IOException {
//        String line;
//        int contentLength = 0;
//
//        // 헤더를 읽어 Content-Length 확인
//        while ((line = br.readLine()) != null && !line.isEmpty()) {
//            if (line.startsWith("Content-Length")) {
//                String[] contentLengthHeader = line.split(":");
//                contentLength = Integer.parseInt(contentLengthHeader[1].trim());
//            }
//        }
//
//        // Content-Length 만큼의 본문 데이터 읽기
//        char[] body = new char[contentLength];
//        int bytesRead = br.read(body, 0, contentLength);
//        String bodyString = new String(body, 0, bytesRead);
//
//        // POST 데이터를 파싱하여 Map에 저장
//        return parseBody(bodyString);
//    }
//
//    // POST: body 파싱
//    private Map<String, String> parseBody(String body) throws IOException {
//        Map<String, String> postParams = new HashMap<>();
//        String[] pairs = body.split("&");
//
//        for (String pair : pairs) {
//            String[] keyValue = pair.split("=");
//            if (keyValue.length > 1) {
//                String key = URLDecoder.decode(keyValue[0], "UTF-8");
//                String value = URLDecoder.decode(keyValue[1], "UTF-8");
//                postParams.put(key, value);
//            }
//        }
//
//        return postParams;
//    }
//
//    // GET: query 파싱
//    private Map<String, String> parseQuery() throws IOException {
//        Map<String, String> queryParams = new HashMap<>();
//        if (path.contains("?")) {
//            String[] parts = path.split("\\?", 2);
//            String queryString = parts[1];
//            String[] pairs = queryString.split("&");
//
//            for (String pair : pairs) {
//                String[] keyValue = pair.split("=");
//                if (keyValue.length > 1) {
//                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
//                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
//                    queryParams.put(key, value);
//                }
//            }
//        }
//        return queryParams;
//    }
//
//
//    public Map<String, String> getQueryParams() {
//        return params;
//    }
//
//    public Map<String, String> getBodyParams() {
//        return params;
//    }
//
//    // 쿠키 값을 가져오는 메서드
//    public String getCookie(String name) {
//        return cookies.get(name);
//    }
//
//    // Getter 메서드들
//    public String getMethod() {
//        return method;
//    }
//
//    public String getPath() {
//        return path;
//    }
//}

package webserver;

import http.util.HttpRequestUtils;
import http.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private Map<String, String> bodyParams;

    // 생성자
    private Request(String method, String path, String version, Map<String, String> headers,
                        Map<String, String> queryParams, Map<String, String> bodyParams) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.queryParams = queryParams;
        this.bodyParams = bodyParams;
    }

    // 정적 팩토리 메서드: BufferedReader를 통해 HttpRequest 객체 생성
    public static Request from(BufferedReader br) throws IOException {
        // 1. 요청 라인 파싱
        String requestLine = br.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IllegalArgumentException("Invalid request line");
        }
        String[] requestTokens = requestLine.split(" ");
        String method = requestTokens[0];
        String url = requestTokens[1];
        String protocol = requestTokens[2];

        // 2. 헤더 파싱
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            String[] headerTokens = line.split(": ", 2);
            if (headerTokens.length == 2) {
                headers.put(headerTokens[0], headerTokens[1]);
            }
        }

        // 3. URL에서 경로와 쿼리 스트링 분리
        String path = url.split("\\?")[0];
        Map<String, String> queryParams = new HashMap<>();
        if (url.contains("?")) {
            String queryString = url.split("\\?")[1];
            queryParams = HttpRequestUtils.parseQueryParameter(queryString);
        }

        // 4. 바디 파싱 (Content-Length가 있을 때만)
        Map<String, String> bodyParams = new HashMap<>();
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            String bodyContent = IOUtils.readData(br, contentLength);
            bodyParams = HttpRequestUtils.parseQueryParameter(bodyContent);
        }

        return new Request(method, path, protocol, headers, queryParams, bodyParams);
    }

    // Getters
    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getBodyParams() {
        return bodyParams;
    }
}