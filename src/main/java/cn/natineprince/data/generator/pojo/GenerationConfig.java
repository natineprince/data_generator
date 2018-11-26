package cn.natineprince.data.generator.pojo;

import java.util.List;

import cn.natineprince.data.generator.entity.DataSourceDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GenerationConfig {
	private BasicConfig basicConfig;
	private List<FieldConfig> fieldConfigs;

	@Getter
	@Setter
	@ToString
	public static class BasicConfig {
		private Long total;
		private String tableName;
		private DataSourceDefinition dataSource;
	}

	@Getter
	@Setter
	@ToString
	public static class FieldConfig {
		private String fieldName;
		private String type;
		private String value;
		private String rule;
		private String expression;
	}
}
