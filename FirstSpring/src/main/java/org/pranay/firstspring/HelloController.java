package org.pranay.firstspring;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public HelloResponse hello(){
        return new HelloResponse("Hello world");
    }

    @GetMapping("/hello/{name}")
    public HelloResponse helloPram(@PathVariable String name){
        return new HelloResponse("Hello "+name);
    }

    @PostMapping("/helloo")
    public HelloResponse helloPost(@RequestBody String name){
        return new HelloResponse("Hello yo yo nigga " +name+" !");
    }
}
