
~~~java
package com.xiaoxiaofeng;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.cango.base.model.HttpReqInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 笑小枫
 */
public class HttpClientUtil {

    private HttpClientUtil() {
        
    }

    protected static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final String ERROR_MSG = "Exception :";


    /**
     * 取得默认连接客户端，默认获取连接，发送，读取为5分钟超时.
     *
     * @return 默认连接客户端
     */
    public static CloseableHttpClient getDefaultHttpClient() {
        return getCloseableHttpClient(false);
    }

    /**
     * 取得连接客户端，默认获取连接，发送，读取为5分钟超时.
     * 跳过ssl证书验证，为全信任
     *
     * @return 默认连接客户端
     */
    public static CloseableHttpClient getSslHttpsClient() {
        return getCloseableHttpClient(true);
    }

    /**
     * 取得连接客户端，默认获取连接，发送，读取为5分钟超时.
     *
     * @param isSsl 是否ssl证书验证，并信任跳过
     * @return 默认连接客户端
     */
    private static CloseableHttpClient getCloseableHttpClient(boolean isSsl) {
        CloseableHttpClient httpClient = null;
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(180000).setConnectTimeout(180000).setSocketTimeout(180000).build();
        try {
            if (isSsl) {
                httpClient = HttpClients.custom().setSSLSocketFactory(createSslConnSocketFactory()).setDefaultRequestConfig(requestConfig).build();
            } else {
                httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
            }

        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        }
        return httpClient;
    }

    /**
     * 发送GET请求
     *
     * @param urlStr 请求url
     * @param param  请求参数
     * @return 请求结果
     */
    public static String sendGet(String urlStr, List<HttpReqInfo> param) {
        return sendGet(getDefaultHttpClient(), urlStr, param);
    }


    /**
     * 发送GET请求(指定客户端)
     *
     * @param httpClient 客户端
     * @param urlStr     请求url
     * @param param      请求参数
     * @return 请求结果
     */
    public static String sendGet(CloseableHttpClient httpClient, String urlStr, List<HttpReqInfo> param) {
        return sendGetContext(httpClient, urlStr, param, null);
    }

    /**
     * 发送GET请求(指定客户端)
     *
     * @param httpClient  客户端
     * @param urlStr      请求url
     * @param param       请求参数
     * @param httpContext 请求http上下文
     * @return 请求结果
     */
    public static String sendGetContext(CloseableHttpClient httpClient, String urlStr, List<HttpReqInfo> param, HttpContext httpContext) {
        String result = "";

        try {
            CloseableHttpResponse httpResponse = sendGetContextRes(httpClient, urlStr, param, httpContext);
            result = consumeResponse(httpResponse);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(ERROR_MSG, e);
            }
        }

