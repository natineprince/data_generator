package cn.natineprince.data.generator.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;

import cn.natineprince.data.generator.constant.DataGeneratorConstant;
import cn.natineprince.data.generator.entity.DataSourceDefinition;

public class DataSourceUtil {
	
	private static ConcurrentMap<String, DataSource> dynamicDataSourceMap = new ConcurrentHashMap<>();
	
	public static DataSource getDataSource(DataSourceDefinition dsDef){
		String key = dsDef.getAlias();
		DataSource ds = dynamicDataSourceMap.get(key);
		if(ds == null){
			ds = DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver")
					.url(String.format(DataGeneratorConstant.URL_PATTERN, dsDef.getIp(), dsDef.getPort(), dsDef.getDbName()))
					.username(dsDef.getUserName()).password(dsDef.getPassword()).build();

			Map<String, String> properties = new HashMap<String, String>();
			properties.put("test-while-idle", "true");
			properties.put("test-on-borrow", "true");
			MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(properties);
			new RelaxedDataBinder(ds).withAlias("url", "jdbcUrl").withAlias("username", "user")
					.bind(mutablePropertyValues);
			dynamicDataSourceMap.putIfAbsent(key, ds);
		}
		return ds;
	}
	
	public static void clearCache(){
		dynamicDataSourceMap.clear();
	}

}
