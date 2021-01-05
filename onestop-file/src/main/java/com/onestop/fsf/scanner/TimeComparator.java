package com.onestop.fsf.scanner;

import java.io.File;
import java.util.Comparator;

public class TimeComparator implements Comparator<File> {

	@Override
	public int compare(File f1, File f2) {
		long result = f1.lastModified() - f2.lastModified();
		if (result > 0) {
			return 1;
		} else if (result == 0) {
			return 0;
		} else {
			return -1;
		}
	}

}
