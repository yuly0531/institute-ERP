<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common.jsp"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>수업 등록 화면</title>
  <script src="js/registFreeDev.js"></script>
  <link href="css/registClass.css" rel="stylesheet">

  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css" integrity="sha512-5A8nwdMOWrSz20fDsjczgUidUBR8liPYU+WymTZP1lmY9G6Oc7HlZv156XqnsgNUzTyMefFTcsFH/tnJE/+xBg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<script>


 function goClassListForm(){
      document.classListForm.submit();
}


function saveData(){

 	var formObj = $("[name='registClassForm']");
	var checkObj_class_name = formObj.find("[name='class_name']");
	var checkObj_class_start_date = formObj.find("[name='class_start_date']");
	var checkObj_class_end_date = formObj.find("[name='class_end_date']");
	var checkObj_take_charge_tea = formObj.find("[name='take_charge_tea']");
	var checkObj_class_subject = formObj.find("[name='class_subject']");
	
	
	if(checkVal(
	        checkObj_class_name
	          ,"수업명은 2~15자리 한글,영어로만 입력됩니다."
	          ,/^[a-z가-힣]{2,15}$/
	        )==false){
	          checkObj_class_name.focus();
	          return;
	  }
 	
	if(checkVal(
	        checkObj_class_start_date
	          ,"개강일은 필수 선택사항입니다."
	          ,/^[^ ]{1,}$/
	        )==false){
	          checkObj_class_start_date.focus();
	          return;
	  }
	
	if(checkVal(
	        checkObj_class_end_date
	          ,"종강일은 필수 선택사항 입니다."
	          ,/^[^ ]{1,}$/
	        )==false){
	          checkObj_class_end_date.focus();
	          return;
	  }
	if(
		checkObj_class_start_date.val()
		>
		checkObj_class_end_date.val())
		{alert("개강일은 종강일 보다 미래 일 수 없습니다.")
		return;
		}
	
	 if(checkVal(
	         checkObj_take_charge_tea
	          ,"담당강사명은 필수 선택사항입 니다."
	          ,/^[^ ]{1,}$/
	        )==false){
	          checkObj_take_charge_tea.focus();
	          return;
	      }
	
	 if(checkVal(
	         checkObj_class_subject
	          ,"수업내용은 임의 문자 2~500자 입력해야하고 공백으로 이루어질수 없습니다."
	          ,/^(.|\n){2,500}$/
	        )==false){
	          checkObj_class_subject.focus();
	          return;
	      }
	
   ajax(
      "/registClassProc.admin.do"
      ,"post"
      ,formObj
      ,function(responseJson){
    	 var classRegCnt = responseJson["classRegCnt"];
         if(classRegCnt>=1) {
            alert("수업 정보가 등록되었습니다.");
            goClassListForm();
         }
         else{
            alert("수업 등록 중 오류가 발생했습니다. 다시 시도해주십시오.")
         }
    });
  }
  </script>
</head>
<body>
  <form class="header">
    <div class="header_box">
      <div class="logo" onclick="location.replace('/adminMain.do')">
        <img src="">
        <div>
          ERP
        </div>
      </div>
      <table>
          <tr class="cate_box">
			<td class="main_cate" onclick="location.replace('/stuList.do')">학생 관리</td>
			<td class="main_cate" onclick="location.replace('/teaList.do')">강사 관리</td>
			<td class="main_cate active" onclick="location.replace('/classList.do')">수업 관리</td>
			<td class="main_cate" onclick="location.replace('/dayOff.do')">결재 관리</td>
          </tr>
      </table>
      <div class="welcome_user">
        <div>
          
          <div class="welcome_name">로그아웃</div>
               </div>
        <div class="logout_btn" onclick="location.replace('/loginForm.do')"><i class="fa fa-sign-out" aria-hidden="true"></i></div>
      </div>
      
    </div>
  </form>
   <form name="registClassForm" class="boardForm">
    <header>
      <div>수업 등록</div>
    </header>
      <div class="inform">
      <div class="title">수업 기본 정보</div>
      <div class="dev_user">
             <div> 
                    <td>수업명</td>
                    <td>
                        <input type="text" name="class_name"> 
                    </td>
                    <td>개강일</td>
                    <td>
                        <input type="date" name="class_start_date">
                    </td>
                    <td>종강일</td>
                    <td>
                        <input type="date" name="class_end_date">
                    </td>
                    </div>
                    <div>
                    <td>담당 강사명</td>
                        <select name="take_charge_tea">
                        	<option value="">
                        <c:forEach var="teacherList" items="${teacherListMap.teacherList}" varStatus="vs">
                        	<option value="${teacherList.tea_id}">${teacherList.tea_name}
                        </c:forEach>								
						</select>
                    </td>
                </div>
		      <div>
		        <div>수업 내용</div>
		        <textarea name="class_subject" cols="30" rows="10" maxlength="300" placeholder="최대 300자 입력"></textarea>
		      </div> 
    <span onclick="location.replace('/classList.do')" name="cancel" class="cancel">취소</span>
    <span onclick="saveData();" name="save" class="save">저장</span>
   </form>
  <form name="classListForm" class="no dumP_form" method="post" action="/classList.do"></form>
</body>
</html>