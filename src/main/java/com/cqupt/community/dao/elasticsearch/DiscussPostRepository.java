package com.cqupt.community.dao.elasticsearch;

import com.cqupt.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/23 11:31
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
