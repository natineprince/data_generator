package cn.natineprince.data.generator.task;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import cn.natineprince.data.generator.constant.FieldType;
import cn.natineprince.data.generator.constant.GenerationRule;
import cn.natineprince.data.generator.pojo.GenerationConfig;
import cn.natineprince.data.generator.pojo.GenerationConfig.FieldConfig;
import cn.natineprince.data.generator.service.DataGenerationService;
import cn.natineprince.data.generator.util.DataSourceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerationTask implements Runnable {

	private DataGenerationService dataGenerationService;
	private Long ticketId;
	private GenerationConfig gc;
	private Long startCount;

	public GenerationTask(DataGenerationService dataGenerationService, Long ticketId, GenerationConfig gc,
			Long startCount) {
		super();
		this.dataGenerationService = dataGenerationService;
		this.ticketId = ticketId;
		this.gc = gc;
		this.startCount = startCount;
	}

	@Override
	public void run() {
		DataSource ds = this.getDataSource();
		String sql = this.getSql();
		long offset = 0;
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			for (long i = startCount; i < startCount + 1000 && i < gc.getBasicConfig().getTotal().longValue(); i++) {
				int pos = 1;
				for (FieldConfig fc : gc.getFieldConfigs()) {
					if (this.setParameter(pos, fc, ps, i)) {
						pos++;
					}
				}
				ps.addBatch();
				offset++;
			}
			ps.executeBatch();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		this.dataGenerationService.reportProgress(ticketId, offset);
	}

	private DataSource getDataSource() {
		return DataSourceUtil.getDataSource(this.gc.getBasicConfig().getDataSource());
	}

	private String getSql() {
		StringBuilder sql = new StringBuilder("insert into ");
		StringBuilder params = new StringBuilder();
		sql.append(gc.getBasicConfig().getDataSource().getDbName()).append(".")
				.append(gc.getBasicConfig().getTableName()).append("(");
		boolean flag = false;
		for (FieldConfig fc : gc.getFieldConfigs()) {
			if (flag) {
				sql.append(",");
				params.append(",");
			}
			sql.append(fc.getFieldName());
			if (GenerationRule.SQL_EXP.equals(GenerationRule.valueOf(fc))) {
				params.append(fc.getExpression());
			} else {
				params.append("?");
			}
			flag = true;
		}
		sql.append(") values (").append(params).append(")");
		log.info(sql.toString());
		return sql.toString();
	}

	private boolean setParameter(int pos, FieldConfig fc, PreparedStatement ps, long offset) throws SQLException {
		if (GenerationRule.SQL_EXP.equals(GenerationRule.valueOf(fc))) {
			if (fc.getExpression().contains("?")) {
				ps.setLong(pos, offset);
			} else {
				return false;
			}
		} else {
			switch (FieldType.valueOf(fc)) {
			case INTEGER:
				ps.setLong(pos, Long.valueOf(this.getInsertValue(fc, offset)));
				break;
			case DECIMAL:
				ps.setBigDecimal(pos, new BigDecimal(this.getInsertValue(fc, offset)));
				break;
			case TIMESTAMP:
				ps.setString(pos, this.getInsertValue(fc, offset));
				break;
			case TEXT:
				ps.setString(pos, this.getInsertValue(fc, offset));
				break;
			}
		}
		return true;
	}

	private String getInsertValue(FieldConfig fc, long offset) {
		switch (GenerationRule.valueOf(fc)) {
		case CONST:
			return fc.getValue();
		case INCR:
			try {
				long val = Long.parseLong(fc.getValue());
				long step = Long.parseLong(fc.getExpression());
				return String.valueOf(val + step * offset);
			} catch (NumberFormatException e) {
				log.error(e.getMessage(), e);
			}
			break;
		case SQL_EXP:
			return "";
		}
		return fc.getValue();
	}

}
