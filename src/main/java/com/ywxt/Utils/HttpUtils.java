package com.ywxt.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    /**
     * 获取param串
     *
     * @param paramMap
     * @return
     */
    public static String getParamContext(HashMap<String, String> paramMap) {
        String context = "";
        for (Map.Entry<String, String> e : paramMap.entrySet()) {
            context = context.concat(e.getKey() + "=" + e.getValue() + "&");
        }
        return context.substring(0, context.length() - 1);
    }

    private static final int SUCCESS_CODE = 200;

    /**
     * 发送带基本验证的GET请求
     *
     * @param url               请求url
     * @param nameValuePairList 请求参数
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String sendHttpGetWithBasicAuth(String username, String password, String url, List<NameValuePair> nameValuePairList, Map<String, String> paramMap) throws Exception {
        paramMap.put("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((username + ":" + password).getBytes()));
        return sendHttpGet(url, nameValuePairList, paramMap);
    }

    /**
     * 发送GET请求
     *
     * @param url               请求url
     * @param nameValuePairList 请求参数
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String sendHttpGet(String url, List<NameValuePair> nameValuePairList, Map<String, String> paramMap) throws Exception {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            // 创建HttpClient对象
            client = HttpClients.createDefault();
            // 创建URIBuilder
            URIBuilder uriBuilder = new URIBuilder(url);
            // 设置参数
            uriBuilder.addParameters(nameValuePairList);
            // 创建HttpGet
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            // 设置请求头部编码
            httpGet.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            // 设置返回编码
            // httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            // 请求头
            if (paramMap.size() > 0) {
                for (Map.Entry<String, String> e : paramMap.entrySet()) {
                    httpGet.setHeader(new BasicHeader(e.getKey(), e.getValue()));
                }
            }
            // 请求服务
            response = client.execute(httpGet);
            // 获取响应吗
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                // 获取返回对象
                HttpEntity entity = response.getEntity();
                // 通过EntityUitls获取返回内容
                return EntityUtils.toString(entity, "UTF-8");
            } else {
                throw new Exception("GET请求失败");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            response.close();
            client.close();
        }
    }


    /**
     * 发送POST请求
     *
     * @param url
     * @param nameValuePairList
     * @return JSON或者字符串
     * @throws Exception
     */
    public static Object sendHttpPost(String url, List<NameValuePair> nameValuePairList) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            // 创建一个httpclient对象
            client = HttpClients.createDefault();
            // 创建一个post对象
            HttpPost post = new HttpPost(url);
            // 包装成一个Entity对象
            StringEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
            // 设置请求的内容
            post.setEntity(entity);
            // 设置请求的报文头部的编码
            post.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            // 设置请求的报文头部的编码
            post.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            // 执行post请求
            response = client.execute(post);
            // 获取响应码
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                // 通过EntityUitls获取返回内容
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                // 转换成json,根据合法性返回json或者字符串
                jsonObject = JSONObject.parseObject(result);
                return jsonObject;
            } else {
                throw new Exception("POST请求失败");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            response.close();
            client.close();
        }
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendConnGet(String url, String param, Map<String, String> paramMap) throws Exception {
        String result = "";
        BufferedReader in = null;
        try {
            if (!param.isEmpty()) {
                url = url + "?" + param;
            }
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (paramMap.size() > 0) {
                for (Map.Entry<String, String> e : paramMap.entrySet()) {
                    connection.setRequestProperty(e.getKey(), e.getValue());
                }
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            //for (String key : map.keySet()) {
            //    System.out.println(key + "--->" + map.get(key));
            //}
            List<String> statusHeader = map.get(null);
            if (statusHeader.get(0).contains("Unauthorized")) {
                throw new IOException("Unauthorized");
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw e;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendConnPost(String url, String param) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流 & 处理乱码
            //out = new PrintWriter(conn.getOutputStream());
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw e;
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
