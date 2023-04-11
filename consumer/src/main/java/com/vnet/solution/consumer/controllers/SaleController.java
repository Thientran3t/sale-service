package com.vnet.solution.consumer.controllers;

import com.vnet.solution.common.controller.ISaleController;
import com.vnet.solution.common.dto.SalesData;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SaleController implements ISaleController {


    @Override
    @SendTo("/vnet/aggregating")
    public List<SalesData> aggregatingData(List<SalesData> payload) {
        return payload;
    }
}
