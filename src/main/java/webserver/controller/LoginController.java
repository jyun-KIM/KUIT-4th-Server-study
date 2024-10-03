package webserver.controller;

import db.MemoryUserRepository;
import model.User;
import webserver.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Controller{
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());
    @Override
    public void execute(Request request, Response response) throws IOException {

        Map<String, String> queryParams = request.getQueryParams();
        String userId = queryParams.get(UserQueryKey.USER_ID.getKey());
        String password = queryParams.get(UserQueryKey.PASSWORD.getKey());

//        String userId = request.getQueryParam(UserQueryKey.USER_ID.getKey());
//        String password = request.getQueryParam(UserQueryKey.PASSWORD.getKey());

//        MemoryUserRepository memoryUserRepository = MemoryUserRepository.getInstance();
        User user = MemoryUserRepository.getInstance().findUserById(userId); //findUser 여기서 user 받기
        log.log(Level.INFO, "여기여기여기여기");  // 경로 출력
        System.out.println(user.getUserId()+"*********");
        log.log(Level.INFO, user.getUserId()+"*****"+user.getPassword());  // 경로 출력

        if(user.getPassword().equals(password)) {
            response.addCookie("logined=true");
            log.log(Level.INFO, "sdfushdfushdkf");  // 경로 출력
            response.redirect("/index.html");
        }else{
            log.log(Level.INFO, "xxxxxxxxxxxxx");  // 경로 출력
            response.redirect("/user/login_failed.html");
        }
    }

}
