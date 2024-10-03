package webserver.controller;

import webserver.Response;
import webserver.Request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface Controller {
    void execute(Request request, Response response) throws IOException;
}
