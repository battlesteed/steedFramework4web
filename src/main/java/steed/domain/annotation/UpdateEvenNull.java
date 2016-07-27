package steed.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 标明该字段即使是空也将会被updateNotNullField方法 update到数据库
 * 用途和详细用法请看steed.util.dao.DaoUtil.updateNotNullField(BaseDatabaseDomain, List<String>)
 * @author 战马
 *
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateEvenNull {
}
