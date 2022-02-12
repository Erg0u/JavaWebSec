package run.ergou.javawebsec.controller;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import run.ergou.javawebsec.service.SqlInjectService;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/sqli")
@Validated
public class SqlInject {
    @Resource
    SqlInjectService sqlInjectService;

    @GetMapping("/inject1")
    public String inject1(String name, String passwd) {
        return sqlInjectService.inject1(name, passwd);
    }

    @GetMapping("/inject2")
    public String inject2(String name, String passwd) {
        return sqlInjectService.inject2(name, passwd);
    }

    @GetMapping("/inject3")
    public String inject3(String name, String passwd) {
        return sqlInjectService.inject3(name, passwd);
    }

    @GetMapping("/inject4")
    public String inject4(String name, String passwd) {
        return sqlInjectService.inject4(name, passwd);
    }

    @GetMapping("/inject5")
    public String inject5(String name) {
        return sqlInjectService.inject5(name);
    }

    @GetMapping("/inject6")
    public String inject6(String name) {
        return sqlInjectService.inject6(name);
    }

    @GetMapping("/inject7")
    public String inject7(String name) {
        return sqlInjectService.inject7(name);
    }

    @GetMapping("/inject8")
    public String inject8(@Range(min=1, max=2) Integer name) {
        return sqlInjectService.inject8(name);
    }

    // /sqli/inject9?ids=1,2
    @GetMapping("/inject9")
    public String inject9(String ids) {
        return sqlInjectService.inject9(ids);
    }

    @GetMapping("/inject10")
    public String inject10(@RequestParam("ids") List<Integer> ids) {
        return sqlInjectService.inject10(ids);
    }

    // /sqli/inject11?names='admin','joychou'
    @GetMapping("/inject11")
    public String inject11(String names) {
        System.out.println(names);
        return sqlInjectService.inject11(names);
    }

    // /sqli/inject12?names=admin,joychou
    @GetMapping("/inject12")
    public String inject12(@RequestParam("names") List<String> names) {
        System.out.println(names);
        return sqlInjectService.inject12(names);
    }

    @GetMapping("/inject13")
    public String inject13(@RequestParam("names") List<String> names) {

        // [admin, joychou]  -->  'admin','joychou'
        StringBuilder stringNames = new StringBuilder();
        for (String name : names) {
            stringNames.append("'").append(name).append("',");
        }
        stringNames.deleteCharAt(stringNames.length() - 1);

        System.out.println(stringNames);
        return sqlInjectService.inject11(stringNames.toString());
    }
}
