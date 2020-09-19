package com.nanrailgun.goods_api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallIndexCarouselDTO implements Serializable {

    private String carouselUrl;

    private String redirectUrl;
}
