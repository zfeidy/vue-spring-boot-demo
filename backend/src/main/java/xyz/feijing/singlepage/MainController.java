package xyz.feijing.singlepage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author feijing
 */
@Controller
public class MainController {

    @GetMapping("/")
    public ModelAndView root() {
        return new ModelAndView("forward:/index.html");
    }

    /**
     * 健康检查，系统部署需要 请不要删除！！
     */
    @GetMapping("/checkpreload.htm")
    public @ResponseBody
    String checkPreload() {
        return "success";
    }
}
