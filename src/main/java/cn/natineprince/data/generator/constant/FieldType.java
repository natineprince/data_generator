package cn.natineprince.data.generator.constant;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import cn.natineprince.data.generator.pojo.GenerationConfig.FieldConfig;

public enum FieldType {

	TEXT, INTEGER, DECIMAL, TIMESTAMP;

	//@formatter:off
	private static final Map<String, FieldType> FIELD_TYPE_MAP = ImmutableMap.<String, FieldType> builder()
			.put("char", TEXT)
			.put("varchar", TEXT)
			.put("tinytext", TEXT)
			.put("text", TEXT)
			.put("blob", TEXT)
			.put("mediumtext", TEXT)
			.put("mediumblob", TEXT)
			.put("longtext", TEXT)
			.put("longblob", TEXT)
			.put("enum", TEXT)
			.put("set", TEXT)
			.put("tinyint", INTEGER)
			.put("smallint", INTEGER)
			.put("mediumint", INTEGER)
			.put("int", INTEGER)
			.put("bigint", INTEGER)
			.put("float", DECIMAL)
			.put("double", DECIMAL)
			.put("decimal", DECIMAL)
			.put("date", TIMESTAMP)
			.put("datetime", TIMESTAMP)
			.put("timestamp", TIMESTAMP)
			.put("time", TIMESTAMP)
			.put("year", TIMESTAMP).build();
	//@formatter:on

	public static final FieldType valueOf(FieldConfig fc) {
		if (fc == null || fc.getType() == null) {
			return TEXT;
		}
		for (Entry<String, FieldType> entry : FIELD_TYPE_MAP.entrySet()) {
			if (fc.getType().startsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		return TEXT;
	}

}
