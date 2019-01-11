package com.zwt.frameworkdatasource.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
* TeacherModel
* table:teacher
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public class TeacherModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 教师编号
     */
    private String teachNo;

    /**
     * 教师姓名
     */
    private String teachName;

    /**
     * 教师性别
     */
    private String teachSex;

    /**
     * 教师生日
     */
    private Date teachBirthday;

    /**
     * 教师所在部门
     */
    private String teachDepart;

    /**
     * 教师职称
     */
    private String teachProf;

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

    /**
     * @return 教师姓名
     */
    public String getTeachName() {
        return teachName;
    }

    /**
     * @param teachName 
	 *            教师姓名
     */
    public void setTeachName(String teachName) {
        this.teachName = teachName;
    }

    /**
     * @return 教师性别
     */
    public String getTeachSex() {
        return teachSex;
    }

    /**
     * @param teachSex 
	 *            教师性别
     */
    public void setTeachSex(String teachSex) {
        this.teachSex = teachSex;
    }

    /**
     * @return 教师生日
     */
    public Date getTeachBirthday() {
        return teachBirthday;
    }

    /**
     * @param teachBirthday 
	 *            教师生日
     */
    public void setTeachBirthday(Date teachBirthday) {
        this.teachBirthday = teachBirthday;
    }

    /**
     * @return 教师所在部门
     */
    public String getTeachDepart() {
        return teachDepart;
    }

    /**
     * @param teachDepart 
	 *            教师所在部门
     */
    public void setTeachDepart(String teachDepart) {
        this.teachDepart = teachDepart;
    }

    /**
     * @return 教师职称
     */
    public String getTeachProf() {
        return teachProf;
    }

    /**
     * @param teachProf 
	 *            教师职称
     */
    public void setTeachProf(String teachProf) {
        this.teachProf = teachProf;
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
        TeacherModel other = (TeacherModel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTeachNo() == null ? other.getTeachNo() == null : this.getTeachNo().equals(other.getTeachNo()))
            && (this.getTeachName() == null ? other.getTeachName() == null : this.getTeachName().equals(other.getTeachName()))
            && (this.getTeachSex() == null ? other.getTeachSex() == null : this.getTeachSex().equals(other.getTeachSex()))
            && (this.getTeachBirthday() == null ? other.getTeachBirthday() == null : this.getTeachBirthday().equals(other.getTeachBirthday()))
            && (this.getTeachDepart() == null ? other.getTeachDepart() == null : this.getTeachDepart().equals(other.getTeachDepart()))
            && (this.getTeachProf() == null ? other.getTeachProf() == null : this.getTeachProf().equals(other.getTeachProf()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTeachNo() == null) ? 0 : getTeachNo().hashCode());
        result = prime * result + ((getTeachName() == null) ? 0 : getTeachName().hashCode());
        result = prime * result + ((getTeachSex() == null) ? 0 : getTeachSex().hashCode());
        result = prime * result + ((getTeachBirthday() == null) ? 0 : getTeachBirthday().hashCode());
        result = prime * result + ((getTeachDepart() == null) ? 0 : getTeachDepart().hashCode());
        result = prime * result + ((getTeachProf() == null) ? 0 : getTeachProf().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}