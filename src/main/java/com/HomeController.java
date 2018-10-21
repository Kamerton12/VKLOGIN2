package com;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {

    @RequestMapping(value = "/force")
    @ResponseBody
    public String greeting() throws ClientException, ApiException, IOException, URISyntaxException, InterruptedException
    {
        Main m = new Main();
        m.force();
        return "Hello World";
    }

    @RequestMapping(value = "/awake")
    @ResponseBody
    public String upd() throws ClientException, ApiException, IOException, URISyntaxException, InterruptedException
    {
        Main m = new Main();
        m.force();
        return "Hello World";
    }

    @RequestMapping(value = "/")
    @ResponseBody
    public String greetings() {
        return "Hello World 2";

    }

}