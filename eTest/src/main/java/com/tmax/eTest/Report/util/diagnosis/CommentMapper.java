package com.tmax.eTest.Report.util.diagnosis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tmax.eTest.Common.model.comment.CommentInfo;
import com.tmax.eTest.Common.model.comment.CommentVersionInfo;
import com.tmax.eTest.Common.repository.comment.CommentRepo;
import com.tmax.eTest.Common.repository.comment.CommentVersionRepo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CommentMapper {
	@Autowired private CommentRepo commentRepo;
	@Autowired private CommentVersionRepo versionRepo;

	// version name, 
    static Map<Type, List<CommentInfo>> commentMap = null;
    static String nowSelectedVersionName = null;


    //Build at application startup
//    @EventListener
//    public void startUpGenerator(ApplicationStartedEvent event){
//    	initRoutine();
//    }

    @Scheduled(fixedRate=60*60*1000)
    public void scheduledUpdater(){
    	initRoutine();
    }
    
    
    public static String getComment(Type type, int idx)
    {
    	String result = "";
    	
    	if(commentMap != null)
    	{
    		result = commentMap.get(type).get(idx).getCommentText();
    	}
    	
    	return result;
    }

    public void initRoutine()
    {
    	Optional<CommentVersionInfo> opt = versionRepo.findByIsSelected(1);
    	
    	if(opt.isPresent())
    	{
    		nowSelectedVersionName = opt.get().getVersionName();
    		List<CommentInfo> commentList = commentRepo.findAllByVersionName(nowSelectedVersionName);
    		
    		for(CommentInfo comment : commentList)
    			if(comment.getCommentText() == null)
    				comment.setCommentText("");
    			else
    				comment.setCommentText(comment.getCommentText().replaceAll("\\\\n","\n"));
    		
    		commentMap = new HashMap<>();
    		
            commentMap.put(Type.GI, commentList.subList(0, 3));
            commentMap.put(Type.RISK_TYPE, commentList.subList(3, 7));    	
            commentMap.put(Type.RISK_INVEST, commentList.subList(7, 13));   
            commentMap.put(Type.RISK_METHOD, commentList.subList(13, 21));   
            commentMap.put(Type.RISK_COMBI, commentList.subList(21, 69));
            commentMap.put(Type.RISK_FIXED, commentList.subList(69, 70));
            commentMap.put(Type.RISK_PROFILE_MAIN, commentList.subList(70, 72));   
            commentMap.put(Type.RISK_TRACING_MAIN, commentList.subList(72, 74));   
            commentMap.put(Type.INVEST_PROFILE_MAIN, commentList.subList(74, 76));   
            commentMap.put(Type.INVEST_TRACING_MAIN, commentList.subList(76, 78));   
            commentMap.put(Type.INVEST_TYPE, commentList.subList(78, 108));   
            commentMap.put(Type.INVEST_MAIN, commentList.subList(108, 112));   
            commentMap.put(Type.KNOWLEDGE_COMMENT, commentList.subList(112, 178));   
            commentMap.put(Type.KNOWLEDGE_MAIN, commentList.subList(178, 181));   
            commentMap.put(Type.KNOWLEDGE_FIXED, commentList.subList(181, 183));   	
    		
            
            log.info("Now Selected Comment Version Is " +nowSelectedVersionName);
    	}
    	else
    	{
    		log.error("Error in CommentMapper. Selected Comment Version not exist!");
    	}
    }

    
    public enum Type{
        GI("gi"),									// 0~2
        RISK_TYPE("riskType"),						// 3~6
        RISK_INVEST("investRiskComment"),			// 7~12
        RISK_METHOD("investMethodComment"),			// 13~20
        RISK_COMBI("combiComment"),					// 21~68
        RISK_FIXED("riskFixed"),					// 69
        RISK_PROFILE_MAIN("profileMainList"),		// 70~71
        RISK_TRACING_MAIN("tracingMainList"),		// 72~73
        
        INVEST_PROFILE_MAIN("profileMainList"), 	// 74~75
        INVEST_TRACING_MAIN("tracingMainList"), 	// 76~77
        INVEST_TYPE("investType"),					// 78~107
        INVEST_MAIN("investMainComment"),			// 108~111
        
        KNOWLEDGE_COMMENT("knowledgeDetailList"),	// 112~177
        KNOWLEDGE_MAIN("knowledgeMain"),			// 178~180
        KNOWLEDGE_FIXED("knowledgeFixed");			// 181~182

        //@Getter
        private String value;
        
        private Type(String value){
            this.value = value;
        }

        public String toString(){
            return this.value;
        }
	}

    

}
