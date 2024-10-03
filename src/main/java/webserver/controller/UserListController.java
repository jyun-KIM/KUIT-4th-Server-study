package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserListController implements Controller{
    @Override
    public void execute(Request request, Response response) throws IOException {
        Map<String, String> headers = request.getHeaders();
        String cookie = headers.get("logined");

        if (cookie != null) {
            response.redirect("/user/list.html");
        }else{
            response.redirect("/user/login.html");
        }
    }
}
