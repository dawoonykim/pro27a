package com.myspring.pro27.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.myspring.pro27.member.service.MemberService;
import com.myspring.pro27.member.vo.MemberVO;

@Controller("memberController")
public class MemberControllerImpl extends MultiActionController implements MemberController {

	@Autowired
	private MemberService memberService;

	private MemberVO memberVO;

	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);
	/*
	 * public void setMemberService(MemberService memberService) {
	 * this.memberService = memberService; }
	 */

	@Override
	@RequestMapping(value = "/member/listMembers.do")
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = getViewName(request);
//		System.out.println("view name : " + viewName);
		List membersList = memberService.listMembers();
		ModelAndView mav = new ModelAndView(viewName);
//		mav.setViewName("listMembers");
		mav.addObject("membersList", membersList);

		return mav;
	}

	@Override
	@RequestMapping(value = "/member/memberForm.do")
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewname = getViewName(request);
		return new ModelAndView(viewname);
	}

	@Override
	@RequestMapping(value = "/member/addMember.do", method = RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("member") MemberVO memberVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		memberService.addMember(memberVO);
		List membersList = memberService.listMembers();
		// PRG 패턴(Post-Redirect-Get)
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		mav.addObject("membersList", membersList);

		return mav;
	}

	@Override
	@RequestMapping(value = "/member/updateMemberForm.do")
	public ModelAndView updateMemberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewname = getViewName(request);
		return new ModelAndView(viewname);
	}

	@Override
	@RequestMapping(value = "/member/updateMember.do", method = RequestMethod.POST)
	public ModelAndView updateMember(@ModelAttribute("member") MemberVO memberVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		memberService.updateMember(memberVO);
		List membersList = memberService.listMembers();
		// PRG 패턴(Post-Redirect-Get)
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		mav.addObject("membersList", membersList);

		return mav;
	}

	@Override
	@RequestMapping(value = "/member/delMember.do", method = RequestMethod.GET)
	public ModelAndView deleteMember(String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		memberService.delMember(id);
		List membersList = memberService.listMembers();
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		mav.addObject("membersList", membersList);

		return mav;
	}

	// 요청명과 같은 파일명을 나오게 하도록
	public String getViewName(HttpServletRequest request) throws Exception {
		String contextPath = request.getContextPath();
//		System.out.println("contextPath : " + contextPath);

		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
//		System.out.println("uri : " + uri);

		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
//			System.out.println("null인 경우 uri : " + uri);
		}

		int begin = 0;

		if (!((contextPath == null) || ("".equals(contextPath)))) {
			begin = contextPath.length();
//			System.out.println("begin : " + begin);
		}

		int end;

//		System.out.println(uri.indexOf(";"));
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
		}
		System.out.println("end : " + end);
		String fileName = uri.substring(begin, end);
//		System.out.println("fileName : "+fileName);

		if (fileName.indexOf(".") != -1) {
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
		}

		if (fileName.lastIndexOf("/") != -1) {
			fileName = fileName.substring(fileName.lastIndexOf("/"), fileName.length());
		}
		return fileName;
	}

}
