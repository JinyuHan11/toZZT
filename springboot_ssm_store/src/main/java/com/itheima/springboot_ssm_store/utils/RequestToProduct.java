package com.itheima.springboot_ssm_store.utils;

import com.itheima.springboot_ssm_store.domain.Product;

import javax.servlet.http.HttpServletRequest;

public class RequestToProduct {

    public static Product requestToProduct(HttpServletRequest request,String completeFileName,String pid,
                                           String pdate){
        Product product = new Product();
        product.setPimage(completeFileName);
        product.setPid(pid);
        product.setPflag(0);
        product.setPdate(pdate);
        product.setCid(request.getParameter("cid"));
        product.setIs_hot(Integer.parseInt(request.getParameter("is_hot")));
        product.setMarket_price(Double.parseDouble(request.getParameter("market_price")));
        product.setPdesc(request.getParameter("pdesc"));
        product.setPname(request.getParameter("pname"));
        product.setShop_price(Double.parseDouble(request.getParameter("shop_price")));

        return product;
    }
}
