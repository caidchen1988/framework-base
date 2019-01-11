package com.zwt.frameworkdatasource.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
* StudentModel
* table:student
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public class StudentModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 学生编号
     */
    private String stuNo;

    /**
     * 学生姓名
     */
    private String stuName;

    private String stuSex;

    /**
     * 学生生日
     */
    private Date stuBirthday;

    /**
     * 学生所在班级
     */
    private String stuClass;

    /**
     * @return 自增主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id 
	 *            自增主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 学生编号
     */
    public String getStuNo() {
        return stuNo;
    }

    /**
     * @param stuNo 
	 *            学生编号
     */
    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    /**
     * @return 学生姓名
     */
    public String getStuName() {
        return stuName;
    }

    /**
     * @param stuName 
	 *            学生姓名
     */
    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuSex() {
        return stuSex;
    }

    public void setStuSex(String stuSex) {
        this.stuSex = stuSex;
    }

    /**
     * @return 学生生日
     */
    public Date getStuBirthday() {
        return stuBirthday;
    }

    /**
     * @param stuBirthday 
	 *            学生生日
     */
    public void setStuBirthday(Date stuBirthday) {
        this.stuBirthday = stuBirthday;
    }

    /**
     * @return 学生所在班级
     */
    public String getStuClass() {
        return stuClass;
    }

    /**
     * @param stuClass 
	 *            学生所在班级
     */
    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        StudentModel other = (StudentModel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStuNo() == null ? other.getStuNo() == null : this.getStuNo().equals(other.getStuNo()))
            && (this.getStuName() == null ? other.getStuName() == null : this.getStuName().equals(other.getStuName()))
            && (this.getStuSex() == null ? other.getStuSex() == null : this.getStuSex().equals(other.getStuSex()))
            && (this.getStuBirthday() == null ? other.getStuBirthday() == null : this.getStuBirthday().equals(other.getStuBirthday()))
            && (this.getStuClass() == null ? other.getStuClass() == null : this.getStuClass().equals(other.getStuClass()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStuNo() == null) ? 0 : getStuNo().hashCode());
        result = prime * result + ((getStuName() == null) ? 0 : getStuName().hashCode());
        result = prime * result + ((getStuSex() == null) ? 0 : getStuSex().hashCode());
        result = prime * result + ((getStuBirthday() == null) ? 0 : getStuBirthday().hashCode());
        result = prime * result + ((getStuClass() == null) ? 0 : getStuClass().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}