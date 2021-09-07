package com.tmax.eTest.TestStudio.controller.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

//import com.google.common.net.HttpHeaders;


@Controller
@RequestMapping(value = "/api/v0.1")
public class TestFileDownLoadApi {


	  @GetMapping("/download")
	  @ResponseBody
	  public ResponseEntity<Resource> downloadFile( )
	      throws InterruptedException, ExecutionException, IOException {
		  
		  Path path = Paths.get("D:/Users/Tmax/Videos/hypermediaserver/media/vod/test/upload/sleepy_cat.mp4");
		  Resource resource = new InputStreamResource(Files.newInputStream(path));
		  return ResponseEntity.ok()
//		            .contentType(MediaType.parseMediaType("application/octet-stream"))
				  	.contentType(MediaType.parseMediaType("video/mp4"))
		            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "nameset.mp4"+ "\"")
		            .body(resource);
		 
	  }
	
	
}
