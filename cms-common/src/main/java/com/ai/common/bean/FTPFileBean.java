package com.ai.common.bean;

import java.io.Serializable;

public class FTPFileBean implements Comparable<FTPFileBean>, Serializable {

	private static final long serialVersionUID = 1L;
	
	public static double SIZE_KB = 1024;
    public static double SIZE_MB = 1024 * 1024;
    public static double SIZE_GB = 1024 * 1024 * 1024;
    public static java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");

    private Integer no;

    private Integer type;

    private String name;

    private Long size;

    private String sizeS;

    private String path;

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
        if (size > SIZE_GB) {
            setSizeS(df.format(size / SIZE_GB) + "GB");
        } else if (size > SIZE_MB) {
            setSizeS(df.format(size / SIZE_MB) + "MB");
        } else {
            setSizeS(df.format(size / SIZE_KB) + "KB");
        }
    }

    public String getSizeS() {
        return sizeS;
    }

    public void setSizeS(String sizeS) {
        this.sizeS = sizeS;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(FTPFileBean o) {
        if (this.getType() < o.getType()) {
            return -1;
        } else if (this.getType() == o.getType()) {
            return this.getName().compareTo(o.getName());
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "FTPFileBean [type=" + type + ", name=" + name + ", size=" + size + ", sizeS=" + sizeS + ", path=" + path + "]";
    }

}
