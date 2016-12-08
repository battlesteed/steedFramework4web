package steed.ext.domain.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import steed.hibernatemaster.domain.BaseRelationalDatabaseDomain;
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Menu extends BaseRelationalDatabaseDomain{
	private static final long serialVersionUID = 5721539566317121839L;
	private String name;
	private String url;
	private String target;
	private Integer fatherId;
	private Menu father;
//	private Power power;
	private Integer id;
	
	private Integer id_max_1;
	
	private Integer order1;
	
	private Boolean show;
	
	private List<Menu> sonList = new ArrayList<Menu>();
	
	@Id
	@GenericGenerator(name="gen1",strategy="assigned")
	@GeneratedValue(generator="gen1")
	public Integer getId() {
		return id;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public Integer getId_max_1() {
		return id_max_1;
	}

	public void setId_max_1(Integer id_max_1) {
		this.id_max_1 = id_max_1;
	}

	public Menu(String name, Integer id) {
		this.name = name;
		this.id = id;
	}
	public Menu() {
	}
	/*public Menu(String name) {
		this.name = name;
	}*/
	
	public Integer getFatherId() {
		return fatherId;
	}

	public Integer getOrder1() {
		return order1;
	}

	public void setOrder1(Integer order1) {
		this.order1 = order1;
	}

	public Menu(Integer id) {
		super();
		this.id = id;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}
/*	@ManyToOne
	public Power getPower() {
		return power;
	}
	public void setPower(Power power) {
		this.power = power;
	}*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Transient
	public Menu getFather() {
		return father;
	}
	public void setFather(Menu father) {
		this.father = father;
	}
	@Transient
	public List<Menu> getSonList() {
		return sonList;
	}
	public void setSonList(List<Menu> sonList) {
		this.sonList = sonList;
	}
	
	public void addSon(Menu menu){
		sonList.add(menu);
	}

	@Override
	public void initialize() {
		Hibernate.initialize(this);
	}

	@Override
	public void initializeAll() {
		initialize();
//		domainInitializeAll(getPower());
	}
	
}
