package com.itheima.springboot_ssm_store.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.itheima.springboot_ssm_store.service.OrderService;
import com.itheima.springboot_ssm_store.service.impl.OrderServiceImpl;
import com.itheima.springboot_ssm_store.utils.OrderConst;
import com.itheima.springboot_ssm_store.domain.Order;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;


public class AlipayWebPay{
    public static String app_id ="2016101000654688";

    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC91E4dfuDs9+UPt+t1BiRCaVYWDYAv/B/kz/Y+i/XSFzzkNLQ8pu4qOhZNsv1uJ1mDSklPLfSo19UBczxbjnHCenkT98WiLu/qA80LRg/zjzKweS9Iys+sPdXGwPc7kjh3Sz5+znsHU0m6PlonYjzWBatreqKSiDSwd0vWXxDpfnq0xH2tAFhRUzP8uFeERJagkVWApcNIjHEeTr4l/YhQx6165d4YaH0x4SBpPcjC9+R5imgF2/06IeHhkhfO1fN4Tg3Pj+Baf8xk77pCfHmUEXYuUJ2SDvmze+WgB2hvoVu8ZmPq9OtRmX8D4syHNssPAJmG9EOBgpdseQV5MR4FAgMBAAECggEAJ/yJu+LXJHOPd8EpQwxZJPglXx3W68SzPzKjT0eSL4Afnn6PnNsY/iIgQpu6kLKPQ32pdZlbmRll0BljFHIr+mhsCs29fs1eUjAcyAwpHggDCHNuh2nZVxHrbyOEsLK7XD2oW3PJLKDZvfaWbQdfg0HMeyZpCm7Qkn9+pRh/an+RrGvBnIfpMe5+dVAZmQX+aBRKX6I1la5Jyxyc/u46pZyYFU0s+rYe+6jL4d+VCptKxUD+DtaVhnrOAIhaib0JEkShJfSwbFqWLkNjYduivOBnH1u8Hgj27vUupqEleuGcS0TJpklRTh9Enh7i2eMOx65vqCPgVIJEfdj1O3UugQKBgQDzSOH6v/j25UDKMI5DCwAa3XNuJqf/WTaIaUAzmHnWmhrfoiW2XuC0yP6m9k6L3lpG35f7O6rljFWsdnGVnHHaI7+FzZe4iHeleAjVNUYUVBEiGRq1ZDmWdfIM1V/keFEZukRJVARknIdhe+91P1Rikl9oZ18DSx3KK0qw+gHrIQKBgQDHwDJJgiGpmS09PWb66vMo9jrOME/kiYzFnVTbrDgZHi7lXMd00HLtL8EySH9KDkcjT+L41lhALYXOgvMOUEq3OhTbOOsTMt5TTdImi2K0YuYDt7If2jeUKTGMK9jlag/oKjSOgb6OxAsvSfxV1qzI+ePxrHOyLUJPVrPFcM4aZQKBgDEE7WuNxMdSmARlVjTSAZpoJnr6VP1RZ3iC5InA3kPMVz/aNwkRAuvKV+0iUd9C1SPUp5XahuurpBOU5fwEQrh2XX/DVh+7d4gisHztOAqeibk6H7SO1j+n0jadw62QRJ8PdfTkdeIj2+9bfns8YG9gGNM7iNxsBTuYD3urVDmhAoGAI/+0UGEedfVAHPBK9d+P8QrnMC+uxk4vy5F+MJTKC2TQKM/etmO2wp4/oDNGlW8bTPbhvdzLTM2ndr9M1ICN5pWWGqgYXX5maPb2b7HhGQdV0sexV2I5b/84ib2BOl8x/LJWhHRHhm04Ys5fpoyo2ROIDA77TtovQCMtBOE+k60CgYEAjHf/41vfoasRA9eAIrGOczW9I+jbU2xJZpHg1HzlFEU8cT1Ye/mGa2L7KMHy5pfdy9RPQwv0teBe4CaHjcOkOu2Pa2//7bE+5TjBFPhmq5EgiAS0wkqJcM6KfcPduYjJg/7bHjU9X/2esbicAhmC7a7wm/XG6u8oXOiXnmer7OY=";

    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvdROHX7g7PflD7frdQYkQmlWFg2AL/wf5M/2Pov10hc85DS0PKbuKjoWTbL9bidZg0pJTy30qNfVAXM8W45xwnp5E/fFoi7v6gPNC0YP848ysHkvSMrPrD3VxsD3O5I4d0s+fs57B1NJuj5aJ2I81gWra3qikog0sHdL1l8Q6X56tMR9rQBYUVMz/LhXhESWoJFVgKXDSIxxHk6+Jf2IUMeteuXeGGh9MeEgaT3IwvfkeYpoBdv9OiHh4ZIXztXzeE4Nz4/gWn/MZO+6Qnx5lBF2LlCdkg75s3vloAdob6FbvGZj6vTrUZl/A+LMhzbLDwCZhvRDgYKXbHkFeTEeBQIDAQAB";

