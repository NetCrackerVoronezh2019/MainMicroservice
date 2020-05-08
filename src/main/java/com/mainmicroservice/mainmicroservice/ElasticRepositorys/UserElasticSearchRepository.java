package com.mainmicroservice.mainmicroservice.ElasticRepositorys;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mainmicroservice.mainmicroservice.Entities.User;



public interface UserElasticSearchRepository extends ElasticsearchRepository<User,Long> {


}
