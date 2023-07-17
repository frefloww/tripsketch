package kr.kro.tripsketch.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "trips")
data class Trip(
    @Id val id: String? = null,
    val userId: User,                       // 외래키-User객체자체를 참조  // val userId: String,
    val scheduleId: String,
    var title: String,
    var content: String,
    var likes: Int,
    var views: Int,
    var location: String? = null,
    var startedAt: LocalDateTime = LocalDateTime.now(),
    var endAt: LocalDateTime = LocalDateTime.now(),
    var hashtag: String,
    var hidden: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    var likeFlag: Int = 0,
    val tripViews: Set<String> = setOf(),   // tripviews 배열로 set 형태로 받겠다?   - 원하면 redis 로도 해줘도 됌!
)
