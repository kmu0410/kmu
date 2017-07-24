package crm.bck.db.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap; 








import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import crm.bck.db.service.DbDb20010VO;
import crm.bck.db.service.DbDb20010Service;
import crm.bck.sy.service.SyNm20010Service;
import crm.bck.sy.service.SyNm20010VO;
import crm.com.mng.service.ComMngService;
import crm.com.mng.service.ComMngVO;
import crm.com.mng.service.TreeVO;
import crm.com.mng.service.SessionVO;
import crm.com.util.CrmComUtil;
import crm.com.util.FileMngUtil;
import crm.com.util.SiteMessageSource;
import crm.com.util.UserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * Class 내용 : 대시보드 Controller
 * @FileName : DbDb20010Controller.java
 * @author :  김홍은
 * @since : 2016. 6. 20.
 * @version 1.0 
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  
 *  </pre>
 */
@Controller
public class DbDb20010Controller {
	
	/** spsp00010Service */
	@Resource(name = "dbdb20010Service")
	private DbDb20010Service dbdb20010Service;
	
	/** ComMngService */
	@Resource(name = "comMngService")
	private ComMngService comMngService;
	
	@Resource(name = "synm20010Service")
	private SyNm20010Service synm20010Service;
	
	
	
	/** MessageSource */
	@Resource(name = "siteMessageSource")
	SiteMessageSource siteMessageSource;
	
	/**
	 * 대시보드 VAN사 화면 이동
	 * @Method : selectDashBoardVanList
	 * @author : 김홍은
	 * @since : 2016. 6. 20.
	 * @version 1.0 
	 * 
	 * <pre>
	 * << 개정이력(Modification Information) >>
	 * 
	 *   수정일      수정자          수정내용
	 *  -------    --------    ---------------------------
	 *  
	 *  </pre>
	 */
	@RequestMapping("/bck_main/db/selectDashBoardVanList.do")
	public String selectDashBoardVanList(@ModelAttribute("searchVO") DbDb20010VO searchVO, ModelMap model,HttpServletRequest request , HttpSession httpSession)
			throws Exception {
		
		String viewUrl = "bck_main/db/DBDB20010R";
		
		//=================  사용자 권한부분 세팅 - 세션정보로 세팅
		SessionVO sessionVO =  (SessionVO) UserDetailsHelper.getAuthenticatedUser();
		searchVO.setSessionInfo(sessionVO);
		searchVO.setMmbrSn(sessionVO.getGmmbrSn());
		
		/*퀵메뉴조회*/
		List quickMenuList = dbdb20010Service.selectQuickMenuList(searchVO);
		model.addAttribute("quickMenuList", quickMenuList);
		/*서비스현황 - 포인트,쿠폰,스템프 */
		List serviceCondition = dbdb20010Service.selectserviceConditionList(searchVO);
		model.addAttribute("serviceCondition", serviceCondition);
		/*회원현황 - 내국인,외국인 */
		List userCondition = dbdb20010Service.selectuserConditionList(searchVO);
		model.addAttribute("userCondition", userCondition);
		/*회원현황 - 제휴가맹점 */
		List jehuCondition = dbdb20010Service.selectjehuConditionList(searchVO);
		model.addAttribute("jehuCondition", jehuCondition);
		/* 온라인 문의현황 */
		//List onlineInquiry = dbdb20010Service.selectonlineInquiryList(searchVO);
		//model.addAttribute("onlineInquiry", onlineInquiry);
		/* VOC처리현황현황(VAN사) */
		List onlineInquiry = dbdb20010Service.selectonlineInquiryVanList(searchVO);
		model.addAttribute("onlineInquiry", onlineInquiry);
		
		ComMngVO comMngVO = new ComMngVO();
		/*핸드폰번호*/
		comMngVO.setAllVal("전체");
		comMngVO.setComClsfCd("170");
		comMngVO.setCndtGbn("");
		comMngVO.setCndtVal("");
		List<?> codeTelList = comMngService.selectCodeMutilList(comMngVO);
		model.addAttribute("codeTelList", codeTelList);
		
		return viewUrl;
	}
	
