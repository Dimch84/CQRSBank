package server.db.elasticsearch

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import server.db.elasticsearch.entities.user_info_entity


@Repository
interface UserInfoRepository : ElasticsearchRepository<user_info_entity, Long>
