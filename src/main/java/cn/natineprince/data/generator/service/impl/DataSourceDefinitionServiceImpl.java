package cn.natineprince.data.generator.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.natineprince.data.generator.entity.DataSourceDefinition;
import cn.natineprince.data.generator.repository.DataSourceDefinitionRepository;
import cn.natineprince.data.generator.service.DataSourceDefinitionService;

@Service
public class DataSourceDefinitionServiceImpl implements DataSourceDefinitionService {
	
	@Autowired
	private DataSourceDefinitionRepository repository;

	@Override
	public DataSourceDefinition save(DataSourceDefinition dsd) {
		return repository.save(dsd);
	}

	@Override
	public DataSourceDefinition update(DataSourceDefinition dsd) {
		return repository.save(dsd);
	}

	@Override
	public void delete(DataSourceDefinition dsd) {
		repository.delete(dsd);
	}

	@Override
	public List<DataSourceDefinition> list() {
		Iterator<DataSourceDefinition> it = repository.findAll().iterator();
		List<DataSourceDefinition> result = new ArrayList<>();
		while(it.hasNext()){
			result.add(it.next());
		}
		return result;
	}

}
