package steed.action.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 标明该action需要校验当前model是否是当前登录用户，防止别人利用modeldriven篡改实体类当前用户
 * 或偷看其它用户资料
 * @author 战马
 *
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateMe {
}
