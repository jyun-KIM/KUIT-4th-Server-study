package webserver;

import db.MemoryUserRepository;
import model.User;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler implements Runnable{
    Socket connection;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());
    private static final String WEB_ROOT = "webapp";
    public RequestHandler(Socket connection) {
        this.connection = connection;
    } // 클라이언트와 연결된 소켓 객체 받음

    @Override
    public void run() {
        //log.log(Level.INFO, "New Client Connect! Connected IP : " + connection.getInetAddress() + ", Port : " + connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            // 첫 번째 요청 라인 처리- GET / HTTP/1.1 등
            String requestLine = br.readLine();
            if (requestLine == null) return;

            // 요청된 파일을 결정
            String[] requestTokens = requestLine.split(" ");
            String method = requestTokens[0];
            String path = requestTokens[1]; // "/" or "/index.html"
            log.log(Level.INFO, "Parsed Path: " + path);  // 경로 출력



            // GET 방식으로 회원가입 요청 처리
            if (path.startsWith("/user/signup") && method.equals("GET")) { //서버 경로로 설정해야함
                Map<String, String> queryParams = parseGETQueryParams(path);

                // 사용자 정보 저장
                String userId = queryParams.get("userId");
                String password = queryParams.get("password");
                String name = queryParams.get("name");
                String email = queryParams.get("email");

                log.log(Level.INFO, "사용자 정보: " + method + " " + path + " " + userId);

                if (userId != null && name != null && password != null) {
                    User newUser = new User(userId, password, name, email);
                    MemoryUserRepository.getInstance().addUser(newUser);
                    log.log(Level.INFO, "사용자: " + newUser);
                }

                // 회원가입 후 index.html 반환
                File indexFile = new File(WEB_ROOT + "/index.html");
                byte[] fileContent = Files.readAllBytes(indexFile.toPath());

                response200Header(dos, fileContent.length);
                responseBody(dos, fileContent);
            }

            //POST 방식으로 로그인
            if (method.equals("POST") && path.startsWith("/user/signup")) {
                log.log(Level.INFO, "경로: " + path);  // 경로 출력
                Map<String, String> postData = getPostData(br);

                String userId = postData.get("userId");
                log.log(Level.INFO, "유저ID: " + userId);

                // 302 리다이렉션
                sendRedirect(dos, "/index.html");

                // 회원가입 후 index.html 반환
//                File indexFile = new File(WEB_ROOT + "/index.html");
//                byte[] fileContent = Files.readAllBytes(indexFile.toPath());

//                response200Header(dos, fileContent.length);
//                responseBody(dos, fileContent);

            }

            // 루드 경로 시 "/index.html"로 처리
            if (path.equals("/")) {
                path = "/index.html";
            }

             //파일 경로 설정
            File file = new File(WEB_ROOT + path);
            byte[] fileContent = Files.readAllBytes(file.toPath());

            response200Header(dos, fileContent.length);
            responseBody(dos, fileContent);

        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }

    // URL 쿼리스트링 파싱 메서드
    private Map<String, String> parseGETQueryParams(String path) throws UnsupportedEncodingException {
        Map<String, String> queryParams = new HashMap<>();
        if (path.contains("?")) {
            String[] parts = path.split("\\?");
            String queryString = parts[1];
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }

    // 쿼리스트링 형식의 본문 데이터를 파싱하는 메소드
    private Map<String, String> parsePOSTQueryParams(String queryString) throws UnsupportedEncodingException {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = queryString.split("&");  // &로 각 key=value 쌍을 분리
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");  // =로 key와 value를 분리
            if (keyValue.length > 1) {  // key=value 형식이 맞을 때만 처리
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                queryParams.put(key, value);  // Map에 key-value 쌍 저장
            }
        }
        return queryParams;
    }


    // POST 데이터 처리
    private Map<String, String> getPostData(BufferedReader br) throws IOException {
        String line;
        int contentLength = 0;

        // Content-Length 헤더를 통해 요청 본문 길이 추출
        while ((line = br.readLine()) != null && !line.isEmpty()) {

            // Content-Length 헤더 찾기
            if (line.startsWith("Content-Length")) {
                String[] contentLengthHeader = line.split(":");
                contentLength = Integer.parseInt(contentLengthHeader[1].trim());
                log.log(Level.INFO, "Content-Length: " + contentLength);
            }
        }

        // Content-Length 만큼 본문 데이터 읽기
        char[] body = new char[contentLength];  // 본문 데이터를 저장할 배열
        int bytesRead = br.read(body, 0, contentLength);  // 본문 읽기
        log.log(Level.INFO, "본문 내용: " + new String(body, 0, bytesRead));

        // POST 데이터 파싱
        return parsePOSTQueryParams(new String(body, 0, bytesRead));
    }

    // POST 요청을 처리한 후, 클라이언트를 /index.html 로 리다이렉트
    private void sendRedirect(DataOutputStream dos, String redirectUrl) throws IOException {
        // 302 상태 코드와 Location 헤더 설정
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + redirectUrl + "\r\n");
        dos.writeBytes("Content-Length: 0\r\n");
        dos.writeBytes("Connection: close\r\n");
        dos.writeBytes("\r\n");
    }



    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

}
