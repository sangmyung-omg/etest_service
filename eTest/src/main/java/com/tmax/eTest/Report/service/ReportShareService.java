package com.tmax.eTest.Report.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Report.dto.MiniTestRecordDTO;
import com.tmax.eTest.Report.dto.ReportShareCreateDTO;
import com.tmax.eTest.Report.dto.DiagnosisRecordMainDTO;
import com.tmax.eTest.Report.util.AES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportShareService {
    @Autowired
    private MiniTestRecordService miniTestRecordService;
    
    @Autowired
    private DiagnosisMainRecordService diagnosisMainRecordService;


    private String AES_SECRET_KEY = this.getClass().getName();

    public static final String TYPE_MINITEST = "minitest";
    public static final String TYPE_DIAG = "diag";

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
        catch(UnsupportedEncodingException e){
            log.error("Encoding not supported");
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
            String userId = !json.get("userId").isJsonNull() ? json.get("userId").getAsString() : null;
            String probSetId = json.get("probSetId").getAsString();
            String expire = json.get("expire").getAsString();

            //TODO check expire
            
            if(type.equals(TYPE_MINITEST) && userId != null){
                MiniTestRecordDTO output = miniTestRecordService.getMiniTestRecord(userId, probSetId);
                return output;
            }
            else if(type.equals(TYPE_DIAG)){
                DiagnosisRecordMainDTO output = diagnosisMainRecordService.getDiagnosisRecordMain(userId, probSetId);
                return output;
            }


            return null;
        }
        catch(UnsupportedEncodingException e){
            log.error("Encoding not supported.");
            return null;
        }
        catch(ClassCastException e){
            log.error("Cannot cast to given field type.");
            return null;
        }
        catch(IllegalStateException e){
            log.error("Cannot cast to given field type.");
            return null;
        }
        catch(ParseException e){
            log.error("parse excepton in field type.");
            return null;
        }
    }

    public ReportShareCreateDTO getDataFromKey(String key){
        try{
            //FIXME: temp solution for + being swapped to " "
            String decoded = URLDecoder.decode(key, StandardCharsets.UTF_8.toString()).replace(" ", "+");
            String decryptedJson = AES.decrypt(decoded, this.AES_SECRET_KEY);

            //Parse info jsonObject
            JsonObject json = JsonParser.parseString(decryptedJson).getAsJsonObject();
            
            //Get type from the token
            String type = json.get("type").getAsString();
            String userId = !json.get("userId").isJsonNull() ? json.get("userId").getAsString() : null;
            String probSetId = json.get("probSetId").getAsString();
            String expire = json.get("expire").getAsString();

            return ReportShareCreateDTO.builder().type(type).userId(userId).probSetId(probSetId).expire(expire).build();
        }
        catch(UnsupportedEncodingException e){
            log.error("Encoding not supported.");
            return null;
        }
        catch(ClassCastException e){
            log.error("Cannot cast to given field type.");
            return null;
        }
        catch(IllegalStateException e){
            log.error("Cannot cast to given field type.");
            return null;
        }
    }
}
