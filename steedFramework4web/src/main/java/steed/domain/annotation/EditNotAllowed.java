package steed.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 该字段用户不可编辑，完全由程序计算，比如用户的余额，（防止利用modeldriven篡改）
 * @author 战马
 *
 */
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface EditNotAllowed {
}
