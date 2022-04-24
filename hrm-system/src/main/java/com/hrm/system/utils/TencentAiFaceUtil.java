package com.hrm.system.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 腾讯云人脸识别工具类
 *
 * @author LZL
 * @version v1.0
 * @date 2022/4/22-22:59
 */
@Slf4j
@Component
public class TencentAiFaceUtil {
    private IaiClient client;
    @Value("${tencent-face.secretId}")
    private String secretId;
    @Value("${tencent-face.secretKey}")
    private String secretKey;
    @Value("${tencent-face.endPoint}")
    private String endPoint;
    @Value("${tencent-face.region}")
    private String region;

    @PostConstruct
    private void init() {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(secretId, secretKey);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(endPoint);
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        client = new IaiClient(cred, region, clientProfile);
    }

    /**
     * 新增人员
     *
     * @param groupId             人员库id
     * @param personName          人员名
     * @param personId            人员id
     * @param gender              性别
     * @param uniquePersonControl 是否唯一
     * @param url                 图片地址
     */
    public boolean createPerson(String groupId, String personName, String personId, long gender, long uniquePersonControl, String url) {
        // 新增前做检测判断人脸是否存在
//        if (isFace(url)) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        CreatePersonRequest req = new CreatePersonRequest();
        req.setGroupId(groupId);
        req.setPersonName(personName);
        req.setPersonId(personId);
        req.setGender(gender);
        req.setUrl(url);
        req.setUniquePersonControl(uniquePersonControl);
        // 返回的resp是一个CreatePersonResponse的实例，与请求对象对应
        CreatePersonResponse resp = null;
        // 输出json格式的字符串回包
        try {
            resp = client.CreatePerson(req);
            log.info("新增人员信息：{}", CreatePersonResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            log.info("新增人员失败：{}", e.getMessage());
            return false;
        }
        return true;
//        }
    }

    /**
     * 人员删除
     *
     * @param personId 人员id
     * @return 删除是否成功
     */
    public boolean delPerson(String personId) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DeletePersonRequest req = new DeletePersonRequest();
        req.setPersonId(personId);
        // 返回的resp是一个DeletePersonResponse的实例，与请求对象对应
        try {
            DeletePersonResponse resp = client.DeletePerson(req);
            log.info("人员ID：{} 删除成功", personId);
            return true;
        } catch (TencentCloudSDKException e) {
            log.info("人员ID：{} 删除失败 - {}", personId, e.getMessage());
            return false;
        }
    }

    /**
     * 是否是一张脸,通过dataurl判断
     *
     * @param url 图片地址
     * @return 是否是一张脸
     */
    public boolean isFace(String url) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DetectFaceRequest req = new DetectFaceRequest();
        req.setImage(url);
        // 是否需要人脸属性1-需要0-不需要
        //req.setNeedFaceAttributes(1L);
        DetectFaceResponse resp = null;
        try {
            resp = client.DetectFace(req);
            log.info("图中有人脸");
        } catch (TencentCloudSDKException e) {
            // 说明图中没有人脸
            log.info("{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 是否活体
     *
     * @param url 图片地址
     * @return 是否活体
     */
    private boolean isLivenessFace(String url) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DetectLiveFaceRequest req = new DetectLiveFaceRequest();
        req.setUrl(url);
        // 返回的resp是一个DetectLiveFaceResponse的实例，与请求对象对应
        DetectLiveFaceResponse resp = null;
        try {
            resp = client.DetectLiveFace(req);
            if (resp.getIsLiveness()) {
                log.info("isLiveness: {}", true);
                return true;
            }
            log.info("isLiveness: {}", false);
            return false;
        } catch (TencentCloudSDKException e) {
            // 说明图中没有人脸
            log.info("{}", e.getMessage());
            return false;
        }
    }

    /**
     * 人脸搜索，在某个组中找对应的人脸是否存在，存在则返回最相似的人脸对应的人员ID
     *
     * @param groupId 组id
     * @param url     人脸地址
     * @return 找到的最相似的那个人员ID
     */
    public String searchPerson(String groupId, String url) {
        SearchFacesRequest req = new SearchFacesRequest();
        req.setGroupIds(new String[]{groupId});
        req.setImage(url);
        req.setMaxPersonNum(1L);
        // 返回的resp是一个SearchFacesResponse的实例，与请求对象对应
        SearchFacesResponse resp = null;
        try {
            resp = client.SearchFaces(req);
            final Result[] results = resp.getResults();
            // 评分高于70才确定是同一个人
            if (results[0].getCandidates()[0].getScore() >= 70) {
                log.info("{}组人脸存在，匹配人员ID：{}", groupId, results[0].getCandidates()[0].getPersonId());
                return results[0].getCandidates()[0].getPersonId();
            }
            log.info("{}组人脸不存在", groupId);
            return null;
        } catch (TencentCloudSDKException e) {
            log.info("{}", e.getMessage());
            return null;
        }
    }

    /**
     * 人员库中是否存在该人员
     *
     * @param personId 人员id
     * @return 是否存在
     */
    public boolean isExist(String personId) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        GetPersonBaseInfoRequest req = new GetPersonBaseInfoRequest();
        req.setPersonId(personId);
        // 返回的resp是一个GetPersonBaseInfoResponse的实例，与请求对象对应
        GetPersonBaseInfoResponse resp = null;
        try {
            resp = client.GetPersonBaseInfo(req);
            log.info("ID：{} - 已存在该人员信息", personId);
            return true;
        } catch (TencentCloudSDKException e) {
            log.info("ID：{} - 不存在该人员信息", personId);
            return false;
        }
    }

