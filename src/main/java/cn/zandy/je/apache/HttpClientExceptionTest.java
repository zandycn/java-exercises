package cn.zandy.je.apache;

/**
 * Created by zandy on 2019/6/3.
 */
public class HttpClientExceptionTest {

    // 测试 apache http client 异常情况
    public static void main(String[] args) {
        // 调用一个无效域名:
        // java.net.UnknownHostException: www.baidua.com: nodename nor servname provided, or not known
        //String url = "http://www.baidua.com/";

        // 调用一个无效的ip端口:
        // org.apache.http.conn.HttpHostConnectException:
        // Connect to 127.0.0.1:9080 [/127.0.0.1] failed: Connection refused (Connection refused)
        //String url = "http://127.0.0.1:9080/erp-stock-web/test";

        // 启动 Tomcat-8080 时调用:
        // java.net.SocketTimeoutException: Read timed out
        //String url = "http://127.0.0.1:8080/erp-stock-web/test";

        //String result = postRawBody(url, ImmutableMap.of("s", 4));
        //System.out.println(result);
    }
}
