package com.xianglin.appserv.common.dal.dataobject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class NodeGrowUpInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NodeGrowUpInfoExample() {
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

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNodeCodeIsNull() {
            addCriterion("node_code is null");
            return (Criteria) this;
        }

        public Criteria andNodeCodeIsNotNull() {
            addCriterion("node_code is not null");
            return (Criteria) this;
        }

        public Criteria andNodeCodeEqualTo(String value) {
            addCriterion("node_code =", value, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeNotEqualTo(String value) {
            addCriterion("node_code <>", value, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeGreaterThan(String value) {
            addCriterion("node_code >", value, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("node_code >=", value, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeLessThan(String value) {
            addCriterion("node_code <", value, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeLessThanOrEqualTo(String value) {
            addCriterion("node_code <=", value, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeLike(String value) {
            addCriterion("node_code like", value, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeNotLike(String value) {
            addCriterion("node_code not like", value, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeIn(List<String> values) {
            addCriterion("node_code in", values, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeNotIn(List<String> values) {
            addCriterion("node_code not in", values, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeBetween(String value1, String value2) {
            addCriterion("node_code between", value1, value2, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeCodeNotBetween(String value1, String value2) {
            addCriterion("node_code not between", value1, value2, "nodeCode");
            return (Criteria) this;
        }

        public Criteria andNodeNameIsNull() {
            addCriterion("node_name is null");
            return (Criteria) this;
        }

        public Criteria andNodeNameIsNotNull() {
            addCriterion("node_name is not null");
            return (Criteria) this;
        }

        public Criteria andNodeNameEqualTo(String value) {
            addCriterion("node_name =", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotEqualTo(String value) {
            addCriterion("node_name <>", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameGreaterThan(String value) {
            addCriterion("node_name >", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameGreaterThanOrEqualTo(String value) {
            addCriterion("node_name >=", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLessThan(String value) {
            addCriterion("node_name <", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLessThanOrEqualTo(String value) {
            addCriterion("node_name <=", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLike(String value) {
            addCriterion("node_name like", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotLike(String value) {
            addCriterion("node_name not like", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameIn(List<String> values) {
            addCriterion("node_name in", values, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotIn(List<String> values) {
            addCriterion("node_name not in", values, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameBetween(String value1, String value2) {
            addCriterion("node_name between", value1, value2, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotBetween(String value1, String value2) {
            addCriterion("node_name not between", value1, value2, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdIsNull() {
            addCriterion("node_manager_party_id is null");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdIsNotNull() {
            addCriterion("node_manager_party_id is not null");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdEqualTo(String value) {
            addCriterion("node_manager_party_id =", value, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdNotEqualTo(String value) {
            addCriterion("node_manager_party_id <>", value, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdGreaterThan(String value) {
            addCriterion("node_manager_party_id >", value, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdGreaterThanOrEqualTo(String value) {
            addCriterion("node_manager_party_id >=", value, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdLessThan(String value) {
            addCriterion("node_manager_party_id <", value, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdLessThanOrEqualTo(String value) {
            addCriterion("node_manager_party_id <=", value, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdLike(String value) {
            addCriterion("node_manager_party_id like", value, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdNotLike(String value) {
            addCriterion("node_manager_party_id not like", value, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdIn(List<String> values) {
            addCriterion("node_manager_party_id in", values, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdNotIn(List<String> values) {
            addCriterion("node_manager_party_id not in", values, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdBetween(String value1, String value2) {
            addCriterion("node_manager_party_id between", value1, value2, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerPartyIdNotBetween(String value1, String value2) {
            addCriterion("node_manager_party_id not between", value1, value2, "nodeManagerPartyId");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameIsNull() {
            addCriterion("node_manager_name is null");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameIsNotNull() {
            addCriterion("node_manager_name is not null");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameEqualTo(String value) {
            addCriterion("node_manager_name =", value, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameNotEqualTo(String value) {
            addCriterion("node_manager_name <>", value, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameGreaterThan(String value) {
            addCriterion("node_manager_name >", value, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameGreaterThanOrEqualTo(String value) {
            addCriterion("node_manager_name >=", value, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameLessThan(String value) {
            addCriterion("node_manager_name <", value, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameLessThanOrEqualTo(String value) {
            addCriterion("node_manager_name <=", value, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameLike(String value) {
            addCriterion("node_manager_name like", value, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameNotLike(String value) {
            addCriterion("node_manager_name not like", value, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameIn(List<String> values) {
            addCriterion("node_manager_name in", values, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameNotIn(List<String> values) {
            addCriterion("node_manager_name not in", values, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameBetween(String value1, String value2) {
            addCriterion("node_manager_name between", value1, value2, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andNodeManagerNameNotBetween(String value1, String value2) {
            addCriterion("node_manager_name not between", value1, value2, "nodeManagerName");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterionForJDBCDate("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterionForJDBCDate("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterionForJDBCDate("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterionForJDBCDate("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("create_date not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andOrderAllIsNull() {
            addCriterion("order_all is null");
            return (Criteria) this;
        }

        public Criteria andOrderAllIsNotNull() {
            addCriterion("order_all is not null");
            return (Criteria) this;
        }

        public Criteria andOrderAllEqualTo(String value) {
            addCriterion("order_all =", value, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllNotEqualTo(String value) {
            addCriterion("order_all <>", value, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllGreaterThan(String value) {
            addCriterion("order_all >", value, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllGreaterThanOrEqualTo(String value) {
            addCriterion("order_all >=", value, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllLessThan(String value) {
            addCriterion("order_all <", value, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllLessThanOrEqualTo(String value) {
            addCriterion("order_all <=", value, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllLike(String value) {
            addCriterion("order_all like", value, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllNotLike(String value) {
            addCriterion("order_all not like", value, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllIn(List<String> values) {
            addCriterion("order_all in", values, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllNotIn(List<String> values) {
            addCriterion("order_all not in", values, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllBetween(String value1, String value2) {
            addCriterion("order_all between", value1, value2, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderAllNotBetween(String value1, String value2) {
            addCriterion("order_all not between", value1, value2, "orderAll");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceIsNull() {
            addCriterion("order_province is null");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceIsNotNull() {
            addCriterion("order_province is not null");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceEqualTo(String value) {
            addCriterion("order_province =", value, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceNotEqualTo(String value) {
            addCriterion("order_province <>", value, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceGreaterThan(String value) {
            addCriterion("order_province >", value, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("order_province >=", value, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceLessThan(String value) {
            addCriterion("order_province <", value, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceLessThanOrEqualTo(String value) {
            addCriterion("order_province <=", value, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceLike(String value) {
            addCriterion("order_province like", value, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceNotLike(String value) {
            addCriterion("order_province not like", value, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceIn(List<String> values) {
            addCriterion("order_province in", values, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceNotIn(List<String> values) {
            addCriterion("order_province not in", values, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceBetween(String value1, String value2) {
            addCriterion("order_province between", value1, value2, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOrderProvinceNotBetween(String value1, String value2) {
            addCriterion("order_province not between", value1, value2, "orderProvince");
            return (Criteria) this;
        }

        public Criteria andOpenDateIsNull() {
            addCriterion("open_date is null");
            return (Criteria) this;
        }

        public Criteria andOpenDateIsNotNull() {
            addCriterion("open_date is not null");
            return (Criteria) this;
        }

        public Criteria andOpenDateEqualTo(Date value) {
            addCriterionForJDBCDate("open_date =", value, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("open_date <>", value, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateGreaterThan(Date value) {
            addCriterionForJDBCDate("open_date >", value, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("open_date >=", value, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateLessThan(Date value) {
            addCriterionForJDBCDate("open_date <", value, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("open_date <=", value, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateIn(List<Date> values) {
            addCriterionForJDBCDate("open_date in", values, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("open_date not in", values, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("open_date between", value1, value2, "openDate");
            return (Criteria) this;
        }

        public Criteria andOpenDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("open_date not between", value1, value2, "openDate");
            return (Criteria) this;
        }

        public Criteria andBankDateIsNull() {
            addCriterion("bank_date is null");
            return (Criteria) this;
        }

        public Criteria andBankDateIsNotNull() {
            addCriterion("bank_date is not null");
            return (Criteria) this;
        }

        public Criteria andBankDateEqualTo(Date value) {
            addCriterionForJDBCDate("bank_date =", value, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("bank_date <>", value, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateGreaterThan(Date value) {
            addCriterionForJDBCDate("bank_date >", value, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("bank_date >=", value, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateLessThan(Date value) {
            addCriterionForJDBCDate("bank_date <", value, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("bank_date <=", value, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateIn(List<Date> values) {
            addCriterionForJDBCDate("bank_date in", values, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("bank_date not in", values, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("bank_date between", value1, value2, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("bank_date not between", value1, value2, "bankDate");
            return (Criteria) this;
        }

        public Criteria andBankStateIsNull() {
            addCriterion("bank_state is null");
            return (Criteria) this;
        }

        public Criteria andBankStateIsNotNull() {
            addCriterion("bank_state is not null");
            return (Criteria) this;
        }

        public Criteria andBankStateEqualTo(String value) {
            addCriterion("bank_state =", value, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateNotEqualTo(String value) {
            addCriterion("bank_state <>", value, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateGreaterThan(String value) {
            addCriterion("bank_state >", value, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateGreaterThanOrEqualTo(String value) {
            addCriterion("bank_state >=", value, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateLessThan(String value) {
            addCriterion("bank_state <", value, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateLessThanOrEqualTo(String value) {
            addCriterion("bank_state <=", value, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateLike(String value) {
            addCriterion("bank_state like", value, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateNotLike(String value) {
            addCriterion("bank_state not like", value, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateIn(List<String> values) {
            addCriterion("bank_state in", values, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateNotIn(List<String> values) {
            addCriterion("bank_state not in", values, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateBetween(String value1, String value2) {
            addCriterion("bank_state between", value1, value2, "bankState");
            return (Criteria) this;
        }

        public Criteria andBankStateNotBetween(String value1, String value2) {
            addCriterion("bank_state not between", value1, value2, "bankState");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNull() {
            addCriterion("balance is null");
            return (Criteria) this;
        }

        public Criteria andBalanceIsNotNull() {
            addCriterion("balance is not null");
            return (Criteria) this;
        }

        public Criteria andBalanceEqualTo(BigDecimal value) {
            addCriterion("balance =", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotEqualTo(BigDecimal value) {
            addCriterion("balance <>", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThan(BigDecimal value) {
            addCriterion("balance >", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("balance >=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThan(BigDecimal value) {
            addCriterion("balance <", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("balance <=", value, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceIn(List<BigDecimal> values) {
            addCriterion("balance in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotIn(List<BigDecimal> values) {
            addCriterion("balance not in", values, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andBalanceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("balance not between", value1, value2, "balance");
            return (Criteria) this;
        }

        public Criteria andCommerceDateIsNull() {
            addCriterion("commerce_date is null");
            return (Criteria) this;
        }

        public Criteria andCommerceDateIsNotNull() {
            addCriterion("commerce_date is not null");
            return (Criteria) this;
        }

        public Criteria andCommerceDateEqualTo(Date value) {
            addCriterionForJDBCDate("commerce_date =", value, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("commerce_date <>", value, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateGreaterThan(Date value) {
            addCriterionForJDBCDate("commerce_date >", value, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("commerce_date >=", value, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateLessThan(Date value) {
            addCriterionForJDBCDate("commerce_date <", value, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("commerce_date <=", value, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateIn(List<Date> values) {
            addCriterionForJDBCDate("commerce_date in", values, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("commerce_date not in", values, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("commerce_date between", value1, value2, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("commerce_date not between", value1, value2, "commerceDate");
            return (Criteria) this;
        }

        public Criteria andCommerceStateIsNull() {
            addCriterion("commerce_state is null");
            return (Criteria) this;
        }

        public Criteria andCommerceStateIsNotNull() {
            addCriterion("commerce_state is not null");
            return (Criteria) this;
        }

        public Criteria andCommerceStateEqualTo(String value) {
            addCriterion("commerce_state =", value, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateNotEqualTo(String value) {
            addCriterion("commerce_state <>", value, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateGreaterThan(String value) {
            addCriterion("commerce_state >", value, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateGreaterThanOrEqualTo(String value) {
            addCriterion("commerce_state >=", value, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateLessThan(String value) {
            addCriterion("commerce_state <", value, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateLessThanOrEqualTo(String value) {
            addCriterion("commerce_state <=", value, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateLike(String value) {
            addCriterion("commerce_state like", value, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateNotLike(String value) {
            addCriterion("commerce_state not like", value, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateIn(List<String> values) {
            addCriterion("commerce_state in", values, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateNotIn(List<String> values) {
            addCriterion("commerce_state not in", values, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateBetween(String value1, String value2) {
            addCriterion("commerce_state between", value1, value2, "commerceState");
            return (Criteria) this;
        }

        public Criteria andCommerceStateNotBetween(String value1, String value2) {
            addCriterion("commerce_state not between", value1, value2, "commerceState");
            return (Criteria) this;
        }

        public Criteria andTransactionNumIsNull() {
            addCriterion("transaction_num is null");
            return (Criteria) this;
        }

        public Criteria andTransactionNumIsNotNull() {
            addCriterion("transaction_num is not null");
            return (Criteria) this;
        }

        public Criteria andTransactionNumEqualTo(String value) {
            addCriterion("transaction_num =", value, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumNotEqualTo(String value) {
            addCriterion("transaction_num <>", value, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumGreaterThan(String value) {
            addCriterion("transaction_num >", value, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumGreaterThanOrEqualTo(String value) {
            addCriterion("transaction_num >=", value, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumLessThan(String value) {
            addCriterion("transaction_num <", value, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumLessThanOrEqualTo(String value) {
            addCriterion("transaction_num <=", value, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumLike(String value) {
            addCriterion("transaction_num like", value, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumNotLike(String value) {
            addCriterion("transaction_num not like", value, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumIn(List<String> values) {
            addCriterion("transaction_num in", values, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumNotIn(List<String> values) {
            addCriterion("transaction_num not in", values, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumBetween(String value1, String value2) {
            addCriterion("transaction_num between", value1, value2, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andTransactionNumNotBetween(String value1, String value2) {
            addCriterion("transaction_num not between", value1, value2, "transactionNum");
            return (Criteria) this;
        }

        public Criteria andFirstProductIsNull() {
            addCriterion("first_product is null");
            return (Criteria) this;
        }

        public Criteria andFirstProductIsNotNull() {
            addCriterion("first_product is not null");
            return (Criteria) this;
        }

        public Criteria andFirstProductEqualTo(String value) {
            addCriterion("first_product =", value, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductNotEqualTo(String value) {
            addCriterion("first_product <>", value, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductGreaterThan(String value) {
            addCriterion("first_product >", value, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductGreaterThanOrEqualTo(String value) {
            addCriterion("first_product >=", value, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductLessThan(String value) {
            addCriterion("first_product <", value, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductLessThanOrEqualTo(String value) {
            addCriterion("first_product <=", value, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductLike(String value) {
            addCriterion("first_product like", value, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductNotLike(String value) {
            addCriterion("first_product not like", value, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductIn(List<String> values) {
            addCriterion("first_product in", values, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductNotIn(List<String> values) {
            addCriterion("first_product not in", values, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductBetween(String value1, String value2) {
            addCriterion("first_product between", value1, value2, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstProductNotBetween(String value1, String value2) {
            addCriterion("first_product not between", value1, value2, "firstProduct");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceIsNull() {
            addCriterion("first_order_price is null");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceIsNotNull() {
            addCriterion("first_order_price is not null");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceEqualTo(BigDecimal value) {
            addCriterion("first_order_price =", value, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceNotEqualTo(BigDecimal value) {
            addCriterion("first_order_price <>", value, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceGreaterThan(BigDecimal value) {
            addCriterion("first_order_price >", value, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("first_order_price >=", value, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceLessThan(BigDecimal value) {
            addCriterion("first_order_price <", value, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("first_order_price <=", value, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceIn(List<BigDecimal> values) {
            addCriterion("first_order_price in", values, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceNotIn(List<BigDecimal> values) {
            addCriterion("first_order_price not in", values, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("first_order_price between", value1, value2, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andFirstOrderPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("first_order_price not between", value1, value2, "firstOrderPrice");
            return (Criteria) this;
        }

        public Criteria andImportDateIsNull() {
            addCriterion("import_date is null");
            return (Criteria) this;
        }

        public Criteria andImportDateIsNotNull() {
            addCriterion("import_date is not null");
            return (Criteria) this;
        }

        public Criteria andImportDateEqualTo(Date value) {
            addCriterion("import_date =", value, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateNotEqualTo(Date value) {
            addCriterion("import_date <>", value, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateGreaterThan(Date value) {
            addCriterion("import_date >", value, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateGreaterThanOrEqualTo(Date value) {
            addCriterion("import_date >=", value, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateLessThan(Date value) {
            addCriterion("import_date <", value, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateLessThanOrEqualTo(Date value) {
            addCriterion("import_date <=", value, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateIn(List<Date> values) {
            addCriterion("import_date in", values, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateNotIn(List<Date> values) {
            addCriterion("import_date not in", values, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateBetween(Date value1, Date value2) {
            addCriterion("import_date between", value1, value2, "importDate");
            return (Criteria) this;
        }

        public Criteria andImportDateNotBetween(Date value1, Date value2) {
            addCriterion("import_date not between", value1, value2, "importDate");
            return (Criteria) this;
        }

        public Criteria andOtherIsNull() {
            addCriterion("other is null");
            return (Criteria) this;
        }

        public Criteria andOtherIsNotNull() {
            addCriterion("other is not null");
            return (Criteria) this;
        }

        public Criteria andOtherEqualTo(String value) {
            addCriterion("other =", value, "other");
            return (Criteria) this;
        }

        public Criteria andOtherNotEqualTo(String value) {
            addCriterion("other <>", value, "other");
            return (Criteria) this;
        }

        public Criteria andOtherGreaterThan(String value) {
            addCriterion("other >", value, "other");
            return (Criteria) this;
        }

        public Criteria andOtherGreaterThanOrEqualTo(String value) {
            addCriterion("other >=", value, "other");
            return (Criteria) this;
        }

        public Criteria andOtherLessThan(String value) {
            addCriterion("other <", value, "other");
            return (Criteria) this;
        }

        public Criteria andOtherLessThanOrEqualTo(String value) {
            addCriterion("other <=", value, "other");
            return (Criteria) this;
        }

        public Criteria andOtherLike(String value) {
            addCriterion("other like", value, "other");
            return (Criteria) this;
        }

        public Criteria andOtherNotLike(String value) {
            addCriterion("other not like", value, "other");
            return (Criteria) this;
        }

        public Criteria andOtherIn(List<String> values) {
            addCriterion("other in", values, "other");
            return (Criteria) this;
        }

        public Criteria andOtherNotIn(List<String> values) {
            addCriterion("other not in", values, "other");
            return (Criteria) this;
        }

        public Criteria andOtherBetween(String value1, String value2) {
            addCriterion("other between", value1, value2, "other");
            return (Criteria) this;
        }

        public Criteria andOtherNotBetween(String value1, String value2) {
            addCriterion("other not between", value1, value2, "other");
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