	/**
	 * 대시보드 제휴사 화면 이동
	 * @Method : selectDashBoardJehuList
	 * @author : 김홍은
	 * @since : 2016. 6. 20.
	 * @version 1.0 
	 * 
	 * <pre>
	 * << 개정이력(Modification Information) >>
	 * 
	 *   수정일      수정자          수정내용
	 *  -------    --------    ---------------------------
	 *  
	 *  </pre>
	 */
	@RequestMapping("/bck_main/db/selectDashBoardJehuList.do")
	public String selectDashBoardJehuList(@ModelAttribute("searchVO") DbDb20010VO searchVO, ModelMap model,HttpServletRequest request , HttpSession httpSession)
			throws Exception {
		
		String viewUrl = "bck_main/db/DBDB20020R";
		
		//=================  사용자 권한부분 세팅 - 세션정보로 세팅
		SessionVO sessionVO =  (SessionVO) UserDetailsHelper.getAuthenticatedUser();
		searchVO.setSessionInfo(sessionVO);
		searchVO.setMmbrSn(sessionVO.getGmmbrSn());
		searchVO.setGjehuSn(sessionVO.getGjehuSn());
		searchVO.setGseplaSn(sessionVO.getGseplaSn());
		searchVO.setGmmbrTypeCd(sessionVO.getGmmbrTypeCd());
		
		/*퀵메뉴조회*/
		List quickMenuList = dbdb20010Service.selectQuickMenuList(searchVO);
		model.addAttribute("quickMenuList", quickMenuList);
		/*서비스현황 - 포인트,쿠폰,스템프 */
		List serviceCondition = dbdb20010Service.selectserviceConditionList(searchVO);
		model.addAttribute("serviceCondition", serviceCondition);
		/*회원현황 - 내국인,외국인 */
		List userCondition = dbdb20010Service.selectuserConditionList(searchVO);
		model.addAttribute("userCondition", userCondition);
		/*회원현황 - 제휴가맹점 */
		List jehuCondition1 = dbdb20010Service.selectjehuConditionList1(searchVO);
		model.addAttribute("jehuCondition1", jehuCondition1);
		/* 온라인 문의현황 */
		List onlineInquiry = dbdb20010Service.selectonlineInquiryList(searchVO);
		model.addAttribute("onlineInquiry", onlineInquiry);
		
		/*공지사항 - 제휴사*/
		SyNm20010VO synm20010VO = new SyNm20010VO();
		
		synm20010VO.setSessionInfo(sessionVO);
		
		synm20010VO.setBbsSn("1");                			/*게시판 일련번호*/
		if(sessionVO.getGmmbrTypeCd().equals("158002"))
		{
			synm20010VO.setSchJehuClsfCd("001002");       	/*제휴 분류코드 : 제휴사(001002) , 제휴가맹(001004) , 단독가맹(001005)*/
		}else if(sessionVO.getGmmbrTypeCd().equals("158003")){
			synm20010VO.setSchJehuClsfCd("001003");       	/*제휴 분류코드 : 제휴사(001002) , 제휴가맹(001004) , 단독가맹(001005)*/
		}	
		synm20010VO.setSchJehuSn(sessionVO.getGjehuSn());   /*제휴사 일련번호 : 세션 정보 */
		synm20010VO.setSchSeplaSn(sessionVO.getGselfCd());  /*제휴매장 자가코드 */
		
		synm20010VO.setTaskClsfCd("156002");   				/*업무분류 : 운영관리*/
		synm20010VO.setUseYn("Y");                			/*사용여부 : Y*/
		synm20010VO.setMaxRowCnt("5");            			/*최대 보여질 row 수*/

		List bbsCttList =  synm20010Service.selectSyNm20010BbscttJehuList(synm20010VO);
		model.addAttribute("bbsCttList", bbsCttList);
		
		
		/*팝업 공지 조회 */
		SyNm20010VO synm20010VO1 = new SyNm20010VO();
		
		synm20010VO1.setSessionInfo(sessionVO);
		
		synm20010VO1.setBbsSn("2");							//팝업 게시판
		if(sessionVO.getGmmbrTypeCd().equals("158002"))
		{
			synm20010VO1.setSchJehuClsfCd("001002");       	/*제휴 분류코드 : 제휴사(001002) , 제휴가맹(001004) , 단독가맹(001005)*/
		}else if(sessionVO.getGmmbrTypeCd().equals("158003")){
			synm20010VO1.setSchJehuClsfCd("001003");       	/*제휴 분류코드 : 제휴사(001002) , 제휴가맹(001004) , 단독가맹(001005)*/
		}
		synm20010VO1.setSchJehuSn(sessionVO.getGjehuSn());  /*제휴사 일련번호 : 세션 정보 */
		synm20010VO1.setSchSeplaSn(sessionVO.getGselfCd()); /*제휴매장 자가코드 */
		
		synm20010VO1.setTaskClsfCd("156002");   			/*업무분류 : 운영관리*/
		synm20010VO1.setBbsClsfCd("157004");
		synm20010VO1.setExlYn("N");
		synm20010VO1.setUseYn("Y");
		
		List<?> popDbNoticeList = synm20010Service.selectSyNm20010BbscttJehuList(synm20010VO1);
		model.addAttribute("popDbNoticeList", popDbNoticeList);
		
		
		
		ComMngVO comMngVO = new ComMngVO();
		/*핸드폰번호*/
		comMngVO.setAllVal("전체");
		comMngVO.setComClsfCd("170");
		comMngVO.setCndtGbn("");
		comMngVO.setCndtVal("");
		List<?> codeTelList = comMngService.selectCodeMutilList(comMngVO);
		model.addAttribute("codeTelList", codeTelList);
		
		return viewUrl;
	}
	
