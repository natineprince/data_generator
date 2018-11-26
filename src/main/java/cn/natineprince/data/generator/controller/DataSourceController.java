package cn.natineprince.data.generator.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.natineprince.data.generator.entity.DataSourceDefinition;
import cn.natineprince.data.generator.service.DataSourceDefinitionService;
import cn.natineprince.data.generator.util.DataSourceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

	@Autowired
	private DataSourceDefinitionService service;

	@Autowired
	private MessageSource messageSource;

	private JdbcTemplate jdbcTemplate;

	@RequestMapping(value = "/list/all", method = RequestMethod.GET)
	public List<DataSourceDefinition> listAll() {
		return service.list();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public DataSourceDefinition create(@RequestBody DataSourceDefinition dsd) {
		return service.save(dsd);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public DataSourceDefinition update(@RequestBody DataSourceDefinition dsd) {
		DataSourceUtil.clearCache();
		return service.update(dsd);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(@RequestBody DataSourceDefinition dsd) {
		DataSourceUtil.clearCache();
		service.delete(dsd);
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String test(@RequestBody DataSourceDefinition dsDef) {
		DataSourceUtil.clearCache();
		DataSource ds = DataSourceUtil.getDataSource(dsDef);
		try (Connection conn = ds.getConnection()) {
			conn.createStatement().executeQuery("select 1").close();
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			return messageSource.getMessage("message.test.connection.fail", new Object[] { e.getMessage() },
					Locale.CHINA);
		}
		return messageSource.getMessage("message.test.connection.success", new Object[] {}, Locale.CHINA);
	}

	@RequestMapping(value = "/show/tables", method = RequestMethod.POST)
	public List<Map<String, Object>> showTables(@RequestBody DataSourceDefinition dsDef) {
		DataSource ds = DataSourceUtil.getDataSource(dsDef);
		jdbcTemplate = new JdbcTemplate(ds);
		return jdbcTemplate.queryForList("show tables");
	}

	@RequestMapping(value = "/table/desc/{tableName:.+}", method = RequestMethod.GET)
	public List<Map<String, Object>> showTables(@PathVariable String tableName) {
		return jdbcTemplate.queryForList("desc `" + tableName + "`");
	}

	@RequestMapping(value = "/show/databases", method = RequestMethod.POST)
	public List<Map<String, Object>> showDatabases(@RequestBody DataSourceDefinition dsDef) {
		DataSource ds = DataSourceUtil.getDataSource(dsDef);
		jdbcTemplate = new JdbcTemplate(ds);
		return jdbcTemplate.queryForList("show databases");
	}

}
