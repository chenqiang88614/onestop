package com.onestop.dao.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vividsolutions.jts.geom.Geometry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public class Satellitedata {

	private String id;
	/** 卫星标识 */
	private String satelliteid;
	/** 传感器标识 */
	private String sensorid;
	/** 接收站 */
	private String station;
	/** 拍摄时间 */
	private String imagedate;
	/** 生产时间 */
	private String productdate;
	/** 归档时间 */
	private String archivedate;
	/** 产品级别 */
	private String productlevel;
	/** 产品类型 */
	private String producttype;
	/** 产品格式 */
	private String productformat;
	/** 地图投影 */
	private String mapprojection ;
	/** 地球模型 */
	private String earthmodel;
	/** 质量码 */
	private String qualitycode;
	/** 波段数 */
	private String bands;
	/** 分辨率 */
	private Double pixelspacing;
	/** 云量 */
	private Double cloudpercent;
	/** 雪量 */
	private Double snowpercent;
	
	
	/** 景列 */
	private Integer scenepath;
	/** 景行 */
	private Integer scenerow;
	

	/** 中心纬度 */
	private Double centerlat;
	/** 中心经度 */
	private Double centerlong;
	/** 左上纬度 */
	private Double lefttoplat;
	/** 左上经度 */
	private Double lefttoplong;
	/** 右上纬度 */
	private Double righttoplat;
	/** 右上经度 */
	private Double righttoplong;
	/** 左下纬度 */
	private Double leftbottomlat;
	/** 左下经度 */
	private Double leftbottomlong;
	/** 右下纬度 */
	private Double rightbottomlat;
	/** 右下经度 */
	private Double rightbottomlong;
	/** 太阳方位角 */
	private Double sunazimuth;
	/** 太阳高度角 */
	private Double sunelevation;
	/** 文件大小 */
	private Double filesize;
	/** filename */
	private String filename;
	/** 数据存储路径 */
	private String datapath;

	/** 拇指图名称 */
	private String thumb;
	/** 浏览图名称 */
	private String browse;
	/** 元信息文件名 */
	private String metadata;

	/** 空间对象 */
	@JsonBackReference
//	@Type(type="org.hibernate.spatial.GeometryType")hibernate-spatial5.0以上无需添加此标签
	private Geometry geometry;
	
	/** 模板标识 */
	private String templateId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSatelliteid() {
		return satelliteid;
	}

	public void setSatelliteid(String satelliteid) {
		this.satelliteid = satelliteid;
	}

	public String getSensorid() {
		return sensorid;
	}

	public void setSensorid(String sensorid) {
		this.sensorid = sensorid;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getImagedate() {
		return imagedate;
	}

	public void setImagedate(String imagedate) {
		this.imagedate = imagedate;
	}

	public String getProductdate() {
		return productdate;
	}

	public void setProductdate(String productdate) {
		this.productdate = productdate;
	}

	public String getArchivedate() {
		return archivedate;
	}

	public void setArchivedate(String archivedate) {
		this.archivedate = archivedate;
	}

	public String getProductlevel() {
		return productlevel;
	}

	public void setProductlevel(String productlevel) {
		this.productlevel = productlevel;
	}

	public String getProducttype() {
		return producttype;
	}

	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}

	public String getProductformat() {
		return productformat;
	}

	public void setProductformat(String productformat) {
		this.productformat = productformat;
	}

	public String getMapprojection() {
		return mapprojection;
	}

	public void setMapprojection(String mapprojection) {
		this.mapprojection = mapprojection;
	}

	public String getEarthmodel() {
		return earthmodel;
	}

	public void setEarthmodel(String earthmodel) {
		this.earthmodel = earthmodel;
	}

	public String getQualitycode() {
		return qualitycode;
	}

	public void setQualitycode(String qualitycode) {
		this.qualitycode = qualitycode;
	}

	public String getBands() {
		return bands;
	}

	public void setBands(String bands) {
		this.bands = bands;
	}

	public Double getPixelspacing() {
		return pixelspacing;
	}

	public void setPixelspacing(Double pixelspacing) {
		this.pixelspacing = pixelspacing;
	}

	public Double getCloudpercent() {
		return cloudpercent;
	}

	public void setCloudpercent(Double cloudpercent) {
		this.cloudpercent = cloudpercent;
	}

	public Double getSnowpercent() {
		return snowpercent;
	}

	public void setSnowpercent(Double snowpercent) {
		this.snowpercent = snowpercent;
	}

	public Integer getScenepath() {
		return scenepath;
	}

	public void setScenepath(Integer scenepath) {
		this.scenepath = scenepath;
	}

	public Integer getScenerow() {
		return scenerow;
	}

	public void setScenerow(Integer scenerow) {
		this.scenerow = scenerow;
	}

	public Double getCenterlat() {
		return centerlat;
	}

	public void setCenterlat(Double centerlat) {
		this.centerlat = centerlat;
	}

	public Double getCenterlong() {
		return centerlong;
	}

	public void setCenterlong(Double centerlong) {
		this.centerlong = centerlong;
	}

	public Double getLefttoplat() {
		return lefttoplat;
	}

	public void setLefttoplat(Double lefttoplat) {
		this.lefttoplat = lefttoplat;
	}

	public Double getLefttoplong() {
		return lefttoplong;
	}

	public void setLefttoplong(Double lefttoplong) {
		this.lefttoplong = lefttoplong;
	}

	public Double getRighttoplat() {
		return righttoplat;
	}

	public void setRighttoplat(Double righttoplat) {
		this.righttoplat = righttoplat;
	}

	public Double getRighttoplong() {
		return righttoplong;
	}

	public void setRighttoplong(Double righttoplong) {
		this.righttoplong = righttoplong;
	}

	public Double getLeftbottomlat() {
		return leftbottomlat;
	}

	public void setLeftbottomlat(Double leftbottomlat) {
		this.leftbottomlat = leftbottomlat;
	}

	public Double getLeftbottomlong() {
		return leftbottomlong;
	}

	public void setLeftbottomlong(Double leftbottomlong) {
		this.leftbottomlong = leftbottomlong;
	}

	public Double getRightbottomlat() {
		return rightbottomlat;
	}

	public void setRightbottomlat(Double rightbottomlat) {
		this.rightbottomlat = rightbottomlat;
	}

	public Double getRightbottomlong() {
		return rightbottomlong;
	}

	public void setRightbottomlong(Double rightbottomlong) {
		this.rightbottomlong = rightbottomlong;
	}

	public Double getSunazimuth() {
		return sunazimuth;
	}

	public void setSunazimuth(Double sunazimuth) {
		this.sunazimuth = sunazimuth;
	}

	public Double getSunelevation() {
		return sunelevation;
	}

	public void setSunelevation(Double sunelevation) {
		this.sunelevation = sunelevation;
	}

	public Double getFilesize() {
		return filesize;
	}

	public void setFilesize(Double filesize) {
		this.filesize = filesize;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDatapath() {
		return datapath;
	}

	public void setDatapath(String datapath) {
		this.datapath = datapath;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getBrowse() {
		return browse;
	}

	public void setBrowse(String browse) {
		this.browse = browse;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
