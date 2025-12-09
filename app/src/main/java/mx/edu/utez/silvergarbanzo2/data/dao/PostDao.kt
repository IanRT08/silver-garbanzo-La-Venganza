package mx.edu.utez.silvergarbanzo2.data.dao

import androidx.room.*
import mx.edu.utez.silvergarbanzo2.data.model.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM posts WHERE esPrivado = 0")
    suspend fun getPublicPosts(): List<Post>

    @Query("SELECT * FROM posts WHERE usuarioId = :userId")
    suspend fun getPostsByUser(userId: Int): List<Post>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): Post?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)

    @Query("UPDATE posts SET contadorVisitas = contadorVisitas + 1 WHERE id = :postId")
    suspend fun incrementVisitCount(postId: Int)
}