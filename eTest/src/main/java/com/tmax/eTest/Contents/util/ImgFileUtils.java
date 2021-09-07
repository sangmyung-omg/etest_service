package com.tmax.eTest.Contents.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import com.tmax.eTest.TestStudio.util.PathUtilTs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImgFileUtils {
    
    @Autowired
    PathUtilTs pathUtil;

    public String getImgFileServiceComponent(Integer probId, String src){
		final Integer BUFFER_SIZE = 3 * 1024;
		FileInputStream fis = null;
		StringBuffer sb = null;

        String dirPath = pathUtil.getDirPath();
		
		try {
			fis = new FileInputStream(dirPath + File.separator + Integer.toString(probId) + File.separator + src);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.info(e.getMessage());
			
			return null;
		}
		
		byte[] buf = new byte[BUFFER_SIZE];
		int len = 0;
		sb = new StringBuffer();
		
		try {
			while( (len = fis.read(buf))!=-1 ){
				if( len == BUFFER_SIZE ){
					sb.append(Base64.getEncoder().encodeToString(buf));
				}
				else{
					byte[] temp = new byte[len];
					System.arraycopy(buf, 0, temp, 0, len);
					sb.append(Base64.getEncoder().encodeToString(temp));
				}
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info(e.getMessage());
			return null;
		}
		
		return sb.toString();
	}
    
}
