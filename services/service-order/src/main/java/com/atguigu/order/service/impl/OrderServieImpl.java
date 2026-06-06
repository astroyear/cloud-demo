package com.atguigu.order.service.impl;

import com.atguigu.order.bean.Order;
import com.atguigu.order.feign.ProductFeignClient;
import com.atguigu.order.service.OrderService;
import com.atguigu.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OrderServieImpl implements OrderService {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    ProductFeignClient productFeignClient;

    @Override
    public Order createOrder(Long productId, Long userId) {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(userId);
        order.setNickName("zhangsan");
        order.setAddress("home");

//        Product product = getProductFromRemoteWithLoaderBalancerAnnotation(productId);  //硬编码，用RestTemplate完成远程调用
        Product product = productFeignClient.getProductById(productId);

        order.setProductList(Arrays.asList(product));
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        return order;
    }

    private Product getProductFromRemoteWithLoaderBalancerAnnotation(Long productId){
//        ServiceInstance choose = loadBalancerClient.choose("service-product");
//        String url = "http://" + choose.getHost()+":" + choose.getPort() + "/product/"+productId;
//        log.info("远程请求路径：{}"+url);

        String url = "http://service-product/product/" + productId;
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }

    private Product getProductFromRemoteWithLoaderBalancer(Long productId){
        ServiceInstance choose = loadBalancerClient.choose("service-product");
        String url = "http://" + choose.getHost()+":" + choose.getPort() + "/product/"+productId;
        log.info("远程请求路径：{}"+url);
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }

    private Product getProductFromRemote(Long productId){
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance instance = instances.get(0);
        String url = "http://" + instance.getHost()+":" + instance.getPort() + "/product/"+productId;
        log.info("远程请求路径：{}"+url);
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }


}
