package com.tech.sprj09.service;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.tech.sprj09.dao.BoardDao;
import com.tech.sprj09.dto.BoardDto;

public class BListService implements BServiceInter{
	@Override
	public void execute(Model model) {
		System.out.println(">>>BListService");
		
		BoardDao dao=new BoardDao();
		ArrayList<BoardDto> dtos=dao.list();//db에서 글전체를 가져오기
		//리턴받은 내용을 모델에 담기
		model.addAttribute("list",dtos);
		
	}

}
