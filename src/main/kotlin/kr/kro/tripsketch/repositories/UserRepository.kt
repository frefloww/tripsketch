package kr.kro.tripsketch.repositories

import kr.kro.tripsketch.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String): User?
    fun findByNickname(nickname: String): User?
    fun existsByNickname(nickname: String): Boolean
    fun findByOurRefreshToken(ourRefreshToken: String): User?

}
