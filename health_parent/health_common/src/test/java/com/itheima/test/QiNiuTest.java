package com.itheima.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class QiNiuTest {

    String accessKey = "Krzm7jxZGoKAD8V2eiqKehyhACfUt4OYEuB-93qj";
    String secretKey = "5-JF9FF0vIggduPPvALPieMA-JN4ySotNLrJhfTK";
    String bucket = "heimahaelth";
    /**
     * 七牛云文件上传
     */
    @Test
    public void fun1(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());

//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "F:\\2020java视频\\阶段4：医疗实战-传智健康-更新完结13天\\项目资料\\day04\\素材\\图片资源\\03a36073-a140-4942-9b9b-712cecb144901.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 七牛云文件删除
     */
    @Test
    public void fun2(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        String key = "1.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 阿里云文件上传
     */
    @Test
    public void fun3(){
        String endpoint = "oss-accelerate.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4GHf6TXutGWhteZkJJaW";
        String accessKeySecret = "LTUZQBpmkvarSUvJF7TqsiUIhN7Rc7";
        String bucketName = "qqqyyy";
// <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = "F:\\2020java视频\\阶段4：医疗实战-传智健康-更新完结13天\\项目资料\\day04\\素材\\图片资源\\1.jpg";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        String content = "Hello OSS";
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));

// 关闭OSSClient。
        ossClient.shutdown();
    }

}
