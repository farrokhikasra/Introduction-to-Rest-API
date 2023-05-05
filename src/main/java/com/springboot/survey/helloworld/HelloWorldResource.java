package com.springboot.survey.helloworld;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {

    @RequestMapping("/hello-world")
    public String helloWorld() {
        return "Hello World!";
    }

    @RequestMapping("/hello-world-path-param/{name}")
    public HelloWorldBean helloWorldPathParam(@PathVariable String name) {
        return new HelloWorldBean("Hello World " + name);
    }

    @RequestMapping("/hello-world-path-param/{name}/message/{message}")
    public HelloWorldBean helloWorldMultiplePathParam(@PathVariable String name, @PathVariable String message) {
        return new HelloWorldBean("Hello " + name + ", " + message);
    }
}
