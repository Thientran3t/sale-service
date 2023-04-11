package com.vnet.solution.common.controller;

import com.vnet.solution.common.dto.SalesData;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface ISaleController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<SalesData> aggregatingData(List<SalesData> payload);

}
