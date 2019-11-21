package com.cy.pj.sys.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.service.EditorService;


@RestController
@RequestMapping("/user/")
public class EditorController {
	@Autowired
	private EditorService editorService;
	@RequestMapping("doinsertEditTxt")
	public JsonResult doinsertEditTxt(@RequestParam("content") String content,HttpServletRequest request) throws FileNotFoundException {
		editorService.saveEditor(content);
		String realPath=request.getSession().getServletContext().getRealPath("/");
		String path1=ResourceUtils.getURL("classpath:").getPath();
		System.out.println("REAL-PATH+++"+realPath);
		System.out.println("path1++++++"+path1);
		return new JsonResult("save ok");
	}
	@RequestMapping("doInsertEditPic")
	public Map<String, String> doInsertEditPic(@RequestParam(value="myFileName") MultipartFile mf,HttpServletRequest request) {
		String realPath=request.getSession().getServletContext().getRealPath("myFileName");
		System.out.println("realPath"+realPath);
		//获取源文件
		String filename=mf.getOriginalFilename();
		System.out.println("filename++"+filename);
		String[] names=filename.split("\\.");
		System.out.println("names"+Arrays.toString(names));
		String uploadFileName=System.currentTimeMillis()+"."+names[names.length-1];
		File targetFile=new File("E:\\upload",uploadFileName);
		//开始从源文件拷贝到目标文件
		try {
			mf.transferTo(targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, String> map=new HashMap<String, String>();
		map.put("error", "1");
		System.out.println("uploadFileName"+uploadFileName);
		map.put("data", "http://localhost:80/static/"+uploadFileName);
		map.put("url", "http://localhost:80/static/"+uploadFileName);
		return map;
	}
}
