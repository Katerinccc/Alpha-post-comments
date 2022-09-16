package com.posada.santiago.alphapostsandcomments.application.adapters.repository;

import com.posada.santiago.alphapostsandcomments.application.generic.models.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class MongoUserRepository{

    private final ReactiveMongoTemplate mongoTemplate;

    public MongoUserRepository(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Mono<AppUser> findUserByEmail(String userEmail) {
        var query = new Query(Criteria.where("userEmail").is(userEmail));
        return mongoTemplate.findOne(query, AppUser.class)
                .doOnNext(find -> log.info("User by email " + userEmail + " found"));
    }

    public Mono<AppUser> createUser(AppUser appUser) {
        return mongoTemplate.save(appUser);
    }
}
