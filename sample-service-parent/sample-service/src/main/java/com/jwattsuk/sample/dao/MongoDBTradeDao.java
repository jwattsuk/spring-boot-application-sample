package com.jwattsuk.sample.dao;

import com.jwattsuk.sample.model.Trade;
import com.mongodb.client.result.DeleteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public class MongoDBTradeDao {
    private static final Logger LOG = LoggerFactory.getLogger(MongoDBTradeDao.class);

    private final MongoTemplate mongoTemplate;

    public MongoDBTradeDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Trade add(Trade Trade) {
        return mongoTemplate.save(Trade);
    }

    public Trade update(Trade Trade) {
        return mongoTemplate.save(Trade);
    }

    public boolean delete(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        DeleteResult result = mongoTemplate.remove(query, Trade.class);
        return result.wasAcknowledged();
    }

    public boolean delete(Trade Trade) {
        DeleteResult result = mongoTemplate.remove(Trade);
        return result.wasAcknowledged();
    }

    public List<Trade> getAll() {
        return mongoTemplate.findAll(Trade.class);
    }

    public List<Trade> getAll(Integer page, Integer size) {
        Sort sort = Sort.by("category").ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Query query = new Query().with(pageable);

        Page<Trade> pagedResult = PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Trade.class),
                pageable,
                () -> mongoTemplate.count(query, Trade.class));

        return pagedResult.getContent();

    }

    public Trade getById(String id) {
        return mongoTemplate.findById(id, Trade.class);
    }

    public List<Trade> search(String name, Integer minQuantity,
                              Integer maxQuantity, String category) {

        Query query = new Query();
        List<Criteria> criterias = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            criterias.add(Criteria.where("name").is(name));
        }

        if (minQuantity != null && minQuantity >= 0) {
            criterias.add(Criteria.where("quantity").gte(minQuantity));
        }

        if (maxQuantity != null && maxQuantity >= 0) {
            criterias.add(Criteria.where("quantity").lte(maxQuantity));
        }

        if (category != null && !category.isEmpty()) {
            criterias.add(Criteria.where("category").is(category));
        }

        if (!criterias.isEmpty()) {
            Criteria criteria = new Criteria()
                    .andOperator(criterias.toArray(new Criteria[criterias.size()]));
            query.addCriteria(criteria);
        }

        List<Trade> Trades = mongoTemplate.find(query, Trade.class);

        return Trades;
    }
}
