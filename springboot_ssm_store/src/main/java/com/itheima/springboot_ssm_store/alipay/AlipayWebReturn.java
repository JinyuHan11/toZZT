package com.itheima.springboot_ssm_store.alipay;


import com.itheima.springboot_ssm_store.service.OrderService;
import com.itheima.springboot_ssm_store.utils.OrderConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class AlipayWebReturn  {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/alipay/web/return")
    protected void alipyreturn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(AlipayWebPay.signVerified(request.getParameterMap())) {
            //商户订单号
            String out_trade_no = request.getParameter("out_trade_no");
            //订单状态,修改为已经付款,正式上线，改为在AlipayWebNotify中完成
            if(orderService.updateOrdersStateById(out_trade_no, OrderConst.PAID)){
                System.out.println("状态更新成功！");
            }else{
                System.out.println("状态更新失败！");
            }
            System.out.println("AlipayWebReturn 成功");
            request.setAttribute("oid",out_trade_no);
            request.getRequestDispatcher("/success.jsp").forward(request,response);
        }else {
            response.getWriter().println("验签失败");
            System.out.println("AlipayWebReturn 失败");
        }
    }
}
