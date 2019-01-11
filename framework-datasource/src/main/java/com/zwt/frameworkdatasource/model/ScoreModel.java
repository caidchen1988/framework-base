package com.zwt.frameworkdatasource.model;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
* ScoreModel
* table:score
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public class ScoreModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 学号
     */
    private String stuNo;

    /**
     * 课程号
     */
    private String couNo;

    /**
     * 成绩
     */
    private BigDecimal score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return 学号
     */
    public String getStuNo() {
        return stuNo;
    }

    /**
     * @param stuNo 
	 *            学号
     */
    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
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
     * @return 成绩
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * @param score 
	 *            成绩
     */
    public void setScore(BigDecimal score) {
        this.score = score;
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
        ScoreModel other = (ScoreModel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStuNo() == null ? other.getStuNo() == null : this.getStuNo().equals(other.getStuNo()))
            && (this.getCouNo() == null ? other.getCouNo() == null : this.getCouNo().equals(other.getCouNo()))
            && (this.getScore() == null ? other.getScore() == null : this.getScore().equals(other.getScore()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStuNo() == null) ? 0 : getStuNo().hashCode());
        result = prime * result + ((getCouNo() == null) ? 0 : getCouNo().hashCode());
        result = prime * result + ((getScore() == null) ? 0 : getScore().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}