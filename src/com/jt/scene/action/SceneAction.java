package com.jt.scene.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jt.bean.gateway.PageMsg;
import com.jt.gateway.util.CMyString;
import com.jt.scene.bean.Scene;
import com.jt.scene.service.SceneService;

@Controller
@RequestMapping("/scene")
public class SceneAction {
	private SceneService sceneService;
	private PageMsg msg;
	private Gson gson;

	public SceneAction() {
		msg = new PageMsg();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

	@RequestMapping(value = "/addScene.do")
	public void addJob(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		Scene scene = null;
		// 参数是否合法:同时包含场景名称、入口词、出口词即可
		String sceneName = request.getParameter("sceneName");
		String enterWords = request.getParameter("enterWords");
		String outWords = request.getParameter("outWords");

		if (sceneName != null && sceneName.length() > 0 && enterWords != null && enterWords.length() > 0
				&& outWords != null && outWords.length() > 0) {
			try {
				// 该场景是否已经存在
				scene = sceneService.getSceneByName(sceneName);
				if (scene != null) {
					msg.setMsg("新增场景[" + scene.getSceneName() + "]失败，错误信息为:[该场景已存在，无法新增]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
				// 不存在则写入数据库
				Date date = new Date();
				scene = new Scene(null, sceneName, enterWords, outWords, date, null);
				sceneService.addScene(scene);

				msg.setMsg("新增场景[" + sceneName + "]成功");
				msg.setSig(true);
				pw.print(gson.toJson(msg));
				return;
			} catch (Exception e) {
				e.printStackTrace();
				msg.setMsg("新增场景[" + sceneName + "]失败，错误信息为:[" + e.getMessage() + "]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
		} else {
			msg.setMsg("新增场景失败，错误信息为:[参数错误]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}

	}

	// 删除任务
	@RequestMapping(value = "delScene.do")
	public void delScene(HttpServletRequest request, HttpServletResponse response) {
		msg = new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		Integer sceneId = 0;
		try {
			sceneId = Integer.parseInt(CMyString.getStrNotNullor0(request.getParameter("sceneId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("删除场景id=[" + sceneId + "]失败,错误信息为[转换sceneId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		Scene scene = sceneService.getSceneById(sceneId);
		if (sceneId != 0 && scene != null) {
			try {
				// 删除scene信息
				sceneService.deleteScene(scene);
			} catch (Exception e) {
				msg.setMsg("删除场景[" + scene.getSceneName() + "]失败，错误信息:[" + e.getMessage() + "]");
				pw.print(gson.toJson(msg));
				return;
			}
			msg.setSig(true);
			msg.setMsg("删除场景[" + scene.getSceneName() + "]成功");
			pw.print(gson.toJson(msg));
			return;
		} else {
			msg.setMsg("删除场景id=[" + sceneId + "]失败,错误信息为[未获得场景ID或不存在指定的场景]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}

	// 修改场景
	@RequestMapping(value = "updateScene.do")
	public void updateScene(HttpServletRequest request, HttpServletResponse response) {
		msg = new PageMsg();
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		Scene scene = null;
		// 参数是否合法:同时包含场景ID、场景名称、入口词、出口词即可
		String sceneName = request.getParameter("sceneName");
		String enterWords = request.getParameter("enterWords");
		String outWords = request.getParameter("outWords");

		Integer sceneId = 0;
		try {
			sceneId = Integer.parseInt(CMyString.getStrNotNullor0(request.getParameter("sceneId"), "0"));
		} catch (Exception e) {
			e.printStackTrace();
			msg.setMsg("修改场景id=[" + sceneId + "]失败,错误信息为[转换sceneId类型失败]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
		scene = sceneService.getSceneById(sceneId);
		if (sceneId != 0 && scene != null) {
			if (sceneName != null && sceneName.length() > 0 && enterWords != null && enterWords.length() > 0
					&& outWords != null && outWords.length() > 0) {
				try {
					// 该场景不存在则不能修改
					scene = sceneService.getSceneById(sceneId);
					if (scene == null) {
						msg.setMsg("修改场景[" + sceneId + "]失败，错误信息为:[该场景不存在，无法修改]");
						msg.setSig(false);
						pw.print(gson.toJson(msg));
						return;
					}
					// 场景存在，将场景写入到数据库
					Date date = new Date();
					scene.setSceneName(sceneName);
					scene.setEnterWords(enterWords);
					scene.setOutWords(outWords);
					scene.setUpdateTime(date);
					msg.setMsg("修改场景[" + sceneName + "]成功");
					msg.setSig(true);
					pw.print(gson.toJson(msg));
					return;
				} catch (Exception e) {
					e.printStackTrace();
					msg.setMsg("修改[" + sceneName + "]场景失败，错误信息为:[" + e.getMessage() + "]");
					msg.setSig(false);
					pw.print(gson.toJson(msg));
					return;
				}
			} else {
				msg.setMsg("修改场景id=[" + sceneId + "]失败,错误信息为[参数错误]");
				msg.setSig(false);
				pw.print(gson.toJson(msg));
				return;
			}
		} else {
			msg.setMsg("修改场景id=[" + sceneId + "]失败,错误信息为[未获得场景ID或不存在指定的场景]");
			msg.setSig(false);
			pw.print(gson.toJson(msg));
			return;
		}
	}

	@RequestMapping("/listScenes.do")
	public void listScenes(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		msg = new PageMsg();
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		List<Scene> list = sceneService.getAllScenes();
		pw.print(gson.toJson(list));
	}

}
