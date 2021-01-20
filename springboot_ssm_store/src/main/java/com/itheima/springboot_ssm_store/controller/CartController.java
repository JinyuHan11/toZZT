package com.itheima.springboot_ssm_store.controller;

import com.itheima.springboot_ssm_store.domain.Cart;
import com.itheima.springboot_ssm_store.domain.CartItem;
import com.itheima.springboot_ssm_store.domain.Order;
import com.itheima.springboot_ssm_store.domain.Product;
import com.itheima.springboot_ssm_store.service.OrderService;
import com.itheima.springboot_ssm_store.service.ProductService;
import com.itheima.springboot_ssm_store.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CartController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/cart")    //pid 商品id；count:加入购物车的数量
    public Result cart(String method, String pid, String count, HttpServletRequest request,String oid){
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if("addCart".equals(method)){
            //把商品，购买数量等信息加入购物车
            //通过session取cart
            if(cart == null){
                //如果session里没有cart对应的对象，创建cart对象并放入session
                cart = new Cart();
                request.getSession().setAttribute("cart",cart);
            }
            CartItem cartItem = new CartItem();
            //通过pid查询商品
            Product product = productService.getProductById(pid);
            //放入购物项中
            cartItem.setProduct(product);
            cartItem.setCount(Integer.parseInt(count));
            //此时，购物项已经完成
            cart.addCart(cartItem);
            return new Result(Result.SUCCESS,"添加购物车成功");
        }else if("showCart".equals(method)){
            //展示购物车时，才把数据传输
            if (cart != null){
                return new Result(Result.SUCCESS,"展示购物车",cart);
            }
        }else if("clearCart".equals(method)){
            //清空购物车
            cart.clearCart();
            return new Result(Result.SUCCESS,"清空购物车成功");
        }else if ("removeCart".equals(method)){
            cart.removeCart(pid);
            return new Result(Result.SUCCESS,"删除购物项成功");
        }else if("getOrderById".equals(method)){
            Order order = orderService.getOrdersById(oid);
            return new Result(Result.SUCCESS,"查询订单详情成功",order);
        }

        return  null;
    }
}
