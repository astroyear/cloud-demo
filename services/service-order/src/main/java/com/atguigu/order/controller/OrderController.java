package com.atguigu.order.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.order.bean.Order;
import com.atguigu.order.properties.OrderProperties;
import com.atguigu.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

    @GetMapping("/seckill")
    @SentinelResource(value = "seckill-order",fallback = "seckillFallback")
    public Order seckill(@RequestParam(value = "userId",required = false) Long userId,
                             @RequestParam(value = "productId", defaultValue = "1000") Long productId){
        Order order = orderService.createOrder(productId, userId);
        order.setId(Long.MAX_VALUE);
        return order;
    }

    public Order seckillFallback(Long userId, Long productId, Throwable exception){
        System.out.println("seckillFallback...");
        Order order = new Order();
        order.setId(productId);
        order.setUserId(userId);
        order.setAddress("异常信息：" + exception.getClass());
        return order;
    }

    @GetMapping("/writeDb")
    public String writeDb(){
        return "writeDb success ...";
    }

    @GetMapping("/readDb")
    public String readDb(){
        log.info("readDb success");
        return "readDb success ...";
    }
}
