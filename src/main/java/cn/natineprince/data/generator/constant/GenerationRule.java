package cn.natineprince.data.generator.constant;

import cn.natineprince.data.generator.pojo.GenerationConfig.FieldConfig;

public enum GenerationRule {
	CONST, INCR, SQL_EXP;

	public static final GenerationRule valueOf(FieldConfig fc) {
		return valueOf(fc.getRule().toUpperCase());
	}
}
