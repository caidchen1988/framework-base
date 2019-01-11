package com.zwt.frameworkdatasource.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
* TeacherCriteria 条件查询类.
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public class TeacherCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer mysqlOffset;

    protected Integer mysqlLength;

    public TeacherCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * @param mysqlOffset 
	 *            指定返回记录行的偏移量<br>
	 *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15
     */
    public void setMysqlOffset(int mysqlOffset) {
        this.mysqlOffset=mysqlOffset;
    }

    /**
     * @param mysqlLength 
	 *            指定返回记录行的最大数目<br>
	 *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15
     */
    public Integer getMysqlOffset() {
        return mysqlOffset;
    }

    /**
     * @param mysqlOffset 
	 *            指定返回记录行的偏移量<br>
	 *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15
     */
    public void setMysqlLength(int mysqlLength) {
        this.mysqlLength=mysqlLength;
    }

    /**
     * @param mysqlLength 
	 *            指定返回记录行的最大数目<br>
	 *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15
     */
    public Integer getMysqlLength() {
        return mysqlLength;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTeachNoIsNull() {
            addCriterion("teach_no is null");
            return (Criteria) this;
        }

        public Criteria andTeachNoIsNotNull() {
            addCriterion("teach_no is not null");
            return (Criteria) this;
        }

        public Criteria andTeachNoEqualTo(String value) {
            addCriterion("teach_no =", value, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoNotEqualTo(String value) {
            addCriterion("teach_no <>", value, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoGreaterThan(String value) {
            addCriterion("teach_no >", value, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoGreaterThanOrEqualTo(String value) {
            addCriterion("teach_no >=", value, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoLessThan(String value) {
            addCriterion("teach_no <", value, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoLessThanOrEqualTo(String value) {
            addCriterion("teach_no <=", value, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoLike(String value) {
            addCriterion("teach_no like", value, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoNotLike(String value) {
            addCriterion("teach_no not like", value, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoIn(List<String> values) {
            addCriterion("teach_no in", values, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoNotIn(List<String> values) {
            addCriterion("teach_no not in", values, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoBetween(String value1, String value2) {
            addCriterion("teach_no between", value1, value2, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNoNotBetween(String value1, String value2) {
            addCriterion("teach_no not between", value1, value2, "teachNo");
            return (Criteria) this;
        }

        public Criteria andTeachNameIsNull() {
            addCriterion("teach_name is null");
            return (Criteria) this;
        }

        public Criteria andTeachNameIsNotNull() {
            addCriterion("teach_name is not null");
            return (Criteria) this;
        }

        public Criteria andTeachNameEqualTo(String value) {
            addCriterion("teach_name =", value, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameNotEqualTo(String value) {
            addCriterion("teach_name <>", value, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameGreaterThan(String value) {
            addCriterion("teach_name >", value, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameGreaterThanOrEqualTo(String value) {
            addCriterion("teach_name >=", value, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameLessThan(String value) {
            addCriterion("teach_name <", value, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameLessThanOrEqualTo(String value) {
            addCriterion("teach_name <=", value, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameLike(String value) {
            addCriterion("teach_name like", value, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameNotLike(String value) {
            addCriterion("teach_name not like", value, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameIn(List<String> values) {
            addCriterion("teach_name in", values, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameNotIn(List<String> values) {
            addCriterion("teach_name not in", values, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameBetween(String value1, String value2) {
            addCriterion("teach_name between", value1, value2, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachNameNotBetween(String value1, String value2) {
            addCriterion("teach_name not between", value1, value2, "teachName");
            return (Criteria) this;
        }

        public Criteria andTeachSexIsNull() {
            addCriterion("teach_sex is null");
            return (Criteria) this;
        }

        public Criteria andTeachSexIsNotNull() {
            addCriterion("teach_sex is not null");
            return (Criteria) this;
        }

        public Criteria andTeachSexEqualTo(String value) {
            addCriterion("teach_sex =", value, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexNotEqualTo(String value) {
            addCriterion("teach_sex <>", value, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexGreaterThan(String value) {
            addCriterion("teach_sex >", value, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexGreaterThanOrEqualTo(String value) {
            addCriterion("teach_sex >=", value, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexLessThan(String value) {
            addCriterion("teach_sex <", value, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexLessThanOrEqualTo(String value) {
            addCriterion("teach_sex <=", value, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexLike(String value) {
            addCriterion("teach_sex like", value, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexNotLike(String value) {
            addCriterion("teach_sex not like", value, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexIn(List<String> values) {
            addCriterion("teach_sex in", values, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexNotIn(List<String> values) {
            addCriterion("teach_sex not in", values, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexBetween(String value1, String value2) {
            addCriterion("teach_sex between", value1, value2, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachSexNotBetween(String value1, String value2) {
            addCriterion("teach_sex not between", value1, value2, "teachSex");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayIsNull() {
            addCriterion("teach_birthday is null");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayIsNotNull() {
            addCriterion("teach_birthday is not null");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayEqualTo(Date value) {
            addCriterionForJDBCDate("teach_birthday =", value, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayNotEqualTo(Date value) {
            addCriterionForJDBCDate("teach_birthday <>", value, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayGreaterThan(Date value) {
            addCriterionForJDBCDate("teach_birthday >", value, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("teach_birthday >=", value, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayLessThan(Date value) {
            addCriterionForJDBCDate("teach_birthday <", value, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("teach_birthday <=", value, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayIn(List<Date> values) {
            addCriterionForJDBCDate("teach_birthday in", values, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayNotIn(List<Date> values) {
            addCriterionForJDBCDate("teach_birthday not in", values, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("teach_birthday between", value1, value2, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachBirthdayNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("teach_birthday not between", value1, value2, "teachBirthday");
            return (Criteria) this;
        }

        public Criteria andTeachDepartIsNull() {
            addCriterion("teach_depart is null");
            return (Criteria) this;
        }

        public Criteria andTeachDepartIsNotNull() {
            addCriterion("teach_depart is not null");
            return (Criteria) this;
        }

        public Criteria andTeachDepartEqualTo(String value) {
            addCriterion("teach_depart =", value, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartNotEqualTo(String value) {
            addCriterion("teach_depart <>", value, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartGreaterThan(String value) {
            addCriterion("teach_depart >", value, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartGreaterThanOrEqualTo(String value) {
            addCriterion("teach_depart >=", value, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartLessThan(String value) {
            addCriterion("teach_depart <", value, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartLessThanOrEqualTo(String value) {
            addCriterion("teach_depart <=", value, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartLike(String value) {
            addCriterion("teach_depart like", value, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartNotLike(String value) {
            addCriterion("teach_depart not like", value, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartIn(List<String> values) {
            addCriterion("teach_depart in", values, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartNotIn(List<String> values) {
            addCriterion("teach_depart not in", values, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartBetween(String value1, String value2) {
            addCriterion("teach_depart between", value1, value2, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachDepartNotBetween(String value1, String value2) {
            addCriterion("teach_depart not between", value1, value2, "teachDepart");
            return (Criteria) this;
        }

        public Criteria andTeachProfIsNull() {
            addCriterion("teach_prof is null");
            return (Criteria) this;
        }

        public Criteria andTeachProfIsNotNull() {
            addCriterion("teach_prof is not null");
            return (Criteria) this;
        }

        public Criteria andTeachProfEqualTo(String value) {
            addCriterion("teach_prof =", value, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfNotEqualTo(String value) {
            addCriterion("teach_prof <>", value, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfGreaterThan(String value) {
            addCriterion("teach_prof >", value, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfGreaterThanOrEqualTo(String value) {
            addCriterion("teach_prof >=", value, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfLessThan(String value) {
            addCriterion("teach_prof <", value, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfLessThanOrEqualTo(String value) {
            addCriterion("teach_prof <=", value, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfLike(String value) {
            addCriterion("teach_prof like", value, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfNotLike(String value) {
            addCriterion("teach_prof not like", value, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfIn(List<String> values) {
            addCriterion("teach_prof in", values, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfNotIn(List<String> values) {
            addCriterion("teach_prof not in", values, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfBetween(String value1, String value2) {
            addCriterion("teach_prof between", value1, value2, "teachProf");
            return (Criteria) this;
        }

        public Criteria andTeachProfNotBetween(String value1, String value2) {
            addCriterion("teach_prof not between", value1, value2, "teachProf");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}