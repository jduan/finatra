package com.twitter.hello.server;

import com.twitter.finagle.http.ParamMap;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.finatra.http.ControllerJ;
import com.twitter.finatra.http.MyAction;

class UserAction implements MyAction {
    public Response action(Request request) {
        ParamMap params = request.params();
        Integer id = (Integer) params.getInt("id").get();
        // Retrieve User by id from database
        Response res = new Response.Ok();
        // Render a view
        res.setContentString("hello world, User " + id);

        return res;
    }
}
public class UserController extends ControllerJ {
    public UserController() {
        addAction("/users/:id", new UserAction());
    }
}
