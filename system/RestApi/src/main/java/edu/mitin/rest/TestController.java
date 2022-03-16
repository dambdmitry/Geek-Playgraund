package edu.mitin.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping(value = "/{id}", produces = "application/json")
    public String getId(@PathVariable int id) {
        return "your id is " + id;
    }
}
