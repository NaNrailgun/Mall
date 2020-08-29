package com.nanrailgun.mall.controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallIndexCarouselVO implements Serializable {

    private String carouselUrl;

    private String redirectUrl;
}
