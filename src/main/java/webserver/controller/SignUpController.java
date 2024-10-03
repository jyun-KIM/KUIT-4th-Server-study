package webserver.controller;

import db.MemoryUserRepository;
import model.User;
import webserver.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SignUpController implements Controller {
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());
    private final MemoryUserRepository memoryUserRepository = MemoryUserRepository.getInstance();

    @Override
    public void execute(Request request, Response response) throws IOException {


//        String path = request.getPath();
        String method = request.getMethod();
        Map<String, String> queryParams = new HashMap<>();

        if (method.equals(HttpMethod.GET.toString())) {
            queryParams = request.getQueryParams();
        }else if(method.equals(HttpMethod.POST.toString())){
            queryParams = request.getBodyParams();
        }

        // 사용자 정보 저장
        String userId = queryParams.get(UserQueryKey.USER_ID.getKey());
        String password = queryParams.get(UserQueryKey.PASSWORD.getKey());
        String name = queryParams.get(UserQueryKey.NAME.getKey());
        String email = queryParams.get(UserQueryKey.EMAIL.getKey());

//        log.log(Level.INFO, "dfdfdfdfdfdf" + email);  // 경로 출력

        if (userId != null && name != null && password != null) {
            User newUser = new User(userId, password, name, email);
            memoryUserRepository.addUser(newUser);
            System.out.println(newUser.getUserId()+ "df11111111111");
        }

//        log.log(Level.INFO, "dㄴㄴㄴㄴfdfdfdf" + email);  // 경로 출력
        response.redirect("/index.html");
    }

}
