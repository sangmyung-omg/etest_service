package com.tmax.eTest.TestStudio.controller.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v0.1")
public class TestFileUploadApi {
	
	String str_REACT_APP_SERVER_PATH="https://192.168.153.176:443/vod";
	private WebClient webClient = WebClient.builder().baseUrl( str_REACT_APP_SERVER_PATH )
			.build();
	
//	private final VideoService videoService;
	
	static {
        disableSSLVerification();
    }
	public static void disableSSLVerification() {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }

        } };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };      
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);           
    }
	
	  /**
	   * User가 upload한 file을 media storage에 저장
	   *
	   * @param MultipartFile file
	   * @param RedirectAttributes attributes
	   * @return void
	   * @throws ExecutionException
	   * @throws InterruptedException
	   * @throws IOException
	   */
	  @PostMapping("/upload")
	  @ResponseBody
	  public 
	  String 
//	  uploadTestGetResponse 
	  uploadFile(
	      HttpServletResponse response, @RequestParam("file") MultipartFile file)
	      throws InterruptedException, ExecutionException, IOException {
//	    VodUser user =
//	        ((VodUserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//	            .getUser();
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    // FIXME: 추후 유저 이름을 받게 되면 수정해야함
//	    FileInfoDTO fileInfo =
//	        FileInfoDTO.builder()
//	            .fileId(fileUtil.generateFileId())
//	            .originalFileName(fileName)
//	            .uploadTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
//	            .userId(user.getUserId())
//	            .build();

	    // Check directory
	    File directory = new File(
	    		"D:/Users/Tmax/Videos/hypermediaserver/media/vod/test/upload__2"
//	    		fileUtil.getFileDirPath(fileInfo)
	    		);
	    if (!directory.exists()) {
	      directory.mkdirs();
	    }
	    // Check if file is empty
	    if (file.isEmpty()) {
	      response.setStatus(HttpStatus.BAD_REQUEST.value());
//	      return FileInfoDTO.builder().build();
	      return null;
	    }
//	    uploadTestGetResponse rst = new uploadTestGetResponse();
	    // save the file on the local file system
	    try {
	      Path path = Paths.get(directory.getAbsolutePath() + "/" + fileName);
	      Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	      
//	      Video video = new Video();
//	      video.setVideoSrc(fileName);
//	      videoService.save(video);
//	      //
//	      File videoF = new File(directory.getAbsolutePath() + "/" + fileName);
//	      String upload = "/api/v0.1/upload";
//	      
//	      byte[] fileContent = Files.readAllBytes(videoF.toPath());
//
//	      MultipartBodyBuilder builder = new MultipartBodyBuilder();
//	     
//	      builder.part("file", new ByteArrayResource(fileContent))
//	      ;
////	          .header("Content-Disposition",
////	              "form-data; name= uploadfile; filename=1234.jpg");
//	      
//	      
//	      rst=
//	      webClient.post()
//	      			.uri(upload)
//	      			.contentType(MediaType.MULTIPART_FORM_DATA)
//	       			.headers( headers -> {
//	       				headers.add("content-type", "multipart/form-data");
//	       				headers.add("X-VOD-SERVICE-KEY", "guest");
//	       			})
//	      			.body(BodyInserters.fromMultipartData(builder.build()))
//	      			.retrieve()
//	      			.bodyToMono(uploadTestGetResponse.class)
//	      			.flux()
//					.toStream()
//					.findFirst()
//					.orElse(null);
	      
	      //
//	      uploadTestGetResponseBaseResolutions baseResolutions = new uploadTestGetResponseBaseResolutions(1L,2L,true);
//	      List<uploadTestGetResponseBaseResolutions> base = new ArrayList<uploadTestGetResponseBaseResolutions>();
//	      uploadTestGetResponseBaseData baseData = new uploadTestGetResponseBaseData(base);
//	      rst.setData(baseData);
	      
	      
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    // fileInfo = kafkaMessageSender.sendSync("resolutions", fileInfo);
//	    this.setSupportedResolutions(fileInfo);

//	    fileInfo = mediaService.registerUserOwnedMedia(fileInfo);
//	    return fileInfo;
//	    return rst;
	    return "test2";
	  }

//	  /**
//	   * "resolution" command에 대한 handler. 변환 가능한 resolution정보를 DTO에 채워줌
//	   *
//	   * @author yonghyun_cho@tmax.co.kr
//	   * @param fileInfoDTO[in, out]: 가능한 resolution을 체크할 파일 DTO FIXME: Transmuxing server에서 resolution을
//	   *     체크해서 반환하도록 수정해야함
//	   */
//	  private void setSupportedResolutions(FileInfoDTO fileInfoDTO)
//	      throws IOException, InterruptedException {
//	    ProcessBuilder builder = new ProcessBuilder();
//	    String ffmpegCommand =
//	        "ffprobe -show_streams "
//	            + fileUtil.getFileDirPath(fileInfoDTO)
//	            + "/"
//	            + fileInfoDTO.getOriginalFileName();
//	    builder.command("sh", "-c", ffmpegCommand);
//	    builder.directory(new File(projectRootDir));
//	    Process process = builder.start();
//
//	    ResolutionDTO resoultionDTO = new ResolutionDTO();
//	    ResolutionParser resolutionParser =
//	        new ResolutionParser(process.getInputStream(), resoultionDTO);
//	    Executors.newSingleThreadExecutor().submit(resolutionParser);
//
//	    int exitCode = process.waitFor();
//
//	    logger.info(
//	        "Resolution of uploaded video \n\t[width: "
//	            + resoultionDTO.getWidth()
//	            + "\n\theight: "
//	            + resoultionDTO.getHeight()
//	            + "]");
//	    List<ResolutionDTO> resolutionList =
//	        transMuxingUtil.generateSupportedResolutionList(resoultionDTO);
//	    fileInfoDTO.setResolutions(resolutionList);
//	  }	
//	
}
