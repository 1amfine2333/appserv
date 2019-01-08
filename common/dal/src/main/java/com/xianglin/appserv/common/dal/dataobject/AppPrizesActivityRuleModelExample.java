package com.xianglin.appserv.common.dal.dataobject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppPrizesActivityRuleModelExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AppPrizesActivityRuleModelExample() {
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

        public Criteria andRuleIdIsNull() {
            addCriterion("RULE_ID is null");
            return (Criteria) this;
        }

        public Criteria andRuleIdIsNotNull() {
            addCriterion("RULE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andRuleIdEqualTo(Long value) {
            addCriterion("RULE_ID =", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotEqualTo(Long value) {
            addCriterion("RULE_ID <>", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdGreaterThan(Long value) {
            addCriterion("RULE_ID >", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdGreaterThanOrEqualTo(Long value) {
            addCriterion("RULE_ID >=", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLessThan(Long value) {
            addCriterion("RULE_ID <", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLessThanOrEqualTo(Long value) {
            addCriterion("RULE_ID <=", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdIn(List<Long> values) {
            addCriterion("RULE_ID in", values, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotIn(List<Long> values) {
            addCriterion("RULE_ID not in", values, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdBetween(Long value1, Long value2) {
            addCriterion("RULE_ID between", value1, value2, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotBetween(Long value1, Long value2) {
            addCriterion("RULE_ID not between", value1, value2, "ruleId");
            return (Criteria) this;
        }

        public Criteria andActiveidIsNull() {
            addCriterion("ACTIVEID is null");
            return (Criteria) this;
        }

        public Criteria andActiveidIsNotNull() {
            addCriterion("ACTIVEID is not null");
            return (Criteria) this;
        }

        public Criteria andActiveidEqualTo(Long value) {
            addCriterion("ACTIVEID =", value, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidNotEqualTo(Long value) {
            addCriterion("ACTIVEID <>", value, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidGreaterThan(Long value) {
            addCriterion("ACTIVEID >", value, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidGreaterThanOrEqualTo(Long value) {
            addCriterion("ACTIVEID >=", value, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidLessThan(Long value) {
            addCriterion("ACTIVEID <", value, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidLessThanOrEqualTo(Long value) {
            addCriterion("ACTIVEID <=", value, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidIn(List<Long> values) {
            addCriterion("ACTIVEID in", values, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidNotIn(List<Long> values) {
            addCriterion("ACTIVEID not in", values, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidBetween(Long value1, Long value2) {
            addCriterion("ACTIVEID between", value1, value2, "activeid");
            return (Criteria) this;
        }

        public Criteria andActiveidNotBetween(Long value1, Long value2) {
            addCriterion("ACTIVEID not between", value1, value2, "activeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidIsNull() {
            addCriterion("PRIZEID is null");
            return (Criteria) this;
        }

        public Criteria andPrizeidIsNotNull() {
            addCriterion("PRIZEID is not null");
            return (Criteria) this;
        }

        public Criteria andPrizeidEqualTo(Long value) {
            addCriterion("PRIZEID =", value, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidNotEqualTo(Long value) {
            addCriterion("PRIZEID <>", value, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidGreaterThan(Long value) {
            addCriterion("PRIZEID >", value, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidGreaterThanOrEqualTo(Long value) {
            addCriterion("PRIZEID >=", value, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidLessThan(Long value) {
            addCriterion("PRIZEID <", value, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidLessThanOrEqualTo(Long value) {
            addCriterion("PRIZEID <=", value, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidIn(List<Long> values) {
            addCriterion("PRIZEID in", values, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidNotIn(List<Long> values) {
            addCriterion("PRIZEID not in", values, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidBetween(Long value1, Long value2) {
            addCriterion("PRIZEID between", value1, value2, "prizeid");
            return (Criteria) this;
        }

        public Criteria andPrizeidNotBetween(Long value1, Long value2) {
            addCriterion("PRIZEID not between", value1, value2, "prizeid");
            return (Criteria) this;
        }

        public Criteria andRulecodeIsNull() {
            addCriterion("RULECODE is null");
            return (Criteria) this;
        }

        public Criteria andRulecodeIsNotNull() {
            addCriterion("RULECODE is not null");
            return (Criteria) this;
        }

        public Criteria andRulecodeEqualTo(String value) {
            addCriterion("RULECODE =", value, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeNotEqualTo(String value) {
            addCriterion("RULECODE <>", value, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeGreaterThan(String value) {
            addCriterion("RULECODE >", value, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeGreaterThanOrEqualTo(String value) {
            addCriterion("RULECODE >=", value, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeLessThan(String value) {
            addCriterion("RULECODE <", value, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeLessThanOrEqualTo(String value) {
            addCriterion("RULECODE <=", value, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeLike(String value) {
            addCriterion("RULECODE like", value, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeNotLike(String value) {
            addCriterion("RULECODE not like", value, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeIn(List<String> values) {
            addCriterion("RULECODE in", values, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeNotIn(List<String> values) {
            addCriterion("RULECODE not in", values, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeBetween(String value1, String value2) {
            addCriterion("RULECODE between", value1, value2, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulecodeNotBetween(String value1, String value2) {
            addCriterion("RULECODE not between", value1, value2, "rulecode");
            return (Criteria) this;
        }

        public Criteria andRulenameIsNull() {
            addCriterion("RULENAME is null");
            return (Criteria) this;
        }

        public Criteria andRulenameIsNotNull() {
            addCriterion("RULENAME is not null");
            return (Criteria) this;
        }

        public Criteria andRulenameEqualTo(String value) {
            addCriterion("RULENAME =", value, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameNotEqualTo(String value) {
            addCriterion("RULENAME <>", value, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameGreaterThan(String value) {
            addCriterion("RULENAME >", value, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameGreaterThanOrEqualTo(String value) {
            addCriterion("RULENAME >=", value, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameLessThan(String value) {
            addCriterion("RULENAME <", value, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameLessThanOrEqualTo(String value) {
            addCriterion("RULENAME <=", value, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameLike(String value) {
            addCriterion("RULENAME like", value, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameNotLike(String value) {
            addCriterion("RULENAME not like", value, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameIn(List<String> values) {
            addCriterion("RULENAME in", values, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameNotIn(List<String> values) {
            addCriterion("RULENAME not in", values, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameBetween(String value1, String value2) {
            addCriterion("RULENAME between", value1, value2, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulenameNotBetween(String value1, String value2) {
            addCriterion("RULENAME not between", value1, value2, "rulename");
            return (Criteria) this;
        }

        public Criteria andRulevalueIsNull() {
            addCriterion("RULEVALUE is null");
            return (Criteria) this;
        }

        public Criteria andRulevalueIsNotNull() {
            addCriterion("RULEVALUE is not null");
            return (Criteria) this;
        }

        public Criteria andRulevalueEqualTo(String value) {
            addCriterion("RULEVALUE =", value, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueNotEqualTo(String value) {
            addCriterion("RULEVALUE <>", value, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueGreaterThan(String value) {
            addCriterion("RULEVALUE >", value, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueGreaterThanOrEqualTo(String value) {
            addCriterion("RULEVALUE >=", value, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueLessThan(String value) {
            addCriterion("RULEVALUE <", value, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueLessThanOrEqualTo(String value) {
            addCriterion("RULEVALUE <=", value, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueLike(String value) {
            addCriterion("RULEVALUE like", value, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueNotLike(String value) {
            addCriterion("RULEVALUE not like", value, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueIn(List<String> values) {
            addCriterion("RULEVALUE in", values, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueNotIn(List<String> values) {
            addCriterion("RULEVALUE not in", values, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueBetween(String value1, String value2) {
            addCriterion("RULEVALUE between", value1, value2, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andRulevalueNotBetween(String value1, String value2) {
            addCriterion("RULEVALUE not between", value1, value2, "rulevalue");
            return (Criteria) this;
        }

        public Criteria andDataStatusIsNull() {
            addCriterion("DATA_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andDataStatusIsNotNull() {
            addCriterion("DATA_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andDataStatusEqualTo(String value) {
            addCriterion("DATA_STATUS =", value, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusNotEqualTo(String value) {
            addCriterion("DATA_STATUS <>", value, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusGreaterThan(String value) {
            addCriterion("DATA_STATUS >", value, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusGreaterThanOrEqualTo(String value) {
            addCriterion("DATA_STATUS >=", value, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusLessThan(String value) {
            addCriterion("DATA_STATUS <", value, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusLessThanOrEqualTo(String value) {
            addCriterion("DATA_STATUS <=", value, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusLike(String value) {
            addCriterion("DATA_STATUS like", value, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusNotLike(String value) {
            addCriterion("DATA_STATUS not like", value, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusIn(List<String> values) {
            addCriterion("DATA_STATUS in", values, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusNotIn(List<String> values) {
            addCriterion("DATA_STATUS not in", values, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusBetween(String value1, String value2) {
            addCriterion("DATA_STATUS between", value1, value2, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andDataStatusNotBetween(String value1, String value2) {
            addCriterion("DATA_STATUS not between", value1, value2, "dataStatus");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CREATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CREATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("CREATE_TIME =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("CREATE_TIME <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CREATE_TIME >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATE_TIME >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CREATE_TIME <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CREATE_TIME <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CREATE_TIME in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CREATE_TIME not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CREATE_TIME between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CREATE_TIME not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UPDATE_TIME =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UPDATE_TIME <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UPDATE_TIME >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UPDATE_TIME >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UPDATE_TIME <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UPDATE_TIME <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UPDATE_TIME in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UPDATE_TIME not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UPDATE_TIME between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UPDATE_TIME not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andCommentsIsNull() {
            addCriterion("COMMENTS is null");
            return (Criteria) this;
        }

        public Criteria andCommentsIsNotNull() {
            addCriterion("COMMENTS is not null");
            return (Criteria) this;
        }

        public Criteria andCommentsEqualTo(String value) {
            addCriterion("COMMENTS =", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsNotEqualTo(String value) {
            addCriterion("COMMENTS <>", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsGreaterThan(String value) {
            addCriterion("COMMENTS >", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsGreaterThanOrEqualTo(String value) {
            addCriterion("COMMENTS >=", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsLessThan(String value) {
            addCriterion("COMMENTS <", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsLessThanOrEqualTo(String value) {
            addCriterion("COMMENTS <=", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsLike(String value) {
            addCriterion("COMMENTS like", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsNotLike(String value) {
            addCriterion("COMMENTS not like", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsIn(List<String> values) {
            addCriterion("COMMENTS in", values, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsNotIn(List<String> values) {
            addCriterion("COMMENTS not in", values, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsBetween(String value1, String value2) {
            addCriterion("COMMENTS between", value1, value2, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsNotBetween(String value1, String value2) {
            addCriterion("COMMENTS not between", value1, value2, "comments");
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