package com.nanrailgun.mall_gateway.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.nanrailgun.config.common.Constants;
import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.config.annotation.MallToken;
import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.config.utils.Result;
import com.nanrailgun.config.utils.ResultGenerator;
import com.nanrailgun.mall_gateway.config.AlipayConfig;
import com.nanrailgun.mall_gateway.controller.param.MallOrderSaveParam;
import com.nanrailgun.order_api.api.MallOrderService;
import com.nanrailgun.order_api.api.dto.MallOrderDetailDTO;
import com.nanrailgun.user_api.api.MallUserAddressService;
import com.nanrailgun.user_api.entity.MallUser;
import com.nanrailgun.user_api.entity.MallUserAddress;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class MallOrderController {

    @Reference
    private MallOrderService mallOrderService;

    @Reference
    private MallUserAddressService mallUserAddressService;

    @GetMapping("/order")
    public Result getOrderList(@RequestParam(required = false) Integer pageNumber,
                               @RequestParam(required = false) Integer status,
                               @MallToken MallUser loginMallUser) {
        Map params = new HashMap();
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUser.getUserId());
        params.put("orderStatus", status);
        params.put("page", pageNumber);
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        PageQueryUtil util = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallOrderService.getMyOrders(util, util.getPage(), util.getLimit()));
    }

    @GetMapping("/order/{orderNo}")
    public Result getOrderDetail(@PathVariable("orderNo") String orderNo, @MallToken MallUser user) {
        return ResultGenerator.genSuccessResult(mallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId()));
    }

    @PutMapping("/order/{orderNo}/cancel")
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, @MallToken MallUser user) {
        String result = mallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @PutMapping("/order/{orderNo}/finish")
    public Result finishOrder(@PathVariable("orderNo") String orderNo, @MallToken MallUser user) {
        String result = mallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(result);
    }

    @PostMapping("/saveOrder")
    public Result saveOrder(@RequestBody MallOrderSaveParam param, @MallToken MallUser user) {
        if (param == null || param.getAddressId() == null || param.getCartItemIds() == null || param.getCartItemIds().length < 1) {
            return ResultGenerator.genFailResult(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        MallUserAddress address = mallUserAddressService.getAddressByAddressId(param.getAddressId());
        if (!address.getUserId().equals(user.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<Long> cartItemIds = Arrays.asList(param.getCartItemIds());
        String orderNo = mallOrderService.saveOrder(user, cartItemIds, address);
        return ResultGenerator.genSuccessResult((Object) orderNo);
    }

    @GetMapping("/pay")
    public void mockPaySuccess(@RequestParam("orderNo") String orderNo,
                               @RequestParam("payType") int payType,
                               @MallToken MallUser user,
                               HttpServletResponse response) {
        if (payType == 2) payType = 1;
        String result = mallOrderService.pay(orderNo, payType);
        MallOrderDetailDTO mallOrder = mallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            //返回支付宝表单
            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

            // 封装请求支付信息
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(orderNo);
            model.setSubject(mallOrder.getOrderNo());
            model.setTotalAmount(String.valueOf(mallOrder.getTotalPrice()));
            model.setBody("订单");
            model.setProductCode("QUICK_WAP_WAY");
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            //alipay_request.setNotifyUrl(AlipayConfig.notify_url);
            // 设置同步地址
            alipay_request.setReturnUrl(AlipayConfig.return_url);

            // form表单生产
            String form;
            try {
                // 调用SDK生成表单
                form = client.pageExecute(alipay_request).getBody();
                response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
                response.getWriter().write(form);//直接将完整的表单html输出到页面
                response.getWriter().flush();
                response.getWriter().close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/payValidation")
    public void payValidation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");

        if (verify_result) {//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            //该页面可做页面美工编辑
            mallOrderService.paySuccess(out_trade_no);
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            //////////////////////////////////////////////////////////////////////////////////////////
        }
        response.sendRedirect("http://10.203.109.200:8080/#/order");
    }
}
