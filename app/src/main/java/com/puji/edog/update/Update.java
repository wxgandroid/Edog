package com.puji.edog.update;

import java.io.Serializable;

/**
 */
public class Update implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2714512435139053795L;

	public final static String UTF8 = "UTF-8";

	private String VersionsNum;
	private String Title;
	private String Path;
	private String Description;

	public String getVersionsNum() {
		return VersionsNum;
	}

	public void setVersionsNum(String versionsNum) {
		VersionsNum = versionsNum;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}
