package com.naver.erp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Controller
public class ExamController {

	@Autowired
	private ExamDAO examDAO;  
	@Autowired
	private MainDAO mainDAO;  

	@Autowired
	private ExamService examService; 

	
	// 시험 리스트 접속, 검색
	@RequestMapping( value="/examList.do")
	public ModelAndView examList(
			ExamDTO examDTO
			,HttpSession session
	){
		String mid;
		if((String)session.getAttribute("stu_id")!=null) {
			mid = (String)session.getAttribute("stu_id");
		    examDTO.setStu_id(mid);
		}
		else if((String)session.getAttribute("tea_id")!=null) {
			mid = (String)session.getAttribute("tea_id");
		    examDTO.setTea_id(mid);
		}
		else {
			mid = (String)session.getAttribute("mana_id");
		}
		
		String whatRole = mainDAO.whatRole(mid);
	    
		Map<String,Object> examListMap = getExamListMap( examDTO );
		ModelAndView mav = new ModelAndView();
		mav.setViewName( "examList.jsp" );
		mav.addObject(   "examListMap" , examListMap     );

		
		if(examDTO.getClass_id()!=null && examDTO.getClass_id()!="" ) {
		Map<String,Object> stuExamScoreListMap = getStuExamScoreList( examDTO );
		mav.addObject(   "stuExamScoreListMap" , stuExamScoreListMap     );
		}

		mav.addObject(   "whatRole" , whatRole     );
		
		return  mav;
	}
	
	// 시험 리스트 불러오는 메소드
		public Map<String,Object> getExamListMap(ExamDTO examDTO){
			Map<String,Object> resultMap = new HashMap<String,Object>();
			List<Map<String,String>> examList;
			int examListCnt;
			Map<String,Integer> pagingMap;
			
			examListCnt =  this.examDAO.getExamListCnt(  examDTO );
			
			pagingMap = Util.getPagingMap(
					examDTO.getSelectPageNo()
					, examDTO.getRowCntPerPage()
					, examListCnt
			);
			

			examDTO.setSelectPageNo((int)pagingMap.get("selectPageNo"));
			examDTO.setRowCntPerPage((int)pagingMap.get("rowCntPerPage"));
			examDTO.setBegin_rowNo((int)pagingMap.get("begin_rowNo"));
			examDTO.setEnd_rowNo((int)pagingMap.get("end_rowNo"));
			
			
			examList       =  this.examDAO.getExamList( examDTO  );

			resultMap.put(  "examList"       , examList        );
			resultMap.put(  "examListCnt"    , examListCnt     );
			resultMap.put(  "examDTO"  , examDTO );
			
			resultMap.put(  "begin_pageNo"          , pagingMap.get("begin_pageNo")        );
			resultMap.put(  "end_pageNo"            , pagingMap.get("end_pageNo")          );
			resultMap.put(  "selectPageNo"          , pagingMap.get("selectPageNo")        );
			resultMap.put(  "last_pageNo"           , pagingMap.get("last_pageNo")         );
			resultMap.put(  "begin_serialNo_asc"    , pagingMap.get("begin_serialNo_asc")  );
			resultMap.put(  "begin_serialNo_desc"   , pagingMap.get("begin_serialNo_desc") );
			
			return resultMap;
		}
		
