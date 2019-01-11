package com.zwt.frameworkdatasource.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
* CourseModel
* table:course
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public class CourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 课程号
     */
    private String couNo;

    /**
     * 课程名称
     */
    private String couName;

    /**
     * 教师编号
     */
    private String teachNo;

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
     * @return 课程号
     */
    public String getCouNo() {
        return couNo;
    }

    /**
     * @param couNo 
	 *            课程号
     */
    public void setCouNo(String couNo) {
        this.couNo = couNo;
    }

    /**
     * @return 课程名称
     */
    public String getCouName() {
        return couName;
    }

    /**
     * @param couName 
	 *            课程名称
     */
    public void setCouName(String couName) {
        this.couName = couName;
    }

    /**
     * @return 教师编号
     */
    public String getTeachNo() {
        return teachNo;
    }

    /**
     * @param teachNo 
	 *            教师编号
     */
    public void setTeachNo(String teachNo) {
        this.teachNo = teachNo;
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
        CourseModel other = (CourseModel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCouNo() == null ? other.getCouNo() == null : this.getCouNo().equals(other.getCouNo()))
            && (this.getCouName() == null ? other.getCouName() == null : this.getCouName().equals(other.getCouName()))
            && (this.getTeachNo() == null ? other.getTeachNo() == null : this.getTeachNo().equals(other.getTeachNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCouNo() == null) ? 0 : getCouNo().hashCode());
        result = prime * result + ((getCouName() == null) ? 0 : getCouName().hashCode());
        result = prime * result + ((getTeachNo() == null) ? 0 : getTeachNo().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}