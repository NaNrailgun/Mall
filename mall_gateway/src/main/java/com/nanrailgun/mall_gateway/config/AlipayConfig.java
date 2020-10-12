package com.nanrailgun.mall_gateway.config;

public class AlipayConfig {
    // 商户appid

    public static String APPID = "2021000116694648";

    // 私钥 pkcs8格式的

    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCW9ca2I2tFNwvVqcF3WyWQrzCT7fyao4/5Zp7XAyEAjHMlnCekHCOis3QoLqQDLKgHUPJmJH41ky4681gU1DM5ocq473wXXT12UWDWYRP9YWW9LyQxMQ4mfAxyBRi8bQjPe3MQ3NkXfyit/GiXZokOVL3GRMwYUDgToBPdyfQ9BAF7dSttWdjITghYZiDV+YmxpPQ94TZSYjkbTiK2bMlIzhFp4XxXzQr+7qdNIfNga5NzTfbQKivtdi66nvGvSe0HaNaVgRQmsOizkNiWOXmnIEtH9tN3K8uEllxZgGsomc/TauicBNZ9zB75nmwcNIHsBVQ2dRcVvnf/Yb0xcpqPAgMBAAECggEAQcD4NumildluPDje3iBDcov8etLUjn7j/Vwnn7vrKhQDK8Sit6bSFtJXrBD0u7U900H882S93JQpNOA944Syc033wSn/QSYJ2XmE1pweWuWLrSXVe9/ELBsoSJnTtm8/wTAiZJvtq4BltbMV06yD0kwzBSN+SrDWXCYDwT1wNwrWREYgNpuDC8kbja3pTo1KASdTi+uZCj01TC/Bt9R4izkOJi0LxBPskO9BCLRoAiMQhVkQi7oQ36biVUjv4pVNx++xjPt9Kjcs3wkLcXKzj1njmEpCTGTlzicqtR0foC/CvC0BICAOv7oWo42idu4PKAlLSOBXAOa7UzrNhiUrQQKBgQDL0CIQNlpQms+5rj2EOthRTX7hAm/TkdCqF3acIkwuotJ6bj8a3/dC3Mcta1AOoR/PcqXerAFsdaFSAzO+O2DXtM/tUf9t3zmuS5lftwoHVEHGSfETwh2RRIiiTETQ+FX5ycpwzRil48BWClHSlGDZIBvjJ/y5OMwYXHajWhuLMQKBgQC9nSbWAuWLvD6NBV4o4QVXwdop8iV0RvFOEwYyKGJjzY2YGhQvWE5y+9OgMZYNgsU42ga5s4ilaOAEuuel7KUMhdzt0DkB70DtHkApjfhSAKxzpYcZBcG1/7n+dPnXqUjQY2qmAE7CEHg9iifnEAu1XmimSfeWZ27Gf97O3D+RvwKBgCr49VWpwB0ipw2lWT8aESYDSu8yTlWzduWGEPIWoS1sEyjROej2s/GbDzvaVoTfIA/jQmnSpnIx0xOxrSU868xPSU/mbA7EdCr1sZ7mvuyQrVWbnYLm+Mj6RDzEC/ZrheUm0NI7zCid/p2li170/f6JsJeShMyg1xeD11fV1dzhAoGAPm4hvVES1yFRzwi3aeo84hoXhLwPjOtWxAgLgDQ/wPCK8EuV3cjLe5xin1n0N9qYqRFQKZBtK1kOeUA4yS7TYP4qMbrfKj3mOKw/H+94lTdXkHQtuRVmJFBgouFM6a75JHO3R5TOWaMmk8yLOs7TrBG7jxTHiiPgG0c6yw1FeO0CgYEAtTXiSEnwlkqThojLA49vD68UGl0Uf4xCVl+C0szIavsS80eCZ7t5PYsP7H6yXvPwjJVMkr5vWKsBvn8qrCxWQjIBtrzYMwq29XjCSknkntRSSFNULvrfNS0hwAc0xkKWbygw19ri1WipQdvcyjRdEL4t+ZIAphym3oD4jGCC3ME=";

    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问

    public static String notify_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址

    public static String return_url = "http://218.244.137.91:9000/payValidation";

    // 请求网关地址

    public static String URL = "https://openapi.alipaydev.com/gateway.do";

    // 编码

    public static String CHARSET = "UTF-8";

    // 返回格式

    public static String FORMAT = "json";

    // 支付宝公钥

    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoYOofEviCm1PSHfGm+dmgrL7S+wMxizzQSicUpmxbtIttYCJgNnks5HMNeiwSfcDXMiXaCwbe18973lSLBI+nPT7UArOAgWw8XniwEja7vkNEKU8Fao+kCOrlfC5GwibM4z4rw0maNJxHkJKyKPkKQVAU7IrzX08v+eVlyw0zmSUNykqPor1AU5QuZcsrTYd8uttnZi/FxC5juCC1NZ5nWgjBtGyiWf0B4ItbyBrosQ5aVhl4GMqSI1RwQ4j/gGTRubA0bmSbGC6V1l5kV+Kxyyy5m7z6A+u9iiwOa65r0iSY7EiZEZxKCScPf3d/gw0/hE9OhC51ishcHHwDUCRpQIDAQAB";

    // 日志记录目录定义在 logFile 中

    public static String log_path = "/log";

    // RSA2

    public static String SIGNTYPE = "RSA2";
}
