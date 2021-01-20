package com.itheima.springboot_ssm_store.alipay;

import com.itheima.springboot_ssm_store.service.OrderService;
import com.itheima.springboot_ssm_store.utils.OrderConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class AlipayWebNotify{

    @Autowired
    private OrderService orderService;

    @RequestMapping("/alipay/web/notify")
    protected void alipynotify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(AlipayWebPay.signVerified(request.getParameterMap())) {//验证成功
            //商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            // 发送给支付宝
            response.getWriter().println("success");
            System.out.println("notify_success");

            //订单状态,修改为已经付款
            if(orderService.updateOrdersStateById(out_trade_no, OrderConst.PAID)){
                System.out.println("状态更新成功！");
            }else{
                System.out.println("状态更新失败！");
            }
        }else {//验证失败
            response.getWriter().println("fail");
            System.out.println("notify_fail");
        }
    }
}
