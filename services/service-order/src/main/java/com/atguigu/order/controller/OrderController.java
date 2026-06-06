package com.atguigu.order.controller;


import com.atguigu.order.bean.Order;
import com.atguigu.order.properties.OrderProperties;
import com.atguigu.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RefreshScope   #自动刷新
@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

//    @Value("${order.timeout}")
//    String orderTimeOut;
//    @Value("${order.auto-confirm}")
//    String orderAutoComfirm;

    @Autowired
    OrderProperties orderProperties;

    @GetMapping("/config")
    public String config(){
        return "order.timeout=" + orderProperties.getTimeOut()
                + "; order.auto-confirm="+orderProperties.getAutoConfirm()
                + "; order.db-url="+orderProperties.getDbUrl();
    }

    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId,
                             @RequestParam("productId") Long productId){
        Order order = orderService.createOrder(productId, userId);
        return order;
    }
}
