package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // === Artworks ===
    @Query("SELECT * FROM artworks")
    fun getAllArtworks(): Flow<List<Artwork>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtwork(artwork: Artwork)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtworks(artworks: List<Artwork>)

    @Query("UPDATE artworks SET isLiked = :isLiked, likesCount = likesCount + (CASE WHEN :isLiked THEN 1 ELSE -1 END) WHERE id = :id")
    suspend fun toggleLikeArtwork(id: String, isLiked: Boolean)

    @Query("UPDATE artworks SET isSaved = :isSaved WHERE id = :id")
    suspend fun toggleSaveArtwork(id: String, isSaved: Boolean)

    @Query("SELECT * FROM artworks WHERE id = :id")
    fun getArtworkById(id: String): Flow<Artwork?>

    // === Commission Inquiries ===
    @Query("SELECT * FROM commission_inquiries ORDER BY timestamp DESC")
    fun getAllInquiries(): Flow<List<CommissionInquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: CommissionInquiry)

    // === Cart Items ===
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateCartItemQuantity(id: String, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // === App Notifications ===
    @Query("SELECT * FROM app_notifications ORDER BY timestamp DESC")
    fun getNotifications(): Flow<List<AppNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: AppNotification)

    @Query("UPDATE app_notifications SET isRead = 1")
    suspend fun markAllNotificationsRead()

    // === Art Reels ===
    @Query("SELECT * FROM art_reels")
    fun getAllReels(): Flow<List<ArtReel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReels(reels: List<ArtReel>)

    @Query("UPDATE art_reels SET isLiked = :isLiked, likesCount = likesCount + (CASE WHEN :isLiked THEN 1 ELSE -1 END) WHERE id = :id")
    suspend fun toggleLikeReel(id: String, isLiked: Boolean)
}
