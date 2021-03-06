package topia.duck.mvc.servlet.web.frontcontroller.v5;

import topia.duck.mvc.servlet.web.frontcontroller.ModelView;
import topia.duck.mvc.servlet.web.frontcontroller.MyView;
import topia.duck.mvc.servlet.web.frontcontroller.v3.ControllerV3;
import topia.duck.mvc.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import topia.duck.mvc.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import topia.duck.mvc.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import topia.duck.mvc.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import topia.duck.mvc.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import topia.duck.mvc.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import topia.duck.mvc.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import topia.duck.mvc.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdpater;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5(){
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdpater());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FrontControllerServletV5.service");

        Object handler = getHandler(req);
        if(handler==null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        ModelView mv = adapter.handle(req, resp, handler);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        try {
            view.render(mv.getModel(), req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MyView viewResolver(String viewName) {
        MyView view = new MyView("/WEB-INF/views/"+ viewName +".jsp");
        return view;
    }

    private Object getHandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        Object handler = handlerMappingMap.get(requestURI);
        return handler;
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler){
        for(MyHandlerAdapter adapter: handlerAdapters){
            if(adapter.supports(handler)){
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter??? ?????? ??? ????????????. handler="+handler);
    }
}
