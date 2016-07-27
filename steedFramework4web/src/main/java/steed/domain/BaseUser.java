package steed.domain;

/**
 * 用户实体类必须实现的接口，该接口用于权限校验，
 * 构造用户路径等。
 * @author 战马
 *
 */
public interface BaseUser{
	public abstract boolean hasPower(String powerName);
/*	public abstract boolean isMe(BaseUser baseUser);*/
}