    public static String notify_url = "http://api.itheimashop.com:8080/alipay/web/notify";

    public static String return_url = "http://api.itheimashop.com:8080/alipay/web/return";

    public static String sign_type = "RSA2";

    public static String charset = "utf-8";

    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    static {
        ResourceBundle resource =  ResourceBundle.getBundle("alipay");
        app_id = resource.getString("app_id");
        merchant_private_key = resource.getString("merchant_private_key");
        alipay_public_key = resource.getString("alipay_public_key");
        notify_url = resource.getString("notify_url");
        return_url = resource.getString("return_url");
        sign_type = resource.getString("sign_type");
        charset = resource.getString("charset");
        gatewayUrl = resource.getString("gatewayUrl");
    }

    /**
     * 获取alipay网站支付的页面Body
     * @return
     * @throws Exception
     */
    public  static String getWebPayBody(Order order) throws Exception {

        // 获得初始化的AlipayClient
		// 向阿里支付接口发送任何请求（支付请求、查询交易状态请求）时必须构建的对象。
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id, merchant_private_key, "json", charset, alipay_public_key, sign_type);

        // 设置请求参数
		// 发送支付请求时，必须构建的对象，该对象会包含商户订单ID、订单金额、标题及内容。
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        // 设置交易数据
        JSONObject jsonData = new JSONObject();
        jsonData.put("out_trade_no",order.getOid());
        jsonData.put("product_code","FAST_INSTANT_TRADE_PAY");
        jsonData.put("total_amount",order.getTotal());
        jsonData.put("subject","黑马商城");
        jsonData.put("body","黑马商城商品");
        alipayRequest.setBizContent(jsonData.toString());
        // 发送请求
        String  result= alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println(result);
        return result;

    }
    /**
     * 数据验证
     * @param requestParams
     * @return
     */
    public static boolean signVerified(Map<String,String[]> requestParams){
        boolean isSignVerified = false;
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            isSignVerified = AlipaySignature.rsaCheckV1(params, alipay_public_key, charset, sign_type); //调用SDK验证签名
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return isSignVerified;
    }

    /**
     * 查询交易状态 （商户交易ID或支付宝交易ID，两者选一）
     * @param out_trade_no 商户交易ID
     * @param trade_no 支付宝交易ID
     */
    public static void queryTrade(String out_trade_no,String trade_no) {

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, app_id, merchant_private_key, "json", charset, alipay_public_key, sign_type);
        // 构建查询订单状态的请求对象
		// 发送查询交易状态请求时，必须构建的对象，需要提供商户订单ID
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
        //设置请求参数
        JSONObject jsonData = new JSONObject();
        jsonData.put("out_trade_no",out_trade_no);
        jsonData.put("trade_no",trade_no);
        alipayRequest.setBizContent(jsonData.toString());
        try{
            //发起请求
            String result = alipayClient.execute(alipayRequest).getBody();
            System.out.println(result);
            JSONObject jsonResult = JSONObject.fromObject(result);
            JSONObject queryResult = jsonResult.getJSONObject("alipay_trade_query_response");
            if(queryResult.getString("code").equals("10000")){
                // 付款已成功
                OrderService orderService = new OrderServiceImpl();
                if( orderService.updateOrdersStateById(out_trade_no, OrderConst.PAID)){
                    System.out.println("状态更新成功！");
                }else{
                    System.out.println("状态更新失败！");
                }
            }
            System.out.println(result);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
