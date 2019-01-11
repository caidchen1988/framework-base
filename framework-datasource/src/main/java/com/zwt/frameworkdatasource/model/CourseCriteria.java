package com.zwt.frameworkdatasource.model;

import java.util.ArrayList;
import java.util.List;

/**
* CourseCriteria 条件查询类.
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public class CourseCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer mysqlOffset;

    protected Integer mysqlLength;

    public CourseCriteria() {
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

        public Criteria andCouNoIsNull() {
            addCriterion("cou_no is null");
            return (Criteria) this;
        }

        public Criteria andCouNoIsNotNull() {
            addCriterion("cou_no is not null");
            return (Criteria) this;
        }

        public Criteria andCouNoEqualTo(String value) {
            addCriterion("cou_no =", value, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoNotEqualTo(String value) {
            addCriterion("cou_no <>", value, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoGreaterThan(String value) {
            addCriterion("cou_no >", value, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoGreaterThanOrEqualTo(String value) {
            addCriterion("cou_no >=", value, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoLessThan(String value) {
            addCriterion("cou_no <", value, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoLessThanOrEqualTo(String value) {
            addCriterion("cou_no <=", value, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoLike(String value) {
            addCriterion("cou_no like", value, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoNotLike(String value) {
            addCriterion("cou_no not like", value, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoIn(List<String> values) {
            addCriterion("cou_no in", values, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoNotIn(List<String> values) {
            addCriterion("cou_no not in", values, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoBetween(String value1, String value2) {
            addCriterion("cou_no between", value1, value2, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNoNotBetween(String value1, String value2) {
            addCriterion("cou_no not between", value1, value2, "couNo");
            return (Criteria) this;
        }

        public Criteria andCouNameIsNull() {
            addCriterion("cou_name is null");
            return (Criteria) this;
        }

        public Criteria andCouNameIsNotNull() {
            addCriterion("cou_name is not null");
            return (Criteria) this;
        }

        public Criteria andCouNameEqualTo(String value) {
            addCriterion("cou_name =", value, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameNotEqualTo(String value) {
            addCriterion("cou_name <>", value, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameGreaterThan(String value) {
            addCriterion("cou_name >", value, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameGreaterThanOrEqualTo(String value) {
            addCriterion("cou_name >=", value, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameLessThan(String value) {
            addCriterion("cou_name <", value, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameLessThanOrEqualTo(String value) {
            addCriterion("cou_name <=", value, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameLike(String value) {
            addCriterion("cou_name like", value, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameNotLike(String value) {
            addCriterion("cou_name not like", value, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameIn(List<String> values) {
            addCriterion("cou_name in", values, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameNotIn(List<String> values) {
            addCriterion("cou_name not in", values, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameBetween(String value1, String value2) {
            addCriterion("cou_name between", value1, value2, "couName");
            return (Criteria) this;
        }

        public Criteria andCouNameNotBetween(String value1, String value2) {
            addCriterion("cou_name not between", value1, value2, "couName");
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