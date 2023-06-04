package com.example.springrabbitmqtutorial.springdemo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class OrderEntity implements Serializable {
    private String id;
    private String orderCode;
    private String receiver;
}
