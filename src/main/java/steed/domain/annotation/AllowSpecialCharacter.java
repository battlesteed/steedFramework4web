package steed.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 标明该字段允许<>"&等特殊字段字符存在，若有该注解，特殊字符将不会被过滤
 * @author 战马
 *
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowSpecialCharacter {

}
