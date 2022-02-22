package topia.duck.mvc.servlet.web.frontcontroller.v3;

import topia.duck.mvc.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {
    ModelView process(Map<String, String> paramMap);
}
