package webserver;

import db.MemoryUserRepository;
import model.User;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler implements Runnable{
    Socket connection;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());
    public RequestHandler(Socket connection) {
        this.connection = connection;
    } // 클라이언트와 연결된 소켓 객체 받음


    // GET, POST 요청인지 확인해서 각각의 핸들 메서드 호출
    @Override
    public void run() {
        //log.log(Level.INFO, "New Client Connect! Connected IP : " + connection.getInetAddress() + ", Port : " + connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            Request request = Request.from(br);
            Response response = new Response(dos);

            // 요청된 파일을 결정
            String method = request.getMethod();
            String path = request.getPath();

            log.log(Level.INFO, "Parsed Path: " + path);  // 경로 출력



            // GET 방식으로 회원가입 요청 처리
            if (method.equals(HttpMethod.GET.name()) && path.startsWith("/user/signup")) { //서버 경로로 설정해야함
                Map<String, String> queryParams = parseGETQueryParams(path);

                // 사용자 정보 저장
                String userId = queryParams.get(UserQueryKey.USER_ID.getKey());
                String password = queryParams.get(UserQueryKey.PASSWORD.getKey());
                String name = queryParams.get(UserQueryKey.NAME.getKey());
                String email = queryParams.get(UserQueryKey.EMAIL.getKey());

                log.log(Level.INFO, "사용자 정보: " + method + " " + path + " " + userId);

                if (userId != null && name != null && password != null) {
                    User newUser = new User(userId, password, name, email);
                    MemoryUserRepository.getInstance().addUser(newUser);
                    log.log(Level.INFO, "사용자: " + newUser);
                }

                // 회원가입 후 index.html 반환
                // responseBody 안에서 한번에 처리
//                File indexFile = new File(WEB_ROOT + "/index.html");
//                byte[] fileContent = Files.readAllBytes(indexFile.toPath());

                response.forward(path);
            }

            //POST 방식으로 회원가입
            if (method.equals(HttpMethod.POST.name()) && path.startsWith("/user/signup")) {
                log.log(Level.INFO, "경로: " + path);  // 경로 출력
                Map<String, String> postData = getPostData(br);

                String userId = postData.get(UserQueryKey.USER_ID.getKey());
                String password = postData.get(UserQueryKey.PASSWORD.getKey());
                String name = postData.get(UserQueryKey.NAME.getKey());
                String email = postData.get(UserQueryKey.EMAIL.getKey());

                User user = new User(userId, password, name, email);
                MemoryUserRepository memoryUserRepository = MemoryUserRepository.getInstance();
                memoryUserRepository.addUser(user);

                log.log(Level.INFO, "유저ID: " + userId);

                // 302 리다이렉션
                response.redirect("/index.html");

            }

            if (method.equals(HttpMethod.POST.name()) && path.startsWith("/user/login")) {
                Map<String, String> postData = getPostData(br);

                String userId = postData.get(UserQueryKey.USER_ID.getKey());
                String password = postData.get(UserQueryKey.PASSWORD.getKey());

                log.log(Level.INFO, "유저ID: " + userId);
                log.log(Level.INFO, "유저password: " + password);


                MemoryUserRepository memoryUserRepository = MemoryUserRepository.getInstance();
                User user = memoryUserRepository.findUserById(userId); //findUser 여기서 user 받기

                if(user.getPassword().equals(password)) {
                    log.log(Level.INFO, "로그인 성공!");
                    addCookie(dos, "logined=true", "/index.html");
                }else{
                    log.log(Level.INFO, "로그인 살패");
                    response.redirect("/user/login_failed.html");
                }
            }


            // cookie 확인 후 userList 띄우기
            if (method.equals(HttpMethod.GET.name()) && path.startsWith("/user/userList")) {
                //Cookie[] cookies = request.getCookies();

                response.redirect("/user/list.html");
            }

            // 루드 경로 시 "/index.html"로 처리
            if (path.equals("/")) {
                path = "/index.html";
                response.redirect("/index.html");
            }

            response.forward(path);

        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
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


    // GET 파싱메서드
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

    // POST 파싱메서드
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

    private void addCookie(DataOutputStream dos, String cookie, String redirectUrl) throws IOException {
        dos.writeBytes( HttpHeader.HTTP_302.getHeaderValue() + "\r\n");
        dos.writeBytes(HttpHeader.COOKIE.getHeaderValue() +": "+ cookie + "\r\n");
        dos.writeBytes(HttpHeader.LOCATION.getHeaderValue() + ": " + redirectUrl + "\r\n");
        dos.writeBytes(HttpHeader.CONTENT_LENGTH.getHeaderValue() + ": 0\r\n");
        dos.writeBytes("Connection: close\r\n");
        dos.writeBytes("\r\n");
    }


}