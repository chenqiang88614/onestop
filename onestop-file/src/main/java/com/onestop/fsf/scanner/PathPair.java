package com.onestop.fsf.scanner;

/**
 * @author fuchl
 *
 */
public class PathPair {

	private String srcPath;
	private String desPath;

	/**
	 * @param s 源路径
	 * @param d 目标路径
	 */
	public PathPair(String s, String d) {
		srcPath = s;
		desPath = d;
	}

	/**
	 * @return 源路径
	 */

	public String getSrcPath() {
		return srcPath;
	}

	/**
	 * @return 目标路径
	 */
	public String getDesPath() {
		return desPath;
	}

}