	/**
	 * 대시보드 제휴가맹점 화면 이동
	 * @Method : selectDashBoardJehuMemberList
	 * @author : 김홍은
	 * @since : 2016. 6. 20.
	 * @version 1.0 
	 * 
	 * <pre>
	 * << 개정이력(Modification Information) >>
	 * 
	 *   수정일      수정자          수정내용
	 *  -------    --------    ---------------------------
	 *  
	 *  </pre>
	 */
	@RequestMapping("/bck_main/db/selectDashBoardJehuMemberList.do")
	public String selectDashBoardJehuMemberList(@ModelAttribute("searchVO") DbDb20010VO searchVO, ModelMap model,HttpServletRequest request , HttpSession httpSession)
			throws Exception {
		
		String viewUrl = "bck_main/db/DBDB20030R";
		
		//=================  사용자 권한부분 세팅 - 세션정보로 세팅
		SessionVO sessionVO =  (SessionVO) UserDetailsHelper.getAuthenticatedUser();
		searchVO.setSessionInfo(sessionVO);
		searchVO.setMmbrSn(sessionVO.getGmmbrSn());
		searchVO.setGjehuSn(sessionVO.getGjehuSn());
		searchVO.setGseplaSn(sessionVO.getGseplaSn());
		searchVO.setGslefCd(sessionVO.getGselfCd());
		searchVO.setGmmbrTypeCd(sessionVO.getGmmbrTypeCd());
		
		/*퀵메뉴조회*/
		List quickMenuList = dbdb20010Service.selectQuickMenuList(searchVO);
		model.addAttribute("quickMenuList", quickMenuList);
		/*서비스현황 - 포인트,쿠폰,스템프 */
		List serviceCondition = dbdb20010Service.selectserviceConditionList(searchVO);
		model.addAttribute("serviceCondition", serviceCondition);
		/*회원현황 - 내국인,외국인 */
		List userCondition = dbdb20010Service.selectuserConditionList(searchVO);
		model.addAttribute("userCondition", userCondition);
		/*회원현황 - 제휴가맹점 */
		//List jehuCondition1 = dbdb20010Service.selectjehuConditionList1(searchVO);
		//model.addAttribute("jehuCondition1", jehuCondition1);

		/* 온라인 문의현황 */
		List onlineInquiry = dbdb20010Service.selectonlineInquiryList(searchVO);
		model.addAttribute("onlineInquiry", onlineInquiry);
		
		/*공지사항 - 제휴사*/
		SyNm20010VO synm20010VO = new SyNm20010VO();

		synm20010VO.setSessionInfo(sessionVO);
		
		synm20010VO.setBbsSn("1");                			/*게시판 일련번호*/
		if(sessionVO.getGmmbrTypeCd().equals("158004"))
		{
			synm20010VO.setSchJehuClsfCd("001004");       	/*제휴 분류코드 : 제휴사(001002) , 제휴가맹(001004) , 단독가맹(001005)*/
		}else if(sessionVO.getGmmbrTypeCd().equals("158005")){
			synm20010VO.setSchJehuClsfCd("001005");       	/*제휴 분류코드 : 제휴사(001002) , 제휴가맹(001004) , 단독가맹(001005)*/
		}	
		synm20010VO.setSchJehuSn(sessionVO.getGjehuSn());   /*제휴사 일련번호 : 세션 정보 */
		synm20010VO.setSchSeplaSn(sessionVO.getGselfCd());  /*제휴매장 자가코드 */

		synm20010VO.setTaskClsfCd("156002");   				/*업무분류 : 운영관리*/
		synm20010VO.setUseYn("Y");                			/*사용여부 : Y*/
		synm20010VO.setMaxRowCnt("4");            			/*최대 보여질 row 수*/

		List bbsCttList =  synm20010Service.selectSyNm20010BbscttJehuList(synm20010VO);
		model.addAttribute("bbsCttList", bbsCttList);
		
		
		/*팝업 공지 조회 */
		SyNm20010VO synm20010VO1 = new SyNm20010VO();
		
		synm20010VO1.setSessionInfo(sessionVO);
		
		synm20010VO1.setBbsSn("2");							//팝업 게시판
		if(sessionVO.getGmmbrTypeCd().equals("158004"))
		{
			synm20010VO1.setSchJehuClsfCd("001004");       	/*제휴 분류코드 : 제휴사(001002) , 제휴가맹(001004) , 단독가맹(001005)*/
		}else if(sessionVO.getGmmbrTypeCd().equals("158005")){
			synm20010VO1.setSchJehuClsfCd("001005");       	/*제휴 분류코드 : 제휴사(001002) , 제휴가맹(001004) , 단독가맹(001005)*/
		}
		synm20010VO1.setSchJehuSn(sessionVO.getGjehuSn());  /*제휴사 일련번호 : 세션 정보 */
		synm20010VO1.setSchSeplaSn(sessionVO.getGselfCd()); /*제휴매장 자가코드 */
		
		synm20010VO1.setTaskClsfCd("156002");   			/*업무분류 : 운영관리*/
		synm20010VO1.setBbsClsfCd("157004");
		synm20010VO1.setUseYn("Y");
		synm20010VO1.setExlYn("N");
		
		List<?> popDbNoticeList = synm20010Service.selectSyNm20010BbscttJehuList(synm20010VO1);
		model.addAttribute("popDbNoticeList", popDbNoticeList);
		
		ComMngVO comMngVO = new ComMngVO();
		/*핸드폰번호*/
		comMngVO.setAllVal("전체");
		comMngVO.setComClsfCd("170");
		comMngVO.setCndtGbn("");
		comMngVO.setCndtVal("");
		List<?> codeTelList = comMngService.selectCodeMutilList(comMngVO);
		model.addAttribute("codeTelList", codeTelList);
		
		
		return viewUrl;
	}
	
