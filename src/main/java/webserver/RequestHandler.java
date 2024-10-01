package webserver;

import db.MemoryUserRepository;
import model.User;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
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


            // 회원가입 요청 처리
            if (path.startsWith("/user/signup")) { //서버 경로로 설정해야함.
                Map<String, String> queryParams = getQueryParams(path);

                // 사용자 정보를 메모리 리포지토리에 저장
                String userId = queryParams.get("userId");
                String password = queryParams.get("password");
                String name = queryParams.get("name");
                String email = queryParams.get("email");

                log.log(Level.INFO, "New Client Request: " + method + " " + path + " " + userId);

                if (userId != null && name != null && password != null) {
                    User newUser = new User(userId, password, name, email);
                    MemoryUserRepository.getInstance().addUser(newUser);
                    log.log(Level.INFO, "User registered: " + newUser);
                }

                // 회원가입 후 index.html 반환
                File indexFile = new File(WEB_ROOT + "/index.html");
                byte[] fileContent = Files.readAllBytes(indexFile.toPath());

                response200Header(dos, fileContent.length);
                responseBody(dos, fileContent);
            }

            // 루드 경로 시 "/index.html"로 처리
            if (path.equals("/")) {
                path = "/index.html";
            }

            // 파일 경로 설정
            File file = new File(WEB_ROOT + path);
            byte[] fileContent = Files.readAllBytes(file.toPath());

            response200Header(dos, fileContent.length);
            responseBody(dos, fileContent);

        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }

    // URL 쿼리스트링 파싱 메서드
    private Map<String, String> getQueryParams(String path) throws UnsupportedEncodingException {
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
