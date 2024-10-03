package webserver;

import webserver.controller.*;
import java.io.*;
import java.net.Socket;
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

            // GET 방식으로 회원가입 요청 처리
            if (path.startsWith("/user/signup")) { //서버 경로로 설정해야함
                Controller controller = new SignUpController();
                controller.execute(request, response);
            }


            if (method.equals(HttpMethod.POST.name()) && path.startsWith("/user/login")) {
                Controller controller = new LoginController();
                controller.execute(request, response);
            }


            // cookie 확인 후 userList 띄우기
            if (method.equals(HttpMethod.GET.name()) && path.startsWith("/user/userList")) {
                Controller controller = new UserListController();
                controller.execute(request, response);
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

}
