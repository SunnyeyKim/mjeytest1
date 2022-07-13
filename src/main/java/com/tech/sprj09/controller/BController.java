package com.tech.sprj09.controller;

import java.io.FileInputStream;

import java.net.URLEncoder;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.tech.sprj09.dao.IDao;
import com.tech.sprj09.dto.BoardDto;

import com.tech.sprj09.vopage.SearchVO;

@Controller
public class BController {

//	BServiceInter bServiceInter;
	
	@Autowired
	private SqlSession sqlSession;
	
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request, SearchVO searchVO, Model model) {
		System.out.println("======list()======");
//		db에서 데이터 가져오기
//		bServiceInter=new BListService();
//		bServiceInter.execute(model);
		IDao dao=sqlSession.getMapper(IDao.class);
		/* searching */
		
		/* 보관할 변수 필요 */
		String btitle="";
		String bcontent="";
		
		
		String[] brdtitle=request.getParameterValues("searchType");
		if(brdtitle!=null) {
			for(int i=0; i<brdtitle.length;i++) {
				System.out.println("brdtitle : "+brdtitle[i]);
			}
		}
		/* 경우의 수 4가지, 변수에 저장 */
		if (brdtitle!=null) {
			for (String val : brdtitle) {
				if (val.contentEquals("btitle")) {
					model.addAttribute("btitle", "true");//검색체크유지
					btitle="btitle";
				}else if (val.contentEquals("bcontent")) {
					model.addAttribute("bcontent", "true");//검색체크유지
					bcontent="bcontent";
				}
			}
		}
		
		//검색결과 유지하기
		String bt=request.getParameter("btitle");
		String bc=request.getParameter("bcontent");
		
		if (bt!=null) {
			if (bt.equals("btitle")) {
				btitle=bt;
				model.addAttribute("btitle", "true");
			}
		}
		if (bc!=null) {
			if (bc.equals("bcontent")) {
				bcontent=bc;
				model.addAttribute("bcontent", "true");
			}
		}
		
		/* 검색키워드 받기 */
		String searchKeyword=request.getParameter("sk");
		if(searchKeyword==null)
			searchKeyword="";
		model.addAttribute("resk", searchKeyword);
		System.out.println("searchKeyword : "+searchKeyword);
		
		/* paging */
		String strPage=request.getParameter("page"); //HttpServletRequest request, 가져오기
		System.out.println("pagggge1 :"+strPage);
		//null검사
		if(strPage==null)//처음 리스트에서 list페이지로 넘어갈 때, null임.
			strPage="1";
		System.out.println("pagggge2 :"+strPage);
		int page=Integer.parseInt(strPage);
		searchVO.setPage(page);
		
		//total 글의 갯수 구하기
//		int total=dao.selectBoardTotCount();
//		System.out.println("totalrow : "+total);
		
		//조건에 따른 갯수 구하기
		int total=0;
		if (btitle.equals("btitle") && bcontent.equals("")) {
			total=dao.selectBoardTotCount1(searchKeyword);
		}else if (btitle.equals("") && bcontent.equals("bcontent")) {
			total=dao.selectBoardTotCount2(searchKeyword);
		}else if (btitle.equals("btitle") && bcontent.equals("bcontent")) {
			total=dao.selectBoardTotCount3(searchKeyword);
		}else if (btitle.equals("") && bcontent.equals("")) {
			total=dao.selectBoardTotCount4(searchKeyword);
		}
		
		searchVO.pageCalculate(total);
		
		//계산된 내용 출력
		System.out.println("totRow : "+total);
		System.out.println("clickPage : "+strPage);
		System.out.println("pageStart : "+searchVO.getPageStart());
		System.out.println("pageEnd : "+searchVO.getPageEnd());
		System.out.println("pageTot : "+searchVO.getTotPage());
		System.out.println("rowStart : "+searchVO.getRowStart());
		System.out.println("rowEnd : "+searchVO.getRowEnd());

		int rowStart=searchVO.getRowStart();
		int rowEnd=searchVO.getRowEnd();
		
		/*ArrayList<BoardDto> list=dao.list(rowStart,rowEnd);*/
//		ArrayList<BoardDto> list=null;
		
		if (btitle.equals("btitle") && bcontent.equals("")) {
			model.addAttribute("list", dao.list(rowStart, rowEnd, searchKeyword,"1"));
		}else if (btitle.equals("") && bcontent.equals("bcontent")) {
			model.addAttribute("list", dao.list(rowStart, rowEnd, searchKeyword,"2"));			
		}else if (btitle.equals("btitle") && bcontent.equals("bcontent")) {
			model.addAttribute("list", dao.list(rowStart, rowEnd, searchKeyword,"3"));
		}else if (btitle.equals("") && bcontent.equals("")) {
			model.addAttribute("list", dao.list(rowStart, rowEnd, searchKeyword,"4"));
		}
		
//		model.addAttribute("list",list);
		model.addAttribute("totRowcnt",total);
		model.addAttribute("searchVO",searchVO);
		
		return "list";
	}
	@RequestMapping("/write_view")
	public String write_view() {
		System.out.println("======write_view()======");
//		입력데이터폼화면으로 전환
		
		return "write_view";
	}
	
	@RequestMapping("/write")
	public String write(HttpServletRequest request,
			Model model) throws Exception {
		System.out.println("======write()======");
//		db에 데이터 저장
		//toss
//		model.addAttribute("request",request);
//		bServiceInter=new BWriteService();
//		bServiceInter.execute(model);
//		String bname=request.getParameter("bname");
//		String btitle=request.getParameter("btitle");
//		String bcontent=request.getParameter("bcontent");
		
		//upload
		
	      String attachPath="resources\\upload\\";
	      String uploadPath=request.getSession().getServletContext().getRealPath("/");
	      System.out.println("uploadPath : "+uploadPath);
	      String path=uploadPath+attachPath;
	      
	    //멀티파트폼 데이터로 받음
	      MultipartRequest req=
	            new MultipartRequest(request, path, 1024*1024*20, "utf-8",
	                  new DefaultFileRenamePolicy());
	      		//같은 이름이 있는 파일은 숫자를 넣어줘라 bo.jpg bo1.jpg
	      //maxPostSize 몇 메가 파일 크기
	      String bname=req.getParameter("bname");
	      String btitle=req.getParameter("btitle");
	      String bcontent=req.getParameter("bcontent");
	      String fname=req.getFilesystemName("file");

		System.out.println("filename : "+fname);
		if (fname==null) {
			fname="";
		}
		
		IDao dao=sqlSession.getMapper(IDao.class);
		dao.write(bname, btitle, bcontent,fname);
		
		
		return "redirect:list";
	}
	
	@RequestMapping("/content_view")
	public String content_view(HttpServletRequest request,
			Model model) {
		System.out.println("======content_view()======");
//		db에 데이터 저장
		//toss
//		model.addAttribute("request",request);
//		bServiceInter=new BContentViewService();
//		bServiceInter.execute(model);
		String sbid=request.getParameter("bid");
		IDao dao=sqlSession.getMapper(IDao.class);
		
		dao.upHit(sbid);
		
		BoardDto dto=dao.contentView(sbid);
		model.addAttribute("content_view",dto);
		
		return "content_view";
	}
	@RequestMapping("/download")
	public String download(HttpServletRequest request,HttpServletResponse response,
			Model model) throws Exception {
		System.out.println("download");
	
		String path=request.getParameter("p");
		String fname=request.getParameter("f");
		String bid=request.getParameter("bid");
		
		//down
		//header에 신호주기 이것은 
		response.setHeader("Content-Disposition", 
				"Attachment;filename="+URLEncoder.encode(fname,"utf-8"));
		
	    String attachPath="resources\\upload\\";
	    String realPath=request.getSession().getServletContext().getRealPath(attachPath)+"\\"+fname;
	    System.out.println("realPath : "+realPath);
	    
	    //stream연결
	    FileInputStream fin=new FileInputStream(realPath);
	    ServletOutputStream sout=response.getOutputStream();
	    
	    byte[] buf=new byte[1024];
	    int size=0;
	    while ((size=fin.read(buf,0,1024))!=-1) {
	    	sout.write(buf,0,size);
	    }
	    fin.close();
	    sout.close();
	    
		return "content_view?bid="+bid;
	}
	
	@RequestMapping("/content_update")
	public String content_update(HttpServletRequest request,
			Model model) {
		System.out.println("======content_update()======");
//		db에 데이터 저장
		//toss
//		model.addAttribute("request",request);
//		bServiceInter=new BContentViewService();
//		bServiceInter.execute(model);
		
		String sbid=request.getParameter("bid");
		IDao dao=sqlSession.getMapper(IDao.class);
		
		BoardDto dto=dao.contentView(sbid);
		model.addAttribute("content_view",dto);
		
		
		return "content_update";
	}
	@RequestMapping(method = RequestMethod.POST,value = "/modify")
	public String modify(HttpServletRequest request,
			Model model) {
		System.out.println("======modify()======");
//		db에 데이터 저장
		//toss
//		model.addAttribute("request",request);
//		bServiceInter=new BModifyService();
//		bServiceInter.execute(model);
		
		String sbid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		
		IDao dao=sqlSession.getMapper(IDao.class);
		dao.modify(sbid, bname, btitle, bcontent);
		
		return "redirect:list";
	}
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request,
			Model model) {
		System.out.println("======delete()======");
//		db에 데이터 삭제
		//toss
//		model.addAttribute("request",request);
//		bServiceInter=new BDeleteService();
//		bServiceInter.execute(model);
		String sbid=request.getParameter("bid");
		IDao dao=sqlSession.getMapper(IDao.class);
		dao.delete(sbid);
		
		return "redirect:list";
	}
	@RequestMapping("/reply_view")
	public String reply_view(HttpServletRequest request,
			Model model) {
		System.out.println("======reply_view()======");
//		db에 데이터 삭제
		//toss
//		model.addAttribute("request",request);
//		bServiceInter=new BReplyViewService();
//		bServiceInter.execute(model);
		String sbid=request.getParameter("bid");
		IDao dao=sqlSession.getMapper(IDao.class);
		BoardDto dto=dao.replyView(sbid);
		
		model.addAttribute("reply_view",dto);
		//답글쓰기 폼
		return "reply_view";
	}
	
	@RequestMapping("/reply")
	public String reply(HttpServletRequest request,
			Model model) {
		System.out.println("======reply()======");
//		db에 데이터 삭제
		//toss
//		model.addAttribute("request",request);
//		bServiceInter=new BReplyService();
//		bServiceInter.execute(model);
		String bid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		String bgroup=request.getParameter("bgroup");
		String bstep=request.getParameter("bstep");
		String bindent=request.getParameter("bindent");
		IDao dao=sqlSession.getMapper(IDao.class);
		
		dao.replyShape(bgroup, bstep);
		dao.reply(bid, bname, btitle, bcontent,
				bgroup, bstep, bindent);
		
		//답글쓰기 폼
		return "redirect:list";
	}
}