    /**
     * 增加人脸
     *
     * @param personId 人员id
     * @param url      图片url
     * @return 增加是否成功
     */
    public boolean addFace(String personId, String url) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        CreateFaceRequest req = new CreateFaceRequest();
        req.setPersonId(personId);
        String[] urls1 = {url};
        req.setUrls(urls1);
        req.setQualityControl(2L);
        // 返回的resp是一个CreateFaceResponse的实例，与请求对象对应
        CreateFaceResponse resp = null;
        try {
            resp = client.CreateFace(req);
            if (resp.getRetCode()[0] == 0) {
                log.info("{} - 增加人脸成功，人脸ID：{}", personId, resp.getSucFaceIds()[0]);
                return true;
            }
            log.info("{} - 增加人脸失败", personId);
            return false;
        } catch (TencentCloudSDKException e) {
            log.info("{} - 增加人脸失败 ：{}", personId, e.getMessage());
            final String[] personBaseInfo = getPersonBaseInfo(personId);
            if (personBaseInfo != null) {
                final String firstFaceId = personBaseInfo[0];
                final boolean delPersonFace = delPersonFace(personId, firstFaceId);
                if (delPersonFace) {
                    return addFace(personId, url);
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    /**
     * 获取人员的faceids数组
     *
     * @param personId 人员id
     * @return faceIds数组
     */
    public String[] getPersonBaseInfo(String personId) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        GetPersonBaseInfoRequest req = new GetPersonBaseInfoRequest();
        req.setPersonId(personId);
        // 返回的resp是一个GetPersonBaseInfoResponse的实例，与请求对象对应
        try {
            GetPersonBaseInfoResponse resp = client.GetPersonBaseInfo(req);
            return resp.getFaceIds();
        } catch (TencentCloudSDKException e) {
            log.info("获取人员{}基础信息失败", personId);
        }
        return null;
    }

    /**
     * 删除人员的人脸
     *
     * @param personId 人员id
     * @param faceId   人脸id
     * @return 删除是否成功
     */
    public boolean delPersonFace(String personId, String faceId) {
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DeleteFaceRequest req = new DeleteFaceRequest();
        req.setPersonId(personId);
        String[] faceIds1 = {faceId};
        req.setFaceIds(faceIds1);
        // 返回的resp是一个DeleteFaceResponse的实例，与请求对象对应
        try {
            DeleteFaceResponse resp = client.DeleteFace(req);
            log.info("删除人员-{} 人脸-{} 成功", personId, faceId);
            return true;
        } catch (TencentCloudSDKException e) {
            log.info("{}", e.getMessage());
            return false;
        }
    }
}
