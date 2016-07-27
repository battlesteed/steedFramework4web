package steed.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于实体类，表明该字段不用于拼接查询hql
 * @author 战马
 * @email java@beyondstar.com.cn 
 *        battle_steed@163.com
 * @company 深圳市星超越科技有限公司
 */
@Inherited
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotQueryCondition {
}
