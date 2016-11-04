package com.dbalthassat.restapi.service;

import com.dbalthassat.restapi.config.Application;
import com.dbalthassat.restapi.exception.clientError.notFound.ResourceNotFoundException;
import com.dbalthassat.restapi.mapper.GenericMapper;
import com.dbalthassat.restapi.repository.GenericRepository;
import com.dbalthassat.restapi.utils.HttpUtils;
import com.dbalthassat.restapi.utils.Resource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class GenericService {
    private static final Logger LOGGER = getLogger(GenericService.class);

    @Autowired
    private GenericRepository repository;

    public List<?> findAll(String uri) {
        List<Resource> resources = HttpUtils.findResources(uri);
        String entity = resources.get(0).getResource();
        String daoClassName = Application.BASE_PACKAGE + ".dao." + entity + "Dao";
        String mapperClassName = Application.BASE_PACKAGE + ".mapper." + entity + "DaoMapper";
        List<Object> entities = repository.findAll(entity);
        try {
            GenericMapper mapper = (GenericMapper) Class.forName(mapperClassName).newInstance();
            return mapper.map(entities, Class.forName(daoClassName));
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new ResourceNotFoundException(uri, "GET");
        }
    }

    public Object findOne(String uri) {
        List<Resource> resources = HttpUtils.findResources(uri);
        String entity = resources.get(0).getResource();
        Long id = resources.get(0).getId();
        String daoClassName = Application.BASE_PACKAGE + ".dao." + entity + "Dao";
        String mapperClassName = Application.BASE_PACKAGE + ".mapper." + entity + "DaoMapper";
        Object result = repository.findOne(entity, id);
        try {
            GenericMapper mapper = (GenericMapper) Class.forName(mapperClassName).newInstance();
            return mapper.map(result, Class.forName(daoClassName));
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new ResourceNotFoundException(uri, "GET");
        }
    }

    public List<?> findAllWithParent(String uri) {
        List<Resource> resources = HttpUtils.findResources(uri);
        Resource parent = resources.get(0);
        Resource child = resources.get(1);
        String daoClassName = Application.BASE_PACKAGE + ".dao." + child.getResource() + "Dao";
        String mapperClassName = Application.BASE_PACKAGE + ".mapper." + child.getResource() + "DaoMapper";
        List<Object> entities = repository.findAllWithParent(parent.getResource(), parent.getId(), child.getResource());
        try {
            GenericMapper mapper = (GenericMapper) Class.forName(mapperClassName).newInstance();
            return mapper.map(entities, Class.forName(daoClassName));
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new ResourceNotFoundException(uri, "GET");
        }
    }
}
