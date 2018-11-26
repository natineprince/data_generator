package cn.natineprince.data.generator.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "alias" }) })
public class DataSourceDefinition {

	@Id
	@GeneratedValue
	private Long id;
	@NotBlank
	private String alias;
	@NotBlank
	private String ip;
	@NotBlank
	private String port;
	@NotBlank
	private String userName;
	private String password;
	@NotBlank
	private String dbName;

}
