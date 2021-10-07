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
import com.tmax.eTest.TestStudio.controller.TestProblemControllerTs;
import com.tmax.eTest.TestStudio.controller.component.exception.CustomExceptionTs;
import com.tmax.eTest.TestStudio.controller.component.exception.ErrorCodeEnumTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemIDandImageSrcsDTO;
import com.tmax.eTest.TestStudio.dto.problems.out.GetProblemImageDTOOut;
import com.tmax.eTest.TestStudio.service.ProblemServiceTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageFileServerApiComponentTs {
	

	private final ProblemServiceTs problemServiceETest;
	private final PathUtilTs pathUtilTs;
	
	/**
	 * Image Post 
	 * 
	 */
	
//	private String dirPath = pathUtilTs.getDirPath();
	
	public String getDirPath() {
//		return this.dirPath;
		return pathUtilTs.getDirPath();
	}
	
	public String ImageUploadServiceComponent(
					List<MultipartFile> imgMFileList,
					String userId,
					String defaultDarkImg
			) throws Exception {
		
			if(imgMFileList.size()<1) {
				throw new Exception("no-imageData");
			}
//			System.out.println(getDirPath());
//			System.out.println(pathUtilTs.getDirPath());
			//tmp경로 생성
//			File tmpDir = new File(dirPath + File.separator + "tmp");
			File tmpDir = new File(getDirPath() + File.separator + "tmp");
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
//				System.out.println(destDir.toString());
				
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
						log.info("FileOutputStream 초기화에 실패했습니다.");
						throw e;
					}
					
					try {
						byte[] data = imgMFile.getBytes(); 
//						System.out.println("data length: " + data.length);
						fos.write(data);
						fos.flush();
					} catch (IOException e) {
						log.info("IOException occurred while writing data");
						throw e;
					}finally {
						fos.close();
					}
					
					if( alreadyExist ){
//						System.out.println("Success: 이미지 덮어쓰기에 성공하였습니다.");
						log.info("Success: 이미지 덮어쓰기에 성공하였습니다.");
					}else{
//						System.out.println("Success: 이미지 업로드에 성공하였습니다.");
						log.info("Success: 이미지 업로드에 성공하였습니다.");
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
	 * @throws IOException 
	 */
	public Boolean assignImgFileServiceComponent(String userID, Long probID, String src ) throws IOException{
		
//		try {
			String dirPath = getDirPath(); 
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
//			System.out.println(from.getAbsolutePath());
			Path pathTo = Paths.get(toString);	
//			System.out.println(to.getAbsolutePath());
//			Files.move(pathFrom, pathTo);
			Files.move(pathFrom, pathTo, StandardCopyOption.REPLACE_EXISTING);
			return true;
//		} catch (IOException e) {
//			log.info("IOException occurred");
//			throw e;
////			return false;
//		}
		
	
	}
	
	// List로 받아 경로 변경
	public Boolean assignImgFileListServiceComponent(String userID, Long probID, List<String> srcList) throws IOException, CustomExceptionTs {
		
//		try {
				Boolean isSuccess = true;
				
				if(srcList != null){
					if(srcList.size()>50) {
						throw new CustomExceptionTs(ErrorCodeEnumTs.EXCEEDED_REQUEST_SIZE);
					}else {
						for( String src : srcList ){
							
							if(src==null) {
								continue;
							}
							Boolean assignResult = assignImgFileServiceComponent(userID, probID, src);
							if(!assignResult){
//								System.out.println("파일 추가 안함: " + src);
								isSuccess = false;
							}else {
//								System.out.println("파일 추가: " + src);
								log.info("이미지 파일 추가 prob: "+probID+", src: "+src);
							}
						}
					}
				}
				
				return isSuccess;
//			}catch (IOException e) {
//				log.info("IOException occurred");
//				throw e;
//			}
		
	}
	
	/**
	 * probID에 대응한 이미지 저장 폴더내 파일 삭제
	 */
	public Boolean deleteImgSrcFileOfProbIDServiceComponent(Long probID){	


				String dirPath = getDirPath();
				if(probID ==null) return false;
				
				String probIDtoString = Long.toString(probID); 		
				
				File folder = new File(dirPath + File.separator + probIDtoString);
				
				if( folder.exists() ){
					File[] fileList = folder.listFiles();
					for( int i=0 ; i<fileList.length ; i ++ ){
						fileList[i].delete();
//						System.out.println("파일 삭제 prob: " + probIDtoString+"src: "+fileList[i].getName());
						log.info("이미지 파일 삭제 prob: " + probIDtoString+", src: "+fileList[i].getName());
					}
				}
			
			return true;

	}
	public Boolean deleteImgSrcFolerOfProbIDServiceComponent(Long probID){	


				String dirPath = getDirPath();
				if(probID ==null) return false;
				
				String probIDtoString = Long.toString(probID); 		
				
				File folder = new File(dirPath + File.separator + probIDtoString);
				
				if( folder.exists() ){
					folder. delete();
//					System.out.println("폴더 삭제 : " + dirPath + File.separator + probIDtoString);
					log.info("이미지 폴더 삭제 : " +probIDtoString);
				}
			
			return true;

	}
	/**
	 *  userID에 대응한 이미지 저장 tmp폴더내부 데이터 삭제
	 */
	public Boolean deleteImgTempFolerOfUserIDServiceComponent(String userID){	

				String dirPath = getDirPath();
				if(userID==null) return false;
			
				File folder = new File(dirPath + File.separator + "tmp" + File.separator + userID);
				
				if( folder.exists() ){
					File[] fileList = folder.listFiles();
					for( int i=0 ; i<fileList.length ; i ++ ){
						fileList[i].delete();
//						System.out.println("파일 삭제 userID: " + userID+", src: "+fileList[i].getName());
//						log.info("임시 이미지 파일 삭제 userID: " + userID+", src: "+fileList[i].getName());
					}
				}
			
			return true;

	}
	/**
	 * 주어진 probID 이미지 폴더내 imgSrcs에 대응한 이미지 파일 삭제
	 */
	public Boolean deleteImgSrcsOfProbIDServiceComponent(Long probID, List<String> imgSrcs){	

				String dirPath = getDirPath();
				if(probID==null) return false;
				
				String probIDtoString = Long.toString(probID); 		
				
				File folder = new File(dirPath + File.separator + probIDtoString);
				
				if( folder.exists() ){

					for(String src : imgSrcs) {
						if(src == null) continue;

						File imgFile = new File(dirPath + File.separator + probIDtoString + File.separator + src);
//						System.out.println(imgFile.toString());	
						if(imgFile.exists()) {
							imgFile.delete();
//							System.out.println("파일 삭제 prob: " + probIDtoString+", src: "+imgFile.getName());
							log.info("이미지 파일 삭제 prob: " + probIDtoString+", src: "+imgFile.getName());
						}
						
					}
					
				}else {
					return false;
				}
			
			return true;

	}
	
	
	
	/**
	 * 이미지 파일 JsonObject to String 반환  JsonObject:( key:이미지 파일 이름/value:Base64인코딩 된 이미지 {key1:value1,key2:value2})
	 * @throws IOException 
	 */
	public String getImgByProbIDServiceComponent(Long probId) throws IOException{
		String dirPath = getDirPath();
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
	 * @throws IOException 
	 */

	public List<String> getImgJsonToStrListByProbIDServiceComponent(Long probId) throws IOException{
		String dirPath = getDirPath();
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
	 * @throws IOException 
	 */
	public String getImgFileServiceComponent(Long probId, String src) throws IOException, FileNotFoundException{
		final Integer BUFFER_SIZE = 3 * 1024;
		FileInputStream fis = null;
		StringBuffer sb = null;
		
//		try {
			String dirPath = getDirPath();
			fis = new FileInputStream(dirPath + File.separator + Long.toString(probId) + File.separator + src);
//		} catch (FileNotFoundException e) {
//			log.info("FileNotFoundException occurred");	
//			throw e;
////			return null;
//		}
		
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
			
		} catch (IOException e) {
			log.info("IOException occurred while encoding Base64");
			throw e;
//			return null;
		} finally {
			fis.close();
		}
		
		return sb.toString();
	}
	
	
	public GetProblemImageDTOOut ImageListComponent(
			String probIdStr
//			List<Long> probIdList
//			List<String> strProbIdList
			) throws Exception{
		
//		if(probIdStr==null || probIdStr.isBlank() ) return null; // java 11
		if(probIdStr==null) return null;
		
		String[] strProbIdList = probIdStr.replace(" ","").split(",");
		
//		if(probIdList==null || probIdList.isEmpty()) return null;
		
//		if(strProbIdList==null || strProbIdList.isEmpty() ) return null;
		
		 List<BaseProblemIDandImageSrcsDTO> outputBase = new ArrayList<BaseProblemIDandImageSrcsDTO>();
		
		for(String strProbId : strProbIdList) {
//	    for(Long probId : probIdList) {
//	    	String strProbId = probId.toString();
	
			File folder = new File(pathUtilTs.getDirPath() + File.separator + strProbId);
			if( folder.exists() && strProbId !="") {
//				System.out.println("AbsolutePath of the folder : "+folder.getAbsolutePath());
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
