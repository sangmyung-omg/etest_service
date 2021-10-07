package com.tmax.eTest.TestStudio.controller;


import java.io.File;
import java.io.IOException;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tmax.eTest.Contents.controller.ArticleController;
import com.tmax.eTest.TestStudio.controller.component.ImageFileServerApiComponentTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemIDandImageSrcsDTO;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetProblemImageDTOOut;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ImageControllerTs {
	
	private final ImageFileServerApiComponentTs imageFileServerApiComponentETest;
	

	/**
	 * Image Post 
	 * @throws Exception 
	 * 
	 */
	
	
	@PostMapping("/test-studio/Image")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> ImageUpload(
			@RequestPart(value="img") List<MultipartFile> imgMFileList,
			@RequestPart(value="userID") String userId,
			@RequestPart(value="defaultDarkImg", required=false) String defaultDarkImg
			) throws Exception {

			return new ResponseEntity<>( imageFileServerApiComponentETest.ImageUploadServiceComponent(imgMFileList, userId, defaultDarkImg ), HttpStatus.OK );
			
	}	
	
//	@GetMapping("/test-studio/ImageSrc")
	@GetMapping(value="/test-studio/ImageSrc/{userID}/{probIDs}")
	public ResponseEntity<GetProblemImageDTOOut> ImageList(
//			@RequestBody GetProblemDTOIn request
			@PathVariable("userID") String userID,
			@PathVariable("probIDs") String probIDs
			) throws Exception {
	
			return new ResponseEntity<>( imageFileServerApiComponentETest.ImageListComponent(probIDs), HttpStatus.OK );
			
	}	
	
	@GetMapping("/test-studio/image-route-directory")
	public ResponseEntity<String> ImageList(
			
			) throws Exception {
	
//		try {
			File folder = new File(imageFileServerApiComponentETest.getDirPath());
			return new ResponseEntity<>( folder.getAbsolutePath(), HttpStatus.OK );
		
//		} catch (Exception e) {
//			
//			try {
//				return new ResponseEntity<>( imageFileServerApiComponentETest.getDirPath(), HttpStatus.OK );
//			}catch(Exception e_) {
//				e_.printStackTrace(); 
//				log.error(e_.getMessage());
//				throw e_;
//			}
//			
//		}		
	}
	
}
