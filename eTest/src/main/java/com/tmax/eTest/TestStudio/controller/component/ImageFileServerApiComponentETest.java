package com.tmax.eTest.TestStudio.controller.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemIDandImageSrcsDTO;
import com.tmax.eTest.TestStudio.dto.problems.out.GetProblemImageDTOOut;
import com.tmax.eTest.TestStudio.service.ProblemServiceETest;
import com.tmax.eTest.TestStudio.util.PathUtilEtest;

import lombok.RequiredArgsConstructor;



@Service
@Transactional
@RequiredArgsConstructor
public class ImageFileServerApiComponentETest {
	

	private final ProblemServiceETest problemServiceETest;
	private final PathUtilEtest pathUtilEtest = new PathUtilEtest();
	
	/**
	 * Image Post 
	 * 
	 */
	
//	private String dirPath = File.separator + "data" + File.separator + "imgsrc";
	private String dirPath = pathUtilEtest.getDirPath();
	
	public String getDirPath() {
		return this.dirPath;
	}
	
	public String ImageUploadServiceComponent(
					List<MultipartFile> imgMFileList,
					String userId,
					String defaultDarkImg
			) throws Exception {
		
			if(imgMFileList.size()<1) {
				throw new Exception("no-imageData");
			}
			
			//tmp경로 생성
			File tmpDir = new File(dirPath + File.separator + "tmp");
			if( !tmpDir.exists() ){
				tmpDir.mkdirs();
			}
			
			/* 우분투 권한 부여(optional)
				String cmd = "chmod 750 -R " + File.separator + "data";
				Runtime rt = Runtime.getRuntime();
				Process prc = rt.exec(cmd);
				prc.waitFor();
			*/
			
			//넘겨줄 tmp경로 string
			String abPath = tmpDir.getAbsolutePath();
//			System.out.println("temp 절대경로: "+ abPath );
			String tmpDirPath = tmpDir.toString();
			
			if( "t".equals(defaultDarkImg) ) {
				return saveImgFileServiceComponent(imgMFileList,userId,tmpDirPath, true);
			}else {
				return saveImgFileServiceComponent(imgMFileList,userId,tmpDirPath, false);
			}
		
	}
	
	
	/**
	 * image data 저장
	 */
	public String saveImgFileServiceComponent
				( List<MultipartFile> imgMFileList, String userId, String tmpDirPath, Boolean isDefaultDarkImg ) throws Exception {
				
				//userid 에 따른 tmp경로/유저 id 경로 생성
				File destDir = new File(tmpDirPath + File.separator + userId);
				if( !destDir.exists() ){
					destDir.mkdirs();
				}
				System.out.println(destDir.toString());
				
				// 이미지 저장
				for(MultipartFile imgMFile : imgMFileList) {
					 
					FileOutputStream fos = null;
					boolean alreadyExist = false;
					
					if( new File(destDir.toString() + File.separator + imgMFile.getOriginalFilename()).exists() ){
						alreadyExist = true;
					}
					
					// 파일 쓰기
					try {
						fos = new FileOutputStream(destDir.toString() + File.separator + imgMFile.getOriginalFilename());
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new Exception("FileOutputStream 초기화에 실패했습니다.");
					}
					
					try {
						byte[] data = imgMFile.getBytes(); 
						System.out.println("data length: " + data.length);
						fos.write(data);
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if( alreadyExist ){
						System.out.println("Success: 이미지 덮어쓰기에 성공하였습니다.");
					}else{
						System.out.println("Success: 이미지 업로드에 성공하였습니다.");
					}
					
				}
				
//			if( true == isDefaultDarkImg ) {
//				
//				Boolean isDarkImageConverted = true;
//				for(MultipartFile imgMFile : imgMFileList) {
//					
//					if( false == ConvertToDarkImageComponent.convertImage(destDir.toString(), imgMFile.getOriginalFilename()) ) {
//						isDarkImageConverted=false;
//						System.out.println("warning: 다크이미지 생성에 실패하였습니다.");
//					}
//					
//				}
//				
//				if(true == isDarkImageConverted) {
//					return "ok";
////					return new ImagePostResponse("200","success",destDir.toString()+File.separator+imgMFileList.get(0).getOriginalFilename()
////							,destDir.toString()+File.separator+"dark_"+imgMFileList.get(0).getOriginalFilename());
//				}else {
//					return "ok";
////					return new ImagePostResponse("200","success",destDir.toString()+File.separator+imgMFileList.get(0).getOriginalFilename(), "fail");
//				}
//			}
//			else {
				return "ok";
//				return new ImagePostResponse("200","success",destDir.toString()+File.separator+imgMFileList.get(0).getOriginalFilename(), "");
//			}
	}
	
	/**
	 * 이미지 파일 경로 변경
	 */
	public Boolean assignImgFileServiceComponent(String userID, Long probID, String src ){
		
		try {

			if(userID == null || probID == null || src == null) return false;
			// 파일 경로 변경
			File from = new File(dirPath + File.separator + "tmp" + File.separator + userID + File.separator + src);
			File to = new File(dirPath + File.separator + Long.toString(probID));
//			System.out.println("문제 파일경로"+ to);
			
			String fromString = from.toString();
			String toString = dirPath + File.separator + Long.toString(probID) + File.separator + src;
			
			if( !from.exists() ){
				return false;
			}
			if( !to.exists() ){
				to.mkdirs();
			}
			
			Path pathFrom = Paths.get(fromString);
			System.out.println(from.getAbsolutePath());
			Path pathTo = Paths.get(toString);	
			System.out.println(to.getAbsolutePath());
//			Files.move(pathFrom, pathTo);
			Files.move(pathFrom, pathTo, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	
	}
	
	// List로 받아 경로 변경
	public Boolean assignImgFileListServiceComponent(String userID, Long probID, List<String> srcList) {
		
		try {
				Boolean isSuccess = true;
				
				if(srcList != null){
					for( String src : srcList ){
						if(src==null) {
							continue;
						}
						Boolean assignResult = assignImgFileServiceComponent(userID, probID, src);
						if(!assignResult){
							System.out.println("파일 추가 안함: " + src);
							isSuccess = false;
						}else {
							System.out.println("파일 추가: " + src);
						}
					}
				}
				
				return isSuccess;
			}catch (Exception e) {
				throw e;
			}
		
	}
	
	/**
	 * probID에 대응한 이미지 저장 폴더내 파일 삭제
	 */
	public Boolean deleteImgSrcFileOfProbIDServiceComponent(Long probID){	

		try {
				if(probID ==null) return false;
				
				String probIDtoString = Long.toString(probID); 		
				
				File folder = new File(dirPath + File.separator + probIDtoString);
				
				if( folder.exists() ){
					File[] fileList = folder.listFiles();
					for( int i=0 ; i<fileList.length ; i ++ ){
						fileList[i].delete();
						System.out.println("파일 삭제 prob: " + probIDtoString+"src: "+fileList[i].getName());
					}
				}
			
			return true;
		}catch (Exception e) {
			throw e;
		}
	}
	public Boolean deleteImgSrcFolerOfProbIDServiceComponent(Long probID){	

		try {
				if(probID ==null) return false;
				
				String probIDtoString = Long.toString(probID); 		
				
				File folder = new File(dirPath + File.separator + probIDtoString);
				
				if( folder.exists() ){
					folder. delete();
					System.out.println("폴더 삭제 : " + dirPath + File.separator + probIDtoString);
				}
			
			return true;
		}catch (Exception e) {
			throw e;
		}
	}
	/**
	 *  userID에 대응한 이미지 저장 tmp폴더내부 데이터 삭제
	 */
	public Boolean deleteImgTempFolerOfUserIDServiceComponent(String userID){	

		try {
			
				if(userID==null) return false;
			
				File folder = new File(dirPath + File.separator + "tmp" + File.separator + userID);
				
				if( folder.exists() ){
					File[] fileList = folder.listFiles();
					for( int i=0 ; i<fileList.length ; i ++ ){
						fileList[i].delete();
						System.out.println("파일 삭제 userID: " + userID+", src: "+fileList[i].getName());
					}
				}
			
			return true;
		}catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 주어진 probID 이미지 폴더내 imgSrcs에 대응한 이미지 파일 삭제
	 */
	public Boolean deleteImgSrcsOfProbIDServiceComponent(Long probID, List<String> imgSrcs){	

		try {
				if(probID==null) return false;
				
				String probIDtoString = Long.toString(probID); 		
				
				File folder = new File(dirPath + File.separator + probIDtoString);
				
				if( folder.exists() ){

					for(String src : imgSrcs) {
						if(src == null) continue;

						File imgFile = new File(dirPath + File.separator + probIDtoString + File.separator + src);
						System.out.println(imgFile.toString());	
						if(imgFile.exists()) {
							imgFile.delete();
							System.out.println("파일 삭제 prob: " + probIDtoString+", src: "+imgFile.getName());
						}
						
					}
					
				}else {
					return false;
				}
			
			return true;
		}catch (Exception e) {
			throw e;
		}
	}
	
	
	
	/**
	 * 이미지 파일 JsonObject to String 반환  JsonObject:( key:이미지 파일 이름/value:Base64인코딩 된 이미지 {key1:value1,key2:value2})
	 */
	public String getImgByProbIDServiceComponent(Long probId){
		
		if(probId==null) return null;
		
		JsonObject jsonObject = new JsonObject();
		
		String probIDtoString = Long.toString(probId); 		
		
		File folder = new File(dirPath + File.separator + probIDtoString);
		
		if( folder.exists() ){

			File[] fileList = folder.listFiles();
			for( int i=0 ; i<fileList.length ; i ++ ){
				String src = fileList[i].getName();
				String imgFileBase64ToString = getImgFileServiceComponent(probId, src);
				jsonObject.addProperty(src, imgFileBase64ToString);
				
			}

			
		}
		
		return jsonObject.toString();
	}
	
	/**
	 * norm or dark mode 에 따른
	 * 이미지 파일 JsonObject to String List반환  JsonObject:( key:이미지 파일 이름/value:Base64인코딩 된 이미지 {key1:value1,key2:value2})
	 */

	public List<String> getImgJsonToStrListByProbIDServiceComponent(Long probId){
		
		if(probId==null) return null;
		
		List<String> result = new ArrayList<String>();
		JsonObject jsonObjectNorm = new JsonObject();
//		JsonObject jsonObjectDark = new JsonObject();
		
		String probIDtoString = Long.toString(probId); 			
		
		File folder = new File(dirPath + File.separator + probIDtoString);
		
		if( folder.exists() ){

			File[] fileList = folder.listFiles();
			for( int i=0 ; i<fileList.length ; i ++ ){
				String src = fileList[i].getName();
				String imgFileBase64ToString = getImgFileServiceComponent(probId, src);
//				System.out.println(src);
//				if(problemImage.getDarkMode()==false) {
				if(true) {
					jsonObjectNorm.addProperty(src, imgFileBase64ToString);
				}else {
//					jsonObjectDark.addProperty(src, imgFileBase64ToString);
				}
				
			}

		}
		
		result.add(jsonObjectNorm.toString());
//		result.add(jsonObjectDark.toString());
		return result;
	}

	
	/**
	 * 이미지 파일 base64 인코딩 후 String 으로 반환
	 */
	private String getImgFileServiceComponent(Long probId, String src){
		final Integer BUFFER_SIZE = 3 * 1024;
		FileInputStream fis = null;
		StringBuffer sb = null;
		
		try {
			fis = new FileInputStream(dirPath + File.separator + Long.toString(probId) + File.separator + src);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
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
			e.printStackTrace();		
			return null;
		}
		
		return sb.toString();
	}
	
	
	public GetProblemImageDTOOut ImageListComponent(String probIdStr) throws Exception{
		
		if(probIdStr==null) return null;
		
		String[] strProbIdList = probIdStr.replace(" ","").split(",");
		
		 List<BaseProblemIDandImageSrcsDTO> outputBase = new ArrayList<BaseProblemIDandImageSrcsDTO>();
		
		for(String strProbId : strProbIdList) {
			File folder = new File(pathUtilEtest.getDirPath() + File.separator + strProbId);
			if( folder.exists()) {

				List<String> srcList = new ArrayList<String>();
				
				for(String src:  folder.list() ) {
					srcList.add(src);
				}
				
				outputBase.add( new BaseProblemIDandImageSrcsDTO(strProbId, srcList) );
			}else {
				outputBase.add(null);
			}
		}
		
		return new GetProblemImageDTOOut( outputBase );
	}
}
