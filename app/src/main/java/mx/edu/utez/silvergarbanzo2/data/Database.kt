package mx.edu.utez.silvergarbanzo2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import mx.edu.utez.silvergarbanzo2.data.dao.PostDao
import mx.edu.utez.silvergarbanzo2.data.dao.UserDao
import mx.edu.utez.silvergarbanzo2.data.model.Post
import mx.edu.utez.silvergarbanzo2.data.model.User

@Database(
    entities = [User::class, Post::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao

    companion object {
        const val DATABASE_NAME = "social_lugares_db"
    }
}
