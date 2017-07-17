package com.wch.libs.views.FileExplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wch.libs.activity.BaseActivity;


public class FileUtils {

	private static String m_sCurrentPath = Global.ROOT_PATH;

	public static final int BACK = 70;

	public static String getSubDictory(int a_Index) {
		File t_File = new File(m_sCurrentPath);
		File[] t_Files = t_File.listFiles();

		if (t_Files[a_Index].isDirectory()) {
			String l_Path = t_Files[a_Index].getPath();

			return l_Path;
		}
		return null;
	}

	public static String getSubDictory(String a_FileName) {
		File l_File = new File(a_FileName);

		if (!l_File.exists())
			return null;
		if (l_File.isDirectory()) {
			return l_File.getPath();
		}
		return null;
	}

	public static String getCurPath() {
		return m_sCurrentPath;
	}

	public static boolean setCurPath(String a_sPath) {
		File t_File = new File(a_sPath);

		if (t_File.isDirectory()) {
			m_sCurrentPath = t_File.getPath();
			return true;
		}
		return false;
	}

	public static String getParentPath() {
		File t_File = new File(m_sCurrentPath);
		if (m_sCurrentPath.equals("/"))
			return "/";
		return t_File.getParent();
	}

	public static List<String> getCurDirFileNames() {

		File t_File = new File(m_sCurrentPath);
		File[] t_Files = t_File.listFiles();
		List<String> l_FileNames = new LinkedList<String>();

		if (t_Files == null)
			return null;

		l_FileNames.clear();

		for (int i = 0; i < t_Files.length; i++) {
			l_FileNames.add(t_Files[i].getAbsolutePath());
		}
		sortByType(l_FileNames);

		return l_FileNames;
	}

	public static String getFileName(String a_Path) {

		int l_Index = a_Path.lastIndexOf('/');

		return a_Path.substring(l_Index + 1);
	}

	public static int getFileType(String a_FilePath) {
		File l_File = new File(a_FilePath);

		if (l_File.isDirectory())
			return Global.FileType_Dir;

		String l_FileName = l_File.getName();

		for (int i = 0; i < Global.FileTypes.length; i++) {

			int j = checkStringEnds(l_FileName, Global.FileTypes[i]);

			if (j == -1)
				continue;

			return Global.TypeStart[i] + j;
		}

		return Global.FileType_Other;

	}

	public static void sortByName(List<String> a_FileNames) {
		Collections.sort(a_FileNames);
	}

	public static void sortByType(List<String> a_FileNames) {
		List<String> Res = new LinkedList<String>();

		Collections.sort(a_FileNames);

		// Dir
		for (int i = 0; i < a_FileNames.size(); i++) {
			if (getFileType(a_FileNames.get(i)) == Global.FileType_Dir)
				Res.add(a_FileNames.get(i));
		}

		for (int j = 0; j < Global.FileTypes.length; j++) {
			for (int k = 0; k < Global.FileTypes[j].length; k++) {
				for (int i = 0; i < a_FileNames.size(); i++) {
					if (a_FileNames.get(i).endsWith(Global.FileTypes[j][k]))
						Res.add(a_FileNames.get(i));
				}
			}
		}

		for (int i = 0; i < a_FileNames.size(); i++) {
			if (getFileType(a_FileNames.get(i)) == Global.FileType_Other)
				Res.add(a_FileNames.get(i));
		}

		a_FileNames.clear();

		for (int i = 0; i < Res.size(); i++)
			a_FileNames.add(Res.get(i));
	}

	public static boolean removeFile(String a_FileName) {
		File l_File = new File(a_FileName);
		if (l_File.exists()) {

			if (l_File.isDirectory()) {
				File[] l_Files = l_File.listFiles();
				for (int i = 0; i < l_Files.length; i++) {
					removeFile(l_Files[i].getAbsolutePath());
				}
			}

			return l_File.delete();
		}

		return false;
	}

	public static boolean isImage(String a_FileName) {
		File l_File = new File(a_FileName);
		boolean issuccess = false;
		if (l_File.isFile()
				|| checkEndsWithInStringArray(a_FileName,
						Global.FileTypes[Global.IndexImage])) {
			issuccess = true;
		}
		return issuccess;
	}