		public Map<String,Object> getStuExamScoreList(ExamDTO examDTO){
			Map<String,Object> resultMap = new HashMap<String,Object>();
			List<Map<String,String>> StuExamScoreList;
			StuExamScoreList       =  this.examDAO.getStuExamScoreList( examDTO  );

			resultMap.put(  "StuExamScoreList"       , StuExamScoreList        );
			resultMap.put(  "examDTO"  , examDTO );
			
			return resultMap;
		}
		
		
		// 시험 상세
		@RequestMapping( value="/examDetail.do")
		public ModelAndView examDetail(
				ExamDTO examDTO
				,HttpSession session
				,@RequestParam(value="exam_id") int exam_id
		){
			String mid;
			if((String)session.getAttribute("stu_id")!=null) {
				mid = (String)session.getAttribute("stu_id");
			    examDTO.setStu_id(mid);
			}
			else if((String)session.getAttribute("tea_id")!=null) {
				mid = (String)session.getAttribute("tea_id");
			    examDTO.setTea_id(mid);
			}
			else {
				mid = (String)session.getAttribute("mana_id");
			}
			
			String whatRole = mainDAO.whatRole(mid);
		    examDTO.setExam_id(exam_id);
		    
			Map<String,Object> examDetailMap = examDetailMap( examDTO );
			ModelAndView mav = new ModelAndView();
			mav.setViewName( "examDetail.jsp" );
			mav.addObject(   "examDetailMap" , examDetailMap     );
			mav.addObject(   "whatRole" , whatRole     );
			
			return  mav;
		}
		
		// 시험 리스트 불러오는 메소드
			public Map<String,Object> examDetailMap(ExamDTO examDTO){
				Map<String,Object> resultMap = new HashMap<String,Object>();
				List<Map<String,String>> examDetailInfo;
				List<Map<String,String>> examDetailProblem;
				List<Map<String,String>> examDetailAnswer;
				examDetailInfo       =  this.examDAO.getExamDetailInfo( examDTO  );
				examDetailProblem       =  this.examDAO.getExamDetailProblem( examDTO  );
				examDetailAnswer       =  this.examDAO.getExamDetailAnswer( examDTO  );

				resultMap.put("examDetailInfo", examDetailInfo);
				resultMap.put("examDetailProblem", examDetailProblem);
				resultMap.put("examDetailAnswer", examDetailAnswer);
				resultMap.put("examDTO", examDTO);
			
				return resultMap;
			}
 
			
	// 시험 삭제
	@RequestMapping(
			value="/deleteExamInfo.do"
			,method=RequestMethod.POST
			,produces="application/json;charset=UTF-8"
			)
	@ResponseBody
	public int deleteExamInfo(
			ExamDTO examDTO
			) throws Exception {

		int deleteExamCnt = 0;
				
		try {
			deleteExamCnt = this.examService.deleteExamInfo(examDTO);
		} catch (Exception e) {
			deleteExamCnt = -1;
		}
		
		return deleteExamCnt;
	}
 
	// 시험 수정
	@RequestMapping(
			value="/updateExamInfo.do"
			,method=RequestMethod.POST
			,produces="application/json;charset=UTF-8"
			)
	@ResponseBody
	public int updateExamInfo(
			ExamDTO examDTO
			) throws Exception {
		int updateExamCnt = 0;
		try {
			updateExamCnt = this.examService.updateExamInfo(examDTO);
		} catch (Exception e) {
			updateExamCnt = -1;
		}
		return updateExamCnt;
	}
	
	
	// 시험 등록
	@RequestMapping(
			value="/registExamProc.do" 
			,method=RequestMethod.POST
			,produces="application/json;charset=UTF-8"
	)
	@ResponseBody
	public Map<String,String> registExamProc(  
			ExamDTO  examDTO

	){
	
		Map<String,String> responseMap = new HashMap<String,String>();
		int examRegCnt = 0;
		
				try{
					examRegCnt = this.examService.insertExamInfo(examDTO);
			
		}
		catch(Exception ex){
			examRegCnt = -1;
		
		}
		responseMap.put("examRegCnt" , examRegCnt+"" );
	
		
		return responseMap;
		
	}
	
	
	// 성적 등록
		@RequestMapping(
				value="/registExamScore.do" 
				,method=RequestMethod.POST
				,produces="application/json;charset=UTF-8"
		)
		@ResponseBody
		public int registExamScore(  
				ExamDTO  examDTO

		){
			int examScoreRegCnt = 0;
			try{
				examScoreRegCnt = this.examService.insertExamScore(examDTO);
			}
			catch(Exception ex){
				examScoreRegCnt = -1;
			}
		
			return examScoreRegCnt;
			
		}

}
