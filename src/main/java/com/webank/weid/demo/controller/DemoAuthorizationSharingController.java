package com.webank.weid.demo.controller;

import com.webank.weid.blockchain.constant.ErrorCode;
import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.demo.common.model.CreateQualificationCertificateModel;
import com.webank.weid.demo.common.model.CreateStudentIDCARDModel;
import com.webank.weid.demo.common.model.QualificationCertificateModel;
import com.webank.weid.demo.common.model.StudentIDCARDModel;
import com.webank.weid.demo.common.util.PrivateKeyUtil;
import com.webank.weid.demo.service.DemoOtherService;
import com.webank.weid.demo.service.DemoService;
import com.webank.weid.protocol.base.CptBaseInfo;
import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.util.DataToolUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :Lictory
 * @date : 2023/09/19
 */


@RestController
@Api(description = "基于DID的数据授权共享案例 (在数据的拥有者授权下 某些应用方可以更改或查询拥有者的部分数据)"
+"本案例中以 医疗机构作为数据共享背景,医疗机构AB发行资格证,医疗机构之间通过验证资格证进行数据的共享等",
tags = "基于医疗机构的数据共享案例")
public class DemoAuthorizationSharingController {

    private static final Logger logger = LoggerFactory.getLogger(DemoAuthorizationSharingController.class);

    @Autowired
    private DemoService demoService;

    @Autowired
    private DemoOtherService demoOtherService;

    /**
     * create weId without parameters and call the settings property method.
     *
     * @return returns weId and public key
     */
    @ApiOperation(value = "医疗机构注册上链：资格证场景，创建WeId")
    @PostMapping("/step1/qualificationcertificate/createWeId")
    public ResponseData<CreateWeIdDataResult> createWeId() {
        return demoService.createWeId();
    }

    /**
     * institutional publication of CPT.
     * claim is a JSON object
     * @return returns CptBaseInfo
     */
    @ApiOperation(value = "机构定义通用的资格证模板：注册CPT")
    @PostMapping("/step2/qualificationcertificate/registCpt")
    public ResponseData<CptBaseInfo> registCpt(
            @ApiParam(name = "qualificationCertificateModel", value = "机构定义通用的资格证模板 CPT模板")
            @RequestBody QualificationCertificateModel qualificationCertificateModel) {

        ResponseData<CptBaseInfo> response;
        try {
            if (null == qualificationCertificateModel) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            String publisher = qualificationCertificateModel.getPublisher();
            String credentialInfo = DataToolUtils.mapToCompactJson(qualificationCertificateModel.getCredentialInfo());

            // get the private key from the file according to weId.
            String privateKey
                    = PrivateKeyUtil.getPrivateKeyByWeId(PrivateKeyUtil.KEY_DIR, publisher);
            logger.info("param,publisher:{},privateKey:{},claim:{}", publisher, privateKey, credentialInfo);

            // converting claim in JSON format to map.
            Map<String, Object> credentialInfoMap = new HashMap<String, Object>();
            credentialInfoMap =
                    (Map<String, Object>) DataToolUtils.deserialize(
                            credentialInfo,
                            credentialInfoMap.getClass()
                    );

            // call method to register CPT on the chain.
            response = demoService.registCpt(publisher, privateKey, credentialInfoMap);
            logger.info("registCpt response: {}", DataToolUtils.objToJsonStrWithNoPretty(response));
            return response;
        } catch (Exception e) {
            logger.error("registCpt error", e);
            return new ResponseData<>(null, ErrorCode.TRANSACTION_EXECUTE_ERROR);
        }
    }

    /**
     * institutional publication of Credential.
     *
     * @return returns  credential
     * @throws IOException  it's possible to throw an exception
     */
    @ApiOperation(value = "颁发资格证电子凭证")
    @PostMapping("/step3/qualificationcertificate/createCredential")
    public ResponseData<CredentialPojo> createCredential(
            @ApiParam(name = "createIDCARDModel", value = "创建电子凭证")
            @RequestBody CreateQualificationCertificateModel createQualificationCertificateModel) {

        ResponseData<CredentialPojo> response;
        try {
            if (null == createQualificationCertificateModel) {
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            // getting cptId data.
            Integer cptId = createQualificationCertificateModel.getCptId();
            // getting issuer data.
            String issuer = createQualificationCertificateModel.getIssuer();
            // getting claimData data.
            String claimData = DataToolUtils.mapToCompactJson(createQualificationCertificateModel.getClaimData());

            // get the private key from the file according to weId.
            String privateKey = PrivateKeyUtil.getPrivateKeyByWeId(PrivateKeyUtil.KEY_DIR, issuer);
            logger.info(
                    "param,cptId:{},issuer:{},privateKey:{},claimData:{}",
                    cptId,
                    issuer,
                    privateKey,
                    claimData
            );
            // converting claimData in JSON format to map.
            Map<String, Object> claimDataMap = new HashMap<String, Object>();
            claimDataMap =
                    (Map<String, Object>) DataToolUtils.deserialize(
                            claimData,
                            claimDataMap.getClass()
                    );

            // call method to create credentials.
            response = demoService.createCredential(cptId, issuer, privateKey, claimDataMap);
            logger.info("createCredential response: {}",
                    DataToolUtils.objToJsonStrWithNoPretty(response));
            return response;
        } catch (Exception e) {
            logger.error("createCredential error", e);
            return new ResponseData<CredentialPojo>(null, ErrorCode.CREDENTIAL_ERROR);
        }
    }





}



