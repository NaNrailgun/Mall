package com.nanrailgun.order_api.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MallOrderDetailDTO implements Serializable {
    private String orderNo;

    private Integer totalPrice;

    private Byte payStatus;

    private Byte payType;

    private String payTypeString;

    private Date payTime;

    private Byte orderStatus;

    private String orderStatusString;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private List<MallOrderItemDTO> mallOrderItemVOS;
}
