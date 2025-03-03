package kr.kro.tripsketch.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kr.kro.tripsketch.domain.User
import kr.kro.tripsketch.dto.TokenResponse
import kr.kro.tripsketch.utils.EnvLoader
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.servlet.http.HttpServletRequest

@Service
class JwtService {
    private val secretKeyString = EnvLoader.getProperty("SECRET_KEY") ?: ""
    private val secretKey = SecretKeySpec(secretKeyString.toByteArray(), SignatureAlgorithm.HS256.jcaName)
    private val accessTokenValidityInMilliseconds: Long = EnvLoader.getProperty("ACCESS_TOKEN_VALIDITY")?.toLong() ?: 3600000 // 1 hour
    private val refreshTokenValidityInMilliseconds: Long = EnvLoader.getProperty("REFRESH_TOKEN_VALIDITY")?.toLong() ?: 2592000000 // 30 days

    fun createTokens(user: User): TokenResponse {
        val now = Date()

        // Access Token 생성
        val accessTokenValidity = Date(now.time + accessTokenValidityInMilliseconds)
        val accessToken = Jwts.builder()
            .setSubject(user.email)
            .claim("nickname", user.nickname)
            .setIssuedAt(now)
            .setExpiration(accessTokenValidity)
            .signWith(secretKey)
            .compact()

        // Refresh Token 생성
        val refreshTokenValidity = Date(now.time + refreshTokenValidityInMilliseconds)
        val refreshToken = Jwts.builder()
            .setSubject(user.email)
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(now)
            .setExpiration(refreshTokenValidity)
            .signWith(secretKey)
            .compact()

        return TokenResponse(accessToken, refreshToken, refreshTokenValidity.time)
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            // 토큰 파싱에 실패하면 false를 반환합니다.
            false
        }
    }

    fun getEmailFromToken(token: String): String {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body.subject
    }


    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else {
            null
        }
    }
}
