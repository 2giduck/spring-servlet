package topia.duck.mvc.servlet.web.frontcontroller.v4;

import topia.duck.mvc.servlet.web.frontcontroller.MyView;
import topia.duck.mvc.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import topia.duck.mvc.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import topia.duck.mvc.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@WebServlet(name="frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4(){
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("FrontControllerServletV4.service");

        String requestURI = req.getRequestURI();

        ControllerV4 controller = controllerMap.get(requestURI);
        if(controller==null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paramMap
        Map<String, String> paramMap = createParamMap(req);
        Map<String, Object> model = new HashMap<>();

        String viewName = controller.process(paramMap, model);
        MyView view = viewResolver(viewName);

        try {
            view.render(model, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<>();
        req.getParameterNames().asIterator()
                .forEachRemaining(paramName->paramMap.put(paramName, req.getParameter(paramName)));
        return paramMap;
    }

    private MyView viewResolver(String viewName) {
        MyView view = new MyView("/WEB-INF/views/"+ viewName +".jsp");
        return view;
    }
}
