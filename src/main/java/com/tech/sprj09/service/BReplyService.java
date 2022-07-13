package com.tech.sprj09.service;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tech.sprj09.dao.BoardDao;
import com.tech.sprj09.dto.BoardDto;

public class BReplyService implements BServiceInter{
	@Override
	public void execute(Model model) {
		System.out.println(">>>BReplyService");
		
//		map으로변환
		Map<String, Object> map=model.asMap();
		//map에서 request 빼오기
		HttpServletRequest request=
				(HttpServletRequest) map.get("request");
		String bid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		String bgroup=request.getParameter("bgroup");
		String bstep=request.getParameter("bstep");
		String bindent=request.getParameter("bindent");
		
//		System.out.println("bid >>>>>"+bid);
//		System.out.println("btitle >>>>>"+btitle);
		
		BoardDao dao=new BoardDao();
		dao.reply(bid,bname,btitle,bcontent,
				bgroup,bstep,bindent);
//		
		
	}

}