        return result;
    }

    /**
     * 发送GET请求(指定客户端)
     *
     * @param httpClient  客户端
     * @param urlStr      请求url
     * @param param       请求参数
     * @param httpContext 请求http上下文
     * @return 请求结果
     * @throws IOException 异常
     */
    public static CloseableHttpResponse sendGetContextRes(CloseableHttpClient httpClient,
                                                          String urlStr,
                                                          List<HttpReqInfo> param,
                                                          HttpContext httpContext) throws IOException {
        urlStr = generateUrlWithParam(urlStr, param);
        HttpGet httpGet = new HttpGet(urlStr);
        return httpClient.execute(httpGet, httpContext);
    }

    /**
     * JSON形式发送POST请求
     *
     * @param urlStr 请求url
     * @param json   json内容
     * @return 请求结果
     */
    public static String sendJsonPost(String urlStr, String json) {
        return sendJsonPostHeader(getDefaultHttpClient(), urlStr, json, null);
    }

    /**
     * JSON形式发送POST请求(指定客户端)
     *
     * @param httpClient 客户端
     * @param urlStr     请求url
     * @param json       json内容
     * @return 请求结果
     */
    public static String sendJsonPost(CloseableHttpClient httpClient, String urlStr, String json) {
        return sendJsonPostHeader(httpClient, urlStr, json, null);
    }

    /**
     * JSON形式发送POST请求(指定客户端、包含请求头)
     *
     * @param httpClient 客户端
     * @param urlStr     请求url
     * @param json       json内容
     * @param header     请求头
     * @return 请求结果
     */
    public static String sendJsonPostHeader(CloseableHttpClient httpClient, String urlStr, String json, List<HttpReqInfo> header) {
        return sendBodyPostHeaderContext(httpClient, urlStr, json, header, null);
    }

    /**
     * 发送POST请求(指定客户端、包含请求头)
     *
     * @param httpClient  客户端
     * @param urlStr      请求url
     * @param body        body内容
     * @param header      请求头
     * @param httpContext 请求http上下文
     * @return 请求结果
     */
    public static String sendBodyPostHeaderContext(CloseableHttpClient httpClient,
                                                   String urlStr,
                                                   String body,
                                                   List<HttpReqInfo> header,
                                                   HttpContext httpContext) {
        String result = "";
        try {
            CloseableHttpResponse httpResponse = sendBodyPostHeaderContextRes(httpClient, urlStr, body, header, httpContext);
            result = consumeResponse(httpResponse);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(ERROR_MSG, e);
            }
        }
        return result;
    }

    /**
     * 发送POST请求(指定客户端、包含请求头)
     *
     * @param httpClient  客户端
     * @param urlStr      请求url
     * @param body        body内容
     * @param header      请求头
     * @param httpContext 请求http上下文
     * @return 请求结果
     * @throws IOException 异常
     */
    public static CloseableHttpResponse sendBodyPostHeaderContextRes(CloseableHttpClient httpClient, String urlStr, String body, List<HttpReqInfo> header, HttpContext httpContext) throws IOException {
        EntityBuilder eb = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(body);
        HttpPost httpPost = new HttpPost(urlStr);
        if (header != null && !header.isEmpty()) {
            for (HttpReqInfo headerInfo : header) {
                if (!headerInfo.isHeader()) {
                    continue;
                }
                httpPost.addHeader(headerInfo.getParam(), headerInfo.getValue());
            }
        }
        httpPost.setEntity(eb.build());
        return httpClient.execute(httpPost, httpContext);
    }

    /**
     * JSON形式发送PUT请求
     *
     * @param urlStr 请求url
     * @param json   json内容
     * @return 请求结果
     */
    public static String sendJsonPut(String urlStr, String json) {
        return sendJsonPutHeader(getDefaultHttpClient(), urlStr, json, null);
    }

    /**
     * JSON形式发送PUT请求(指定客户端)
     *
     * @param httpClient 客户端
     * @param urlStr     请求url
     * @param json       json内容
     * @return 请求结果
     */
    public static String sendJsonPut(CloseableHttpClient httpClient, String urlStr, String json) {
        return sendJsonPutHeader(httpClient, urlStr, json, null);
    }

    /**
     * JSON形式发送PUT请求(指定客户端、包含请求头)
     *
     * @param httpClient 客户端
     * @param urlStr     请求url
     * @param json       json内容
     * @param header     请求头
     * @return 请求结果
     */
    public static String sendJsonPutHeader(CloseableHttpClient httpClient, String urlStr, String json, List<HttpReqInfo> header) {
        return sendBodyPutHeaderContext(httpClient, urlStr, json, header, null);
    }

    /**
     * 发送PUT请求(指定客户端、包含请求头)
     *
     * @param httpClient  客户端
     * @param urlStr      请求url
     * @param body        body内容
     * @param header      请求头
     * @param httpContext 请求http上下文
     * @return 请求结果
     */
    public static String sendBodyPutHeaderContext(CloseableHttpClient httpClient,
                                                  String urlStr,
                                                  String body,
                                                  List<HttpReqInfo> header,
                                                  HttpContext httpContext) {
        String result = "";
        try {
            CloseableHttpResponse httpResponse = sendBodyPutHeaderContextRes(httpClient, urlStr, body, header, httpContext);
            result = consumeResponse(httpResponse);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(ERROR_MSG, e);
            }
        }
        return result;
    }

    /**
     * 发送PUT请求(指定客户端、包含请求头)
     *
     * @param httpClient  客户端
     * @param urlStr      请求url
     * @param body        body内容
     * @param header      请求头
     * @param httpContext 请求http上下文
     * @return 请求结果
     * @throws IOException 异常
     */
    public static CloseableHttpResponse sendBodyPutHeaderContextRes(CloseableHttpClient httpClient,
                                                                    String urlStr,
                                                                    String body, List<HttpReqInfo> header,
                                                                    HttpContext httpContext) throws IOException {
        EntityBuilder eb = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(body);
        HttpPut httpPut = new HttpPut(urlStr);
        if (header != null && !header.isEmpty()) {
            for (HttpReqInfo headerInfo : header) {
                if (!headerInfo.isHeader()) {
                    continue;
                }
                httpPut.addHeader(headerInfo.getParam(), headerInfo.getValue());
            }
        }
        httpPut.setEntity(eb.build());
        return httpClient.execute(httpPut, httpContext);
    }

    /**
     * 发送DELETE请求
     *
     * @param urlStr 请求url
     * @param param  请求参数
     * @return 请求结果
     */
    public static String sendDelete(String urlStr, List<HttpReqInfo> param) {
        return sendDelete(getDefaultHttpClient(), urlStr, param);
    }


    /**
     * 发送GET请求(指定客户端)
     *
     * @param httpClient 客户端
     * @param urlStr     请求url
     * @param param      请求参数
     * @return 请求结果
     */
    public static String sendDelete(CloseableHttpClient httpClient, String urlStr, List<HttpReqInfo> param) {
        return sendDeleteContext(httpClient, urlStr, param, null);
    }

    /**
     * 发送GET请求(指定客户端)
     *
     * @param httpClient  客户端
     * @param urlStr      请求url
     * @param param       请求参数
     * @param httpContext 请求http上下文
     * @return 请求结果
     */
    public static String sendDeleteContext(CloseableHttpClient httpClient, String urlStr, List<HttpReqInfo> param, HttpContext httpContext) {
        String result = "";

        try {
            CloseableHttpResponse httpResponse = sendDeleteContextRes(httpClient, urlStr, param, httpContext);
            result = consumeResponse(httpResponse);
        } catch (Exception e) {
            logger.error(ERROR_MSG, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(ERROR_MSG, e);
            }
        }

        return result;
    }

    /**
     * 发送GET请求(指定客户端)
     *
     * @param httpClient  客户端
     * @param urlStr      请求url
     * @param param       请求参数
     * @param httpContext 请求http上下文
     * @return 请求结果
     * @throws IOException 异常
     */
    public static CloseableHttpResponse sendDeleteContextRes(CloseableHttpClient httpClient,
                                                             String urlStr,
                                                             List<HttpReqInfo> param,
                                                             HttpContext httpContext) throws IOException {
        urlStr = generateUrlWithParam(urlStr, param);
        HttpDelete httpDelete = new HttpDelete(urlStr);
        return httpClient.execute(httpDelete, httpContext);
    }

    /**
     * 生成完整url
     *
     * @param url   url
     * @param param 参数
     * @return 完整url
     */
    public static String generateUrlWithParam(String url, List<HttpReqInfo> param) {
        if (param == null || param.isEmpty()) {
            return url;
        }
        List<NameValuePair> nvpList = new ArrayList<>();
        for (HttpReqInfo reqInfo : param) {
            nvpList.add(new BasicNameValuePair(reqInfo.getParam(), reqInfo.getValue()));
        }
        return String.format("%s?%s", url, URLEncodedUtils.format(nvpList, StandardCharsets.UTF_8));
    }

    /**
     * 消费返回报文
     *
     * @param httpResponse 接口返回的结果
     * @return 解析后的结果
     * @throws IOException 异常信息
     */
    private static String consumeResponse(CloseableHttpResponse httpResponse) throws IOException {
        String result = "";
        try {
            // 获取响应实体
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            }
            EntityUtils.consume(entity);
        } finally {
            httpResponse.close();
        }
        return result;
    }

    /**
     * 创建SSL安全连接
     */
    private static SSLConnectionSocketFactory createSslConnSocketFactory() {
        SSLConnectionSocketFactory ssl = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
            ssl = new SSLConnectionSocketFactory(sslContext, (s, sslSession) -> Boolean.TRUE);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return ssl;
    }
}
~~~