	/**
	 * 대시보드 퀵메뉴 삭제
	 * @Method : deleteQMenu
	 * @author : 김홍은
	 * @since : 2016. 6. 20.
	 * @version 1.0 
	 * 
	 * <pre>
	 * << 개정이력(Modification Information) >>
	 * 
	 *   수정일      수정자          수정내용
	 *  -------    --------    ---------------------------
	 *  
	 *  </pre>
	 */
	@RequestMapping("/bck_main/db/deleteQMenu.json")
	public ResponseEntity<EgovMap> deleteQMenu(
			@ModelAttribute("searchVO") DbDb20010VO searchVO, ModelMap model,HttpServletRequest request , HttpSession httpSession)
			throws Exception {
		
		int result    = -1;
		
	    EgovMap resultMap = new EgovMap();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		
		String mmbrSn 	 	 = request.getParameter("dashMmbrSn");
		String menuSn 	 	 = request.getParameter("dashMoveMenuSn");
		
		searchVO.setMmbrSn(mmbrSn);
		searchVO.setMenuSn(menuSn);
		
		try {
			result = dbdb20010Service.deleteQMenu(searchVO,request);
		}
		catch(Exception e) {
			  e.printStackTrace();
		  }
		
		 //================= 메세지처리부분
		if(result == -1)
		{   
			resultMap.put("resultCd", "-1");
			resultMap.put("infoTitle", siteMessageSource.getMessage("etc.title.error"));  // 에러
			resultMap.put("message",  "퀵메뉴 삭제가 실패되었습니다.");      //저장이 실패되었습니다.
		}else{
			resultMap.put("resultCd", result);
			resultMap.put("infoTitle", siteMessageSource.getMessage("etc.title.confirm"));  // 확인
			resultMap.put("message",  "성공적으로 퀵메뉴가 삭제되었습니다.");        //성공적으로 저장되었습니다.

		}
		
		return new ResponseEntity<EgovMap>(resultMap, responseHeaders, HttpStatus.CREATED);
	}
	
	
	
	
	
	
}
