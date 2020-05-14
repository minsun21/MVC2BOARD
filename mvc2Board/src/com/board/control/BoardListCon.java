package com.board.control;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.board.bean.BoardBean;
import com.board.dao.BoardDAO;

@WebServlet("/BoardListCon.do")
public class BoardListCon extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		reqPro(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		reqPro(request, response);
	}

	protected void reqPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("euc-kr");

		// 화면에 보여질 게시글 개수
		int pageSize = 10;

		// 현재 보여지고 있는 페이지의 넘버 값
		String pageNum = request.getParameter("pageNum");
		if (pageNum == null)
			pageNum = "1";

		// 전체 게시글의 개수
		int count = 0;

		// jsp 페이지 내에서 보여질 넘버링 숫자값
		int number = 0;

		// 현재 보여지고 있는 페이지 문자를 숫자로
		int currentPage = Integer.parseInt(pageNum);

		BoardDAO dao = new BoardDAO();
		count = dao.getAllListCount();

		// 현재 보여질 페이지 시작 번호 설정
		int startRow = (currentPage - 1) * pageSize + 1;
		int endRow = currentPage * pageSize;

		List<BoardBean> list = dao.getAllBoard(startRow, endRow);
		number = count - (currentPage - 1) * pageSize;
		
		request.setAttribute("boardList", list);
		request.setAttribute("number", number);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("count", count);
		request.setAttribute("pageSize", pageSize);
		
		RequestDispatcher rd = request.getRequestDispatcher("BoardList.jsp");
		rd.forward(request,response);
	}
}
