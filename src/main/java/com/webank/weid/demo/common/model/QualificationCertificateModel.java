package com.webank.weid.demo.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * @author :Lictory
 * @date : 2023/09/19
 */

@ApiModel(description = "注册机构资格证CPT接口模板")
public class QualificationCertificateModel {


    @ApiModelProperty(name = "publisher", value = "发布者weid", required = true,
            example = "did:weid:1:0x19607cf2bc4538b49847b43688acf3befc487a41")
    private String publisher;

    @ApiModelProperty(name = "credentialInfo", value = "学生证模板CPT数据类型定义", required = true,
            example = "{\n"
                    + "    \"title\": \"cpt\",\n"
                    + "    \"description\": \"this is cpt\",\n"
                    + "    \"properties\" : {\n"
                    + "        \"name\": {\n"
                    + "            \"type\": \"string\",\n"
                    + "            \"description\": \"the name of certificate owner\"\n"
                    + "        },\n"
                    + "        \"gender\": {\n"
                    + "            \"enum\": [\"F\", \"M\"],\n"
                    + "            \"type\": \"string\",\n"
                    + "            \"description\": \"the gender of certificate owner\"\n"
                    + "        },\n"
                    + "        \"cardNumber\": {\n"
                    + "            \"type\": \"string\",\n"
                    + "            \"description\": \"the student id card number \"\n"
                    + "        },\n"
                    + "          \"major\": {\n"
                    + "            \"type\": \"string\",\n"
                    + "            \"description\": \"the major of the student \"\n"
                    + "        },\n"
                    + "          \"educationalSystem\": {\n"
                    + "            \"type\": \"string\",\n"
                    + "            \"description\": \"the educational system of the student \"\n"
                    + "        },\n"
                    + "          \"startTime\": {\n"
                    + "            \"type\": \"string\",\n"
                    + "            \"description\": \"the startTime of the student \"\n"
                    + "        },\n"
                    + "          \"medicalAgencyName\": {\n"
                    + "            \"type\": \"string\",\n"
                    + "            \"description\": \"the name of the medical agency \"\n"
                    + "        }\n"
                    + "    },\n"
                    + "    \"required\": [\"name\", \"gender\", \"cardNumber\", \"major\", \"educationalSystem\", \"startTime\",\"medicalAgencyName\"]\n"
                    + "}")
    private Map<String, Object> credentialInfo;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Map<String, Object> getCredentialInfo() {
        return credentialInfo;
    }

    public void setCredentialInfo(Map<String, Object> credentialInfo) {
        this.credentialInfo = credentialInfo;
    }
}