package com.tmax.eTest.Report.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Report.dto.MiniTestRecordDTO;
import com.tmax.eTest.Report.dto.ReportShareCreateDTO;
import com.tmax.eTest.Report.util.AES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportShareService {
    @Autowired
    private MiniTestRecordService miniTestRecordService;
    
    @Autowired
    private DiagnosisReportService diagnosisReportService;


    private String AES_SECRET_KEY = this.getClass().getName();

    public String createReportShareKey(String userId, ReportShareCreateDTO createDTO){
        return createReportShareKey(createDTO.getType(), userId, createDTO.getProbSetId(), createDTO.getExpire());
    }

    public String createReportShareKey(String type, String userId, String probSetId, String expire){
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("userId", userId);
        json.addProperty("probSetId", probSetId);
        json.addProperty("expire", expire);

        String encryptedString = AES.encrypt(json.toString(), this.AES_SECRET_KEY);

        try{
            return URLEncoder.encode(encryptedString, StandardCharsets.UTF_8.toString());
        }
        catch(Exception e){
            return null;
        }
    }

    public Object getReportDataFromKey(String key){
        try{
            //FIXME: temp solution for + being swapped to " "
            String decoded = URLDecoder.decode(key, StandardCharsets.UTF_8.toString()).replace(" ", "+");
            String decryptedJson = AES.decrypt(decoded, this.AES_SECRET_KEY);

            //Parse info jsonObject
            JsonObject json = JsonParser.parseString(decryptedJson).getAsJsonObject();
            
            //Get type from the token
            String type = json.get("type").getAsString();
            String userId = json.get("userId").getAsString();
            String probSetId = json.get("probSetId").getAsString();
            String expire = json.get("expire").getAsString();

            //TODO check expire
            
            if(type.equals("minitest")){
                MiniTestRecordDTO output = miniTestRecordService.getMiniTestRecord(userId, probSetId);
                return output;
            }


            return null;
        }
        catch(Exception e){
            return null;
        }
    }
}