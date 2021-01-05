package com.onestop.fsf.scanner;

import com.onestop.fsf.api.IFileObserver;

import java.io.File;


public class NotifyThread extends Thread {
	IFileObserver observer;
	File			file;
	
	public NotifyThread(IFileObserver _observer, File _file) {
		observer = _observer;
		file = _file;
	}
	
	@Override
	public void run() {
		observer.update(file);
	}
}
