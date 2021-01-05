package com.onestop.ftpclient.spi;

import com.onestop.ftpclient.impl.FtpOperationImpl;
import com.onestop.ftpclient.util.FtpParam;


public class FtpOperationFactory {
	
	public static IFtpOperation getFtpOperation(){
		return new FtpOperationImpl();
	}
	
	public static IFtpOperation getFtpOperation(FtpParam param){
		//IFtpOperation ftp = new FtpOperationImpl();
		IFtpOperation ftp = new FtpOperationImpl();
		ftp.setParam(param);
		return ftp;
	}
}
