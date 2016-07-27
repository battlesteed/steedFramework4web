package steed.action.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 标明该action需要校验当前model是否属于当前登录用户，防止别人利用modeldriven篡改实体类所属用户
 * 或偷看当前实体类
 * @author 战马
 *
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateUser {
}
