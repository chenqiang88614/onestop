package com.onestop.fsf.scanner;

import com.onestop.fsf.api.IFileMatcher;
import com.onestop.fsf.api.IFileObserver;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fuchl 文件扫描配置类
 */
@Slf4j
public class ConfigFileParser {
    private String configFileName;
    private static ConfigFileParser instance = null;
    private int timeInterval;
    private boolean subPathFlag = false;
    /**
     * 未匹配的文件存放路径
     */
    private String ignoreFilePath = "";
    private List<PathPair> scanPathList = new ArrayList<PathPair>();
    private List<IFileObserver> observerList = new ArrayList<IFileObserver>();
    private List<IFileMatcher> filematcherList = new ArrayList<IFileMatcher>();
    private List<IFileMatcher> ignoreFilematcherList = new ArrayList<IFileMatcher>();
    private Document doc = null;

    /**
     * @param configFileName
     */
    public ConfigFileParser(String configFileName) {
        this.configFileName = configFileName;
        init();
    }

    private void init() {
        doc = this.getDocument();
        setFileMatcherList(doc);
        setObserverList(doc);
        setScanPathList(doc);
        setInterval(doc);
        setSubPathFlag(doc);
        setIgnoreFilePath(doc);
    }

    /**
     * @param configFileName
     * @return configFileName的唯一实例
     */
    public static ConfigFileParser getInstance(String configFileName) {
        if (instance == null) {
            instance = new ConfigFileParser(configFileName);
        }
        return instance;
    }

    /**
     * @return
     */
    public String getConfigFileName() {
        return configFileName;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInteger(Integer time) {
        this.timeInterval = time;
    }

    public boolean getSubPathFlag() {
        return subPathFlag;
    }

    public List<PathPair> getScanPathList() {
        return scanPathList;
    }

    public void setScanPathList(List<PathPair> pathlist) {
        this.scanPathList = pathlist;
    }

    public List<IFileObserver> getObserverList() {
        return observerList;
    }

    public List<IFileMatcher> getFilematcherList() {
        return filematcherList;
    }

    public List<IFileMatcher> getIgnoreFilematcherList() {
        return ignoreFilematcherList;
    }

    public String getIgnoreFilePath() {
        return ignoreFilePath;
    }

    public void setIgnoreFilePath(String ignorePath) {
        this.ignoreFilePath = ignorePath;
    }

    public boolean updateFile() {
        Element root = doc.getRootElement();

        root.getChild("TimeInterval").setText(this.timeInterval + "");
        root.getChild("IgnoreFilePath").setText(this.ignoreFilePath);
        String now = "";
        for (PathPair par : scanPathList) {
            String pat = par.getSrcPath() + "," + par.getDesPath();
            now += pat + ";";
        }
        root.getChild("Path").setText(now);

        XMLOutputter out = new XMLOutputter();
        OutputStream fos;
        try {
            fos = new FileOutputStream(new File(this.configFileName));
            out.output(doc, fos);
            return true;
        } catch (FileNotFoundException e) {
            log.error("update fsfScan configfile failed,reason:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("update fsfScan configfile failed,reason:" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Document getDocument() {
        SAXBuilder sb = new SAXBuilder();
        String source = configFileName;
        Document doc = null;
        try {
            doc = sb.build(new FileInputStream(source));
        } catch (JDOMException | IOException e) {

            e.printStackTrace();
        }
        return doc;
    }

    private void setIgnoreFilePath(Document doc) {
        Element root = doc.getRootElement();
        if (root.getChild("IgnoreFilePath") != null) {
            ignoreFilePath = root.getChildText("IgnoreFilePath");
            if (ignoreFilePath.startsWith("./")) {
//				ignoreFilePath =FileScannerImpl.class.getResource("").getPath().split("WEB-INF")[0].substring(1)+ignoreFilePath.substring(2);
                ignoreFilePath = ignoreFilePath.substring(2);
            }
        }
    }

    private void setScanPathList(Document doc) {
        Element root = doc.getRootElement();
        String s = root.getChild("Path").getText();
        String[] ss = s.split(";");
        int i;
        for (i = 0; i < ss.length; i++) {
            String src = "";
            String des = "";
            src = ss[i].split(",")[0];
            des = ss[i].split(",")[1];
            if (src.startsWith("./")) {
                src = src.substring(2);
            }
            if (des.startsWith("./")) {
                des = des.substring(2);
            }
            PathPair p = new PathPair(src, des);
            //创建目录
            File path_1 = new File(src);
            File path_2 = new File(des);
            if (!path_1.exists() || !path_2.exists()) {
                path_1.mkdirs();
                path_2.mkdirs();
            }
            scanPathList.add(p);
        }
    }

    @SuppressWarnings("unchecked")
    private void setObserverList(Document doc) {
        Element root = doc.getRootElement();
        int i;
        IFileObserver o = null;
        List<Element> list = root.getChild("FileObserver").getChildren();
        for (i = 0; i < list.size(); i++) {
            Element e = list.get(i);
            String className = e.getChildText("ClassName");
            String flag = e.getChildText("Flag");
            if ("true".equals(flag)) {
                String parm = e.getChildText("Parm");
                String matchers = e.getChildText("Matchers");
                try {
                    o = (IFileObserver) Class.forName(className).newInstance();
                } catch (InstantiationException e1) {
                    log.error(e1.toString());
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    log.error(e1.toString());
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    log.error(e1.toString());
                    e1.printStackTrace();
                }
                o.setParmString(parm);
                o.setMatchers(matchers);
                observerList.add(o);

            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setFileMatcherList(Document doc) {
        Element root = doc.getRootElement();
        int i, j;
        IFileMatcher m = null;
        List<Element> list = root.getChild("FileMatcher").getChildren();
        for (i = 0; i < list.size(); i++) {
            Element e = list.get(i);
            String className = e.getChildText("ClassName");
            String flag = e.getChildText("Flag");
            if ("true".equals(flag) || "ignore".equals(flag)) {
                String[] parm = e.getChildText("Parm").split(";");
                String name = e.getChildText("Name");
                for (j = 0; j < parm.length; j++) {
                    try {
                        m = (IFileMatcher) Class.forName(className).newInstance();
                    } catch (InstantiationException e1) {
                        log.error(e1.toString());
                        e1.printStackTrace();
                    } catch (IllegalAccessException e1) {
                        log.error(e1.toString());
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        log.error(e1.toString());
                        e1.printStackTrace();
                    }
                    m.setParmString(parm[j]);
                    m.setName(name);
                    if ("true".equals(flag)) {
                        filematcherList.add(m);// add to filematcherList
                    } else {
                        ignoreFilematcherList.add(m);
                    }


                }
            }
        }
    }

    private void setInterval(Document doc) {
        Element root = doc.getRootElement();
        Element eInterval = root.getChild("TimeInterval");
        this.timeInterval = new Integer(eInterval.getText());
    }

    private void setSubPathFlag(Document doc) {
        Element root = doc.getRootElement();
        Element eTtempSubPath = root.getChild("TempSubPath");
        this.subPathFlag = "true".equals(eTtempSubPath.getText());
    }
}
