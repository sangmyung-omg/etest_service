package com.tmax.eTest.TestStudio.controller;


import java.io.IOException;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tmax.eTest.TestStudio.controller.component.ImageFileServerApiComponentTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemIDandImageSrcsDTO;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetProblemImageDTOOut;

import lombok.RequiredArgsConstructor;


@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ImageControllerETest {
	
	private final ImageFileServerApiComponentTs imageFileServerApiComponentETest;
	

	/**
	 * Image Post 
	 * 
	 */
	
	
	@PostMapping("/Image")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> ImageUpload(
			@RequestPart(value="img") List<MultipartFile> imgMFileList,
			@RequestPart(value="userID") String userId,
			@RequestPart(value="defaultDarkImg", required=false) String defaultDarkImg
			) throws IllegalStateException, IOException {
	
		try {

			return new ResponseEntity<>( imageFileServerApiComponentETest.ImageUploadServiceComponent(imgMFileList, userId, defaultDarkImg ), HttpStatus.OK );
		
		} catch (Exception e) {
			
			e.printStackTrace(); 
//			return new ResponseEntity<>( new ImagePostResponse("500","fail"), HttpStatus.INTERNAL_SERVER_ERROR );
			return null;
			
		}		
	}	
	
	@GetMapping("/ImageSrc")
	public ResponseEntity<GetProblemImageDTOOut> ImageList(
			@RequestBody GetProblemDTOIn request
			) throws IllegalStateException, IOException {
	
		try {

			return new ResponseEntity<>( imageFileServerApiComponentETest.ImageListComponent(request.getProbIDs()), HttpStatus.OK );
		
		} catch (Exception e) {
			
			e.printStackTrace(); 
//			return new ResponseEntity<>( new ImagePostResponse("500","fail"), HttpStatus.INTERNAL_SERVER_ERROR );
			return null;
			
		}		
	}	
	
}
