package com.cqupt.community;

import com.cqupt.community.dao.DiscussPostMapper;
import com.cqupt.community.dao.elasticsearch.DiscussPostRepository;
import com.cqupt.community.entity.DiscussPost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/23 11:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTests {
    @Autowired
    private DiscussPostMapper discussMapper;

    @Autowired
    private DiscussPostRepository discussRepository;

    @Autowired
    private ElasticsearchTemplate elasticTemplate;

    /**
     * 测试 ElasticSearch(ES) 新增1条数据
     */
    @Test
    public void testInsert() {
        discussRepository.save(discussMapper.selectDiscussPostById(241));
        discussRepository.save(discussMapper.selectDiscussPostById(242));
        discussRepository.save(discussMapper.selectDiscussPostById(243));
        discussRepository.save(discussMapper.selectDiscussPostById(280));
    }

    /**
     * 测试 ES 新增多条数据
     */
    @Test
    public void testInsertList() {
        discussRepository.saveAll(discussMapper.selectDiscussPost(101, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(102, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(103, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(111, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(112, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(131, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(132, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(133, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(134, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(149, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(145, 0, 100));
        discussRepository.saveAll(discussMapper.selectDiscussPost(161, 0, 100));
    }

    /**
     * 测试 ES 修改1条数据
     */
    @Test
    public void testUpdate() {
        DiscussPost post = discussMapper.selectDiscussPostById(231);
        post.setContent("我是新人,带带弟弟好吗");
        discussRepository.save(post);
    }

    /**
     * 测试 ES 删除1(多)条数据
     */
    @Test
    public void testDelete() {
//         discussRepository.deleteById(231);
        discussRepository.deleteAll();
    }

    /**
     * 测试 ES 搜索功能  -  ElasticsearchRepository 方式
     * 组件: SearchQuery - 构造搜索条件
     * NativeSearchQueryBuilder 构造 SearchQuery 接口的实现类
     * SearchQuery searchQuery = new NativeSearchQueryBuilder()
     *
     */
    @Test
    public void testSearchByRepository() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content")) //QueryBuilders 构造搜索条件
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))  //SortBuilders 构造查询(排序)条件
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))  //PageRequest 构造分页条件
                .withHighlightFields(  // 高亮显示
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        // ElasticsearchRepository 底层调用了
        // elasticTemplate.queryForPage(searchQuery, class, SearchResultMapper)进行处理
        // 底层获取得到了高亮显示的值, 但是没有返回.
        //处理方法见下一个测试方法

        Page<DiscussPost> page = discussRepository.search(searchQuery);
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for (DiscussPost post : page) {
            System.out.println(post);
        }
    }

    /**
     * 测试 ES 搜索功能  -  elasticTemplate 方式
     */
    @Test
    public void testSearchByTemplate() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        Page<DiscussPost> page = elasticTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                SearchHits hits = response.getHits();
                if (hits.getTotalHits() <= 0) { //没有查到数据
                    return null;
                }

                //查到数据 -> 把数据放到集合中,再返回
                List<DiscussPost> list = new ArrayList<>();
                for (SearchHit hit : hits) { //遍历命中的数据
                    DiscussPost post = new DiscussPost();//将数据包装到实体类中

                    String id = hit.getSourceAsMap().get("id").toString();
                    post.setId(Integer.valueOf(id));

                    String userId = hit.getSourceAsMap().get("userId").toString();
                    post.setUserId(Integer.valueOf(userId));

                    String title = hit.getSourceAsMap().get("title").toString();
                    post.setTitle(title);

                    String content = hit.getSourceAsMap().get("content").toString();
                    post.setContent(content);

                    String status = hit.getSourceAsMap().get("status").toString();
                    post.setStatus(Integer.valueOf(status));

                    String createTime = hit.getSourceAsMap().get("createTime").toString();
                    post.setCreateTime(new Date(Long.valueOf(createTime)));

                    String commentCount = hit.getSourceAsMap().get("commentCount").toString();
                    post.setCommentCount(Integer.valueOf(commentCount));

                    // 处理高亮显示的结果
                    HighlightField titleField = hit.getHighlightFields().get("title");
                    if (titleField != null) {
                        post.setTitle(titleField.getFragments()[0].toString());
                    }

                    HighlightField contentField = hit.getHighlightFields().get("content");
                    if (contentField != null) {
                        post.setContent(contentField.getFragments()[0].toString());
                    }

                    list.add(post);
                }

                //返回AggregatedPage类型，构造AggregatedPage实现类
                return new AggregatedPageImpl(list, pageable,
                        hits.getTotalHits(), response.getAggregations(), response.getScrollId(), hits.getMaxScore());
            }
        });

        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for (DiscussPost post : page) {
            System.out.println(post);
        }
    }

}
