package cn.natineprince.data.generator.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cn.natineprince.data.generator.entity.DataSourceDefinition;

@Repository
public interface DataSourceDefinitionRepository extends CrudRepository<DataSourceDefinition, Serializable> {

}