	public static boolean openFile(String a_FileName, Context a_Context) {
		File l_File = new File(a_FileName);
		boolean issuccess = false;
		if (l_File.isFile()) {
			Intent intent;
			issuccess = true;
			if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexImage])) {
				intent = MToolBox.getImageFileIntent(l_File);
				((BaseActivity) a_Context).startActivityForResult(intent, BACK);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexAudio])) {
				intent = MToolBox.getHtmlFileIntent(l_File);
				((BaseActivity) a_Context).startActivityForResult(intent, BACK);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexPackage])) {
				intent = MToolBox.getApkFileIntent(l_File);
				((BaseActivity) a_Context).startActivityForResult(intent, BACK);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexAudio])) {
				intent = MToolBox.getAudioFileIntent(l_File);
				a_Context.startActivity(intent);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexVideo])) {
				intent = MToolBox.getVideoFileIntent(l_File);
				a_Context.startActivity(intent);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexText])) {
				intent = MToolBox.getTextFileIntent(l_File);
				a_Context.startActivity(intent);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexPDF])) {
				intent = MToolBox.getPdfFileIntent(l_File);
				a_Context.startActivity(intent);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexWord])) {
				intent = MToolBox.getWordFileIntent(l_File);
				a_Context.startActivity(intent);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexExcel])) {
				intent = MToolBox.getExcelFileIntent(l_File);
				a_Context.startActivity(intent);
			} else if (checkEndsWithInStringArray(a_FileName,
					Global.FileTypes[Global.IndexPPT])) {
				intent = MToolBox.getPPTFileIntent(l_File);
				a_Context.startActivity(intent);
			} else {
				Dialog l_msgDlg = new Dialog(a_Context);
				l_msgDlg.setTitle("Warning");
				TextView l_Text = new TextView(a_Context);
				l_Text.setText("No program");
				l_msgDlg.setContentView(l_Text);
				l_msgDlg.show();
				l_msgDlg.setCanceledOnTouchOutside(true);
			}
		}

		return issuccess;
	}

	public static boolean checkEndsWithInStringArray(String a_CheckItem,
			String[] a_Array) {

		for (String l_Item : a_Array)

			if (a_CheckItem.endsWith(l_Item))
				return true;

		return false;
	}

	private static int checkStringEnds(String a_CheckItem, String[] a_Array) {

		for (int i = 0; i < a_Array.length; i++)

			if (a_CheckItem.endsWith(a_Array[i]))
				return i;

		return -1;
	}

	public static void renameFile(String a_FileName, String a_NewName) {
		File l_File = new File(a_FileName);
		File l_NewFile = new File(a_NewName);
		l_File.renameTo(l_NewFile);
	}

	public static boolean copyFile(String a_FilePath, String a_DestPath,
			ProgressBar a_ProgressBar) {

		File l_File = new File(a_FilePath);
		if (l_File.getParent().equals(a_DestPath))
			return false;
		String l_FileName = l_File.getName();

		File l_File2 = new File(a_DestPath + "/" + l_FileName);

		long l_FileLen = l_File.length();

		if (l_File.isDirectory()) {
			File[] l_Files = l_File.listFiles();
			l_File2.mkdirs();

			for (int i = 0; i < l_Files.length; i++) {
				boolean l_Rc = copyFile(l_Files[i].getAbsolutePath(),
						l_File2.getAbsolutePath(), a_ProgressBar);
				if (!l_Rc)
					return false;
			}
			return true;
		}
		if (l_File2.exists()) {
			return true;
		}
		try {
			FileInputStream fis = new FileInputStream(l_File);
			FileOutputStream fos = new FileOutputStream(l_File2);

			byte l_Buffer[] = new byte[1024 * 1024];

			l_FileLen = l_FileLen / 1024;
			int max = (int) (l_FileLen / 1024);

			a_ProgressBar.setMax(max);

			int len = 0;
			int count = 0;

			while (len != -1) {
				count++;
				len = fis.read(l_Buffer);
				fos.write(l_Buffer);
				a_ProgressBar.setProgress(count);
			}
			fis.close();
			fos.close();

		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
