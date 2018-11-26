package cn.natineprince.data.generator.service;

import java.util.List;

import cn.natineprince.data.generator.entity.DataSourceDefinition;

public interface DataSourceDefinitionService {
	
	DataSourceDefinition save(DataSourceDefinition dsd);
	DataSourceDefinition update(DataSourceDefinition dsd);
	void delete(DataSourceDefinition dsd);
	List<DataSourceDefinition> list();

}
